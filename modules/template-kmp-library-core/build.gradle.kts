plugins {
  id("convention.kotlin-mpp-tier3")
  id("convention.library-android")
  id("convention.library-mpp")
  id("convention.publishing-mpp")
}

kotlin {
  sourceSets {
    commonTest {
      dependencies {
      }
    }
  }
}
