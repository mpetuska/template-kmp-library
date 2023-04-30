package local.sandbox

import dev.petuska.template.kmp.library.core.CoreLib
import dev.petuska.template.kmp.library.dsl.withPlatform
import dev.petuska.template.kmp.library.dsl.withPlatformSuspend
import kotlinx.coroutines.runBlocking

fun main() {
  val core = CoreLib()
  println(core.sampleApi())
  println(core.withPlatform("Blocking"))
  runBlocking {
    suspendingMain()
  }
}

suspend fun suspendingMain() {
  val core = CoreLib()
  println(core.sampleSuspendApi())
  println(core.withPlatformSuspend("Suspending"))
}
