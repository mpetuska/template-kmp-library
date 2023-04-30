package dev.petuska.template.kmp.library.dsl

import dev.petuska.template.kmp.library.core.CoreLib
import dev.petuska.template.kmp.library.core.platform

public fun CoreLib.withPlatform(message: String): String = "[$platform] $message"
public suspend fun CoreLib.withPlatformSuspend(message: String): String = withPlatform(message)
