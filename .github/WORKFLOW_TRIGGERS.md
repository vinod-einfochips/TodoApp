# GitHub Actions Workflow Triggers

## Current Configuration

All workflows now trigger **only on Pull Requests** (not on direct commits to branches).

### Workflows

#### 1. **android-ci.yml** - Android CI
**Triggers:**
- Pull requests to `main` or `develop` branches
- When PR is: opened, updated (new commits), or reopened

**Runs:**
- Unit tests
- Debug & Release APK builds
- Lint checks
- Status check (ensures all jobs pass)

---

#### 2. **android-build.yml** - Android Build and Test
**Triggers:**
- Pull requests to `main`, `develop`, or `master` branches
- When PR is: opened, updated (new commits), or reopened

**Runs:**
- Unit tests
- Debug APK build
- Lint checks
- Uploads test results and APK artifacts
- Comments on PR with build status

---

#### 3. **build-dev-apk.yml** - Build Dev APK
**Triggers:**
- Pull requests to `develop` branch
- When PR is: opened, updated (new commits), or reopened
- Manual trigger via workflow_dispatch (Actions tab in GitHub)

**Runs:**
- Unit tests (must pass before building)
- Debug & Release APK builds
- Uploads APK artifacts with 30-day retention
- Shows APK sizes in job summary

---

#### 4. **pr-checks.yml** - PR Checks
**Triggers:**
- Pull requests (any branch)
- When PR is: opened, updated (new commits), or reopened

**Runs:**
- Unit tests with code coverage
- JaCoCo test report
- Coverage report on PR
- Debug APK build
- Lint checks
- Comments on PR with CI summary

---

## Workflow Behavior

### When you create a PR:
✅ All applicable workflows trigger automatically

### When you push new commits to a PR:
✅ Workflows re-run with the new code

### When you commit directly to main/develop:
❌ Workflows do NOT run (PR-only triggers)

### Manual trigger:
✅ `build-dev-apk.yml` can be triggered manually from GitHub Actions tab

---

## PR Event Types

- **opened**: When a new PR is created
- **synchronize**: When new commits are pushed to the PR branch
- **reopened**: When a closed PR is reopened

---

## To Run Workflows

1. Create a feature branch: `git checkout -b feature/my-feature`
2. Make your changes and commit
3. Push to GitHub: `git push origin feature/my-feature`
4. Create a Pull Request to `main` or `develop`
5. ✅ Workflows automatically trigger!
