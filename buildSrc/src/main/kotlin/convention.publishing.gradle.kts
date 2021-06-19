import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractKotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import util.KotlinTargetDetails
import util.NativeHost

plugins {
  id("convention.library")
  id("org.jetbrains.dokka")
  `maven-publish`
  signing
}

internal val currentOS = OperatingSystem.current()
internal val mainOS = OperatingSystem.forName(project.properties["project.mainOS"] as String)
internal val isMainOS = currentOS == mainOS

logger.info(
  """
  [OS Info] CurrentOS: $currentOS
  [OS Info] MainOS: $mainOS
  [OS Info] IsMainOS: $isMainOS
  [OS Info] IsLinux: ${currentOS.isLinux}
  [OS Info] IsMacOSX: ${currentOS.isMacOsX}
  [OS Info] IsWindows: ${currentOS.isWindows}
""".trimIndent()
)

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
        "Created-From" to Git.headCommitHash
      )
    }
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

publishing {
  publications {
    val ghOwnerId: String = project.properties["gh.owner.id"]!!.toString()
    val ghOwnerName: String = project.properties["gh.owner.name"]!!.toString()
    val ghOwnerEmail: String = project.properties["gh.owner.email"]!!.toString()
    repositories {
      maven("https://maven.pkg.github.com/$ghOwnerId/${project.name}") {
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
        url by "https://github.com/$ghOwnerId/${project.name}"
        description by rootProject.description

        licenses {
          license {
            name by "The Apache License, Version 2.0"
            url by "https://www.apache.org/licenses/LICENSE-2.0.txt"
          }
        }

        developers {
          developer {
            id to ghOwnerId
            name to ghOwnerName
            email to ghOwnerEmail
          }
        }

        scm {
          connection by "scm:git:git@github.com:$ghOwnerId/${project.name}.git"
          url by "https://github.com/$ghOwnerId/${project.name}"
          tag by Git.headCommitHash
        }
      }
    }
  }
}

kotlin {
  val claimedTargets = mutableSetOf<KotlinTarget>()
  fun filterTargetsByHost(host: NativeHost?): Collection<KotlinTarget> = KotlinTargetDetails.values()
    .filter { host?.let { h -> h in it.supportedBuildHosts } ?: it.supportedBuildHosts.isEmpty() }
    .map { it.presetName }
    .run { targets.filter { it.preset?.name in this && it !in claimedTargets } }
    .also { claimedTargets.addAll(it) }

  val crossPlatformTargets = filterTargetsByHost(null)

  val windowsHostTargets = filterTargetsByHost(NativeHost.WINDOWS)
  val linuxHostTargets = filterTargetsByHost(NativeHost.LINUX)
  val osxHostTargets = filterTargetsByHost(NativeHost.OSX)
  val mainHostTargets = crossPlatformTargets + Named { "kotlinMultiplatform" }

  fun Collection<Named>.onlyPublishIf(enabled: Spec<in Task>) {
    val publications: Collection<String> = map { it.name }
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

  linuxHostTargets.onlyPublishIf { currentOS.isLinux }
  osxHostTargets.onlyPublishIf { currentOS.isMacOsX }
  windowsHostTargets.onlyPublishIf { currentOS.isWindows }
  mainHostTargets.onlyPublishIf { isMainOS }
}
