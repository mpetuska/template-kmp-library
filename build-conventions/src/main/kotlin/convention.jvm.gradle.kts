import com.android.build.gradle.LibraryExtension

plugins {
  id("convention.base")
  `java-base`
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get().toInt()))
  }
}

plugins.withId("com.android.library") {
  configure<LibraryExtension> {
    compileOptions {
      sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
      targetCompatibility = sourceCompatibility
    }
  }
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}