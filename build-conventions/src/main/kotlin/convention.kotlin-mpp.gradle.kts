import Env.CI
import Env.SANDBOX
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import util.buildHost
import util.enabled
import util.mainHost

plugins {
  id("convention.base")
  kotlin("multiplatform")
}

kotlin {
  @OptIn(ExperimentalKotlinGradlePluginApi::class)
  targetHierarchy.default()

  targets.all target@{
    val control = Action<Task> {
      onlyIf {
        !CI || this@target.enabled || (SANDBOX && this@target.buildHost == mainHost)
      }
    }
    compilations.all compilation@{
      compileTaskProvider.configure(control)

      if (this@compilation is KotlinNativeCompilation) {
        this@compilation.cinterops.all {
          tasks.named(interopProcessingTaskName, control)
        }
      }
    }
  }
}
