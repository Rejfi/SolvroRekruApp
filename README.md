# Spis treści

- [Opis](#Opis)
- [Technologie](#Technologie)
- [Funkcjonalności](#Funkcjonalności)
- [Przykłady](#Przykłady)

## Opis

Aplikacja rekrutacyjna do koła naukowego Solvro. 
Wykorzystuje technologie natywne do obsługi Cocktail API (https://cocktails.solvro.pl/).

Posiada prostą architekturę MVVM, wykorzystuje bibliotekę Ktor oraz kotlinx.serialization do obsługi zapytań REST API,
lokalną bazę danych SQL (przy użyciu Room) oraz zapewnia płynną obsługę dzięki asynchroniczności dostarczanej przez Coroutines.

Pozwala na proste wyszukiwanie, filtrowanie, sortowanie dostarczane przez REST API.

Posiada mechanizm ulubionych oraz prosty cache (ulubiony drink jest zapisywany lokalnie - kolejne jego otwarcia nie odpytują serwera).

Ekrany posiadają infinite scroll (brak jawnej paginacji, automatyczne ładowanie potrzebnych danych).

UI zbudowany jest przy pomocy Jetpack Compose, a nawigacja realizowana dzięki Navigation-Compose.

Posiada prostą obsługę języka angielskiego i polskiego (w zależności od ustawień telefonu).

## Technologie:
- Kotlin
- Jetpack Compose
- Room
- Coroutines
- kotlinx.serialization
- Ktor

## Funkcjonalności

MVP:
- [x] Wyświetlanie listy koktajli (zdjęcie, tytuł).
- [X] Wyświetlanie szczegółów wybranego drinka (składniki i inne dane).
- [X] Wyszukiwanie drinków na liście (conajmniej po nazwie) 

Opcjonalne funkcje z zadania:
- [x] Filtrowanie koktajli.
- [X] Sortowanie wyników.
- [X] Paginacja w wersji Infinite Scroll
- [X] Zadbaj o podstawy dla prostego wsparcia wielu języków (np. polski, angielski). Nie musisz faktycznie dodawać wielu tłumaczeń.
- [X] Cache danych dla mniejszego obciążenia serwera i zwiększenia wydajności w słabych warunkach internetowych. W dowolnej funkcjonalnej formie - nie musisz wymyślać zaawansowanych strategii inwalidacji cache'u.
- [X] Mechanizm ulubionych - oznaczanie (serduszkowanie, lajkowanie, itd.) poszczególnych pozycji z zapisem stanu w "local storage"
- [X] Ochrona przed nadmiernym równoległym wysyłaniem żądań

## Przykłady

![Ashampoo_Snap_Saturday, October 26, 2024_12h13m43s_001_Running Devices - SolvroRekruApp](https://github.com/user-attachments/assets/83da6922-9322-405f-adbe-534cb5a79e23)

![Ashampoo_Snap_Saturday, October 26, 2024_12h14m38s_004_Running Devices - SolvroRekruApp](https://github.com/user-attachments/assets/2106da1c-7db0-4703-8f1d-08e5a68f6960)

![Ashampoo_Snap_Saturday, October 26, 2024_12h14m17s_003_Running Devices - SolvroRekruApp](https://github.com/user-attachments/assets/e4e21e05-4d6e-4e65-8086-922cf8ad3c79)

![Ashampoo_Snap_Saturday, October 26, 2024_12h13m57s_002_Running Devices - SolvroRekruApp](https://github.com/user-attachments/assets/2e8bb515-8af6-4cf8-8c5a-5caa9abb2b26)
