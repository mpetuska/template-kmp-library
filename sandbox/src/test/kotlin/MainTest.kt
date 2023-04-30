package local.sandbox

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class MainTest {
  @Test
  fun test() = runTest {
    println(main())
    delay(500)
    println("After Delay: ${suspendingMain()}")
  }
}
