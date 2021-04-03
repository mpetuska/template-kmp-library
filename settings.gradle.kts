import de.fayard.refreshVersions.bootstrapRefreshVersions

buildscript {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
  dependencies { classpath("de.fayard.refreshVersions:refreshVersions:0.9.7") }
}

bootstrapRefreshVersions()

rootProject.name = "template-kmp-library"
include(":sandbox", ":test")
include(
  ":lib:template-kmp-library-core",
  ":lib:template-kmp-library-dsl",
  ":lib"
)
