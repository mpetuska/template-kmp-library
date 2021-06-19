plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
  google()
  if (project.properties["useSnapshotRepositories"] == "true") {
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
  }
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
//  implementation("com.android.library:com.android.library.gradle.plugin:_")
  implementation("org.jetbrains.dokka:dokka-gradle-plugin:_")
  //TODO Revert to `_` placeholder
  implementation("org.jetbrains.kotlin:kotlin-serialization:1.5.20-RC")
  implementation("org.jlleitschuh.gradle:ktlint-gradle:_")
  implementation("io.github.gradle-nexus:publish-plugin:_")
}
