package com.example.movierate_backend.dto;

/**
 * Model opisujący podstawowe dane zbiorczej listy (stworzonej przez użytkownika), powiązanej z jego profilem.
 */
public class UserListResponse {
    private final Long id;
    private final String name;
    private final String type;
    private final int itemCount;

    /**
     * Uzupełniający konstruktor dla obiektu DTO odzwierciedlającego zgrupowany ciąg danych do widoku folderów list na profilu.
     * @param id id wewnątrzsystemowe wpisu listy
     * @param name wizytówkowa etykieta
     * @param type tag techniczny (tzw. FAVORITES/WATCHLIST)
     * @param itemCount wyliczona wartość przebywających na liście powiązań do rekordów
     */
    public UserListResponse(Long id, String name, String type, int itemCount) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.itemCount = itemCount;
    }

    /**
     * Odczyt identyfikatora systemowego ułatwiający dalszą obróbkę rekordu (np. rozwijanie folderu).
     * @return ID numeryczne
     */
    public Long getId() { return id; }
    
    /**
     * Pobiera miano pod którym zarchiwizowana została lista.
     * @return etykieta użytkownika
     */
    public String getName() { return name; }
    
    /**
     * Zwraca klasyfikator dla zaprogramowanej z góry reguły list natywnych m.in. Ulubione produkcje.
     * @return typ logiczny przyporządkowania folderowego
     */
    public String getType() { return type; }
    
    /**
     * Zwraca skalkulowaną sumarycznie pojemność zagnieżdżonych rekordów dla widoków poglądowych.
     * @return rozmiar liczbowy przypisanych pozycji
     */
    public int getItemCount() { return itemCount; }
}
