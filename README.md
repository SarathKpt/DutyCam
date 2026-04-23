# DutyCam

DutyCam is a functional Android camera app focused on work capture workflows.

## Core behavior

- Captures photos and videos with CameraX
- Saves all media to `DCIM/WorkMedia` through MediaStore
- Surfaces as a dedicated WorkMedia album in common gallery apps
- Uses a premium, minimalist camera UI inspired by GCam patterns

## Tech

- Kotlin
- Jetpack Compose
- CameraX (`camera2`, `lifecycle`, `video`, `view`)
- AndroidX Material 3

## Local run

1. Open the repository in Android Studio.
2. Let Gradle sync.
3. Run the `app` configuration on a physical Android device (recommended) or emulator with camera support.
