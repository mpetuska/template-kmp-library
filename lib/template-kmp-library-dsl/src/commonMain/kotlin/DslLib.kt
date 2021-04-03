package com.github.mpetuska.template.kmp.library.dsl

internal expect val platform: String

public fun String.withPlatform(): String = "[$platform] $this"
public suspend fun String.withPlatformSuspend(): String = withPlatform()
