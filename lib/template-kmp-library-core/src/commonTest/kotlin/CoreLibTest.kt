package com.github.mpetuska.template.kmp.library.core

import com.github.mpetuska.template.kmp.library.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CoreLibTest {
  @Test
  fun test() {
    val result = CoreLib().sampleApi()
    println(result)
    assertEquals(result, platform)
  }

  @Test
  fun testSuspend() = runBlockingTest {
    val result = CoreLib().sampleSuspendApi()
    println(result)
    assertEquals(result, platform)
  }
}
