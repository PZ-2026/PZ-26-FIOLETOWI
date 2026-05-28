package com.example.movierate_backend.dto;

public class UserListResponse {
    private final Long id;
    private final String name;
    private final String type;
    private final int itemCount;

    public UserListResponse(Long id, String name, String type, int itemCount) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.itemCount = itemCount;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getItemCount() { return itemCount; }
}
