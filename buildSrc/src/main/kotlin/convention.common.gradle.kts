plugins {
  id("org.jlleitschuh.gradle.ktlint")
  idea
}

repositories {
  mavenCentral()
  google()
  if (project.properties["project.useSnapshotRepositories"] == "true") {
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
  }
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}