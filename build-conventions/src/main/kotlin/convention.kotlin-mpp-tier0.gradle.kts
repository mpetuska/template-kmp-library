import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
  id("convention.jvm")
  id("convention.kotlin-mpp")
}

plugins.withId("com.android.library") {
  configure<KotlinMultiplatformExtension> {
    android()
  }
}

kotlin {
  js {
    useCommonJs()
    browser {
      commonWebpackConfig {
        cssSupport { enabled.set(true) }
        scssSupport { enabled.set(true) }
      }
      testTask { useKarma() }
    }
  }

  jvm()
}
