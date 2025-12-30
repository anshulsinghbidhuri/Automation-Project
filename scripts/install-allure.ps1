<#
  install-allure.ps1
  ------------------
  Safely installs Scoop (if missing) and then Allure CLI on Windows.

  Usage (PowerShell):
    Open a PowerShell window (no admin required for Scoop by default) and run:
      powershell -ExecutionPolicy RemoteSigned -File .\scripts\install-allure.ps1

  Behavior:
  - Detects whether Scoop is installed. If not, it installs Scoop via the official installer.
  - Adds common buckets (main, extras) and installs `allure` via Scoop.
  - If Scoop install or allure install fails, tries Chocolatey (if available) as a fallback.
  - If both fail, prints manual instructions and the GitHub releases URL.
  - Verifies the installation by running `allure --version` at the end.

  Notes:
  - This script changes the current user's execution policy for the session if needed.
  - If you run into permission issues, re-run the script from an elevated PowerShell (Run as Administrator).
#>

param()

function Write-Ok($m) { Write-Host "[OK]    $m" -ForegroundColor Green }
function Write-Info($m) { Write-Host "[INFO]  $m" -ForegroundColor Cyan }
function Write-Warn($m) { Write-Host "[WARN]  $m" -ForegroundColor Yellow }
function Write-Err($m) { Write-Host "[ERR]   $m" -ForegroundColor Red }

Write-Info "Starting Allure CLI installer helper"

# Check for scoop
$scoopCmd = Get-Command scoop -ErrorAction SilentlyContinue
if ($null -ne $scoopCmd) {
    Write-Ok "Scoop already installed: $($scoopCmd.Path)"
} else {
    Write-Info "Scoop not found. Installing Scoop (user-level)..."
    try {
        # Ensure execution policy allows the installer
        Set-ExecutionPolicy RemoteSigned -Scope CurrentUser -Force -ErrorAction Stop
        iex (iwr -useb get.scoop.sh)
        Write-Ok "Scoop installation completed"
    } catch {
        Write-Err "Scoop installer failed: $($_.Exception.Message)"
    }
}

# Refresh environment (scoop path is usually in $env:USERPROFILE\scoop\shims)
if (-not (Get-Command scoop -ErrorAction SilentlyContinue)) {
    $scoopShim = Join-Path $env:USERPROFILE "scoop\shims"
    if (Test-Path $scoopShim) {
        Write-Info "Adding Scoop shims to PATH for this session: $scoopShim"
        $env:PATH = "$scoopShim;$env:PATH"
    }
}

# Try using scoop to install allure
if (Get-Command scoop -ErrorAction SilentlyContinue) {
    try {
        Write-Info "Updating Scoop and adding buckets..."
        scoop update | Out-Null
        scoop bucket add main 2>$null | Out-Null
        scoop bucket add extras 2>$null | Out-Null
        Write-Info "Installing Allure via Scoop..."
        scoop install allure | Out-Null
        Write-Ok "Scoop installed package 'allure'"
    } catch {
        Write-Warn "Scoop installation of Allure failed: $($_.Exception.Message)"
    }
}

# If allure is not available, try Chocolatey as fallback
if (-not (Get-Command allure -ErrorAction SilentlyContinue)) {
    $choco = Get-Command choco -ErrorAction SilentlyContinue
    if ($null -ne $choco) {
        Write-Info "Attempting to install Allure via Chocolatey (requires admin)..."
        try {
            choco install allure -y | Out-Null
            Write-Ok "Chocolatey installed 'allure'"
        } catch {
            Write-Warn "Chocolatey installation failed: $($_.Exception.Message)"
        }
    }
}

# Final verification
if (Get-Command allure -ErrorAction SilentlyContinue) {
    try {
        $ver = (& allure --version) -join " `n "
        Write-Ok "Allure CLI is installed. Version info:`n$ver"
    } catch {
        Write-Warn "Allure command exists but failed to run: $($_.Exception.Message)"
    }
} else {
    Write-Err "Allure CLI was not installed by this script."
    Write-Info "Manual install options:"
    Write-Host " - Chocolatey (Admin): choco install allure -y" -ForegroundColor Gray
    Write-Host " - Manual: download and unzip from https://github.com/allure-framework/allure2/releases" -ForegroundColor Gray
}

Write-Info "Script finished. If Allure didn't install, please check network access or run the script as Administrator and try again."
