import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import util.nativeTargetGroup
import util.KotlinTargetDetails

plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("convention.common")
}

kotlin {
  explicitApi()
  jvm()
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

  nativeTargetGroup(
    "androidNdk",
    androidNativeArm32(),
    androidNativeArm64(),
  )

  nativeTargetGroup(
    "linux",
    linuxX64(),
    linuxMips32(),
    linuxMipsel32(),
    linuxArm64(),
    linuxArm32Hfp(),
  )

  nativeTargetGroup(
    "ios",
    iosArm32(),
    iosArm64(),
    iosX64(),
  )

  nativeTargetGroup(
    "watchos",
    watchosArm32(),
    watchosArm64(),
    watchosX86(),
    watchosX64(),
  )

  nativeTargetGroup(
    "tvos",
    tvosArm64(),
    tvosX64(),
  )

  macosX64()

  nativeTargetGroup(
    "mingw",
    mingwX86(),
    mingwX64(),
  )

  sourceSets {
    commonTest {
      dependencies {
        implementation(project(":test"))
      }
    }
  }

  val targetsWithCoroutines = KotlinTargetDetails.values()
    .filter(KotlinTargetDetails::hasCoroutines)
    .map(KotlinTargetDetails::presetName)

  targets.filter { it.preset?.name in targetsWithCoroutines }
    .forEach {
      it.compilations["main"].defaultSourceSet {
        dependencies {
          api("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
        }
      }
    }
}

tasks {
  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = project.properties["org.gradle.project.targetCompatibility"]!!.toString()
    }
  }
}
