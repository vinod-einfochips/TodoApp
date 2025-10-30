# ChatGPT PR Review Workflow - Improvements

## Issues in Original Version

### 1. **JSON Escaping Problem**
```yaml
# ❌ ORIGINAL - Breaks with quotes, newlines, special chars
"content": "'"$(cat pr_diff.txt | sed 's/"/\\"/g')"'"
```
**Problem:** `sed` only escapes quotes, but JSON needs escaping for `\n`, `\t`, `\r`, etc.

### 2. **No Size Limit**
- Large PRs can exceed OpenAI's token limits (128k tokens for gpt-4o-mini)
- No handling for huge diffs
- Can cause API failures or high costs

### 3. **Missing jq**
- Ubuntu runners don't have `jq` pre-installed
- Workflow will fail when trying to parse JSON

### 4. **No Error Handling**
- If API call fails, no error message
- No way to debug what went wrong
- Silent failures

### 5. **Security Issues**
- API key exposed in logs if curl fails
- No validation of API response

---

## Improvements in New Version

### ✅ 1. Proper JSON Escaping
```yaml
# ✅ IMPROVED - Uses jq for proper JSON encoding
DIFF_CONTENT=$(cat pr_diff.txt | jq -Rs .)
```
**Benefit:** `jq -Rs` properly escapes ALL special characters for JSON

### ✅ 2. Size Limiting
```yaml
# Truncate to 8000 lines if too large
if [ $LINES -gt 8000 ]; then
  head -n 8000 full_diff.txt >> pr_diff.txt
  echo "TRUNCATED=true" >> $GITHUB_OUTPUT
fi
```
**Benefit:** Prevents API failures and controls costs

### ✅ 3. Install jq
```yaml
- name: Install jq
  run: sudo apt-get update && sudo apt-get install -y jq
```
**Benefit:** Ensures jq is available for JSON processing

### ✅ 4. Error Handling
```yaml
# Check HTTP status code
if [ "$HTTP_CODE" -ne 200 ]; then
  echo "❌ OpenAI API call failed with status code: $HTTP_CODE"
  cat response.json
  exit 1
fi
```
**Benefit:** Clear error messages and debugging info

### ✅ 5. Better Security
```yaml
# Save response to file, check status separately
HTTP_CODE=$(curl -w "%{http_code}" -o response.json ...)
```
**Benefit:** API key not exposed in error logs

### ✅ 6. Skip Empty/Draft PRs
```yaml
if: github.event.pull_request.draft == false
```
**Benefit:** Don't waste API calls on draft PRs

### ✅ 7. Debug Artifacts
```yaml
- name: Upload artifacts on failure
  if: failure()
  uses: actions/upload-artifact@v4
```
**Benefit:** Easy debugging when things go wrong

### ✅ 8. Better Prompt
```yaml
"content": "You are a senior Android/Kotlin code reviewer. Analyze the PR diff and provide:
1. Summary of changes
2. Potential bugs or issues
3. Performance concerns
4. Security issues
5. Best practice violations
6. Suggestions for improvement"
```
**Benefit:** More structured and useful reviews

---

## Setup Required

### 1. Add OpenAI API Key Secret
1. Go to GitHub → Repository → Settings → Secrets and variables → Actions
2. Click "New repository secret"
3. Name: `OPENAI_API_KEY`
4. Value: Your OpenAI API key (starts with `sk-`)

### 2. Get OpenAI API Key
1. Go to https://platform.openai.com/api-keys
2. Create new secret key
3. Copy and save it (you won't see it again)

### 3. Cost Considerations
- **gpt-4o-mini**: ~$0.15 per 1M input tokens, ~$0.60 per 1M output tokens
- Average PR review: ~$0.01-0.05 per review
- Set usage limits in OpenAI dashboard to control costs

---

## Usage

### Replace Old File
```bash
# Remove old version
rm .github/workflows/ai-pr-review.yml

# Rename improved version
mv .github/workflows/chatgpt-pr-review-improved.yml .github/workflows/ai-pr-review.yml

# Commit
git add .github/workflows/ai-pr-review.yml
git commit -m "feat: Improve ChatGPT PR review workflow with error handling"
git push
```

### Test It
1. Merge to main/develop branch first
2. Create a new PR
3. ChatGPT will review and comment automatically

---

## Comparison Table

| Feature | Original | Improved |
|---------|----------|----------|
| JSON Escaping | ❌ sed (incomplete) | ✅ jq (proper) |
| Size Limiting | ❌ None | ✅ 8000 lines max |
| Error Handling | ❌ None | ✅ HTTP status checks |
| jq Installation | ❌ Missing | ✅ Installed |
| Draft PR Handling | ❌ Reviews drafts | ✅ Skips drafts |
| Empty Diff Check | ❌ None | ✅ Skips empty |
| Debug Support | ❌ None | ✅ Artifacts on failure |
| Security | ⚠️ Key in logs | ✅ Protected |
| Prompt Quality | ⚠️ Basic | ✅ Structured |

---

## Recommended: Use Existing Action Instead

For production use, consider using a maintained action:

```yaml
name: ChatGPT PR Review

on:
  pull_request:
    types: [opened, synchronize]

permissions:
  contents: read
  pull-requests: write

jobs:
  review:
    runs-on: ubuntu-latest
    steps:
      - uses: anc95/ChatGPT-CodeReview@main
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          OPENAI_API_KEY: ${{ secrets.OPENAI_API_KEY }}
          LANGUAGE: en
          MODEL: gpt-4o-mini
```

**Benefits:**
- ✅ Battle-tested and maintained
- ✅ Better error handling
- ✅ Incremental reviews (only changed files)
- ✅ Configurable review depth
- ✅ Multiple language support
