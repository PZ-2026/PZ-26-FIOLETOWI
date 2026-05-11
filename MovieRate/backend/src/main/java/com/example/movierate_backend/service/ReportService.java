package com.example.movierate_backend.service;

import com.example.movierate_backend.dto.MovieReportItemRequest;
import com.example.movierate_backend.dto.MovieReportRequest;
import com.example.pdfreports.MovieReportItem;
import com.example.pdfreports.MovieReportParams;
import com.example.pdfreports.PdfReportGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final PdfReportGenerator pdfReportGenerator = new PdfReportGenerator();

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

    private MovieReportItem mapMovie(MovieReportItemRequest movie) {
        return new MovieReportItem(
                movie.title(),
                movie.releaseYear(),
                movie.type(),
                movie.averageRating()
        );
    }
}
