package test

import kotlinx.cinterop.toKStringFromUtf8
import platform.posix.getenv

actual object Env {
  actual operator fun get(key: String): String? = getenv(key)?.toKStringFromUtf8()
}