# personal-finance-app
Personal Finance Management App - Kotlin, Jetpack Compose, Room, Firebase

## Requirements
- Android Studio (Giraffe+ recommended)
- JDK 17
- Android SDK with `compileSdk` 34

## Build
From the repo root:
- `./gradlew assembleDebug`

## Run
- Open the project in Android Studio
- Select the `app` configuration
- Run on an emulator/device (minSdk 26)

## Firebase
This project uses Firebase (Auth + Firestore). The Gradle build applies `com.google.gms.google-services`, so `app/google-services.json` must be present and match your Firebase project.
