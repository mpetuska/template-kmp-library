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

includeBuild("../")
includeBuild("../build-conventions")
