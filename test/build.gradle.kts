plugins {
  id("local.library-conventions")
}

description = "Local test utilities"

kotlin {
  explicitApi = null
  sourceSets {
    commonMain {
      dependencies {
        api(kotlin("test"))
        api(kotlin("test-annotations-common"))
      }
    }
    named("jvmMain") {
      dependencies {
        api(kotlin("test-junit"))
      }
    }
    named("jsMain") {
      dependencies {
        api(kotlin("test-js"))
      }
    }
  }
}
tasks {
  withType<AbstractPublishToMaven>().configureEach { onlyIf { false } }
  withType<org.jetbrains.dokka.gradle.AbstractDokkaTask>().configureEach { onlyIf { false } }
}
