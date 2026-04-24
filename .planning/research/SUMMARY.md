# Research Summary: DutyCam

## Stack

Kotlin + Jetpack Compose + CameraX + MediaStore is the most direct and reliable path for v1.

## Table Stakes

- Fast preview and capture
- Stable photo/video flows
- Permission handling
- Deterministic album visibility in gallery

## Watch Outs

- Keep MediaStore routing exact (`DCIM/WorkMedia`)
- Handle recording lifecycle and stop states carefully
- Preserve minimal UI clarity over visual clutter
