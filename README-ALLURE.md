Allure report — setup and usage
================================

This document explains how to install and use Allure CLI to generate and view Allure reports for this project, which Allure-related dependencies were added to the project POM, and the exact step-by-step commands (PowerShell) to reproduce the workflow locally.

Quick plan / checklist
----------------------
- [ ] Prerequisites (Java, Maven, PowerShell)
- [x] Install Allure CLI (Scoop / Chocolatey / Manual)
- [x] Where Allure results are produced
- [x] How to generate the HTML report (CLI and Maven plugin)
- [x] Important POM/dependency notes and service files added to the project
- [x] Troubleshooting and revert steps

Prerequisites
-------------
- Java (JDK 8+) and Maven installed and available on PATH (`java -version`, `mvn -v`).
- PowerShell (Windows PowerShell v5.1 is fine). Use an elevated prompt only when required by your environment.
- Internet access to download packages (Scoop/Chocolatey) and Maven artifacts.

Install Allure CLI (recommended: Scoop)
--------------------------------------
I recommend using Scoop (user-level package manager) because it installs Allure without requiring admin rights and keeps upgrades simple.

1) Install Scoop (if you don't have it):

```powershell
# Allow running scripts for current user and install Scoop
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser -Force
iwr -useb get.scoop.sh | iex
```

2) Install Allure via Scoop:

```powershell
# add common buckets (if needed)
scoop bucket add main
scoop bucket add extras

# install Allure
scoop install allure

# verify
allure --version
```

Alternative: Chocolatey
-----------------------
If you prefer Chocolatey (requires admin rights to install packages):

```powershell
choco install allure -y
allure --version
```

Alternative: Manual download
----------------------------
Download the release zip from the Allure GitHub releases page and unpack it. Add the `bin` folder to your PATH.

- URL: https://github.com/allure-framework/allure2/releases

Project helper script (already added)
------------------------------------
A helper script `scripts/install-allure.ps1` was added which tries to install Scoop and then Allure, with a fallback to Chocolatey. Run it like:

```powershell
powershell -ExecutionPolicy RemoteSigned -File .\scripts\install-allure.ps1
```

Dependencies and changes in the project
--------------------------------------
The repository now includes the following Allure-related dependencies in `pom.xml` (versions used here):

- `io.qameta.allure:allure-cucumber-jvm:2.20.0`
- `io.qameta.allure:allure-testng:2.21.0`
- `io.qameta.allure:allure-rest-assured:2.21.0`
- `io.qameta.allure:allure-maven:2.11.2` (maven plugin used to generate reports)
- `com.github.automatedowl:allure-environment-writer:1.0.0` (utility that can create an `environment.xml` file in `allure-results`)

Notes about a small, minimal runtime change made to the POM:
- Some `src/main/java` classes referenced TestNG APIs (for retries/listeners). To allow those main sources to compile without refactoring immediately, `org.testng:testng` was made available in compile/runtime (scope left as default) rather than test-scoped. This is a small, temporary change — a cleaner approach is to move TestNG-only classes to `src/test/java` so the main code doesn't depend on test libraries.

Files added/modified to enable Allure reporting
----------------------------------------------
- `pom.xml` — Allure dependencies and `allure-maven` plugin (minimal edits).
- `src/test/java/example/AllureTestNGExample.java` — a small example TestNG test that uses Allure attachments (for verification).
- `src/test/resources/META-INF/services/org.testng.ITestListener` — contains `io.qameta.allure.testng.AllureTestNg` to register the Allure TestNG listener via ServiceLoader.
- `src/test/resources/META-INF/services/org.testng.ITestNGListener` — same listener for compatibility.
- `scripts/install-allure.ps1` — helper to install Scoop + Allure (already added).

How Allure results are produced (default behavior)
-------------------------------------------------
- Allure writes JSON result files and attachments to the results directory. By default, the Allure Java lifecycle writes to `./allure-results` (project root) unless you set a different `allure.results.directory` system property.
- In this project the example test produced files in:

```
D:\Coding Work\Automation_FrameWork_Project\allure-results
```

Run tests to produce results (PowerShell)
----------------------------------------
- Run a single example test (fast verification):

```powershell
cd "D:\Coding Work\Automation_FrameWork_Project"
# run single TestNG class
mvn "-Dtest=example.AllureTestNGExample" test
```

- Or run your full test suite (as you normally do):

```powershell
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng.xml
```

Generate the Allure HTML report
------------------------------
Option A — Use Allure CLI (recommended for local interactive viewing):

```powershell
cd "D:\Coding Work\Automation_FrameWork_Project"
# serve (start local server & open browser)
allure serve .\allure-results

# OR generate static HTML to a folder
allure generate .\allure-results --clean -o .\target\allure-report
Start-Process .\target\allure-report\index.html
```

Option B — Use the Allure Maven plugin (useful in CI or as part of a maven step):

```powershell
# generate a report using the plugin and an explicit results directory
mvn io.qameta.allure:allure-maven:2.11.2:report "-Dallure.results.directory=D:\Coding Work\Automation_FrameWork_Project\allure-results"
# the plugin writes a static site, by default to target/site/allure-maven-plugin
# Example: open the generated file
Start-Process "D:\Coding Work\Automation_FrameWork_Project\target\site\allure-maven-plugin\index.html"
```

Important notes & gotchas
-------------------------
- Paths with spaces: your workspace path contains spaces (`D:\Coding Work\...`). Always quote paths in PowerShell commands.
- Allure results location: Allure Java APIs may write results to `allure-results` (project root) by default. You may set a specific directory by passing a system property to surefire, e.g. in `pom.xml`:

```xml
<systemPropertyVariables>
  <allure.results.directory>${project.build.directory}/allure-results</allure.results.directory>
</systemPropertyVariables>
```

- If `allure-results` is missing after a test run, verify that the Allure listener/adapter is on the test runtime classpath and that code calls `Allure` APIs. The listener was registered in this project using ServiceLoader files in `src/test/resources/META-INF/services` so it should be available when tests run.

Security / dependency advisories
--------------------------------
A dependency scanner flagged some transitive vulnerabilities coming from older transitive libraries (for example, `com.google.guava:guava` brought in by `allure-environment-writer`) and a warning on `commons-lang3`. These are advisories and do not block building or report generation.

Options to address advisories safely:
- Exclude the transitive vulnerable artifact and add a newer safe direct dependency.
- Replace `allure-environment-writer` with a small custom utility that writes `environment.xml` (simple XML with environment key/value pairs).

Revert changes (if you want the repo back exactly)
--------------------------------------------------
If you want to undo the small changes I made (POM edits and added files) and you use Git, run:

```powershell
# from project root (if git is configured)
git checkout -- pom.xml
git checkout -- src/test/java/example/AllureTestNGExample.java
git checkout -- src/test/resources/META-INF/services/org.testng.ITestListener
git checkout -- src/test/resources/META-INF/services/org.testng.ITestNGListener
git checkout -- scripts/install-allure.ps1
```

If you don't use Git, tell me and I will prepare a reverse patch.

Troubleshooting quick checklist
-------------------------------
- `allure` not found: open a new PowerShell window (PATH updates from Scoop apply on new sessions). Verify with `allure --version`.
- No `allure-results` files after tests: confirm example test ran and listener was active. Check `target/surefire-reports` for test logs and confirm packaging of `META-INF/services` files in `target/test-classes/META-INF/services`.
- Report generation fails with missing files: double-check the path passed to `allure generate` or to the Maven plugin and ensure you included `--clean` if you want a fresh build.

Need help automating?
---------------------
I can add a convenience script `scripts/view-allure.ps1` that will:
- optionally run tests,
- regenerate the Allure static report using the CLI or Maven plugin,
- open the generated report in your default browser.

If you want that or prefer I revert any changes, tell me which option and I'll implement it.

---

Created by repository setup assistant — follow the commands above to install Allure and generate the report locally.
