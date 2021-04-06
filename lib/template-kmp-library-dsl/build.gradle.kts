plugins {
  id("local.library-conventions")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(project(":lib:template-kmp-library-core"))
      }
    }
  }
}
