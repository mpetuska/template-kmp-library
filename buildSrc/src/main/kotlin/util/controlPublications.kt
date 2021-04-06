package util

import gradle.kotlin.dsl.accessors._05ea63dd43a6d082ce0e5da185a84c9c.publishing
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.specs.Spec
import org.gradle.kotlin.dsl.withType

fun Project.controlPublications(publications: Collection<String>, enabled: Spec<in Task>) {
  publishing {
    publications {
      matching { it.name in publications }.all {
        val targetPublication = this@all
        tasks.withType<AbstractPublishToMaven>()
          .matching { it.publication == targetPublication }
          .configureEach {
            onlyIf(enabled)
          }
      }
    }
  }
}
