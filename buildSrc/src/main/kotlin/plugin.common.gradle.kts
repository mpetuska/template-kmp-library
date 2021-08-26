import de.fayard.refreshVersions.core.versionFor
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinTest
import util.by

plugins {
  id("org.jlleitschuh.gradle.ktlint")
  idea
}

repositories {
  mavenCentral()
  google()
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

ktlint {
  version by versionFor("version.ktlint")
  additionalEditorconfigFile.set(rootDir.resolve(".editorconfig"))
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }

  afterEvaluate {
    if (tasks.findByName("compile") == null) {
      register("compile") {
        dependsOn(withType(AbstractKotlinCompile::class))
        group = "build"
      }
    }
    if (tasks.findByName("test") == null) {
      register("test") {
        dependsOn(withType(KotlinTest::class))
        group = "verification"
      }
    }
  }
}
