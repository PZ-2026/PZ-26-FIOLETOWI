package com.example.movierate_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Główna klasa uruchomieniowa dla architektury aplikacji backendowej Spring Boot. 
 * Składa i inicjalizuje kompletny moduł webowy.
 */
@SpringBootApplication
public class MovieRateBackendApplication {

    /**
     * Domyślny konstruktor niezbędny do uruchomienia instancji przez środowisko Spring'a (DI).
     */
    public MovieRateBackendApplication() {
    }

    /**
     * Metoda bazowa (wejściowa) bootstrapująca program główny Java.
     * @param args wejściowe argumenty i komendy poleceń (tzw. CLI properties do np. overrideowania ustawień bazy)
     */
	public static void main(String[] args) {
		SpringApplication.run(MovieRateBackendApplication.class, args);
	}

}
