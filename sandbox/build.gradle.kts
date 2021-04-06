plugins {
  id("local.common-conventions")
  kotlin("jvm")
  application
}

description = "Local consumer sandbox"

application {
  mainClass.set("com.github.mpetuska.template.kmp.library.sandbox.MainKt")
}

dependencies {
  implementation(rootProject)
  testImplementation(project(":test"))
}
