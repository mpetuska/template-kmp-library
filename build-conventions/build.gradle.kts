plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  google()
  mavenCentral()
}

dependencies {
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

  implementation(libs.plugin.kotlin)
  implementation(libs.plugin.kotlin.serialization)
  implementation(libs.plugin.android)
  implementation(libs.plugin.git.hooks)
  implementation(libs.plugin.detekt)
  implementation(libs.plugin.dokka)
  implementation(libs.plugin.versions)
  implementation(libs.plugin.versions.update)
  implementation(libs.plugin.container.tasks)
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}
