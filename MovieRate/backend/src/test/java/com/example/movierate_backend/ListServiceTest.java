package com.example.movierate_backend;

import com.example.movierate_backend.dto.UserListItemResponse;
import com.example.movierate_backend.dto.UserListResponse;
import com.example.movierate_backend.service.ListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ListService listService;

    @Test
    void getUserLists_returnsUserLists() {
        UserListResponse list1 = new UserListResponse(1L, "Do obejrzenia", "WATCHLIST", 3);
        UserListResponse list2 = new UserListResponse(2L, "Obejrzane", "WATCHED", 5);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong()))
                .thenReturn(List.of(list1, list2));

        List<UserListResponse> result = listService.getUserLists(1L, null);

        assertEquals(2, result.size());
        assertEquals("Do obejrzenia", result.get(0).getName());
        assertEquals("WATCHLIST", result.get(0).getType());
        assertEquals(3, result.get(0).getItemCount());
    }

    @Test
    void getUserLists_filteredByType_returnsFilteredLists() {
        UserListResponse list = new UserListResponse(1L, "Ulubione", "FAVORITES", 8);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong(), anyString()))
                .thenReturn(List.of(list));

        List<UserListResponse> result = listService.getUserLists(1L, "FAVORITES");

        assertEquals(1, result.size());
        assertEquals("FAVORITES", result.get(0).getType());
    }

    @Test
    void getListItems_returnsItems() {
        UserListItemResponse item = new UserListItemResponse(1L, 10L, "Breaking Bad", 2008, "Serial", 9.5, 1);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong()))
                .thenReturn(List.of(item));

        List<UserListItemResponse> result = listService.getListItems(1L);

        assertEquals(1, result.size());
        assertEquals("Breaking Bad", result.get(0).getMovieTitle());
        assertEquals(9.5, result.get(0).getAverageRating());
    }

    @Test
    void addMovieToList_executesInsert() {
        listService.addMovieToList(1L, 10L, 0);

        verify(jdbcTemplate).update(
                "INSERT INTO user_list_items (list_id, movie_id, position) VALUES (?, ?, ?)",
                1L, 10L, 0
        );
    }

    @Test
    void removeMovieFromList_executesDelete() {
        listService.removeMovieFromList(1L, 10L);

        verify(jdbcTemplate).update(
                "DELETE FROM user_list_items WHERE list_id = ? AND movie_id = ?",
                1L, 10L
        );
    }

    @Test
    void createList_returnsNewListId() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), anyLong(), anyString()))
                .thenReturn(3L);

        Long result = listService.createList(1L, "Nowa lista", "CUSTOM");

        assertEquals(3L, result);
        verify(jdbcTemplate).update(
                "INSERT INTO user_lists (user_id, name, type) VALUES (?, ?, ?)",
                1L, "Nowa lista", "CUSTOM"
        );
    }

    @Test
    void deleteList_removesItemsAndList() {
        listService.deleteList(1L);

        verify(jdbcTemplate).update("DELETE FROM user_list_items WHERE list_id = ?", 1L);
        verify(jdbcTemplate).update("DELETE FROM user_lists WHERE id = ?", 1L);
    }
}
