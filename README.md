# MovieRate - PZ-26-FIOLETOWI

Aplikacja "MovieRate" pozwala użytkownikom na przeglądanie filmów, ocenianie, pisanie recenzji oraz tworzenie własnych list. Projekt składa się z backendu stworzonego w technologii Spring Boot oraz aplikacji mobilnej na system Android.

## Technologie
* **Backend:** Java 21, Spring Boot (Web, Data JPA, Validation), PostgreSQL, JDBC
* **Frontend:** Android (Kotlin)
* **Infrastruktura:** Docker, Docker Compose

## Uruchomienie projektu (Docker)

Szybki start środowiska przy wykorzystaniu narzędzia Docker Compose:

1. Upewnij się, że masz zainstalowany Docker i Docker Compose.
2. Przejdź do katalogu `MovieRate`.
3. Uruchom polecenie:
   ```bash
   docker-compose up --build -d
   ```
4. Aplikacja backendowa będzie dostępna pod portem `8080`, a baza PostgreSQL pod `5432`.

---

## Architektura i Diagramy UML

Poniżej znajdują się kluczowe diagramy opisujące przepływy oraz strukturę logiki projektu.

### 1. Diagram Przypadków Użycia (Use Case)

![Diagram Przypadków Użycia](http://www.plantuml.com/plantuml/svg/TP8zJyCm48Pt_ufJzahqkrrG-H1RHTMgGc8xacjmuzYLxL0bY95VnCe6_2zSarAdQZS_VX-TxsnFZ4LjYrmmGIiBLe7cwRE5X6kABLUIcOpB3MhCOODnXYajR2a8TZ7AL11Z65kb8Lh-V9OsKrl9imZGmDAGtcTNISubDrQZGv_w7SR-oq0qLI-SvcW95gMnb4VmoW0AGp4Q5yzqHQduVazGSe8L5_dNn_Rle9jsA3wKfYeolZtXTKBlFYR9JqZTngdSUCNrIR9QyzhfXSuZwihrLNbHk2anttlzq9lZHcbRmkpsheu7BZvRgJZSvw7WCDIlXNAvbpCbRQsCpbPyGhS-cdOSsbELaCPuZSVpjY_O6sEx5uLMwzBVU02T4BeXz4BeXp1Wp7-16eSD6ZLet23NoeHailkolm==)

### 2. Diagramy Sekwencji

#### 2.1. Logowanie i Autoryzacja

```mermaid
sequenceDiagram
    participant App as Aplikacja Mobilna
    participant Auth as AuthController
    participant Service as AuthService
    participant DB as Baza Danych

    App->>Auth: POST /api/auth/login (email, password)
    Auth->>Service: login(request)
    Service->>DB: findByEmail(email)
    DB-->>Service: User object (with password hash)
    
    alt Dane poprawne
        Service->>Service: BCrypt.checkpw()
        Service-->>Auth: AuthResponse (username, token/role)
        Auth-->>App: 200 OK (AuthResponse)
    else Błędne dane
        Service-->>Auth: IllegalArgumentException
        Auth-->>App: 401 Unauthorized / 400 Bad Request
    end
```

#### 2.2. Wyszukiwanie Filmów

```mermaid
sequenceDiagram
    participant User as Użytkownik
    participant App as Aplikacja Mobilna
    participant MovieCtrl as MovieController
    participant MovieSvc as MovieService
    participant DB as Baza Danych

    User->>App: Wpisuje frazę (np. "Incepcja")
    App->>MovieCtrl: GET /api/movies/search?q=Incepcja
    MovieCtrl->>MovieSvc: searchMovies("Incepcja")
    MovieSvc->>DB: LOWER(title) LIKE LOWER('%Incepcja%')
    DB-->>MovieSvc: Zwraca listę pasujących rekordów
    MovieSvc-->>MovieCtrl: List<MovieResponse>
    MovieCtrl-->>App: 200 OK (JSON)
    App-->>User: Wyświetla listę wyników
```

#### 2.3. Generowanie Raportu PDF

```mermaid
sequenceDiagram
    participant Admin as Administrator
    participant ReportCtrl as ReportController
    participant ReportSvc as ReportService
    participant Generator as PdfReportGenerator

    Admin->>ReportCtrl: POST /api/reports/movies (MovieReportRequest)
    ReportCtrl->>ReportSvc: generateMovieReport(request)
    ReportSvc->>Generator: createPdf(moviesList)
    Generator-->>ReportSvc: byte[] pdfData
    ReportSvc-->>ReportCtrl: byte[]
    ReportCtrl-->>Admin: 200 OK (application/pdf)
```

### 3. Diagramy Aktywności

#### 3.1. Dodawanie Recenzji

```mermaid
stateDiagram-v2
    direction TB
    [*] --> WyborFilmu: Użytkownik wybiera film
    WyborFilmu --> WypelnienieFormularza: Klika "Dodaj recenzję"
    
    WypelnienieFormularza --> WeryfikacjaDanych: Przycisk "Zapisz" (wysłanie JSON z userId)
    
    state if_state <<choice>>
    WeryfikacjaDanych --> if_state
    if_state --> WypelnienieFormularza : Pusty tekst (Błąd)
    if_state --> ZapisDB : Walidacja OK
    
    ZapisDB --> OdswiezenieWidoku
    OdswiezenieWidoku --> [*]
```

#### 3.2. Tworzenie własnej listy filmów

```mermaid
stateDiagram-v2
    direction TB
    [*] --> KlikniecieUtworz: Użytkownik klika "Utwórz Listę"
    KlikniecieUtworz --> WpisanieNazwy
    WpisanieNazwy --> WyborWidocznosci: Ustawienie is_public (tak/nie)
    WyborWidocznosci --> Zapisz: Przycisk "Zapisz"
    
    state WalidacjaDanych <<choice>>
    Zapisz --> WalidacjaDanych
    WalidacjaDanych --> BladWalidacji: Nazwa jest pusta
    BladWalidacji --> WpisanieNazwy: Komunikat błędu
    WalidacjaDanych --> Sukces: Walidacja pomyślna
    
    Sukces --> DodanieDoBazy
    DodanieDoBazy --> [*]
```

#### 3.3. Blokowanie Użytkownika

```mermaid
stateDiagram-v2
    direction TB
    [*] --> WyborUzytkownika: Administrator wyszukuje konto
    WyborUzytkownika --> WyslanieZadania: Wybiera opcję "Zablokuj"
    
    WyslanieZadania --> AktualizacjaBazy: PUT /api/admin/users/{id}/block
    AktualizacjaBazy --> InformacjaZwrotna: UPDATE is_blocked = NOT is_blocked
    InformacjaZwrotna --> ZmianaStatusu: 200 OK
    ZmianaStatusu --> [*]
```
