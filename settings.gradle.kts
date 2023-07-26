pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

plugins {
  id("com.gradle.enterprise") version "3.14.1"
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

rootProject.name = "template-kmp-library"
includeBuild("./build-conventions/")
include(":tests:test-utils")

include(
  ":modules:template-kmp-library-core",
  ":tests:template-kmp-library-core-tests"
)

include(
  ":modules:template-kmp-library-dsl",
  ":tests:template-kmp-library-dsl-tests"
)
