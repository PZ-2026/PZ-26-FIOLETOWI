$ErrorActionPreference = "Stop"

$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
$env:GRADLE_USER_HOME = Join-Path $PSScriptRoot ".gradle-user-home"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Set-Location $PSScriptRoot
.\gradlew.bat --no-daemon bootRun 1>>backend.log 2>>backend.err.log
