# Requirements: DutyCam

**Defined:** 2026-04-23
**Core Value:** Every capture should be instant and reliably saved to the WorkMedia album without any manual file management.

## v1 Requirements

### Capture Core

- [ ] **CAP-01**: User can open app and see live camera preview within the main screen.
- [ ] **CAP-02**: User can capture a photo with a single shutter press.
- [ ] **CAP-03**: User can start and stop video recording with clear UI state.
- [ ] **CAP-04**: User can switch between rear and front camera.
- [ ] **CAP-05**: User can toggle flash/torch state where supported.

### Storage Routing

- [ ] **STO-01**: Photo captures are written via MediaStore with `RELATIVE_PATH` set to `DCIM/WorkMedia`.
- [ ] **STO-02**: Video captures are written via MediaStore with `RELATIVE_PATH` set to `DCIM/WorkMedia`.
- [ ] **STO-03**: Captured media appears in gallery apps under the WorkMedia album path.
- [ ] **STO-04**: Capture filenames are unique and timestamp-based to prevent collisions.

### UX and Permissions

- [ ] **UX-01**: User is prompted for required camera permission on first use.
- [ ] **UX-02**: User is prompted for microphone permission when needed for video with audio.
- [ ] **UX-03**: If permissions are denied, user sees a recoverable in-app explanation state.
- [ ] **UX-04**: UI follows a premium minimalist camera layout with clear mode focus.
- [ ] **UX-05**: User can switch between photo and video mode using a bottom mode selector.

## v2 Requirements

### Media Workflow

- [ ] **MED-01**: User can see a thumbnail of the most recent capture on the camera screen.
- [ ] **MED-02**: User can tap the thumbnail to open a minimalist full-screen viewer.
- [ ] **MED-03**: User can share or delete the media directly from the viewer.

### Professional Composition

- [ ] **PRO-01**: User can toggle a 3x3 grid overlay for framing.
- [ ] **PRO-02**: User can see a horizontal level indicator to prevent tilted shots.

## Out of Scope

| Feature | Reason |
|---------|--------|
| Cloud backup/sync | Not required for local work capture promise in v1 |
| Built-in editor (crop/trim/filters) | Adds complexity outside core routing and capture flow |
| Account/login system | No user identity requirement for v1 |
| iOS version | Android-only goal |

## Traceability

| Requirement | Phase | Status |
|-------------|-------|--------|
| CAP-01 | Phase 1 | Pending |
| CAP-02 | Phase 2 | Pending |
| CAP-03 | Phase 2 | Pending |
| CAP-04 | Phase 2 | Pending |
| CAP-05 | Phase 3 | Validated |
| UX-04 | Phase 3 | Validated |
| UX-05 | Phase 3 | Validated |
| MED-01 | Phase 4 | Pending |
| MED-02 | Phase 4 | Pending |
| MED-03 | Phase 4 | Pending |
| PRO-01 | Phase 5 | Pending |
| PRO-02 | Phase 5 | Pending |

**Coverage:**
- v1 requirements: 14 total
- Mapped to phases: 14
- Unmapped: 0

---
*Requirements defined: 2026-04-23*
*Last updated: 2026-04-23 after initial definition*
