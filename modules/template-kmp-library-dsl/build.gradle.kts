plugins {
  id("convention.kotlin-mpp-tier3")
  id("convention.library-android")
  id("convention.library-mpp")
  id("convention.publishing-mpp")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(project(":modules:template-kmp-library-core"))
      }
    }
  }
}
