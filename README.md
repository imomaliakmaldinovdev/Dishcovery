# Dishcovery

Dishcovery is an Android recipe-discovery app built with Kotlin and Jetpack Compose. It loads recipes from the Oshxona API and provides connected Home, Explore, Saved, Profile, and Recipe Detail screens.

## Architecture

The app follows a practical clean-architecture boundary:

`Compose UI → ViewModel → use case → repository → Oshxona API`

- `presentation/`: screen state and view models
- `domain/`: app models, repository contracts, and use cases
- `data/`: API-backed repository and persistent user preferences
- `di/AppContainer.kt`: application-level dependency composition

Language and saved recipe IDs are persisted locally. The default interface language is English. Recipe text is returned in the languages supplied by the public API; translating it would require a translation content source or API.

## Requirements

- Android Studio Ladybug or newer
- JDK 17
- Android SDK 35

## Build and test

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
```

The debug APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

To make a release candidate locally:

```bash
./gradlew :app:assembleRelease
```

Release builds enable R8 code shrinking and resource shrinking. Before Play Store publishing, create a private upload keystore and configure its signing credentials outside version control; no signing key is included in this repository.
The local unsigned release artifact is written to `app/build/outputs/apk/release/app-release-unsigned.apk`.

## API

Base URL: `https://oshxona-api.zokirov-mob-dev.uz/`

The client has explicit connection/read/write timeouts and retries failed connections. The app requires internet access.
