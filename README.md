# template-kmp-library
Kotlin multiplatform library template.

Has a baseline setup for a multiplatform library supporting all kotlin [targets](https://kotlinlang.org/docs/mpp-supported-platforms.html) except deprecated wasm32.

## Features
* Native target grouping and shared sourceSets
* Wrapper library module that declares dependencies on all lib modules
* Uniform configuration via `allprojects`
* Local `test` module for shared test utilities (a helper function to run coroutine tests in common sourceSet included)
* Local `sandbox` module for easy library consumer side checks
* Publication control to avoid multiple publications for targets that can be built on multiple hosts
* `ktlint` plugin with automatic `git-hooks`
* `refreshVersions` plugin for better version control
* Main host for publications can be changed via `gradle.properties#project.mainOS` property
