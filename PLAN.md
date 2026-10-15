# PingMate Project Blueprint

## Project Overview
**Name:** PingMate
**Description:** A real-time messaging and calling application for Android (APK).
**Tech Stack:** Kotlin, Jetpack Compose, Firebase (Auth, Firestore, Storage, FCM), Hilt, Room, WebRTC.

## Development Progress
- [x] **Milestone 1: Infrastructure & Navigation (Completed)**
    - Project structure and Gradle setup.
    - Material 3 Theme (Light/Dark) with specific color palette.
    - Navigation Graph with defined routes: Auth, Home, Chat, Profile, Contacts.
    - Basic placeholder UI for all primary screens.
    - Repository and Model definitions.

- [ ] **Milestone 2: Identity & Onboarding (NEXT)**
    - Firebase Authentication (Email/Password).
    - Profile management (Display Name, Bio, Avatar Photo upload).
    - User Discovery/Search system.
    - Contact management (Add/Remove).

- [ ] **Milestone 3: Core Chat Engine**
    - Real-time Firestore message syncing.
    - Message types (Text, Image, Audio, System).
    - "Blue Bubble" UI and "Neutral" incoming bubbles.
    - Chat pagination and history.

- [ ] **Milestone 4: Rich Media & Interactions**
    - Image compression and WebP conversion.
    - Emoji reactions.
    - Presence system (Online/Offline status).
    - Read receipts.

- [ ] **Milestone 5: Calling & Real-Time Communication (RTC)**
    - WebRTC integration for P2P Voice/Video calls.
    - In-call UI and signaling logic.

- [ ] **Milestone 6: Polish & Distribution**
    - Notifications (FCM).
    - Local storage management (Cache/Pruning).
    - "Resilient Architecture" (State restoration, Error boundaries).
    - APK/AAB generation for Google Play.

## Key UX & Technical Specs
- **Colors:** Primary Blue (`#2196F3`), Neutral 100 (`#F5F5F5`), Neutral 800 (`#212121`).
- **Security:** TLS encryption, EncryptedSharedPreferences.
- **Optimization:** WebRTC for zero-cost calls, Room for local caching, WebP for image compression.
