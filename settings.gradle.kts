plugins {
  id("de.fayard.refreshVersions") version "0.30.2"
  id("com.gradle.enterprise") version "3.8.1"
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
