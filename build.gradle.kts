plugins {
  alias(libs.plugins.nexus.publish)
  id("convention.versions")
  id("convention.git-hooks")

  id("convention.kotlin-mpp-tier3")
  id("convention.library-android")
  id("convention.library-mpp")
  id("convention.publishing-mpp")
}

nexusPublishing.repositories {
  sonatype {
    nexusUrl by uri("https://s01.oss.sonatype.org/service/local/")
    snapshotRepositoryUrl by uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
  }
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}

kotlin {
  sourceSets {
    commonMain {
      dependencies { subprojects.filter { it.path.startsWith(":modules:") }.forEach { api(it) } }
    }
  }
}
