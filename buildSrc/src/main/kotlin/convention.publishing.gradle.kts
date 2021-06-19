import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

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
    repositories {
      maven("https://maven.pkg.github.com/mpetuska/${project.name}") {
        name = "GitHub"
        credentials {
          username = System.getenv("GH_USERNAME")
          password = System.getenv("GH_PASSWORD")
        }
      }
    }
    val ghOwnerId: String = project.properties["gh.owner.id"]!!.toString()
    val ghOwnerName: String = project.properties["gh.owner.name"]!!.toString()
    val ghOwnerEmail: String = project.properties["gh.owner.email"]!!.toString()
    withType<MavenPublication> {
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

afterEvaluate {
  tasks {
    withType<Jar> {
      manifest {
        attributes += sortedMapOf(
          "Built-By" to System.getProperty("user.name"),
          "Build-Jdk" to System.getProperty("java.version"),
          "Implementation-Version" to project.version,
          "Created-By" to "${org.gradle.util.GradleVersion.current()}",
          "Created-From" to Git.headCommitHash
        )
      }
    }
  }
}

kotlin {
  val mainHostTargets = (targets.filterIsInstance<KotlinJvmTarget>().also {
    it.firstOrNull()?.let {
      tasks {
        val javadocJar = create<Jar>("javadocJar") {
          dependsOn(dokkaJavadoc)
          archiveClassifier.set("javadoc")
          from(dokkaJavadoc.get().outputDirectory)
        }
        publishing {
          publications {
            named(it.name, MavenPublication::class) {
              artifact(javadocJar)
            }
          }
        }
      }
    }
  } + targets.filterIsInstance<KotlinJsTargetDsl>() + targets.filterIsInstance<KotlinAndroidTarget>()).toSet()

  val nativeTargets = targets.filterIsInstance<KotlinNativeTarget>()
  val androidNdkTargets = setOf(
    "androidNativeArm32",
    "androidNativeArm64",
  ).run { nativeTargets.filter { it.preset?.name in this }.toSet() }
  val linuxTargets = setOf(
    "linuxArm32Hfp",
    "linuxArm64",
    "linuxMips32",
    "linuxMipsel32",
    "linuxX64",
  ).run { nativeTargets.filter { it.preset?.name in this }.toSet() }
  val iosTargets = setOf(
    "iosArm32",
    "iosArm64",
    "iosX64",
  ).run { nativeTargets.filter { it.preset?.name in this }.toSet() }
  val watchosTargets = setOf(
    "watchosX86",
    "watchosX64",
    "watchosArm64",
    "watchosArm32",
  ).run { nativeTargets.filter { it.preset?.name in this }.toSet() }
  val tvosTargets = setOf(
    "tvosArm64",
    "tvosX64",
  ).run { nativeTargets.filter { it.preset?.name in this }.toSet() }
  val macosTargets = setOf(
    "macosX64",
  ).run { nativeTargets.filter { it.preset?.name in this }.toSet() }
  val mingwTargets = setOf(
    "mingwX64",
    "mingwX86",
  ).run { nativeTargets.filter { it.preset?.name in this }.toSet() }

  val windowsHostTargets = mingwTargets
  val linuxHostTargets = linuxTargets + androidNdkTargets
  val osxHostTargets = iosTargets + watchosTargets + tvosTargets + macosTargets

  fun controlPublications(publications: Collection<String>, enabled: Spec<in Task>) {
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
  controlPublications(linuxHostTargets.map { it.name }) { currentOS.isLinux }
  controlPublications(osxHostTargets.map { it.name }) { currentOS.isMacOsX }
  controlPublications(windowsHostTargets.map { it.name }) { currentOS.isWindows }
  controlPublications(mainHostTargets.map { it.name } + "kotlinMultiplatform") { isMainOS }
}
