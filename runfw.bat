@echo off
setlocal enabledelayedexpansion

REM Default tag expression
set "tag_expression=default_tag"

REM Default value for generating the report
set "generate_report=false"

REM Default value for enabling debug mode
set "enable_debug=false"

REM Normalize project_root for predictable paths
set "project_root=%~dp0"
if "%project_root:~-1%"=="\" set "project_root=%project_root:~0,-1%"

REM Parse the arguments and iterate through them
for %%a in (%*) do (
    REM Check for the "generate" argument (y or report)
    if /i "%%~a"=="y" (
        set "generate_report=true"
        if exist "%project_root%\target\allure-results" (
            rd /s /q "%project_root%\target\allure-results"
        )
    ) else (
        if /i "%%~a"=="report" (
            set "generate_report=true"
        ) else (
            if /i "%%~a"=="debug" (
                set "enable_debug=true"
                set "tag_expression=debug"
            ) else (
                if /i "%%~a"=="-cls" (
                    REM Terminate services running on port 8000
                    for /f "tokens=5" %%x in ('netstat -aon ^| find "8000"') do taskkill /F /PID %%x
                    exit /b 0
                ) else (
                    REM Anything else is treated as tag_expression (overwrites previous)
                    set "tag_expression=%%~a"
                )
            )
        )
    )
)

REM Construct the Maven command with debug options if enabled
if "%enable_debug%"=="true" (
    set "debug_options=-Dmaven.surefire.debug=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=localhost:8000"
) else (
    set "debug_options="
)

REM Pass individual Cucumber properties (cucumber.options is deprecated)
set "cucumber_features=classpath:"
set "cucumber_plugin=io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
set "cucumber_tags=@%tag_expression%"

REM Allow selecting a TestNG runner class (default to TestRunner.TestRunner)
set "test_runner=TestRunner.TestRunner"

echo mvn %debug_options% -Dtest=%test_runner% test -Denv=local -Dcucumber.features=%cucumber_features% -Dcucumber.plugin=%cucumber_plugin% -Dcucumber.filter.tags="%cucumber_tags%" -Dtestng.output=./target/testng-results.xml
call mvn %debug_options% -Dtest=%test_runner% test -Denv=local -Dcucumber.features=%cucumber_features% -Dcucumber.plugin=%cucumber_plugin% -Dcucumber.filter.tags="%cucumber_tags%" -Dtestng.output=./target/testng-results.xml

:generate_allure
if "%generate_report%"=="true" (
    REM Execute Allure command to serve the report if results exist
    if exist "%project_root%\target\allure-results" (
        if exist "%ProgramFiles%\allure\bin\allure.exe" (
            echo Serving Allure report...
            allure serve "%project_root%\target\allure-results"
        ) else (
            echo Attempting to run Allure CLI (must be on PATH)...
            allure serve "%project_root%\target\allure-results"
        )
    ) else (
        echo No allure results found at "%project_root%\target\allure-results"
    )
)

endlocal
