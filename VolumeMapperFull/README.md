# Volume Mapper Full

Ready-to-build Android project for Minecraft Bedrock tap mapping.

## GitHub Actions

This repository includes `.github/workflows/build-apk.yml`.

On GitHub, open **Actions → Build Volume Mapper Full APK → Run workflow**.
The workflow installs JDK 17, Gradle 8.7 and the Android SDK components, then builds:

`VolumeMapper-Full.apk`

The APK is published as a GitHub Actions artifact named **VolumeMapper-Full**.

### Important: uploading a ZIP

GitHub does **not** execute a workflow that is merely stored inside a ZIP file. The ZIP must be **extracted into the repository** so that `.github/workflows/build-apk.yml` exists at that exact path. After that, every push to `main`/`master` or a manual workflow run builds the APK.

## Features

- Android AccessibilityService with gesture support.
- Requests key-event filtering and handles Volume Down press/release state.
- Minecraft Bedrock package detection: `com.mojang.minecraftpe`.
- Repeated `dispatchGesture()` taps at configurable X/Y coordinates.
- Interval 30–1000 ms with CPS calculation.
- Manual Start/Stop, Volume Down toggle and emergency stop.
- Stops when Minecraft is no longer foreground.
- SharedPreferences persistence.
- No INTERNET permission.

## Device limitation

Android/MIUI may prevent AccessibilityService from receiving hardware Volume Down events. The app requests the required key-event filtering capability, but cannot guarantee interception on every MIUI build.

## Server rules

Automated/repeated input may be prohibited by Minecraft servers. This project does not guarantee that its use is allowed on any server.
