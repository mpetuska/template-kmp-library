package local.test

expect interface CoroutineScope

expect fun runBlockingTest(test: suspend CoroutineScope.() -> Unit)
