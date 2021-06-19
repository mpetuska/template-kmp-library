plugins {
  id("convention.common")
  kotlin("jvm")
  application
}

//repositories {
//  mavenCentral()
//  google()
//}

description = "Local consumer sandbox"

application {
  mainClass.set("com.github.mpetuska.template.kmp.library.sandbox.MainKt")
}

dependencies {
  implementation(rootProject)
  testImplementation("dev.petuska:template-kmp-library")
}
