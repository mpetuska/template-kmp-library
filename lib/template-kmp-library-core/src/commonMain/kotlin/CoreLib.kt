package com.github.mpetuska.template.kmp.library.core

internal expect val platform: String

public class CoreLib {
  public fun sampleApi(): String = platform
  public suspend fun sampleSuspendApi(): String = sampleApi()
}
