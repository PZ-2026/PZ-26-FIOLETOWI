package com.example.pdfreports;

import java.time.LocalDateTime;
import java.util.List;

public record MovieReportParams(
        String title,
        String generatedBy,
        LocalDateTime generatedAt,
        List<MovieReportItem> movies
) {
}
