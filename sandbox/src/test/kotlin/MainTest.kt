package com.github.mpetuska.template.kmp.library.sandbox

import com.github.mpetuska.template.kmp.library.test.runBlockingTest
import kotlinx.coroutines.delay
import org.junit.Test

class MainTest {
  @Test
  fun test() = runBlockingTest {
    println(main())
    delay(500)
    println(suspendingMain())
  }
}
