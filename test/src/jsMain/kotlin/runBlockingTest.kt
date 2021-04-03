package com.github.mpetuska.template.kmp.library.test

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

actual typealias CoroutineScope = kotlinx.coroutines.CoroutineScope

actual fun runBlockingTest(test: suspend CoroutineScope.() -> Unit): dynamic =
  GlobalScope.promise(block = test)
