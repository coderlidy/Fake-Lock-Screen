# Fake Lock Screen 🔒

[English](README.md) | [中文](README_zh.md)

[![GitHub stars](https://img.shields.io/github/stars/lee/fake-lock-screen.svg?style=social&label=Star)](https://github.com/lee/fake-lock-screen)

An innovative Android application that simulates screen locking through accessibility service.

## Features

- 🎯 Floating fake lock button with drag-and-drop functionality
- 🔓 Quick unlock with 5 taps anywhere on the screen
- 🛡️ No root permission required
- 🎨 Clean and intuitive user interface
- 📱 Compatible with all screen sizes and system versions

## Innovation Highlights

- Uses accessibility service instead of system-level permissions
- Implements a novel quick-unlock mechanism
- Provides seamless user experience with minimal impact on system resources

## Installation

### System Requirements

- Android 6.0 (API level 23) or higher
- Minimum 10MB storage space
- No root access needed

### Setup Steps

1. Download and install the APK
2. Open the app and follow the on-screen instructions
3. Enable accessibility service when prompted
4. The floating fake lock button will appear on your screen

## Usage

### Basic Operations

1. **Fake Lock**: Tap the floating fake lock button
2. **Unlock**: Tap anywhere on the black overlay 5 times quickly
3. **Move Button**: Press and drag the floating button to any position
4. **Disable Service**: Go to System Settings > Accessibility > Fake Lock Screen

### Advanced Features

- Automatic fake lock based on accessibility events
- Position memory for floating button
- System-wide overlay support

## Technical Details

### Architecture

```kotlin
// Key components
├── MainActivity              // Main UI and service management
├── LockScreenAccessibilityService  // Core fake lock functionality
├── FloatButtonView          // Draggable floating button
└── Resources               // Layouts and configurations
```

### Implementation Highlights

- Utilizes Android Accessibility Framework
- Implements custom view for floating button
- Uses Kotlin Coroutines for async operations
- Material Design 3 components

## Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.