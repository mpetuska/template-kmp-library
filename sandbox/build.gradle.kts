plugins {
  kotlin("jvm") version "1.5.10"
  application
}

repositories {
  mavenCentral()
  google()
}

description = "Local consumer sandbox"

application {
  mainClass.set("local.sandbox.MainKt")
}

dependencies {
  implementation("dev.petuska:template-kmp-library")
  testImplementation("dev.petuska:test")
}
