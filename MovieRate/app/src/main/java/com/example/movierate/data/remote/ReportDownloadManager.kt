package com.example.movierate.data.remote

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ReportDownloadManager {
    private val fileTimestampFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")

    data class DownloadedReport(
        val fileName: String,
        val uri: Uri
    )

    suspend fun generateMovieReport(
        context: Context,
        title: String,
        generatedBy: String?,
        topRatedLimit: Int? = null
    ): Result<DownloadedReport> = withContext(Dispatchers.IO) {
        runCatching {
            val moviesResponse = if (topRatedLimit == null) {
                RetrofitClient.moviesApi.getMovies()
            } else {
                RetrofitClient.moviesApi.getTopRatedMovies(topRatedLimit)
            }

            if (!moviesResponse.isSuccessful) {
                error("Nie udało się pobrać filmów: HTTP ${moviesResponse.code()}")
            }

            val movies = moviesResponse.body().orEmpty()
            val reportRequest = MovieReportRequest(
                title = title,
                generatedBy = generatedBy,
                movies = movies.map { movie ->
                    MovieReportItemRequest(
                        title = movie.title,
                        releaseYear = movie.releaseYear,
                        type = movie.type,
                        averageRating = movie.averageRating
                    )
                }
            )

            val reportResponse = RetrofitClient.reportsApi.generateMovieReport(reportRequest)
            if (!reportResponse.isSuccessful) {
                error("Nie udało się wygenerować raportu: HTTP ${reportResponse.code()}")
            }

            val pdfBytes = reportResponse.body()?.bytes()
                ?: error("Backend zwrócił pusty plik PDF")
            val fileName = createFileName(title)
            val uri = savePdf(context, fileName, pdfBytes)

            DownloadedReport(fileName, uri)
        }
    }

    fun openPdf(context: Context, report: DownloadedReport): Boolean {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(report.uri, "application/pdf")
            clipData = ClipData.newUri(context.contentResolver, report.fileName, report.uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        return try {
            val chooser = Intent.createChooser(intent, "Otworz raport PDF").apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
            true
        } catch (_: ActivityNotFoundException) {
            false
        }
    }

    private fun createFileName(title: String): String {
        val slug = title.lowercase()
            .replace(Regex("[^a-z0-9]+"), "-")
            .trim('-')
            .ifBlank { "raport" }
        return "$slug-${LocalDateTime.now().format(fileTimestampFormatter)}.pdf"
    }

    private fun savePdf(context: Context, fileName: String, bytes: ByteArray): Uri {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                ?: error("Nie udało się utworzyć pliku w Downloads")

            resolver.openOutputStream(uri)?.use { output ->
                output.write(bytes)
            } ?: error("Nie udało się zapisać pliku PDF")

            values.clear()
            values.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
            return uri
        }

        val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?: context.filesDir
        val file = File(downloadsDir, fileName)
        file.writeBytes(bytes)

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}
