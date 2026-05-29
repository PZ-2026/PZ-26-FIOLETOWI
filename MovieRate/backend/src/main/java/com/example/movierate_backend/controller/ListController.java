package com.example.movierate_backend.controller;

import com.example.movierate_backend.dto.*;
import com.example.movierate_backend.service.ListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping
    public ResponseEntity<List<UserListResponse>> getUserLists(
            @RequestParam Long userId,
            @RequestParam(required = false) String type) {
        return ResponseEntity.ok(listService.getUserLists(userId, type));
    }

    @GetMapping("/{listId}/items")
    public ResponseEntity<List<UserListItemResponse>> getListItems(@PathVariable Long listId) {
        return ResponseEntity.ok(listService.getListItems(listId));
    }

    @PostMapping
    public ResponseEntity<Long> createList(@Valid @RequestBody CreateListRequest request) {
        Long listId = listService.createList(request.getUserId(), request.getName(), request.getType());
        return ResponseEntity.status(HttpStatus.CREATED).body(listId);
    }

    @PostMapping("/{listId}/items")
    public ResponseEntity<Void> addMovieToList(
            @PathVariable Long listId,
            @Valid @RequestBody AddListItemRequest request) {
        listService.addMovieToList(listId, request.getMovieId(), request.getPosition());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{listId}/items/{movieId}")
    public ResponseEntity<Void> removeMovieFromList(
            @PathVariable Long listId,
            @PathVariable Long movieId) {
        listService.removeMovieFromList(listId, movieId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteList(@PathVariable Long listId) {
        try {
            listService.deleteList(listId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{listId}/rename")
    public ResponseEntity<Void> renameList(
            @PathVariable Long listId,
            @RequestBody java.util.Map<String, String> body) {
        String newName = body.get("name");
        if (newName == null || newName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            listService.renameList(listId, newName);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
