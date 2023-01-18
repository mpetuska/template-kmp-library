plugins {
  id("de.fayard.refreshVersions") version "0.51.0"
  id("com.gradle.enterprise") version "3.12.2"
}

refreshVersions {
  extraArtifactVersionKeyRules(rootDir.resolve("versions.rules"))
}

rootProject.name = "template-kmp-library"

include(":test")

include(
  ":lib:template-kmp-library-core",
  ":lib:template-kmp-library-dsl"
)
