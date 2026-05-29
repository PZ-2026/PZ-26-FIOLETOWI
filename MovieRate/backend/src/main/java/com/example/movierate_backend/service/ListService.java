package com.example.movierate_backend.service;

import com.example.movierate_backend.dto.UserListResponse;
import com.example.movierate_backend.dto.UserListItemResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class ListService {

    private final JdbcTemplate jdbcTemplate;

    public ListService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<UserListResponse> getUserLists(Long userId, String type) {
        String sql = """
                SELECT ul.id, ul.name, ul.type,
                       COALESCE((SELECT COUNT(*) FROM user_list_items uli WHERE uli.list_id = ul.id), 0) AS item_count
                FROM user_lists ul
                WHERE ul.user_id = ?
                """;
        if (type != null && !type.isBlank()) {
            sql += " AND ul.type = ?";
        }
        sql += " ORDER BY ul.type, ul.name";

        if (type != null && !type.isBlank()) {
            return jdbcTemplate.query(sql, this::mapUserList, userId, type);
        }
        return jdbcTemplate.query(sql, this::mapUserList, userId);
    }

    public List<UserListItemResponse> getListItems(Long listId) {
        String sql = """
                SELECT uli.id, m.id AS movie_id, m.title, m.release_year, m.type,
                       COALESCE(ROUND(AVG(r.rating)::numeric, 1), 0) AS average_rating,
                       uli.position
                FROM user_list_items uli
                JOIN movies m ON m.id = uli.movie_id
                LEFT JOIN ratings r ON r.movie_id = m.id
                WHERE uli.list_id = ?
                GROUP BY uli.id, m.id, m.title, m.release_year, m.type, uli.position
                ORDER BY uli.position ASC NULLS LAST, m.title ASC
                """;
        return jdbcTemplate.query(sql, this::mapListItem, listId);
    }

    public void addMovieToList(Long listId, Long movieId, Integer position) {
        jdbcTemplate.update(
                "INSERT INTO user_list_items (list_id, movie_id, position) VALUES (?, ?, ?)",
                listId, movieId, position
        );
    }

    public void removeMovieFromList(Long listId, Long movieId) {
        jdbcTemplate.update(
                "DELETE FROM user_list_items WHERE list_id = ? AND movie_id = ?",
                listId, movieId
        );
    }

    public Long createList(Long userId, String name, String type) {
        jdbcTemplate.update(
                "INSERT INTO user_lists (user_id, name, type) VALUES (?, ?, ?)",
                userId, name, type
        );
        return jdbcTemplate.queryForObject(
                "SELECT id FROM user_lists WHERE user_id = ? AND name = ?",
                Long.class, userId, name
        );
    }

    public void deleteList(Long listId) {
        // Protect system lists from deletion
        String type = jdbcTemplate.query(
            "SELECT type FROM user_lists WHERE id = ?",
            (rs) -> rs.next() ? rs.getString("type") : null,
            listId);
        if (type != null && !type.equals("CUSTOM")) {
            throw new IllegalArgumentException("Cannot delete system list of type: " + type);
        }
        jdbcTemplate.update("DELETE FROM user_list_items WHERE list_id = ?", listId);
        jdbcTemplate.update("DELETE FROM user_lists WHERE id = ?", listId);
    }

    public void renameList(Long listId, String newName) {
        // Protect system lists from rename
        String type = jdbcTemplate.query(
            "SELECT type FROM user_lists WHERE id = ?",
            (rs) -> rs.next() ? rs.getString("type") : null,
            listId);
        if (type != null && !type.equals("CUSTOM")) {
            throw new IllegalArgumentException("Cannot rename system list of type: " + type);
        }
        jdbcTemplate.update(
                "UPDATE user_lists SET name = ? WHERE id = ?",
                newName, listId
        );
    }

    private UserListResponse mapUserList(ResultSet rs, int rowNum) throws SQLException {
        return new UserListResponse(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("type"),
                rs.getInt("item_count")
        );
    }

    private UserListItemResponse mapListItem(ResultSet rs, int rowNum) throws SQLException {
        return new UserListItemResponse(
                rs.getLong("id"),
                rs.getLong("movie_id"),
                rs.getString("title"),
                rs.getInt("release_year"),
                rs.getString("type"),
                rs.getDouble("average_rating"),
                rs.getObject("position", Integer.class)
        );
    }
}
