# Pitfalls Research: DutyCam

## Pitfall 1: Saving outside MediaStore conventions

- **Warning signs**: Files exist in storage but do not appear in gallery album.
- **Prevention**: Use MediaStore output APIs with correct MIME + `RELATIVE_PATH = DCIM/WorkMedia`.
- **Phase mapping**: Phase 2 (capture engine and storage routing).

## Pitfall 2: Video recording state leaks

- **Warning signs**: UI says recording stopped but encoder still running or callback errors after lifecycle change.
- **Prevention**: Keep a single `Recording?` reference, stop/release on lifecycle disposal, gate UI actions while state transitions.
- **Phase mapping**: Phase 2/3.

## Pitfall 3: Permission dead-end UX

- **Warning signs**: User denies permission once and cannot recover in-app.
- **Prevention**: Show concise rationale state with a retry permission action.
- **Phase mapping**: Phase 1.

## Pitfall 4: Overdesigned camera chrome

- **Warning signs**: Controls obscure preview or capture latency increases due to heavy effects.
- **Prevention**: Keep visual hierarchy minimal, prioritize shutter and mode switch clarity.
- **Phase mapping**: Phase 3.
