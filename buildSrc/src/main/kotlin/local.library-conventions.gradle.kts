import org.gradle.internal.os.OperatingSystem
import util.nativeTargetGroup
import util.by
import util.Git

plugins {
  id("local.common-conventions")
  kotlin("multiplatform")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("org.jetbrains.dokka")
  id("org.jlleitschuh.gradle.ktlint")
  `maven-publish`
}

internal val currentOs = OperatingSystem.current()
internal val mainOS = OperatingSystem.forName(project.properties["project.mainOS"] as String)
internal val isMainOS = currentOs == mainOS

kotlin {
  explicitApi()
  val mainHostTargets = setOf(
    jvm(),
    js {
      binaries.library()
      useCommonJs()
      nodejs()
//          browser {
//            commonWebpackConfig {
//              cssSupport.enabled = true
//            }
//            testTask {
//              useKarma {
//                useFirefox()
//                useChrome()
//                useSafari()
//              }
//            }
//          }
    }
  )

  val androidNdkTargets = nativeTargetGroup(
    "androidNdk",
    androidNativeArm32() to false,
    androidNativeArm64() to false
  )
  val linuxTargets = nativeTargetGroup(
    "linux",
    linuxX64() to true,
    linuxMips32() to false,
    linuxMipsel32() to false,
    linuxArm64() to false,
    linuxArm32Hfp() to false
  )
  val iosTargets = nativeTargetGroup(
    "ios",
    iosArm32() to true,
    iosArm64() to true,
    iosX64() to true
  )
  val watchosTargets = nativeTargetGroup(
    "watchos",
    watchosArm32() to true,
    watchosArm64() to true,
    watchosX86() to true,
    watchosX64() to false
  )
  val tvosTargets = nativeTargetGroup(
    "tvos",
    tvosArm64() to true,
    tvosX64() to true
  )
  val windowsHostTargets = nativeTargetGroup(
    "windows",
    mingwX64() to true,
    mingwX86() to false
  )
  val macosTargets = nativeTargetGroup(
    "macos",
    macosX64() to true
  )
  val linuxHostTargets = linuxTargets + androidNdkTargets
  val osxHostTargets = nativeTargetGroup(
    "osx",
    *(iosTargets + watchosTargets + tvosTargets + macosTargets).map { it to false }.toTypedArray()
  )
  val nativeTargets = linuxHostTargets + osxHostTargets + windowsHostTargets


  sourceSets {
    commonTest {
      dependencies {
        implementation(project(":test"))
      }
    }
    named("jvmMain") {
      dependencies {
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
      }
    }
    named("jsMain") {
      dependencies {
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
      }
    }
  }

  val linuxPublications = linuxHostTargets.map { it.name }
  val osxPublications = osxHostTargets.map { it.name }
  val windowsPublications = windowsHostTargets.map { it.name }
  val mainPublications = mainHostTargets.map { it.name } + "kotlinMultiplatform"

  publishing {
    publications {
      matching { it.name in linuxPublications }.all {
        val targetPublication = this@all
        tasks.withType<AbstractPublishToMaven>()
          .matching { it.publication == targetPublication }
          .configureEach { onlyIf { currentOs.isLinux } }
      }
      matching { it.name in osxPublications }.all {
        val targetPublication = this@all
        tasks.withType<AbstractPublishToMaven>()
          .matching { it.publication == targetPublication }
          .configureEach { onlyIf { currentOs.isMacOsX } }
      }
      matching { it.name in windowsPublications }.all {
        val targetPublication = this@all
        tasks.withType<AbstractPublishToMaven>()
          .matching { it.publication == targetPublication }
          .configureEach { onlyIf { currentOs.isWindows } }
      }
      matching { it.name in mainPublications }.all {
        val targetPublication = this@all
        tasks.withType<AbstractPublishToMaven>()
          .matching { it.publication == targetPublication }
          .configureEach { onlyIf { isMainOS } }
      }
      withType<MavenPublication> {
        pom {
          name by project.name
          url by "https://github.com/mpetuska/${rootProject.name}"
          description by (project.description ?: rootProject.description)

          licenses {
            license {
              name by "The Apache License, Version 2.0"
              url by "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
          }

          scm {
            connection by "scm:git:git@github.com:mpetuska/${rootProject.name}.git"
            url by "https://github.com/mpetuska/${rootProject.name}"
            tag by Git.headCommitHash
          }
        }
      }
    }
    repositories {
      maven("GitHub") {
        url = uri("https://maven.pkg.github.com/mpetuska/${rootProject.name}")
        credentials {
          username = System.getenv("GH_USERNAME")
          password = System.getenv("GH_PASSWORD")
        }
      }
    }
  }
}
