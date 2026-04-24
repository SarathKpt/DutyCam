# Architecture Research: DutyCam

## Components

- **UI Layer (Compose)**: Renders preview container, mode controls, shutter controls, status feedback.
- **Camera Layer (CameraX Session)**: Owns Preview, ImageCapture, VideoCapture and lens/torch control.
- **Storage Layer (MediaStore Writer)**: Creates output options with display name, MIME type, and `RELATIVE_PATH`.
- **Permission Layer**: Manages camera/audio permission flow and recovery states.

## Data Flow

1. UI mode + intent (`photo` or `video`) triggers capture action.
2. Camera layer executes capture call and emits result/error.
3. Storage layer writes to MediaStore with `DCIM/WorkMedia` relative path.
4. UI receives completion state and surfaces confirmation/toast.

## Suggested Build Order

1. Basic project scaffold + permissions
2. Camera preview binding
3. Photo capture to WorkMedia
4. Video recording to WorkMedia
5. Premium minimal UI pass and UX polish
