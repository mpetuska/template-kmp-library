plugins {
  id("convention.kotlin-mpp-tier1")
  id("convention.library-android")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(libs.kotlinx.coroutines.test)
        api(libs.bundles.kotest.assertions)
      }
    }
  }
}