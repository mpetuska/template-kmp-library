plugins {
    id("de.fayard.refreshVersions") version "0.10.1"
}

rootProject.name = "template-kmp-library"
include(":test")
include(
  ":lib:template-kmp-library-core",
  ":lib:template-kmp-library-dsl"
)
