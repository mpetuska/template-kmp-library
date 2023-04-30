plugins {
  id("convention.kotlin-mpp-tier0")
}

// https://kotlinlang.org/docs/native-target-support.html#tier-1
kotlin {
  linuxX64()

  macosX64()
  macosArm64()

  iosX64()
  iosSimulatorArm64()
}
