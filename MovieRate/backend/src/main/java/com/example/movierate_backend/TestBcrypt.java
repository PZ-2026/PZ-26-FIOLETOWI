package com.example.movierate_backend;
import org.mindrot.jbcrypt.BCrypt;

public class TestBcrypt {
    public static void main(String[] args) {
        String hash = BCrypt.hashpw("1234", BCrypt.gensalt());
        System.out.println("VALID_HASH_1234: " + hash);
    }
}
