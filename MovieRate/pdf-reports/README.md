# PDF Reports

Separate Java library for generating parameterized PDF reports used by the MovieRate backend.

## Build JAR

From the `MovieRate` directory:

```powershell
.\gradlew.bat -p pdf-reports jar
```

Copy the generated file into the backend:

```powershell
Copy-Item -Force pdf-reports\build\libs\pdf-reports-1.0.0.jar backend\libs\pdf-reports-1.0.0.jar
```

The backend loads the library from `backend/libs/pdf-reports-1.0.0.jar`.

## Backend Endpoint

```http
POST /api/reports/movies
Content-Type: application/json
Accept: application/pdf
```

Example body:

```json
{
  "title": "Top movies report",
  "generatedBy": "MovieRate",
  "movies": [
    {
      "title": "Inception",
      "releaseYear": 2010,
      "type": "movie",
      "averageRating": 9.2
    }
  ]
}
```
