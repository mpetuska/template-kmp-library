package util

import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.nativeTargetGroup(
  name: String,
  vararg targets: Pair<KotlinNativeTarget, Boolean /* has coroutines */>
): Set<KotlinNativeTarget> {
  val groupTargets = targets.toMap()

  val allWithCoroutines = groupTargets.values.all { it }
  sourceSets {
    val commonMain = getByName("commonMain")
    val commonTest = getByName("commonTest")
    val main = if (groupTargets.size > 1) {
      create("${name}Main") {
        dependsOn(commonMain)
        if (allWithCoroutines) {
          dependencies {
            api("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
          }
        }
      }
    } else null
    val test = if (groupTargets.size > 1) {
      create("${name}Test") {
        dependsOn(commonTest)
      }
    } else null
    groupTargets.entries.forEach { (target, hasCoroutines) ->
      target.compilations["main"].defaultSourceSet {
        main?.let { dependsOn(main) }
        if (hasCoroutines) {
          dependencies {
            api("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
          }
        }
      }
      target.compilations["test"].defaultSourceSet {
        test?.let { dependsOn(test) }
      }
    }
  }
  return groupTargets.keys
}
