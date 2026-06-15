package com.example.pdfreports;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PdfReportGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final int PAGE_WIDTH = 842;
    private static final int PAGE_HEIGHT = 595;
    private static final int LEFT_MARGIN = 40;
    private static final int RIGHT_MARGIN = 40;
    private static final int TOP_Y = 555;
    private static final int BOTTOM_MARGIN = 38;
    private static final int CONTENT_TOP_Y = 418;
    private static final int CONTENT_WIDTH = PAGE_WIDTH - LEFT_MARGIN - RIGHT_MARGIN;

    private record RenderedMovie(MovieReportItem movie, List<String> titleLines, List<String> reviewLines, int height) {}
    private record ReportPage(List<RenderedMovie> rows) {}

    public byte[] generateMovieReport(MovieReportParams params) {
        List<ReportPage> pages = paginate(params);
        List<String> renderedPages = new ArrayList<>();
        for (int i = 0; i < pages.size(); i++) {
            renderedPages.add(renderPage(params, pages.get(i), i + 1, pages.size()));
        }
        return buildPdf(renderedPages);
    }

    private List<ReportPage> paginate(MovieReportParams params) {
        List<MovieReportItem> movies = params.movies() == null ? List.of() : params.movies();
        List<ReportPage> pages = new ArrayList<>();
        List<RenderedMovie> currentRows = new ArrayList<>();
        int availableHeight = CONTENT_TOP_Y - BOTTOM_MARGIN;
        int usedHeight = 0;

        if (movies.isEmpty()) {
            pages.add(new ReportPage(List.of()));
            return pages;
        }

        for (MovieReportItem movie : movies) {
            RenderedMovie rendered = renderable(movie);
            if (!currentRows.isEmpty() && usedHeight + rendered.height() > availableHeight) {
                pages.add(new ReportPage(currentRows));
                currentRows = new ArrayList<>();
                usedHeight = 0;
            }
            currentRows.add(rendered);
            usedHeight += rendered.height();
        }

        if (!currentRows.isEmpty()) {
            pages.add(new ReportPage(currentRows));
        }
        return pages;
    }

    private RenderedMovie renderable(MovieReportItem movie) {
        List<String> titleLines = wrapText(blankToDefault(movie.title(), "Untitled"), 58);
        List<String> reviewLines = wrapText(blankToDefault(movie.reviewContent(), "Brak recenzji"), 98);
        int textHeight = 18 + titleLines.size() * 13 + reviewLines.size() * 11;
        int height = Math.max(62, textHeight + 18);
        return new RenderedMovie(movie, titleLines, reviewLines, height);
    }

    private String renderPage(MovieReportParams params, ReportPage reportPage, int pageNumber, int pageCount) {
        List<MovieReportItem> allMovies = params.movies() == null ? List.of() : params.movies();
        String title = blankToDefault(params.title(), "MovieRate report");
        String generatedBy = blankToDefault(params.generatedBy(), "MovieRate");
        String generatedAt = params.generatedAt() == null ? "-" : params.generatedAt().format(DATE_FORMATTER);

        StringBuilder page = new StringBuilder();
        renderHeader(page, title, generatedBy, generatedAt, allMovies, pageNumber, pageCount);

        if (allMovies.isEmpty()) {
            appendRect(page, LEFT_MARGIN, 330, CONTENT_WIDTH, 56, "0.97 0.98 0.99", true);
            appendRect(page, LEFT_MARGIN, 330, CONTENT_WIDTH, 56, "0.82 0.86 0.92", false);
            appendText(page, "Brak danych do wygenerowania raportu.", LEFT_MARGIN + 18, 355, 13, "/F1", "0.16 0.20 0.27");
            return page.toString();
        }

        int y = CONTENT_TOP_Y;
        int startIndex = pageNumber == 1 ? 1 : countRowsBefore(params, pageNumber);
        for (int i = 0; i < reportPage.rows().size(); i++) {
            RenderedMovie row = reportPage.rows().get(i);
            renderMovieCard(page, row, startIndex + i, y);
            y -= row.height();
        }
        return page.toString();
    }

    private int countRowsBefore(MovieReportParams params, int pageNumber) {
        List<ReportPage> pages = paginate(params);
        int count = 1;
        for (int i = 0; i < pageNumber - 1 && i < pages.size(); i++) {
            count += pages.get(i).rows().size();
        }
        return count;
    }

    private void renderHeader(StringBuilder page, String title, String generatedBy, String generatedAt,
                              List<MovieReportItem> movies, int pageNumber, int pageCount) {
        appendRect(page, 0, 500, PAGE_WIDTH, 95, "0.08 0.12 0.18", true);
        appendText(page, "MovieRate", LEFT_MARGIN, TOP_Y, 12, "/F2", "0.82 0.86 0.92");
        appendText(page, safeLine(title, 74), LEFT_MARGIN, 527, 23, "/F1", "1 1 1");
        appendText(page, "Wygenerowano przez: " + safeLine(generatedBy, 42), LEFT_MARGIN, 486, 10, "/F2", "0.36 0.42 0.50");
        appendText(page, "Data: " + generatedAt, 375, 486, 10, "/F2", "0.36 0.42 0.50");
        appendText(page, "Strona " + pageNumber + " / " + pageCount, 705, 486, 10, "/F2", "0.36 0.42 0.50");

        appendSummaryCard(page, "Pozycji", String.valueOf(movies.size()), LEFT_MARGIN, 438);
        appendSummaryCard(page, "Srednia ocena", averageRating(movies), 215, 438);
        appendSummaryCard(page, "Z ocenami usera", String.valueOf(countUserRatings(movies)), 390, 438);
        appendSummaryCard(page, "Z recenzjami", String.valueOf(countReviews(movies)), 565, 438);
    }

    private void appendSummaryCard(StringBuilder page, String label, String value, int x, int y) {
        appendRect(page, x, y, 150, 42, "0.95 0.97 0.99", true);
        appendRect(page, x, y, 150, 42, "0.78 0.83 0.90", false);
        appendText(page, label, x + 12, y + 25, 8, "/F2", "0.38 0.43 0.50");
        appendText(page, value, x + 12, y + 9, 15, "/F1", "0.08 0.12 0.18");
    }

    private void renderMovieCard(StringBuilder page, RenderedMovie row, int index, int topY) {
        MovieReportItem movie = row.movie();
        int cardY = topY - row.height() + 8;
        appendRect(page, LEFT_MARGIN, cardY, CONTENT_WIDTH, row.height() - 10, index % 2 == 0 ? "0.98 0.99 1" : "1 1 1", true);
        appendRect(page, LEFT_MARGIN, cardY, CONTENT_WIDTH, row.height() - 10, "0.86 0.90 0.95", false);
        appendRect(page, LEFT_MARGIN, cardY, 5, row.height() - 10, "0.23 0.51 0.96", true);

        appendText(page, String.valueOf(index), LEFT_MARGIN + 16, topY - 22, 12, "/F1", "0.23 0.51 0.96");
        int titleX = LEFT_MARGIN + 48;
        int titleY = topY - 19;
        for (String line : row.titleLines()) {
            appendText(page, line, titleX, titleY, 11, "/F1", "0.08 0.12 0.18");
            titleY -= 13;
        }

        int metaX = 520;
        appendText(page, "Rok: " + valueOrDash(movie.releaseYear()), metaX, topY - 20, 9, "/F2", "0.30 0.34 0.40");
        appendText(page, "Typ: " + blankToDefault(movie.type(), "-"), metaX + 82, topY - 20, 9, "/F2", "0.30 0.34 0.40");
        appendText(page, "Srednia: " + formatRating(movie.averageRating()), metaX + 165, topY - 20, 9, "/F1", "0.08 0.12 0.18");
        appendText(page, "Moja: " + valueOrDash(movie.userRating()), metaX + 255, topY - 20, 9, "/F1", "0.08 0.12 0.18");

        int reviewY = titleY - 5;
        appendText(page, "Recenzja:", titleX, reviewY, 8, "/F1", "0.38 0.43 0.50");
        reviewY -= 12;
        for (String line : row.reviewLines()) {
            appendText(page, line, titleX, reviewY, 8, "/F2", "0.24 0.28 0.34");
            reviewY -= 10;
        }
    }

    private void appendText(StringBuilder page, String text, int x, int y, int fontSize, String font) {
        appendText(page, text, x, y, fontSize, font, "0 0 0");
    }

    private void appendText(StringBuilder page, String text, int x, int y, int fontSize, String font, String color) {
        page.append(color).append(" rg BT ")
                .append(font).append(" ").append(fontSize).append(" Tf ")
                .append(x).append(" ").append(y)
                .append(" Td (").append(escapePdfText(toPdfSafe(text))).append(") Tj ET\n");
    }

    private void appendRect(StringBuilder page, int x, int y, int width, int height, String color, boolean fill) {
        page.append(color).append(fill ? " rg " : " RG ")
                .append(x).append(" ").append(y).append(" ").append(width).append(" ").append(height)
                .append(" re ").append(fill ? "f" : "S").append("\n");
    }

    private byte[] buildPdf(List<String> pageContents) {
        List<byte[]> objects = new ArrayList<>();
        objects.add("<< /Type /Catalog /Pages 2 0 R >>".getBytes(StandardCharsets.ISO_8859_1));
        objects.add(buildPagesObject(pageContents.size()).getBytes(StandardCharsets.ISO_8859_1));
        objects.add("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold >>".getBytes(StandardCharsets.ISO_8859_1));
        objects.add("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>".getBytes(StandardCharsets.ISO_8859_1));

        int firstPageObject = 5;
        int firstContentObject = firstPageObject + pageContents.size();

        for (int i = 0; i < pageContents.size(); i++) {
            int contentObjectNumber = firstContentObject + i;
            objects.add(("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 " + PAGE_WIDTH + " " + PAGE_HEIGHT
                    + "] /Resources << /Font << /F1 3 0 R /F2 4 0 R >> >> /Contents "
                    + contentObjectNumber + " 0 R >>").getBytes(StandardCharsets.ISO_8859_1));
        }

        for (String pageContent : pageContents) {
            byte[] stream = pageContent.getBytes(StandardCharsets.ISO_8859_1);
            objects.add(("<< /Length " + stream.length + " >>\nstream\n"
                    + pageContent + "endstream").getBytes(StandardCharsets.ISO_8859_1));
        }

        return writePdf(objects);
    }

    private String buildPagesObject(int pageCount) {
        StringBuilder kids = new StringBuilder();
        for (int i = 0; i < pageCount; i++) {
            kids.append(5 + i).append(" 0 R ");
        }
        return "<< /Type /Pages /Kids [" + kids + "] /Count " + pageCount + " >>";
    }

    private byte[] writePdf(List<byte[]> objects) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        List<Integer> offsets = new ArrayList<>();
        write(output, "%PDF-1.4\n");

        for (int i = 0; i < objects.size(); i++) {
            offsets.add(output.size());
            write(output, (i + 1) + " 0 obj\n");
            output.writeBytes(objects.get(i));
            write(output, "\nendobj\n");
        }

        int xrefOffset = output.size();
        write(output, "xref\n0 " + (objects.size() + 1) + "\n");
        write(output, "0000000000 65535 f \n");
        for (Integer offset : offsets) {
            write(output, String.format(Locale.ROOT, "%010d 00000 n \n", offset));
        }
        write(output, "trailer\n<< /Size " + (objects.size() + 1) + " /Root 1 0 R >>\n");
        write(output, "startxref\n" + xrefOffset + "\n%%EOF");

        return output.toByteArray();
    }

    private void write(ByteArrayOutputStream output, String text) {
        output.writeBytes(text.getBytes(StandardCharsets.ISO_8859_1));
    }

    private String blankToDefault(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }

    private String valueOrDash(Integer value) {
        return value == null ? "-" : value.toString();
    }

    private String formatRating(Double rating) {
        return rating == null ? "-" : String.format(Locale.ROOT, "%.1f", rating);
    }

    private String averageRating(List<MovieReportItem> movies) {
        double average = movies.stream()
                .filter(movie -> movie.averageRating() != null)
                .mapToDouble(MovieReportItem::averageRating)
                .average()
                .orElse(0);
        return String.format(Locale.ROOT, "%.1f", average);
    }

    private long countUserRatings(List<MovieReportItem> movies) {
        return movies.stream().filter(movie -> movie.userRating() != null).count();
    }

    private long countReviews(List<MovieReportItem> movies) {
        return movies.stream().filter(movie -> movie.reviewContent() != null && !movie.reviewContent().isBlank()).count();
    }

    private List<String> wrapText(String raw, int maxChars) {
        String text = toPdfSafe(blankToDefault(raw, ""));
        List<String> lines = new ArrayList<>();
        for (String paragraph : text.split("\\R")) {
            String remaining = paragraph.trim();
            if (remaining.isEmpty()) {
                lines.add("");
                continue;
            }
            while (remaining.length() > maxChars) {
                int splitAt = remaining.lastIndexOf(' ', maxChars);
                if (splitAt < maxChars / 2) {
                    splitAt = maxChars;
                }
                lines.add(remaining.substring(0, splitAt).trim());
                remaining = remaining.substring(splitAt).trim();
            }
            if (!remaining.isEmpty()) {
                lines.add(remaining);
            }
        }
        return lines.isEmpty() ? List.of("") : lines;
    }

    private String safeLine(String value, int maxChars) {
        String safe = toPdfSafe(blankToDefault(value, ""));
        return safe.length() <= maxChars ? safe : safe.substring(0, Math.max(0, maxChars - 3)) + "...";
    }

    private String escapePdfText(String text) {
        return text.replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)");
    }

    private String toPdfSafe(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace('ą', 'a').replace('ć', 'c').replace('ę', 'e').replace('ł', 'l')
                .replace('ń', 'n').replace('ó', 'o').replace('ś', 's').replace('ż', 'z').replace('ź', 'z')
                .replace('Ą', 'A').replace('Ć', 'C').replace('Ę', 'E').replace('Ł', 'L')
                .replace('Ń', 'N').replace('Ó', 'O').replace('Ś', 'S').replace('Ż', 'Z').replace('Ź', 'Z')
                .replace('–', '-').replace('—', '-').replace('•', '-')
                .replace('“', '"').replace('”', '"').replace('„', '"')
                .replace('’', '\'').replace('‘', '\'');
    }
}
