# PulseChat Project Blueprint

## Project Overview
**Name:** PulseChat
**Description:** A real-time messaging and calling application for Android (APK).
**Tech Stack:** Kotlin, Jetpack Compose, Firebase (Auth, Firestore, Storage, FCM), Hilt, Room, WebRTC.

## Development Progress
- [x] **Milestone 1: Infrastructure & Navigation (Completed)**
    - Project structure and Gradle setup.
    - Material 3 Theme (Light/Dark) with specific color palette.
    - Navigation Graph with defined routes: Auth, Home, Chat, Profile, Contacts.
    - Basic placeholder UI for all primary screens.
    - Repository and Model definitions.

- [x] **Milestone 2: Identity & Onboarding (Completed)**
    - Firebase Authentication (Email/Password).
    - Profile management (Display Name, Bio, Avatar Photo upload).
    - User Discovery/Search system.
    - Contact management (Add/Remove).

- [x] **Milestone 3: Core Chat Engine (Completed)**
    - Real-time Firestore message syncing.
    - Message types (Text, Image, Audio, System).
    - "Blue Bubble" UI and "Neutral" incoming bubbles.
    - Chat pagination and history.

- [x] **Milestone 4: Rich Media & Interactions (Completed)**
    - Image compression and WebP conversion.
    - Emoji reactions.
    - Presence system (Online/Offline status).
    - Read receipts.

- [x] **Milestone 5: Calling & Real-Time Communication (RTC) (Completed)**
    - WebRTC integration for P2P Voice/Video calls.
    - In-call UI and signaling logic.

- [x] **Milestone 6: Polish & Distribution (Completed)**
    - Notifications (FCM).
    - Local storage management (Cache/Pruning).
    - "Resilient Architecture" (State restoration, Error boundaries).
    - APK/AAB generation for Google Play.

- [x] **Milestone 7: Consumer-Ready Features (Completed)**
    - Chat list/inbox with unread counts and last message preview.
    - Message delivery states: sending → sent → delivered → read.
    - Typing indicators per chat.
    - Group chats (create, name, photo URL).
    - Voice messages (record, upload, play).
    - Message edit/delete/reply-to.
    - Offline cache with Room + retry on reconnect.
    - Error banners with retry.
    - Crashlytics + Analytics + Remote Config.
    - i18n: English, Spanish, French.
    - CI/CD: GitHub Actions build + test.

- [x] **Milestone 8: Safety & Discovery (Completed)**
    - Block/unblock users with blocked-users list UI.
    - Blocked users' messages filtered out of chat.
    - Message search within chat (Firestore content range query).
    - Push notification reply action (open chat from notification).
    - Notification channel with high importance + vibration.

## Key UX & Technical Specs
- **Colors:** Primary Blue (`#2196F3`), Neutral 100 (`#F5F5F5`), Neutral 800 (`#212121`).
- **Security:** TLS encryption, EncryptedSharedPreferences.
- **Optimization:** WebRTC for zero-cost calls, Room for local caching, WebP for image compression.

