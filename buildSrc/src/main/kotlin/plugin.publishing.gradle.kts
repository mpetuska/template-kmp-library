import gradle.kotlin.dsl.accessors._22fb0f66704d76c1f3b70c84bc95bb50.publishing
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.target.HostManager
import util.Git
import util.by

plugins {
  id("plugin.common")
  id("org.jetbrains.dokka")
  `maven-publish`
  signing
}

tasks {
  register<Jar>("javadocJar") {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.get().outputDirectory)
  }
  withType<Jar> {
    manifest {
      attributes += sortedMapOf(
        "Built-By" to System.getProperty("user.name"),
        "Build-Jdk" to System.getProperty("java.version"),
        "Implementation-Version" to project.version,
        "Created-By" to "${GradleVersion.current()}",
        "Created-From" to "${Git.headCommitHash}"
      )
    }
  }
  val cleanMavenLocal by registering {
    group = "build"
    doLast {
      val groupRepo =
        file("${System.getProperty("user.home")}/.m2/repository/${project.group.toString().replace(".", "/")}")
      publishing.publications.filterIsInstance<MavenPublication>().forEach {
        groupRepo.resolve(it.artifactId).deleteRecursively()
      }
    }
  }
  named("clean") {
    dependsOn(cleanMavenLocal)
  }
}

signing {
  val signingKey: String? by project
  val signingPassword: String? by project
  if (signingKey != null) {
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
  }
}

val isMainHost = HostManager.simpleOsName().equals("${project.properties["project.mainOS"]}", true)
tasks {
  withType<KotlinCompile> {
    onlyIf
  }
}

publishing {
  fun Collection<KotlinTarget>.onlyBuildIf(enabled: Spec<in Task>) {
    forEach {
      it.compilations.all {
        compileKotlinTask.onlyIf(enabled)
      }
    }
  }

  fun Collection<Named>.onlyPublishIf(enabled: Spec<in Task>) {
    val publications: Collection<String> = map { it.name }
    afterEvaluate {
      publishing {
        publications {
          matching { it.name in publications }.all {
            val targetPublication = this@all
            tasks.withType<AbstractPublishToMaven>()
              .matching { it.publication == targetPublication }
              .configureEach {
                onlyIf(enabled)
              }
            tasks.withType<GenerateModuleMetadata>()
              .matching { it.publication.get() == targetPublication }
              .configureEach {
                onlyIf(enabled)
              }
          }
        }
      }
    }
  }
  publications {
    val ghOwnerId: String = project.properties["gh.owner.id"]!!.toString()
    val ghOwnerName: String = project.properties["gh.owner.name"]!!.toString()
    val ghOwnerEmail: String = project.properties["gh.owner.email"]!!.toString()
    repositories {
      maven("https://maven.pkg.github.com/$ghOwnerId/${rootProject.name}") {
        name = "GitHub"
        credentials {
          username = System.getenv("GH_USERNAME")
          password = System.getenv("GH_PASSWORD")
        }
      }
    }
    withType<MavenPublication> {
      artifact(tasks["javadocJar"])
      pom {
        name by project.name
        url by "https://github.com/$ghOwnerId/${rootProject.name}"
        description by project.description

        licenses {
          license {
            name by "The Apache License, Version 2.0"
            url by "https://www.apache.org/licenses/LICENSE-2.0.txt"
          }
        }

        developers {
          developer {
            id by ghOwnerId
            name by ghOwnerName
            email by ghOwnerEmail
          }
        }

        scm {
          connection by "scm:git:git@github.com:$ghOwnerId/${rootProject.name}.git"
          url by "https://github.com/$ghOwnerId/${rootProject.name}"
          tag by Git.headCommitHash
        }
      }
    }
  }
}
