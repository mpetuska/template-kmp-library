package com.github.mpetuska.template.kmp.library.test

import kotlinx.coroutines.runBlocking

actual typealias CoroutineScope = kotlinx.coroutines.CoroutineScope

actual fun runBlockingTest(test: suspend CoroutineScope.() -> Unit) =
  runBlocking(block = test)
