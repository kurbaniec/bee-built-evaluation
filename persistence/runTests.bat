@echo off
SETLOCAL EnableDelayedExpansion

REM Define the list of directories
set "dirs=test.bee.persistent.blaze test.bee.persistent.jpa test.jpa.eager test.jpa.lazy"

REM Save the current directory
set "parentDir=%CD%"

REM Loop through the directories and run gradlew test
for %%d in (%dirs%) do (
    echo.
    echo --------------------------------------
    echo Testing in directory: %%d
    echo --------------------------------------
    pushd %%d
    IF EXIST gradlew.bat (
        gradlew.bat test -i --rerun
    ) ELSE (
        echo gradlew.bat not found in %%d
    )
    popd
)

echo.
echo All tests completed.
ENDLOCAL
