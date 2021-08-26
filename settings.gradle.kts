plugins {
  id("de.fayard.refreshVersions") version "0.20.0"
  id("com.gradle.enterprise") version "3.6.4"
}

rootProject.name = "template-kmp-library"
include(":test")
include(
  ":lib:template-kmp-library-core",
  ":lib:template-kmp-library-dsl"
)
