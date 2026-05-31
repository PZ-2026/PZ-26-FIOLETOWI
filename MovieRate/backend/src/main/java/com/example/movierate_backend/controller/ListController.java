package com.example.movierate_backend.controller;

import com.example.movierate_backend.dto.*;
import com.example.movierate_backend.service.ListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler REST służący do operacji na listach filmów tworzonych przez użytkowników.
 * Pozwala na przeglądanie, tworzenie, modyfikację i usuwanie własnych zestawień filmowych.
 */
@RestController
@RequestMapping("/api/lists")
public class ListController {

    private final ListService listService;

    /**
     * Konstruktor wstrzykujący ListService.
     * @param listService serwis odpowiadający za logikę list filmowych
     */
    public ListController(ListService listService) {
        this.listService = listService;
    }

    /**
     * Pobiera listy filmów zdefiniowane przez danego użytkownika, z opcjonalnym filtrem po typie.
     * @param userId identyfikator użytkownika, którego listy są pobierane
     * @param type opcjonalny typ listy (np. publiczna, prywatna itp.)
     * @return kolekcja obiektów UserListResponse ze szczegółami o listach
     */
    @GetMapping
    public ResponseEntity<List<UserListResponse>> getUserLists(
            @RequestParam Long userId,
            @RequestParam(required = false) String type) {
        return ResponseEntity.ok(listService.getUserLists(userId, type));
    }

    /**
     * Pobiera zawartość konkretnej listy filmów.
     * @param listId identyfikator wybranej listy
     * @return elementy listy pod postacią obiektów UserListItemResponse
     */
    @GetMapping("/{listId}/items")
    public ResponseEntity<List<UserListItemResponse>> getListItems(@PathVariable Long listId) {
        return ResponseEntity.ok(listService.getListItems(listId));
    }

    /**
     * Tworzy nową listę filmową dla określonego użytkownika.
     * @param request obiekt zawierający dane tworzonej listy (nazwa, typ, powiązany użytkownik)
     * @return id nowo utworzonej listy wraz z kodem 201 Created
     */
    @PostMapping
    public ResponseEntity<Long> createList(@Valid @RequestBody CreateListRequest request) {
        Long listId = listService.createList(request.getUserId(), request.getName(), request.getType());
        return ResponseEntity.status(HttpStatus.CREATED).body(listId);
    }

    /**
     * Dodaje wybrany film do istniejącej listy filmowej.
     * @param listId identyfikator listy
     * @param request obiekt z prośbą o dodanie filmu (movieId oraz docelowa pozycja)
     * @return status 201 Created w przypadku pomyślnego dodania
     */
    @PostMapping("/{listId}/items")
    public ResponseEntity<Void> addMovieToList(
            @PathVariable Long listId,
            @Valid @RequestBody AddListItemRequest request) {
        listService.addMovieToList(listId, request.getMovieId(), request.getPosition());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Usuwa przypisanie filmu do listy.
     * @param listId identyfikator listy
     * @param movieId identyfikator filmu do usunięcia
     * @return status 204 No Content
     */
    @DeleteMapping("/{listId}/items/{movieId}")
    public ResponseEntity<Void> removeMovieFromList(
            @PathVariable Long listId,
            @PathVariable Long movieId) {
        listService.removeMovieFromList(listId, movieId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Całkowicie usuwa wskazaną listę filmową.
     * @param listId identyfikator listy do usunięcia
     * @return status 204 No Content lub 400 Bad Request w przypadku błędu usunięcia
     */
    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteList(@PathVariable Long listId) {
        try {
            listService.deleteList(listId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Zmienia nazwę istniejącej listy filmowej.
     * @param listId identyfikator listy do modyfikacji
     * @param body request body zawierające nową nazwę pod kluczem "name"
     * @return status 200 OK w przypadku pomyślnej edycji lub 400 w razie błędu
     */
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
