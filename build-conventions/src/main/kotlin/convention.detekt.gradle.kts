import io.gitlab.arturbosch.detekt.Detekt

plugins {
  id("convention.base")
  id("io.gitlab.arturbosch.detekt")
}

dependencies {
  detektPlugins(libs.detekt.formatting)
}

detekt {
  config.from(rootDir.resolve("gradle/detekt.yml"))
  buildUponDefaultConfig = true
}

tasks.withType<Detekt> {
  reports {
    // observe findings in your browser with structure and code snippets
    html.required.set(true)
    // checkstyle like format mainly for integrations like Jenkins
    xml.required.set(true)
    // similar to the console output, contains issue signature to manually edit baseline files
    txt.required.set(true)
    // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with Github Code Scanning
    sarif.required.set(true)
  }
}
