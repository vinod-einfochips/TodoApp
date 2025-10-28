# Setup Guide - Todo App

## Prerequisites

Before you begin, ensure you have the following installed:

- **Android Studio** (latest version - 2023.1 or later)
- **Java Development Kit (JDK)** 11 or higher
- **Android SDK** with API level 34
- **Gradle** 8.1 or later (included with Android Studio)

## Installation Steps

### 1. Clone or Download the Project

```bash
# If cloning from a repository
git clone <repository-url>
cd windsurf-project-2

# Or extract the project folder
```

### 2. Open in Android Studio

1. Launch Android Studio
2. Click **File** → **Open**
3. Navigate to the project folder and select it
4. Click **Open**

Android Studio will automatically:
- Detect the Gradle project structure
- Download necessary dependencies
- Build the project

### 3. Configure Android SDK

If you haven't set up the Android SDK:

1. Go to **Android Studio** → **Preferences** (macOS) or **File** → **Settings** (Windows/Linux)
2. Navigate to **Appearance & Behavior** → **System Settings** → **Android SDK**
3. Install the following:
   - **SDK Platforms**: Android 14 (API 34)
   - **SDK Tools**: 
     - Android SDK Build-Tools 34.0.0
     - Android Emulator
     - Android SDK Platform-Tools

### 4. Create an Android Virtual Device (Emulator)

1. In Android Studio, go to **Tools** → **Device Manager**
2. Click **Create Device**
3. Select a device (e.g., Pixel 6)
4. Select API level 34 (Android 14)
5. Click **Finish**

### 5. Build the Project

```bash
# Using Gradle wrapper (recommended)
./gradlew build

# Or in Android Studio:
# Build → Make Project (Ctrl+F9 / Cmd+F9)
```

### 6. Run the App

**Option 1: Using Android Studio**
1. Click the **Run** button (green play icon) in the toolbar
2. Select your emulator or connected device
3. Click **OK**

**Option 2: Using Gradle**
```bash
./gradlew installDebug
```

## Project Structure Overview

```
windsurf-project-2/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/todoapp/
│   │   │   │   ├── data/              # Database layer
│   │   │   │   └── ui/                # UI layer
│   │   │   ├── res/                   # Resources
│   │   │   └── AndroidManifest.xml
│   │   └── test/                      # Unit tests
│   ├── build.gradle                   # App-level build config
│   └── proguard-rules.pro
├── build.gradle                       # Project-level build config
├── settings.gradle                    # Project settings
├── gradle.properties                  # Gradle properties
├── README.md                          # Project documentation
├── ARCHITECTURE.md                    # Architecture guide
└── SETUP.md                           # This file
```

## Troubleshooting

### Issue: Gradle Build Fails

**Solution:**
```bash
# Clean and rebuild
./gradlew clean build

# Or in Android Studio:
# Build → Clean Project
# Build → Rebuild Project
```

### Issue: Android SDK Not Found

**Solution:**
1. Go to **Preferences** → **Android SDK**
2. Ensure SDK path is correctly set
3. Download required SDK versions

### Issue: Emulator Won't Start

**Solution:**
1. Check if virtualization is enabled in BIOS
2. Try creating a new AVD (Android Virtual Device)
3. Use a physical device instead

### Issue: App Crashes on Launch

**Solution:**
1. Check Logcat for error messages
2. Ensure minimum SDK is 24 or higher
3. Clear app data: **Settings** → **Apps** → **Todo App** → **Clear Data**

## Development Workflow

### Making Changes

1. **Edit Code**: Modify Kotlin files in `app/src/main/java/`
2. **Edit Resources**: Modify XML files in `app/src/main/res/`
3. **Build**: Press `Ctrl+F9` (Windows/Linux) or `Cmd+F9` (macOS)
4. **Run**: Press `Shift+F10` (Windows/Linux) or `Ctrl+R` (macOS)

### Hot Reload

Android Studio supports hot reload for quick iteration:
- **Apply Changes**: `Ctrl+Alt+F10` (Windows/Linux) or `Cmd+Alt+F10` (macOS)
- **Apply Code Changes**: Recompiles and applies code changes without restarting the app

### Debugging

1. Set breakpoints by clicking on line numbers
2. Run app in debug mode: **Run** → **Debug 'app'**
3. Use Logcat to view console output: **View** → **Tool Windows** → **Logcat**

## Building for Release

### Generate Signed APK

1. Go to **Build** → **Generate Signed Bundle/APK**
2. Select **APK**
3. Click **Next**
4. Create a new keystore or select existing one
5. Fill in keystore details
6. Select **release** build variant
7. Click **Finish**

### Generate AAB (Android App Bundle)

1. Go to **Build** → **Generate Signed Bundle/APK**
2. Select **Android App Bundle**
3. Follow the same steps as above

## Dependencies

The project uses the following key dependencies:

```gradle
// Core Android
androidx.appcompat:appcompat:1.6.1
androidx.constraintlayout:constraintlayout:2.1.4

// Lifecycle
androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2
androidx.lifecycle:lifecycle-livedata-ktx:2.6.2

// Room Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// Navigation
androidx.navigation:navigation-fragment-ktx:2.7.6
androidx.navigation:navigation-ui-ktx:2.7.6

// Material Design
com.google.android.material:material:1.11.0

// Coroutines
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3
```

## IDE Configuration

### Code Style

The project follows Kotlin official code style:
- 4 spaces for indentation
- Line length: 120 characters
- Naming conventions: camelCase for variables, PascalCase for classes

### Import Organization

Android Studio automatically organizes imports. To configure:
1. **Preferences** → **Editor** → **Code Style** → **Kotlin**
2. Configure import settings as needed

## Performance Tips

1. **Use Emulator with Hardware Acceleration**: Faster app performance
2. **Enable Instant Run**: Faster iteration during development
3. **Use Profiler**: Monitor CPU, memory, and network usage
4. **Optimize Gradle**: Use `org.gradle.parallel=true` in gradle.properties

## Next Steps

1. Read the **README.md** for feature overview
2. Review **ARCHITECTURE.md** for design patterns
3. Explore the codebase and understand the MVVM pattern
4. Start making modifications and adding features

## Additional Resources

- [Android Developer Documentation](https://developer.android.com/docs)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [ViewModel Guide](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [LiveData Guide](https://developer.android.com/topic/libraries/architecture/livedata)
- [Navigation Component](https://developer.android.com/guide/navigation)

## Support

For issues or questions:
1. Check the troubleshooting section above
2. Review the official Android documentation
3. Check Logcat for error messages
4. Search Stack Overflow for similar issues

---

Happy coding! 🚀
