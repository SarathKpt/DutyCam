# DutyCam

## What This Is

DutyCam is a professional Android camera app for users who need work captures separated from personal media. It records photos and videos directly into `DCIM/WorkMedia` so gallery apps surface a dedicated WorkMedia album automatically. The product experience is premium and minimal, with a GCam-inspired layout focused on speed and confidence during capture.

## Core Value

Every capture should be instant and reliably saved to the WorkMedia album without any manual file management.

## Requirements

### Validated

- [x] Capture photos with low-latency shutter response (v1)
- [x] Capture videos with audio and clear recording state (v1)
- [x] Save all captures under `DCIM/WorkMedia` using MediaStore (v1)
- [x] Present a premium, minimalist camera UI inspired by GCam (v1)
- [x] Support fast switching between photo and video modes (v1)
- [x] Keep permissions flow clear and non-disruptive (v1)

### Active

- [ ] **MED-01**: Quick thumbnail preview of the most recent capture
- [ ] **MED-02**: Minimalist in-app media viewer for WorkMedia album
- [ ] **MED-03**: Basic management (Share/Delete) directly from preview
- [ ] **PRO-01**: Rule-of-thirds grid overlay for professional framing
- [ ] **PRO-02**: Horizon level indicator using device orientation sensors

### Out of Scope

- iOS app - Android-only scope for v1
- Cloud sync and account system - not required for local capture workflow
- AI enhancement filters - not required for core capture reliability
- In-app media editor - defer until core camera workflow is validated

## Context

The repository started empty and requires full app scaffolding. The app targets modern Android storage behavior so MediaStore writes can create and maintain `DCIM/WorkMedia` as an album in the gallery. Camera stack is based on CameraX (Preview, ImageCapture, VideoCapture/Recorder) and a Jetpack Compose UI shell around a `PreviewView`.

## Constraints

- **Platform**: Android only - app must run as a native Android application.
- **Storage Contract**: Write captures to `DCIM/WorkMedia` - this is the principal product promise.
- **UI Direction**: Premium minimalist/GCam-inspired - visual and interaction style must stay intentionally restrained.
- **Permissions**: Runtime camera/audio permission model - app must degrade gracefully when denied.
- **Scope**: Functional v1 first - avoid non-essential features that add risk to capture reliability.

## Key Decisions

| Decision | Rationale | Outcome |
|----------|-----------|---------|
| Use CameraX (`camera2`, `lifecycle`, `video`, `view`) | Stable modern API for preview + photo + video | - Pending |
| Use MediaStore with `RELATIVE_PATH = DCIM/WorkMedia` | Ensures gallery-compatible album routing | - Pending |
| Build UI in Jetpack Compose with embedded `PreviewView` | Fast, maintainable modern Android UI while preserving camera preview performance | - Pending |
| Target modern Android storage behavior (Android 10+) | Simplifies reliable album writes without legacy external storage edge cases | - Pending |

## Evolution

This document evolves at phase transitions and milestone boundaries.

**After each phase transition** (via `$gsd-transition`):
1. Requirements invalidated? -> Move to Out of Scope with reason
2. Requirements validated? -> Move to Validated with phase reference
3. New requirements emerged? -> Add to Active
4. Decisions to log? -> Add to Key Decisions
5. "What This Is" still accurate? -> Update if drifted

**After each milestone** (via `$gsd-complete-milestone`):
1. Full review of all sections
2. Core Value check - still the right priority?
3. Audit Out of Scope - reasons still valid?
4. Update Context with current state

---
*Last updated: 2024-04-24 for Milestone v2 transition*
