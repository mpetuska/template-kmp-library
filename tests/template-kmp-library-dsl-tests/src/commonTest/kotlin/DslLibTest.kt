package dev.petuska.template.kmp.library.dsl

import dev.petuska.template.kmp.library.core.CoreLib
import dev.petuska.template.kmp.library.core.platform
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DslLibTest {
  @Test
  fun test() = runTest {
    val result = CoreLib().withPlatform("sync")
    result shouldContain platform
  }

  @Test
  fun testSuspend() = runTest {
    val result = CoreLib().withPlatformSuspend("async")
    result shouldContain platform
  }
}
