@REM ----------------------------------------------------------------------------
@REM bat file to execute generator-bash
@REM ----------------------------------------------------------------------------

@echo off

set IGEN_VERSION=1.0.0-M1
set IGEN_RUNNABLE_JAR=%IGEN_HOME%\boot\generator-bash-%IGEN_VERSION%.jar
set IGEN_LIB=%IGEN_HOME%\lib
set IGEN_CLASSPATH=%IGEN_RUNNABLE_JAR%;%IGEN_LIB%\*

set RUNNABLE_CLASS=org.ifolks.generator.bash.launcher.MainLauncher

echo current directory : %CD%
echo generator home : %IGEN_HOME%
echo version : %IGEN_VERSION%

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OK_JAVA_HOME

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto ERROR

:OK_JAVA_HOME
if exist "%JAVA_HOME%\bin\java.exe" goto CHECK_JAVA_HOME

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = %JAVA_HOME%
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto ERROR

:CHECK_JAVA_HOME
if not "%IGEN_HOME%"=="" goto OK_IGEN_HOME

echo.
echo ERROR: IGEN_HOME not found in your environment.
echo Please set the IGEN_HOME variable in your environment to match the
echo location of iFolks generator installation
echo.
goto ERROR

:OK_IGEN_HOME
if exist "%IGEN_HOME%\bin\IGEN.bat" goto CHECK_IGEN_HOME

echo.
echo ERROR: IGEN_HOME is set to an invalid directory.
echo IGEN_HOME = %IGEN_HOME%
echo Please set the IGEN_HOME variable in your environment to match the
echo location of iFolks generator installation
echo.
goto ERROR

@REM ==== END VALIDATION ====
:CHECK_IGEN_HOME


:GET_IGEN_CMD_LINE_ARGS
@REM ==== START COMMAND LINE ARGS ====
set IGEN_CMD_LINE_ARGS=%1
set DATABASE=%2

@REM ==== END COMMAND LINE ARGS ====


:RUN
echo start IGEN
"%JAVA_HOME%\bin\java" -classpath %IGEN_CLASSPATH% %RUNNABLE_CLASS% "%IGEN_CMD_LINE_ARGS%" "%CD%" "%DATABASE%"
echo end IGEN
goto END



:ERROR
echo FAILED

:END
echo END