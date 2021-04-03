package com.github.mpetuska.template.kmp.library.dsl

import com.github.mpetuska.template.kmp.library.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertTrue

class DslLibTest {
  @Test
  fun test() {
    val result = "sync".withPlatform()
    println(result)
    assertTrue(result.contains(platform))
  }

  @Test
  fun testSuspend() = runBlockingTest {
    val result = "async".withPlatformSuspend()
    println(result)
    assertTrue(result.contains(platform))
  }
}
