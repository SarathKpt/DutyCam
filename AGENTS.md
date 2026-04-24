# DutyCam Agent Guide

This repository uses a pragmatic build-first workflow:

1. Define context in `.planning/PROJECT.md`.
2. Keep scope/checklist in `.planning/REQUIREMENTS.md`.
3. Execute phase goals from `.planning/ROADMAP.md`.
4. Update `.planning/STATE.md` as work progresses.

## Current Delivery Target

Build a functional Android camera app named `DutyCam` with:

- Photo + video capture
- Automatic routing to `DCIM/WorkMedia`
- Premium minimalist GCam-inspired UX

## Working Rules

- Prioritize capture reliability over feature breadth.
- Keep storage routing contract (`DCIM/WorkMedia`) intact.
- Keep UI minimal and intentionally camera-first.
