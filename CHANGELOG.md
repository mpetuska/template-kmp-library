# v1.1.1

## Changes

* Stricter publication and test control to avoid duplicate builds on matrix jobs
* Reworked gradle precompiled script plugins to better separate different functionalities
* Sandbox GH action to quickly check gradle scripts on different hosts
* Added baseline flow for setting up required native libraries on different CI hosts via [scripts/] directory
* Native library caching for check and release actions
* Upgraded versions
* Fixed detekt issues