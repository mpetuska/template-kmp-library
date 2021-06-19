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

  androidNativeArm32()
  androidNativeArm64()

  linuxX64()
  linuxMips32()
  linuxMipsel32()
  linuxArm64()
  linuxArm32Hfp()

  iosArm32()
  iosArm64()
  iosX64()

  watchosArm32()
  watchosArm64()
  watchosX86()
  watchosX64()

  tvosArm64()
  tvosX64()

  macosX64()

  mingwX86()
  mingwX64()

  sourceSets {
    commonTest {
      dependencies {
        implementation(project(":test"))
      }
    }
  }

  val targetsWithCoroutines = listOf(
      jvm(),
      js(),
      linuxX64(),
      iosArm32(),
      iosArm64(),
      iosX64(),
      watchosArm32(),
      watchosArm64(),
      watchosX86(),
      tvosArm64(),
      tvosX64(),
      macosX64(),
      mingwX64(),
  )
  targetsWithCoroutines.forEach {
    it.compilations["main"].defaultSourceSet {
      dependencies {
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
      }
    }
  }
}
