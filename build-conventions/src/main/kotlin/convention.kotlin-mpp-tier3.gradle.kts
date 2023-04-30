import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
  id("convention.kotlin-mpp-tier2")
}

// https://kotlinlang.org/docs/native-target-support.html#tier-3
kotlin {
  @OptIn(ExperimentalWasmDsl::class)
  wasm {
    browser {
      commonWebpackConfig {
        cssSupport { enabled.set(true) }
        scssSupport { enabled.set(true) }
      }
      testTask { useKarma() }
    }
  }
  androidNativeArm32()
  androidNativeArm64()
  androidNativeX86()
  androidNativeX64()
  mingwX64()
  watchosDeviceArm64()
}
