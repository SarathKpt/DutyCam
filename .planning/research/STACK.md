# Stack Research: Android Camera Application

## Recommendation

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Camera**: CameraX (`camera-core`, `camera-camera2`, `camera-lifecycle`, `camera-video`, `camera-view`)
- **State/Lifecycle**: AndroidX lifecycle + Compose runtime state
- **Storage**: MediaStore API with `RELATIVE_PATH` routing into `DCIM/WorkMedia`

## Why This Stack

- CameraX gives stable capture APIs and better device compatibility than direct Camera2 implementation.
- Compose allows a clean, premium layout system suitable for minimalist UI with fast iteration.
- MediaStore handles system gallery indexing and album creation semantics without custom file scanners.

## Avoid

- Direct legacy filesystem writes (`Environment.getExternalStorageDirectory`) for modern Android targets.
- Building v1 on Camera2-only abstractions unless device-specific manual tuning is required.

## Confidence

- CameraX + MediaStore + Compose for this use case: **High**
