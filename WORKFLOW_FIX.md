# GitHub Actions Workflow Fix

## Problem
The GitHub Actions workflows were failing with the error:
```
Cannot locate tasks that match '':app:testDebugUnitTest'  ' as project ''' not found in root project 'TodoApp'.
```

## Root Cause
There were **two issues**:

1. **Multi-module project**: The TodoApp project is a multi-module Gradle project with an `app` module. The Gradle tasks like `testDebugUnitTest`, `assembleDebug`, etc., are specific to the `app` module, not the root project.

2. **YAML parsing issue**: In YAML, when a string contains a colon (`:`) followed by a space, it can be interpreted as a key-value pair separator. The command `./gradlew :app:testDebugUnitTest` was being misinterpreted by the YAML parser, causing the task name to be corrupted with extra quotes and spaces.

## Solution
1. Updated all Gradle task commands to use the correct module path prefix `:app:`
2. **Wrapped all Gradle commands in single quotes** to prevent YAML from misinterpreting the colons

## Changes Made

### 1. android-build.yml
- `run: ./gradlew testDebugUnitTest` → `run: './gradlew :app:testDebugUnitTest'`
- `run: ./gradlew assembleDebug` → `run: './gradlew :app:assembleDebug'`
- `run: ./gradlew lintDebug` → `run: './gradlew :app:lintDebug'`

### 2. android-ci.yml
- `run: ./gradlew test` → `run: './gradlew :app:testDebugUnitTest'`
- `run: ./gradlew assembleDebug` → `run: './gradlew :app:assembleDebug'`
- `run: ./gradlew assembleRelease` → `run: './gradlew :app:assembleRelease'`
- `run: ./gradlew lint` → `run: './gradlew :app:lintDebug'`

### 3. pr-checks.yml
- `run: ./gradlew testDebugUnitTest jacocoTestReport` → `run: './gradlew :app:testDebugUnitTest :app:jacocoTestReport'`
- `run: ./gradlew assembleDebug` → `run: './gradlew :app:assembleDebug'`
- `run: ./gradlew lintDebug` → `run: './gradlew :app:lintDebug'`

**Key Point**: All commands are now wrapped in **single quotes** to prevent YAML from parsing the colons as key-value separators.

## Verification
To verify the fix locally, you can run:
```bash
# This should work now
./gradlew :app:testDebugUnitTest

# Or list all available tasks
./gradlew tasks --all
```

## Module Structure
The project follows this structure:
```
TodoApp (root project)
└── app (Android application module)
```

All Android-specific Gradle tasks are available in the `app` module, which is why they need to be prefixed with `:app:` when running from the root project directory.
