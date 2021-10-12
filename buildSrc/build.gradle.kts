plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
  google()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
  implementation("com.android.library:com.android.library.gradle.plugin:_")
  implementation("org.jetbrains.dokka:dokka-gradle-plugin:_")
  implementation("org.jetbrains.kotlin:kotlin-serialization:_")
  implementation("org.jlleitschuh.gradle:ktlint-gradle:_")
  implementation("io.github.gradle-nexus:publish-plugin:_")
}
