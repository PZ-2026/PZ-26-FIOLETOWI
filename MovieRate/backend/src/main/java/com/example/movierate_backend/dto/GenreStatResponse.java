package com.example.movierate_backend.dto;

public class GenreStatResponse {
    private final String name;
    private final long count;

    public GenreStatResponse(String name, long count) {
        this.name = name;
        this.count = count;
    }

    public String getName() { return name; }
    public long getCount() { return count; }
}
