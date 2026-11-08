@echo off
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0smoke-test.ps1" %*
if errorlevel 1 (
    echo.
    echo Smoke test FAILED!
    if "%~1"=="" pause
    exit /b 1
)
if "%~1"=="" pause
