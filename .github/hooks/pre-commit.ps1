# Check if KtLint passes
Write-Output "Running KtLint Format"
& .\gradlew.bat ktlintFormat

Write-Output "Running KtLint Format"
& .\gradlew.bat ktlintCheck

# Exit if KtLint fails
if ($LASTEXITCODE -ne 0) {
    Write-Output "KtLint failed. Fix the issues before committing."
    exit 1
}

Write-Output "KtLint passed."
