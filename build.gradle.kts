import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("com.github.jakemarsden.git-hooks")
  id("org.jmailen.kotlinter")
  `maven-publish`
  idea
}

internal val currentOs = OperatingSystem.current()
internal val mainOS = OperatingSystem.forName(project.properties["project.mainOS"] as String)
internal val isMainOS = currentOs == mainOS

allprojects {
  apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
  apply(plugin = "org.jmailen.kotlinter")
  
  kotlinter {
    indentSize = 2
    experimentalRules = true
  }
  
  repositories {
    mavenCentral()
  }
  
  val isLib = path.startsWith(":lib:") || this == rootProject
  val testPath = ":test"
  val isTest = path.startsWith(testPath)
  if (isLib || isTest) {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
    kotlin {
      if (isLib) {
        explicitApi()
      }
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
      
      fun nativeTargetGroup(
        name: String,
        targetsWithoutCoroutines: Set<Int>,
        vararg targets: KotlinNativeTarget
      ): Set<KotlinNativeTarget> {
        val targetsSet = targets.toSet()
        sourceSets {
          val commonMain by getting
          val commonTest by getting
          val main = if (targetsSet.size > 1) {
            create("${name}Main") {
              dependsOn(commonMain)
              if (targetsWithoutCoroutines.isEmpty()) {
                dependencies {
                  api("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
                }
              }
            }
          } else null
          val test = if (targetsSet.size > 1) {
            create("${name}Test") {
              dependsOn(commonTest)
            }
          } else null
          targetsSet.forEachIndexed { i, it ->
            it.compilations["main"].defaultSourceSet {
              main?.let { dependsOn(main) }
              if (i !in targetsWithoutCoroutines) {
                dependencies {
                  api("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
                }
              }
            }
            it.compilations["test"].defaultSourceSet {
              test?.let { dependsOn(test) }
            }
          }
        }
        return targetsSet
      }
      
      val androidNdkTargets = nativeTargetGroup(
        "androidNdk",
        setOf(0, 1),
        androidNativeArm32(),
        androidNativeArm64()
      )
      val linuxTargets = nativeTargetGroup(
        "linux",
        setOf(1, 2, 3, 4, 5),
        linuxX64(),
        linuxMips32(),
        linuxMipsel32(),
        linuxArm64(),
        linuxArm32Hfp()
      )
      val iosTargets = nativeTargetGroup(
        "ios",
        setOf(),
        iosArm32(),
        iosArm64(),
        iosX64()
      )
      val watchOsTargets = nativeTargetGroup(
        "watchOs",
        setOf(),
        watchosArm32(),
        watchosArm64(),
        watchosX86(),
        watchosX64()
      )
      val tvOsTargets = nativeTargetGroup(
        "tvOs",
        setOf(),
        tvosArm64(),
        tvosX64()
      )
      val windowsHostTargets = nativeTargetGroup(
        "windows",
        setOf(),
        mingwX64(),
        mingwX86()
      )
      val linuxHostTargets = linuxTargets + androidNdkTargets
      val osxHostTargets = iosTargets + watchOsTargets + tvOsTargets + nativeTargetGroup("macos", setOf(), macosX64())
      val nativeTargets = linuxHostTargets + osxHostTargets + windowsHostTargets
      
      
      sourceSets {
        commonTest {
          dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-annotations-common"))
            if (!isTest) {
              implementation(project(testPath))
            }
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
        named("jvmTest") {
          dependencies {
            implementation(kotlin("test-junit"))
          }
        }
        named("jsTest") {
          dependencies {
            implementation(kotlin("test-js"))
          }
        }
      }
      
      if (isLib) {
        apply(plugin = "maven-publish")
        
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
          }
        }
      }
    }
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

gitHooks {
  setHooks(mapOf("pre-commit" to "formatKotlin", "pre-push" to "check"))
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}
