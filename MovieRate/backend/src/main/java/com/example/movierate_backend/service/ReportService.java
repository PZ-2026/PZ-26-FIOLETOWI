package com.example.movierate_backend.service;

import com.example.movierate_backend.dto.MovieReportItemRequest;
import com.example.movierate_backend.dto.MovieReportRequest;
import com.example.pdfreports.MovieReportItem;
import com.example.pdfreports.MovieReportParams;
import com.example.pdfreports.PdfReportGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serwis pośredniczący między systemem wewnątrz Spring Boota a zewnętrznym modułem bibliotecznym (pdf-reports).
 * Wywołuje mechanizm budowy plików i przekształca struktury modeli.
 */
@Service
public class ReportService {

    /**
     * Egzemplarz obiektu biblioteki zewnetrznej stworzonej na poczet zadania.
     */
    private final PdfReportGenerator pdfReportGenerator = new PdfReportGenerator();

    /**
     * Mapuje przesłane żądanie tworzenia raportu i angażuje bibliotekę PdfReportGenerator do wytworzenia gotowego
     * pliku wynikowego jako ciąg bajtów (PDF).
     * @param request zbiór parametrów konfiguracyjnych i wyciągniętych dotychczas list filmowych na poczet raportu
     * @return czysty zestaw bajtów utworzonego pliku, gotowy na odesłanie go w sieci pod postacią PDF
     */
    public byte[] generateMovieReport(MovieReportRequest request) {
        List<MovieReportItem> movies = request.movies() == null
                ? List.of()
                : request.movies().stream()
                .map(this::mapMovie)
                .toList();

        MovieReportParams params = new MovieReportParams(
                request.title(),
                request.generatedBy(),
                LocalDateTime.now(),
                movies
        );

        return pdfReportGenerator.generateMovieReport(params);
    }

    /**
     * Konwerter z wewnętrznego DTO aplikacji (MovieReportItemRequest) do standardu rozumianego przez samą bibliotekę.
     */
    private MovieReportItem mapMovie(MovieReportItemRequest movie) {
        return new MovieReportItem(
                movie.title(),
                movie.releaseYear(),
                movie.type(),
                movie.averageRating(),
                movie.userRating(),
                movie.reviewContent()
        );
    }
}
