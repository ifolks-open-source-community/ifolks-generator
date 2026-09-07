@echo off
set /p NEW_VERSION=Enter new version (e.g. 1.1.0 or 1.2.0-SNAPSHOT): 

if "%NEW_VERSION%"=="" (
    echo No version entered. Aborting.
    pause
    exit /b 1
)

echo.
echo ========================================================
echo Updating Maven POMs across all modules to %NEW_VERSION%...
echo ========================================================
call mvn versions:set -DnewVersion=%NEW_VERSION% -DgenerateBackupPoms=false

echo.
echo ========================================================
echo Synchronizing skeleton templates with generator version...
echo ========================================================
powershell -NoProfile -Command "(Get-Content 'generator-rest-skeletons\src\main\resources\root\pom.xml.vm') -replace '<ifolks\.generator\.version>.*?</ifolks\.generator\.version>', ('<ifolks.generator.version>%NEW_VERSION%</ifolks.generator.version>') | Set-Content 'generator-rest-skeletons\src\main\resources\root\pom.xml.vm'"

echo.
echo ========================================================
echo Version successfully updated to %NEW_VERSION%!
echo ========================================================
pause