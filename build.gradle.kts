plugins {
  id("convention.library")
  id("convention.publishing")
  id("com.github.jakemarsden.git-hooks")
  idea
}

gitHooks {
  setHooks(
      mapOf(
          "post-checkout" to "ktlintApplyToIdea",
          "pre-commit" to "ktlintFormat",
          "pre-push" to "check"
      )
  )
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        subprojects.filter { it.path.startsWith(":lib:") }.forEach {
          api(it)
        }
      }
    }
  }
}
