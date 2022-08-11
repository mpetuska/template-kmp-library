plugins {
  id("de.fayard.refreshVersions") version "0.40.1"
  id("com.gradle.enterprise") version "3.11.1"
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
