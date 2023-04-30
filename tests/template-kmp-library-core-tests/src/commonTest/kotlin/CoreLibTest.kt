package dev.petuska.template.kmp.library.core

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoreLibTest {

  @BeforeTest
  fun setUp() {
    println("Running on $platform")
  }

  @Test
  fun test() = runTest {
    val result = CoreLib().sampleApi()
    result shouldBe platform
  }

  @Test
  fun testSuspend() = runTest {
    val result = CoreLib().sampleSuspendApi()
    result shouldBe platform
  }

  @Test
  fun testValue() = runTest {
    val result = CoreLib().sampleValue
    result shouldBe 28980
  }
}
