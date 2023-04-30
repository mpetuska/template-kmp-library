plugins {
  id("convention.publishing")
  id("org.jetbrains.dokka")
}

tasks {
  register<Jar>("javadocJar") {
    dependsOn(dokkaHtml)
    archiveClassifier by "javadoc"
    from(dokkaHtml)
  }
}

publishing {
  publications {
    withType<MavenPublication> {
      artifact(tasks["javadocJar"])
    }
  }
}
