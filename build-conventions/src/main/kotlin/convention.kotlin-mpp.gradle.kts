import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
  id("convention.base")
  kotlin("multiplatform")
}

kotlin {
  @OptIn(ExperimentalKotlinGradlePluginApi::class)
  targetHierarchy.default()

  sourceSets {
    commonTest {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}
