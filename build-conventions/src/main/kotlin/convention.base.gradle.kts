plugins {
  idea
  id("convention.local-properties")
}

idea {
  module {
    isDownloadJavadoc = true
    isDownloadSources = true
  }
}

repositories {
  mavenCentral()
  google()
  mavenLocal()
}
