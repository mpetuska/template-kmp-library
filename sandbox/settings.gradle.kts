pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

plugins {
  id("com.gradle.enterprise") version "3.13"
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

includeBuild("../")
includeBuild("../build-conventions")
