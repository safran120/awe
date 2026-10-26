# PulseChat

Android real-time messaging & calling app.
Kotlin, Jetpack Compose, Material Design 3, Firebase (Auth, Firestore, Storage, FCM, Crashlytics, Analytics), Hilt, Room, WebRTC.

## Design System
- **Material Design 3** with dynamic color (light/dark themes)
- **Brand:** Indigo `#6366F1`, Dark surface `#0F172A`, rich deep slate backgrounds
- **Typography:** M3 type scale, headline → title → body → label hierarchy
- **Spacing:** 8dp grid, 16dp–24dp card corners, subtle 1dp outline borders
- **Interactions:** Ripple on all touch targets, scale feedback, elevation shifts on press

## Features
- Auth: email/password sign up & login, profile with bio + avatar
- Chat: real-time firestore sync, delivery states (sending/sent/delivered/read), typing indicators, pagination, message search
- Media: image compression (WebP), voice messages
- Groups: create group chats with name and photo
- Calls: WebRTC P2P voice calls with offer/answer/ICE signaling
- Offline: Room cache for messages, retry on reconnect
- Safety: block/unblock users, blocked messages filtered from chat
- Notifications: FCM push messages with reply action
- i18n: English, Spanish, French
- Crashlytics + Analytics

## Setup
1. Install Android SDK (API 34) and Gradle 8.5+.
2. Generate Gradle wrapper: `gradle wrapper --gradle-version 8.5`
3. Create a Firebase project and add an Android app (`com.safranpulse.pulsechat`).
4. Download `google-services.json` and replace `app/google-services.json` (already included for project `awer-57524`).
5. Enable Firebase Auth (Email/Password), Firestore, Storage, Cloud Messaging.
6. Build: `./gradlew :app:assembleDebug` (Linux/Mac) or `gradlew.bat :app:assembleDebug` (Windows).

## Firebase Firestore Security Rules (required)
```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{uid} {
      allow read, write: if request.auth != null && request.auth.uid == uid;
    }
    match /conversations/{chatId} {
      allow read, write: if request.auth != null && request.auth.uid in resource.data.participants;
      match /messages/{messageId} {
        allow read, write: if request.auth != null;
      }
      match /typing/{userId} {
        allow read, write: if request.auth != null && request.auth.uid == userId;
      }
    }
    match /calls/{callId} {
      allow read, write: if request.auth != null;
    }
  }
}
```

## Storage Security Rules
```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /users/{uid}/{allPaths=**} {
      allow read, write: if request.auth != null && request.auth.uid == uid;
    }
    match /chats/{chatId}/{allPaths=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

## Tech Stack
- Kotlin 1.9.22, Jetpack Compose 1.5.10, Material 3
- Hilt DI, Room 2.6.1, Coil image loading
- Firebase BOM 32.7.2 (Auth, Firestore, Storage, Messaging, Analytics, Crashlytics)
- WebRTC 1.0.32006, Compose Animation 1.5.10
- Gradle 8.5, AGP 8.2.2

## CI
GitHub Actions builds debug APK and runs unit tests on every push/PR.
