Allure report - Setup & Usage (Windows, Maven + TestNG + Cucumber)

This document explains how to install Allure CLI (via Scoop or manual), how to make sure the project produces Allure results, and how to generate/serve the report.

Prerequisites
- Java (JDK 8+) and Maven installed and on PATH. Verify: mvn -v and java -version
- Windows PowerShell or CMD for running scripts

1) Project dependencies (already present in this project)
The project uses these Allure Java libraries (declared in `pom.xml`):
- io.qameta.allure:allure-testng:2.21.0
- io.qameta.allure:allure-rest-assured:2.21.0
- io.qameta.allure:allure-cucumber-jvm:2.20.0
- com.github.automatedowl:allure-environment-writer:1.0.0 (utility to write environment.xml)

These dependencies are already included in the repository `pom.xml`. Do not change them unless you know what you're doing.

2) How tests produce Allure results
When tests run they must produce JSON files into `target/allure-results` (Allure adapters on the classpath create these automatically). Typical flows:
- TestNG/Cucumber with Allure adapters will create `target/allure-results/*.json` and attachments
- Some code calls `Allure.addAttachment(...)` to add attachments

3) Installing Allure CLI on Windows
You need the Allure CLI to generate or serve the HTML report from `target/allure-results`.

Recommended: install with Scoop (Windows package manager).

- Install Scoop (if you don't have it):
  - Open PowerShell (run as Administrator only if you can't set execution policy)
  - Run:

```powershell
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser -Force; iwr -useb get.scoop.sh | iex
```

- Add main bucket and install allure:

```powershell
scoop bucket add main; scoop install allure
```

Notes about Scoop:
- Scoop installs tools into your user profile (no admin required by default) and adds them to PATH.
- If `scoop` is not recognized after installation, restart PowerShell.

Manual install (alternative):
- Download Allure zip from https://github.com/allure-framework/allure2/releases
- Unpack and add the `bin` folder to your PATH environment variable

4) Running tests and viewing report
- Run tests (example, with default tag):

```powershell
cd "D:\Coding Work\Automation_FrameWork_Project"
.\runfw.bat
```

- To generate and serve the Allure report after a run, use the runner and add `y` argument (or `report`):

```powershell
.\runfw.bat y
```

This will:
- Run mvn with the configured cucumber/testng plugin options
- If `y` is provided, remove `target/allure-results` before the run and call `allure serve target/allure-results` if Allure CLI is on PATH

If you want to generate a static report instead of serving:

```powershell
allure generate target/allure-results --clean -o target/allure-report
Start target\allure-report\index.html
```

5) Common issues and troubleshooting
- "Allure CLI not found" — install via Scoop or manual and ensure `allure` is on PATH.
- "environment.xml not found" — ensure `target/allure-results` exists; some helper code writes `environment.xml` at runtime. If path has spaces make sure it's quoted in any scripts.
- Permissions — if your project path has spaces or special characters, wrap paths with quotes.

6) Security & versions
- This README uses the Allure versions declared in `pom.xml`. You can upgrade Allure artifacts, but check compatibility between cucumber/testng adapters and the Allure CLI.

7) Reverting accidental changes
- If you changed `pom.xml` and need to revert to the previous version, use your VCS (git checkout) or let me know and I will revert safely.

If you want, I can:
- Add a small script `allure-verify.bat` that runs mvn, ensures `target/allure-results` exists, and runs `allure generate` (non-interactive)
- Revert any changed files to the previous state if you prefer (I can do that now)

---

