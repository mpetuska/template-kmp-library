plugins {
  id("convention.kotlin-mpp-tier1")
  id("convention.library-android")
}

kotlin {
  sourceSets {
    commonTest {
      dependencies {
        implementation(project(":tests:test-utils"))
        implementation(project(":modules:template-kmp-library-dsl"))
      }
    }
  }
}