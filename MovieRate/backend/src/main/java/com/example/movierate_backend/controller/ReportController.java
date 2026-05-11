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

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping(value = "/movies", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateMovieReport(@Valid @RequestBody MovieReportRequest request) {
        byte[] pdf = reportService.generateMovieReport(request);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=movie-report.pdf")
                .body(pdf);
    }
}
