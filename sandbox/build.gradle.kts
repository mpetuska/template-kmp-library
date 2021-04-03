plugins {
  kotlin("jvm")
  application
}

application {
  mainClass.set("com.github.mpetuska.template.kmp.library.sandbox.MainKt")
}

dependencies {
  implementation(rootProject)
  testImplementation(project(":test"))
  testImplementation(kotlin("test"))
  testImplementation(kotlin("test-annotations-common"))
  testImplementation(kotlin("test-junit"))
}
