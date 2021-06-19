package util

import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.nativeTargetGroup(
  name: String,
  vararg targets: KotlinNativeTarget
): Array<out KotlinNativeTarget> {
  sourceSets {
    val (main, test) = if (targets.size > 1) {
      val commonMain = getByName("commonMain")
      val commonTest = getByName("commonTest")
      val main = create("${name}Main") {
        dependsOn(commonMain)
      }
      val test = create("${name}Test") {
        dependsOn(commonTest)
      }
      main to test
    } else (null to null)

    targets.forEach { target ->
      main?.let {
        target.compilations["main"].defaultSourceSet {
          dependsOn(main)
        }
      }
      test?.let {
        target.compilations["test"].defaultSourceSet {
          dependsOn(test)
        }
      }
    }
  }
  return targets
}
