# Roadmap: DutyCam

**Created:** 2026-04-23  
**Project:** DutyCam

## Summary

- Phases: 3
- v1 requirements mapped: 14/14
- Coverage: 100%

## Phase Table

| # | Phase | Goal | Requirements |
|---|-------|------|--------------|
| 1 | Foundations and Permissions | Establish app shell, runtime permissions, and preview bootstrap | CAP-01, UX-01, UX-02, UX-03 |
| 2 | Capture and WorkMedia Routing | Implement reliable photo/video capture and save pipeline to `DCIM/WorkMedia` | CAP-02, CAP-03, CAP-04, STO-01, STO-02, STO-03, STO-04 |
| 3 | Premium Camera UX Polish | Deliver minimalist GCam-inspired interaction polish and mode ergonomics | CAP-05, UX-04, UX-05 |
| 4 | Gallery Preview & Management | Implement quick thumbnail preview and minimalist in-app media management | MED-01, MED-02, MED-03 |
| 5 | Composition Tools | Add 3x3 grid and horizon level indicator for professional shots | PRO-01, PRO-02 |

## Phase Details

### Phase 1: Foundations and Permissions

Goal: Build the base Android app shell with camera preview support and robust permission flow.

Requirements: `CAP-01`, `UX-01`, `UX-02`, `UX-03`

Success criteria:
1. App launches into camera screen and can render preview when permission is granted.
2. Camera and microphone permission requests work from in-app action flow.
3. Denied permissions show recoverable UI with retry action.
4. No crash when permissions are denied/revoked.

### Phase 2: Capture and WorkMedia Routing

Goal: Make capture fully functional for both photos and videos with deterministic storage routing.

Requirements: `CAP-02`, `CAP-03`, `CAP-04`, `STO-01`, `STO-02`, `STO-03`, `STO-04`

Success criteria:
1. Photo capture succeeds and lands under `DCIM/WorkMedia`.
2. Video recording starts/stops reliably and lands under `DCIM/WorkMedia`.
3. Lens switch works between front and back cameras.
4. Generated filenames are unique and timestamp based.
5. Captures appear in standard gallery apps after save.

### Phase 3: Premium Camera UX Polish

Goal: Apply premium minimalist visual direction and tighten interaction model to feel production-ready.

Requirements: `CAP-05`, `UX-04`, `UX-05`

Success criteria:
1. Bottom mode selector supports `Photo` and `Video` with clear active state.
2. Shutter/record control communicates state clearly and with minimal UI noise.
3. Flash/torch control is integrated and understandable.
4. Overall UI aligns with premium minimalist camera aesthetic.

### Phase 4: Gallery Preview & Management

Goal: Close the capture loop by allowing users to instantly review and manage their WorkMedia captures.

Requirements: `MED-01`, `MED-02`, `MED-03`

Success criteria:
1. Shutter row includes a thumbnail of the last captured photo/video.
2. Tapping thumbnail opens a full-screen, minimalist media viewer.
3. User can swipe to share or delete the current media.
4. Deletion correctly removes the file from `DCIM/WorkMedia`.

### Phase 5: Composition Tools

Goal: Enhance capture precision with professional visual guides.

Requirements: `PRO-01`, `PRO-02`

Success criteria:
1. User can toggle a 3x3 grid overlay from the top settings.
2. A subtle horizon level appears when the device is near 0/90/180/270 degrees.
3. Level indicator uses smooth animations to communicate tilt.

---
*Last updated: 2024-04-24 for Milestone v2*
