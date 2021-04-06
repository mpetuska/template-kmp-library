package com.github.mpetuska.template.kmp.library.test

actual interface CoroutineScope

actual fun runBlockingTest(test: suspend CoroutineScope.() -> Unit): Unit = println("Coroutines not supported on linuxMips32")
