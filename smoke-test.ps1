param(
    [string]$DemoPath = (Join-Path $PSScriptRoot "..\ifolks-demo"),
    [switch]$SkipGeneratorBuild,
    [switch]$SkipPopulator
)

$ErrorActionPreference = "Stop"

function Write-Step([string]$Title) {
    Write-Host ""
    Write-Host "================================================================================" -ForegroundColor Cyan
    Write-Host " $Title" -ForegroundColor Cyan
    Write-Host "================================================================================" -ForegroundColor Cyan
}

$GeneratorRoot = (Resolve-Path $PSScriptRoot).Path
if (-not (Test-Path $DemoPath)) {
    throw "Demo directory not found at: $DemoPath"
}
$DemoRoot = (Resolve-Path $DemoPath).Path

if (-not (Test-Path (Join-Path $DemoRoot ".git"))) {
    throw "Demo directory is not a Git repository: $DemoRoot"
}

Write-Step "0. Pre-flight Checks"
$initialGitStatus = git -C $DemoRoot status --porcelain
if ($LASTEXITCODE -ne 0) {
    throw "Failed to check git status in $DemoRoot"
}
if ($initialGitStatus) {
    Write-Host "Demo repository has uncommitted changes:" -ForegroundColor Red
    Write-Host ($initialGitStatus | Out-String)
    throw "Aborting smoke test: $DemoRoot must have a clean working tree before running."
}

if (-not $env:JAVA_HOME -or -not (Test-Path (Join-Path $env:JAVA_HOME "bin\java.exe"))) {
    $javaCmd = Get-Command java -ErrorAction SilentlyContinue
    if (-not $javaCmd) {
        throw "JAVA_HOME is not set and 'java' was not found in PATH."
    }
    $env:JAVA_HOME = Split-Path (Split-Path $javaCmd.Source -Parent) -Parent
}
Write-Host "Generator Root : $GeneratorRoot"
Write-Host "Demo Root      : $DemoRoot"
Write-Host "JAVA_HOME      : $env:JAVA_HOME"

$TempRoot = Join-Path ([System.IO.Path]::GetTempPath()) "ifolks-smoke-test-$PID"
$NodeModulesCache = Join-Path $TempRoot "node_modules_cache"
$NodeModulesPath = Join-Path $DemoRoot "demo-ui\node_modules"

New-Item -ItemType Directory -Path $TempRoot -Force | Out-Null

try {
    Write-Step "1. Build ifolks-generator & Prepare IGEN CLI Distribution"
    if (-not $SkipGeneratorBuild) {
        mvn -f (Join-Path $GeneratorRoot "pom.xml") clean install
        if ($LASTEXITCODE -ne 0) {
            throw "Failed to build and install ifolks-generator."
        }
    } else {
        Write-Host "Skipping generator build (-SkipGeneratorBuild)." -ForegroundColor Yellow
    }

    [xml]$genPom = Get-Content (Join-Path $GeneratorRoot "pom.xml")
    $generatorVersion = $genPom.project.version
    $zipPath = Join-Path $GeneratorRoot "generator-bash\target\generator-bash-$generatorVersion-bin.zip"
    if (-not (Test-Path $zipPath)) {
        throw "Generator CLI distribution zip not found at: $zipPath"
    }

    Expand-Archive -Path $zipPath -DestinationPath $TempRoot -Force
    $IgenHome = Join-Path $TempRoot "generator-bash-$generatorVersion"
    $env:IGEN_HOME = $IgenHome
    $IgenBat = Join-Path $IgenHome "bin\igen.bat"
    if (-not (Test-Path $IgenBat)) {
        throw "igen.bat not found in extracted distribution: $IgenBat"
    }
    Write-Host "Prepared IGEN_HOME ($generatorVersion) at: $IgenHome" -ForegroundColor Green

    Write-Step "2. Clean and Reset Demo Directory"
    if (Test-Path $NodeModulesPath) {
        Write-Host "Caching demo-ui/node_modules outside repository..."
        Move-Item -Path $NodeModulesPath -Destination $NodeModulesCache -Force
    }

    Get-ChildItem -Path $DemoRoot -Force | Where-Object { $_.Name -ne ".git" } | ForEach-Object {
        Remove-Item -Path $_.FullName -Recurse -Force
    }
    Write-Host "Demo working directory wiped clean (preserving .git)." -ForegroundColor Green

    Write-Step "3. Run 'igen init' & Verify Empty Application (Compile + SetupTest)"
    # 1 = SPRING_REST_ANGULAR, 2 = POSTGRESQL
    $initInputs = @(
        "org.ifolks",
        "demo",
        "1",
        "2",
        "demo",
        "localhost",
        "5432",
        "postgres",
        "postgres",
        "Y"
    ) -join [Environment]::NewLine

    Push-Location $DemoRoot
    try {
        $initOutput = $initInputs | & $IgenBat init 2>&1
        $initOutput | ForEach-Object { Write-Host $_ }
        if ($LASTEXITCODE -ne 0 -or ($initOutput | Select-String -Pattern "\[ERROR\]" -Quiet)) {
            throw "'igen init' failed."
        }
    } finally {
        Pop-Location
    }

    if (-not (Test-Path (Join-Path $DemoRoot "data-model\skeleton.xml"))) {
        throw "'igen init' did not generate data-model\skeleton.xml"
    }

    mvn -f (Join-Path $DemoRoot "pom.xml") clean test
    if ($LASTEXITCODE -ne 0) {
        throw "Empty initialized application failed 'mvn clean test' (SetupTest)."
    }
    Write-Host "Empty initialized application compiled and passed SetupTest!" -ForegroundColor Green

    Write-Step "4. Restore Reference skeleton.xml & Run 'igen generate'"
    git -C $DemoRoot checkout HEAD -- data-model/skeleton.xml
    if ($LASTEXITCODE -ne 0) {
        throw "Failed to restore data-model/skeleton.xml from Git HEAD."
    }

    Push-Location $DemoRoot
    try {
        $genOutput = "Y" | & $IgenBat generate 2>&1
        $genOutput | ForEach-Object { Write-Host $_ }
        if ($LASTEXITCODE -ne 0 -or ($genOutput | Select-String -Pattern "\[ERROR\]" -Quiet)) {
            throw "'igen generate' failed."
        }
    } finally {
        Pop-Location
    }
    Write-Host "Full model code generation completed!" -ForegroundColor Green

    Write-Step "5. Restore Specific Demo Assets, Menus, i18n Overlays, CSVs & Tests"
    $specificPaths = @(
        ".gitignore",
        "README.md",
        "assets",
        "demo-ui/package-lock.json",
        "demo-ui/src/app/templates/private/data/navigation-menus.ts",
        "demo-ui/src/assets/i18n/main_fr_FR.json",
        "demo-ui/src/assets/i18n/model_fr_FR.json",
        "demo-ui/src/app/core/services/I18nService.spec.ts",
        "demo-populator/src/main/resources/population/plans/init",
        "demo-tests/src/test/resources/junit/data",
        "demo-tests/src/test/java/org/ifolks/demo/junit/DemoFunctionalTest.java"
    )

    git -C $DemoRoot checkout HEAD -- @specificPaths
    if ($LASTEXITCODE -ne 0) {
        throw "Failed to restore specific demo assets from Git HEAD."
    }

    if (Test-Path $NodeModulesCache) {
        Write-Host "Restoring cached demo-ui/node_modules..."
        Move-Item -Path $NodeModulesCache -Destination $NodeModulesPath -Force
    } else {
        Write-Host "Installing npm dependencies in demo-ui (npm ci)..."
        npm --prefix (Join-Path $DemoRoot "demo-ui") ci
        if ($LASTEXITCODE -ne 0) {
            throw "'npm ci' failed in demo-ui."
        }
    }

    Write-Step "6. Compile, Run Backend Tests, Build & Populate DB, Build & Test UI"
    Write-Host "-> Running Backend Maven Clean Test (Spotless + SetupTest + DemoFunctionalTest)..." -ForegroundColor Yellow
    mvn -f (Join-Path $DemoRoot "pom.xml") clean test
    if ($LASTEXITCODE -ne 0) {
        throw "Backend 'mvn clean test' failed on generated demo project."
    }

    if (-not $SkipPopulator) {
        Write-Host "-> Running Database Rebuild & CSV Population (demo-populator)..." -ForegroundColor Yellow
        mvn -f (Join-Path $DemoRoot "pom.xml") org.codehaus.mojo:exec-maven-plugin:3.5.0:java -pl demo-populator `
            "-Dexec.mainClass=org.ifolks.demo.populator.ApplicationStarter" `
            "-Dexec.cleanupDaemonThreads=false" `
            "-Dexec.args=--spring.profiles.active=local --workspace-path=$DemoRoot --populate --population-plan-path=init --no-prompt"
        if ($LASTEXITCODE -ne 0) {
            throw "Database build and population (demo-populator) failed."
        }
    } else {
        Write-Host "Skipping demo-populator (-SkipPopulator)." -ForegroundColor Yellow
    }

    Write-Host "-> Running Frontend Build (Prettier + Angular AOT Build)..." -ForegroundColor Yellow
    npm --prefix (Join-Path $DemoRoot "demo-ui") run build
    if ($LASTEXITCODE -ne 0) {
        throw "Frontend 'npm run build' failed in demo-ui."
    }

    Write-Host "-> Running Frontend Unit Tests (Karma / Jasmine)..." -ForegroundColor Yellow
    npm --prefix (Join-Path $DemoRoot "demo-ui") test -- --watch=false
    if ($LASTEXITCODE -ne 0) {
        throw "Frontend 'npm test' failed in demo-ui."
    }

    Write-Step "7. Assert Git Status Cleanliness on Generated Project"
    git -C $DemoRoot add --renormalize . | Out-Null
    git -C $DemoRoot reset --quiet | Out-Null
    $finalGitStatus = git -C $DemoRoot status --porcelain
    if ($LASTEXITCODE -ne 0) {
        throw "Failed to check final git status in $DemoRoot"
    }
    if ($finalGitStatus) {
        Write-Host "Git status is NOT clean after smoke test! Differences found:" -ForegroundColor Red
        Write-Host ($finalGitStatus | Out-String)
        git -C $DemoRoot diff --stat
        throw "Smoke test failed: generated project differs from reference repository."
    }

    Write-Host ""
    Write-Host "================================================================================" -ForegroundColor Green
    Write-Host " SMOKE TEST PASSED SUCCESSFULLY! (0 Git diff, all Backend/DB/UI checks green)" -ForegroundColor Green
    Write-Host "================================================================================" -ForegroundColor Green
} finally {
    if ((Test-Path $NodeModulesCache) -and -not (Test-Path $NodeModulesPath)) {
        if (Test-Path (Join-Path $DemoRoot "demo-ui")) {
            Move-Item -Path $NodeModulesCache -Destination $NodeModulesPath -Force -ErrorAction SilentlyContinue
        }
    }
    if (Test-Path $TempRoot) {
        Remove-Item -Path $TempRoot -Recurse -Force -ErrorAction SilentlyContinue
    }
}
