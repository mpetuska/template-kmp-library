[![Dokka docs](https://img.shields.io/badge/docs-dokka-orange?style=flat-square)](http://mpetuska.github.io/template-kmp-library)
[![Version maven-central](https://img.shields.io/maven-central/v/dev.petuska/template-kmp-library?logo=apache-maven&style=flat-square)](https://mvnrepository.com/artifact/dev.petuska/template-kmp-library/latest)

# template-kmp-library

Kotlin multiplatform library template.

Has a baseline setup for a multiplatform library supporting all
kotlin [targets](https://kotlinlang.org/docs/mpp-supported-platforms.html)
except deprecated wasm32.

## Features

* Native target grouping and shared sourceSets
* Wrapper library module that declares dependencies on all library modules
* Uniform configuration via convention
  plugins `convention.base`, `convention.library-mpp`, `convention.library-android` & `convention.publishing-mpp`
* Incremental kotlin target sets following support tiers
* Separation of source and tests to allow for lower support tier sources, being tested by higher and
  more supported tier test module
* Local `sandbox` module for easy library consumer side checks
* Publication control to avoid multiple publications for targets that can be built on multiple hosts
* `detekt` plugin with automatic `git-hooks`
* Main host for publications can be changed via `gradle.properties#project.mainOS` property
* Gradle Build Scan setup
* Gradle version catalog with easy updates `./gradlew versionCatalogUpdate`
* GH dependabot setup
* GH release action for platform dependant publications
* GH check action for platform dependant tests on PRs
* Maven Central publishing setup
* GH Packages publishing setup
* `SDKMAN` support for local env (`sdk man env install` to get the required JDM and `sdkman env` to
  switch to it)

## Setup

Here are some pointers to help you get up and running with this template

### Badges

This README contains some useful badges for your project. To tailor them to your artefacts the
following changes needs
to be made:

* `Dokka docs` - change the link as `(http://$GH_USERNAME.github.io/$GH_PROJECT_NAME)`
* `Version maven-central` - change all occurrences of `dev.petuska` to your own group
  and `template-kmp-library` to your
  root library name

### gradle.properties

Have a look at `gradle.properties` file and change these properties as needed

* `gh.owner.id` - main library developer's username
* `gh.owner.name` - main library developer's name
* `gh.owner.email` - main library developer's email
* `project.mainOS` - main host to publish cross-platform artefacts from (to avoid duplicate
  publications). Supported values are `linux`, `windows`, `macosX64` & `macosArm64`. Note that some
  mac targets can be compiled on all architectures and are set to require `macosX64` by default.
  This can be changed in [./build-conventions/src/main/kotlin/util/buildHost.kt].
* `group` - artefacts' group
* `description` - library description
* `version` - library version (overridden in CI, so doesn't really matter here)

### Modules

All the library modules should go to [./modules] directory and be included
in [./settings.gradle.kts].
There are already two sample modules to illustrate how simple the setup
is - [./modules/template-kmp-library-core] & [./modules/template-kmp-library-dsl].
They both contain some sample code and tests
in [./tests/template-kmp-library-core-tests] & [./tests/template-kmp-library-dsl-tests]

### Kotlin Targets

The template comes packed with all kotlin targets preconfigured and grouped by support tiers
controlled by a set of plugins. Lower tier plugins apply all of the higher tier plugins so you
should only ever need to pick one.

- `convention.kotlin-mpp-tier0`
- `convention.kotlin-mpp-tier1`
- `convention.kotlin-mpp-tier2`
- `convention.kotlin-mpp-tier3`

Changing tiers will not break any publications as they're configured on top of pre-registered
targets.

### GitHub Actions

The template also comes with GH actions to check builds on PRs and publish artefacts when creating a
GH release. By
default, it'll publish to GH packages and Maven Central. However, to fully unlock Maven Central
publishing, you'll need
to add these secrets to your GH repository. If you want to quickly disable Maven Central publishing,
you can toggle it
at [./.github/workflows/release.yml#L80]`

* `SIGNING_KEY` - GPG signing key
* `SIGNING_KEY_ID` - GPG signing key ID
* `SIGNING_PASSWORD` - GPG signing key password (if set)
* `SONATYPE_PASSWORD` - Sonatype PAT username
* `SONATYPE_USERNAME` - Sonatype PAT password
