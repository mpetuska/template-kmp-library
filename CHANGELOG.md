# 1.1.4

## Changes

* Back to spotless + ktlint
* Fixed GH actions issues with runners
* `local.properties` support
* Custom refreshVersions rules setup
* `.sdkmanrc` for easier local env setup
* Version bumps (kotlin@1.6.10, gradle@7.3.3)

# 1.1.3

## Changes

* Swapped out ktlint for spotless + ktfmt
* Disabling git hooks on CI
* Added android support

# 1.1.2

## Changes

* Removed incorrect hashed imports from gradle scripts
* Bumped versions
* Introduced nativeMain and nativeTest sourceSets
* Added BlockingTest utility interface
* Removed snapshot repository support
* macosArm64() target added
* iosSimulatorArm64() target added
* watchosSimulatorArm64() target added
* tvosSimulatorArm64() target added

# 1.1.1

## Changes

* Stricter publication and test control to avoid duplicate builds on matrix jobs
* Reworked gradle precompiled script plugins to better separate different functionalities
* Sandbox GH action to quickly check gradle scripts on different hosts
* Added baseline flow for setting up required native libraries on different CI hosts via [scripts/] directory
* Native library caching for check and release actions
* Upgraded versions
* Fixed detekt issues
