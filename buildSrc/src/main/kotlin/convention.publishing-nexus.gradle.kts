import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("convention.library")
  id("org.jetbrains.dokka")
  `maven-publish`
  signing
  id("io.github.gradle-nexus.publish-plugin")
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

nexusPublishing {
  repositories {
    sonatype {
      nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
      snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
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
    val ghOwnerId:String  = project.properties["gh.owner.id"]!!.toString()
    val ghOwnerName:String  = project.properties["gh.owner.name"]!!.toString()
    val ghOwnerEmail:String  = project.properties["gh.owner.email"]!!.toString()
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
            id to "$ghOwnerId"
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

signing {
  val signingKey: String? by project
  val signingPassword: String? by project
  if (signingKey != null) {
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
  }
}

tasks {
  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = project.properties["org.gradle.project.targetCompatibility"]!!.toString()
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
//  val mainHostTargets = setOf(
//    jvm(),
//    js()
//  )
//
//  val androidNdkTargets = nativeTargetGroup(
//    "androidNdk",
//    androidNativeArm32() to false,
//    androidNativeArm64() to false
//  )
//  val linuxTargets = nativeTargetGroup(
//    "linux",
//    linuxX64() to true,
//    linuxMips32() to false,
//    linuxMipsel32() to false,
//    linuxArm64() to false,
//    linuxArm32Hfp() to false
//  )
//  val iosTargets = nativeTargetGroup(
//    "ios",
//    iosArm32() to true,
//    iosArm64() to true,
//    iosX64() to true
//  )
//  val watchosTargets = nativeTargetGroup(
//    "watchos",
//    watchosArm32() to true,
//    watchosArm64() to true,
//    watchosX86() to true,
//    watchosX64() to false
//  )
//  val tvosTargets = nativeTargetGroup(
//    "tvos",
//    tvosArm64() to true,
//    tvosX64() to true
//  )
//  val macosTargets = nativeTargetGroup(
//    "macos",
//    macosX64() to true
//  )
//  //Commonizer does not yet work on [mingwX64, mingwX86]
//  val windowsHostTargets = nativeTargetGroup(
//    "windows",
//    mingwX86() to false
//  ) + nativeTargetGroup(
//    "windows",
//    mingwX64() to true,
//  )
//  val linuxHostTargets = linuxTargets + androidNdkTargets
//  val osxHostTargets = iosTargets + watchosTargets + tvosTargets + macosTargets
//  val nativeTargets = linuxHostTargets + osxHostTargets + windowsHostTargets

//  publishing {
//    publications {
//      withType<MavenPublication> {
//        pom {
//          name by project.name
//          url by "https://github.com/mpetuska/${rootProject.name}"
//          description by (project.description ?: rootProject.description)
//
//          licenses {
//            license {
//              name by "The Apache License, Version 2.0"
//              url by "https://www.apache.org/licenses/LICENSE-2.0.txt"
//            }
//          }
//
//          scm {
//            connection by "scm:git:git@github.com:mpetuska/${rootProject.name}.git"
//            url by "https://github.com/mpetuska/${rootProject.name}"
//            tag by Git.headCommitHash
//          }
//        }
//      }
//    }
//    repositories {
//      maven("GitHub") {
//        url = uri("https://maven.pkg.github.com/mpetuska/${rootProject.name}")
//        credentials {
//          username = System.getenv("GH_USERNAME")
//          password = System.getenv("GH_PASSWORD")
//        }
//      }
//    }
//  }
//  fun controlPublications(publications: Collection<String>, enabled: Spec<in Task>) {
//    publishing {
//      publications {
//        matching { it.name in publications }.all {
//          val targetPublication = this@all
//          tasks.withType<AbstractPublishToMaven>()
//            .matching { it.publication == targetPublication }
//            .configureEach {
//              onlyIf(enabled)
//            }
//        }
//      }
//    }
//  }
//  controlPublications(linuxHostTargets.map { it.name }) { currentOS.isLinux }
//  controlPublications(osxHostTargets.map { it.name }) { currentOS.isMacOsX }
//  controlPublications(windowsHostTargets.map { it.name }) { currentOS.isWindows }
//  controlPublications(mainHostTargets.map { it.name } + "kotlinMultiplatform") { isMainOS }
}
