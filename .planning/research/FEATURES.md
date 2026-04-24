# Feature Research: DutyCam

## Table Stakes

- Fast camera preview startup
- Photo capture
- Video capture with recording indicator
- Front/back camera switching
- Runtime permission prompts and graceful fallback UI
- Gallery-visible storage with deterministic folder routing

## Differentiators

- Dedicated work-only album semantics (`DCIM/WorkMedia`)
- Premium minimalist UI tuned for quick operation
- Capture-first layout with low visual noise and mode focus

## Anti-Features (v1)

- Sticker/filter marketplace
- Built-in social sharing network
- Complex media editing timeline

## Complexity Notes

- Folder routing is straightforward on Android 10+ via MediaStore `RELATIVE_PATH`.
- Video capture has higher error/edge-case surface than photo capture and requires careful state handling.
