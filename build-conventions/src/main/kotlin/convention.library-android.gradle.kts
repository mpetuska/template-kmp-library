plugins {
  id("convention.library")
  id("com.android.library")
}

android {
  namespace = "$group.${name.replace(Regex("[_-]"), ".")}"
  compileSdk = 31
  defaultConfig {
    minSdk = 1
    aarMetadata {
      minCompileSdk = minSdk
    }
  }
}
