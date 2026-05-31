package com.example.movierate_backend.controller;

import com.example.movierate_backend.dto.MovieReportRequest;
import com.example.movierate_backend.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler REST obsługujący generowanie zapytań o raporty systemowe.
 * Pozwala na wygenerowanie i pobranie pliku PDF ze statystykami.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    /**
     * Konstruktor dla kontrolera raportów.
     * @param reportService serwis realizujący logikę generowania PDF
     */
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Zleca generację raportu filmowego do formatu PDF i zwraca go jako załącznik do pobrania.
     * @param request obiekt określający kryteria generowania raportu
     * @return plik PDF w postaci binarnej (byte array) z odpowiednimi nagłówkami
     */
    @PostMapping(value = "/movies", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateMovieReport(@Valid @RequestBody MovieReportRequest request) {
        byte[] pdf = reportService.generateMovieReport(request);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=movie-report.pdf")
                .body(pdf);
    }
}
