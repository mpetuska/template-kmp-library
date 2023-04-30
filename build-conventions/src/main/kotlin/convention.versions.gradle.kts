import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.util.*

plugins {
  id("convention.base")
  id("com.github.ben-manes.versions")
  id("nl.littlerobots.version-catalog-update")
}

versionCatalogUpdate {
  keep {
    keepUnusedVersions by true
    keepUnusedLibraries by true
    keepUnusedPlugins by true
  }
}

val isNonStable: (String) -> Boolean = { version ->
  val stableKeyword = setOf("RELEASE", "FINAL", "GA").any { version.uppercase(Locale.ROOT).contains(it) }
  val regex = Regex("^[0-9,.v-]+(-r)?$")
  !stableKeyword && !(version matches regex)
}

tasks {
  withType<DependencyUpdatesTask> {
    gradleReleaseChannel = "current"
    rejectVersionIf {
      isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
  }
}
