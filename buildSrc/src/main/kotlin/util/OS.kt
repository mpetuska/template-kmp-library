package util

import org.apache.tools.ant.taskdefs.condition.Os

enum class OS {
  WINDOWS,
  LINUX,
  OSX;

  val isLinux by lazy { this == LINUX }
  val isOSX by lazy { this == OSX }
  val isWindows by lazy {
    val os = Os.isFamily(Os.FAMILY_WINDOWS)

    this == WINDOWS
  }

  companion object {
    operator fun invoke(name: String) = valueOf(name.toUpperCase())
    fun current() = when {
      Os.isFamily(Os.FAMILY_WINDOWS) -> WINDOWS
      Os.isFamily(Os.FAMILY_MAC) -> OSX
      Os.isFamily(Os.FAMILY_UNIX) && !Os.isFamily(Os.FAMILY_MAC) -> LINUX
      else -> error("Unknown OS")
    }
  }
}
