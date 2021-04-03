package com.github.mpetuska.template.kmp.library.test

expect interface CoroutineScope

expect fun runBlockingTest(test: suspend CoroutineScope.() -> Unit)
