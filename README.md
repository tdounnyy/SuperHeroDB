# SuperHeroDB

A modern Android application built with Jetpack Compose that allows users to browse and search superhero information from the SuperHero API.

## Features

- **Browse Superheroes**: View a comprehensive list of all available superheroes
- **Search Functionality**: Search for specific superheroes by name with real-time results
- **Detailed Profiles**: View detailed information about each superhero including:
  - Power stats (intelligence, strength, speed, durability, power, combat)
  - Biography information
  - Appearance details
  - Work and connections
  - High-quality images
- **Local Database**: Caching using Room for offline access and better performance
- **Modern UI**: Built with Jetpack Compose and Material Design 3
- **Navigation**: Seamless navigation between different screens

## Tech Stack

- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern declarative UI toolkit
- **Retrofit**: Type-safe HTTP client for API calls
- **Room**: Local database persistence
- **Coil**: Image loading library
- **Navigation Compose**: For in-app navigation
- **Coroutines**: For asynchronous operations
- **GSON**: JSON serialization/deserialization

## API Source

This app uses the [SuperHero API](https://akabab.github.io/superhero-api/) to fetch superhero data. The API provides comprehensive information about superheroes including:

- Basic information (name, ID, slug)
- Power statistics
- Biography details
- Appearance data
- Work and connections
- Multiple image sizes

## Project Structure

```
app/
├── src/main/java/felix/duan/superherodb/
│   ├── api/              # API definitions and services
│   │   ├── ApiDefine.kt
│   │   └── ApiService.kt
│   ├── model/            # Data models and Room entities
│   │   └── models.kt
│   ├── repo/             # Repository layer
│   │   ├── SuperHeroDatabase.kt
│   │   └── SuperHeroRepo.kt
│   ├── ui/               # UI components and themes
│   │   ├── theme/
│   │   └── widget/
│   │       ├── HomePage.kt
│   │       ├── HeroListPage.kt
│   │       ├── HeroProfilePage.kt
│   │       ├── SearchPage.kt
│   │       └── Widgets.kt
│   ├── viewmodel/        # ViewModels for state management
│   │   ├── HeroListViewModel.kt
│   │   └── HeroPagingListViewModel.kt
│   └── MainActivity.kt   # App entry point
└── build.gradle.kts      # Dependencies and build configuration
```

## Installation

1. Clone the repository:
```bash
git clone https://github.com/tdounnyy/SuperHeroDB.git
cd SuperHeroDB
```

2. Open the project in Android Studio
3. Sync the Gradle project
4. Build and run the application

## Usage

1. **Home Screen**: Launch the app to see the home screen with options to browse or search
2. **Browse**: Tap "Browse" to see all available superheroes in a list
3. **Search**: Tap "Search" and enter a superhero name to find specific characters
4. **Profile**: Tap any superhero to view their detailed profile
5. **Clear Cache**: Use the "Clear Local DB" button to remove cached data

## Data Model

The app uses a comprehensive data model that includes:

- `SuperHeroData`: Main entity with embedded objects
- `PowerStats`: Intelligence, strength, speed, durability, power, combat
- `Biography`: Full name, alter egos, aliases, place of birth, first appearance, publisher, alignment
- `Appearance`: Gender, race, height, weight, eye color, hair color
- `Work`: Occupation and base location
- `Connections`: Group affiliation and relatives
- `Images`: Multiple image sizes (xs, sm, md, lg)

## Dependencies

Key dependencies include:

- `androidx.compose.*` - Jetpack Compose components
- `retrofit` & `okhttp` - Network requests
- `room-runtime` & `room-ktx` - Database operations
- `coil-compose` - Image loading
- `navigation-compose` - Navigation
- `gson` - JSON processing

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is open source and available under the [MIT License](LICENSE).

## Acknowledgments

- [SuperHero API](https://akabab.github.io/superhero-api/) for providing the superhero data
- Jetpack Compose team for the excellent UI framework
- Android developer community for various libraries and tools