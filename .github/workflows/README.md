# GitHub Actions CI/CD Workflows

This directory contains GitHub Actions workflows for continuous integration and deployment.

## Workflows

### 1. `android-ci.yml` - Main CI Pipeline

**Triggers:**
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop` branches

**Jobs:**

#### Test Job
- Runs all unit tests
- Uploads test results and reports
- Comments test results on PR

#### Build Job
- Builds debug and release APKs
- Uploads APK artifacts
- Only runs if tests pass

#### Lint Job
- Runs Android lint checks
- Uploads lint reports

#### Status Check Job
- Verifies all jobs passed
- Provides summary of CI status

**Artifacts Generated:**
- `test-results`: HTML test reports
- `test-report`: XML test results
- `app-debug`: Debug APK
- `app-release`: Release APK (unsigned)
- `lint-results`: Lint report HTML

---

### 2. `pr-checks.yml` - Enhanced PR Validation

**Triggers:**
- Pull request opened, synchronized, or reopened

**Features:**
- ✅ Validates Gradle wrapper
- ✅ Runs unit tests with code coverage
- ✅ Generates JaCoCo coverage report
- ✅ Comments coverage on PR
- ✅ Enforces minimum coverage thresholds
- ✅ Builds debug APK
- ✅ Runs lint checks
- ✅ Posts comprehensive PR comment with summary

**Coverage Thresholds:**
- Overall coverage: 40% minimum
- Changed files: 60% minimum

**Artifacts Generated:**
- Build artifacts (APKs, reports, coverage)

---

## Setup Instructions

### 1. Enable GitHub Actions

1. Go to your repository on GitHub
2. Click on "Actions" tab
3. Enable workflows if not already enabled

### 2. Configure Branch Protection (Optional but Recommended)

1. Go to Settings → Branches
2. Add branch protection rule for `main`:
   - Require status checks to pass before merging
   - Select: "Test", "Build", "Lint"
   - Require branches to be up to date before merging

### 3. Required Secrets (None for basic setup)

The workflows use `GITHUB_TOKEN` which is automatically provided by GitHub Actions.

### 4. Enable JaCoCo Coverage (Already configured)

The `app/build.gradle` file should have JaCoCo plugin configured:

```gradle
plugins {
    id 'jacoco'
}

jacoco {
    toolVersion = "0.8.10"
}

tasks.register('jacocoTestReport', JacocoReport) {
    dependsOn 'testDebugUnitTest'
    
    reports {
        xml.required = true
        html.required = true
    }
    
    def fileFilter = [
        '**/R.class',
        '**/R$*.class',
        '**/BuildConfig.*',
        '**/Manifest*.*',
        '**/*Test*.*',
        'android/**/*.*'
    ]
    
    def debugTree = fileTree(dir: "$buildDir/intermediates/javac/debug", excludes: fileFilter)
    def mainSrc = "$projectDir/src/main/java"
    
    sourceDirectories.setFrom(files([mainSrc]))
    classDirectories.setFrom(files([debugTree]))
    executionData.setFrom(fileTree(dir: buildDir, includes: [
        'jacoco/testDebugUnitTest.exec'
    ]))
}
```

---

## Workflow Behavior

### On Pull Request

1. **Validation starts automatically**
2. **Unit tests run** - All tests must pass
3. **Code coverage calculated** - Must meet thresholds
4. **Lint checks run** - Warnings reported
5. **APK built** - Ensures build succeeds
6. **PR comment posted** - Summary of all checks
7. **Status checks updated** - Green checkmark or red X

### On Push to Main/Develop

1. **Full CI pipeline runs**
2. **Artifacts generated** - APKs available for download
3. **Reports uploaded** - Test and lint reports

---

## Viewing Results

### Test Results
1. Go to Actions tab
2. Click on the workflow run
3. Download "test-results" artifact
4. Open `index.html` in browser

### APK Downloads
1. Go to Actions tab
2. Click on the workflow run
3. Download "app-debug" or "app-release" artifact

### Coverage Report
1. Check PR comments for coverage summary
2. Download "build-artifacts" for detailed report

---

## Troubleshooting

### Tests Failing
- Check test results artifact
- Review stack traces in workflow logs
- Run tests locally: `./gradlew test`

### Build Failing
- Check Gradle build logs
- Verify dependencies are correct
- Run locally: `./gradlew assembleDebug`

### Coverage Below Threshold
- Add more unit tests
- Focus on changed files
- Review coverage report to find gaps

### Lint Warnings
- Download lint report artifact
- Fix issues: `./gradlew lintDebug`
- Some warnings can be suppressed if justified

---

## Local Testing

Before pushing, run these commands locally:

```bash
# Run unit tests
./gradlew test

# Run tests with coverage
./gradlew testDebugUnitTest jacocoTestReport

# Build APK
./gradlew assembleDebug

# Run lint
./gradlew lintDebug

# Run all checks
./gradlew test assembleDebug lintDebug
```

---

## Customization

### Adjust Coverage Thresholds

Edit `.github/workflows/pr-checks.yml`:

```yaml
min-coverage-overall: 40  # Change this value
min-coverage-changed-files: 60  # Change this value
```

### Change Trigger Branches

Edit workflow files:

```yaml
on:
  pull_request:
    branches: [ main, develop, feature/* ]  # Add more branches
```

### Add More Jobs

Add new jobs to `android-ci.yml`:

```yaml
  my-custom-job:
    name: My Custom Job
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v4
      # Add your steps here
```

---

## Best Practices

1. ✅ **Always run tests locally before pushing**
2. ✅ **Keep tests fast** - CI should complete in < 10 minutes
3. ✅ **Fix failing tests immediately** - Don't let them accumulate
4. ✅ **Maintain good coverage** - Aim for > 70%
5. ✅ **Address lint warnings** - Keep code quality high
6. ✅ **Review CI logs** - Understand why things fail

---

## Status Badges

Add these to your README.md:

```markdown
![Android CI](https://github.com/YOUR_USERNAME/YOUR_REPO/workflows/Android%20CI/badge.svg)
![PR Checks](https://github.com/YOUR_USERNAME/YOUR_REPO/workflows/PR%20Checks/badge.svg)
```

Replace `YOUR_USERNAME` and `YOUR_REPO` with your actual GitHub username and repository name.
