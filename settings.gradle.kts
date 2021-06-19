plugins {
  id("de.fayard.refreshVersions") version "0.10.1"
  id("com.gradle.enterprise") version "3.6.3"
}

rootProject.name = "template-kmp-library"
include(":test")
include(
  ":lib:template-kmp-library-core",
  ":lib:template-kmp-library-dsl"
)
