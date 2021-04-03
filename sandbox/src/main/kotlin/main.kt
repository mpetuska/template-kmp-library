package com.github.mpetuska.template.kmp.library.sandbox

import com.github.mpetuska.template.kmp.library.core.CoreLib
import com.github.mpetuska.template.kmp.library.dsl.withPlatform
import com.github.mpetuska.template.kmp.library.dsl.withPlatformSuspend
import kotlinx.coroutines.runBlocking

fun main() {
  val core = CoreLib()
  println(core.sampleApi().withPlatform())
  runBlocking {
    suspendingMain()
  }
}

suspend fun suspendingMain() {
  val core = CoreLib()
  println(core.sampleSuspendApi().withPlatformSuspend())
}
