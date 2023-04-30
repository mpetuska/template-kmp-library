import Env.CI
import Env.SANDBOX
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import org.jetbrains.kotlin.konan.target.HostManager
import util.buildHost
import util.enabled
import util.mainHost

plugins {
  id("convention.kotlin-mpp")
  id("convention.publishing")
  id("convention.publishing-dokka")
}

kotlin {
  fun configurePublishTasks(name: String, action: Action<in Task>) {
    publishing {
      publications.matching { it.name == name }.all {
        val targetPublication = this@all
        tasks.withType<AbstractPublishToMaven>()
          .matching { it.publication == targetPublication }
          .all(action)
        tasks.withType<GenerateModuleMetadata>()
          .matching { it.publication.orNull == targetPublication }
          .all(action)
      }
    }
  }

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
      configurePublishTasks(this@target.name, control)
    }

    if (this@target is KotlinAndroidTarget && (!CI || this@target.enabled || SANDBOX))
      this@target.publishLibraryVariants("release", "debug")
  }

  configurePublishTasks("kotlinMultiplatform") {
    !CI || SANDBOX || HostManager.host == mainHost
  }
}
