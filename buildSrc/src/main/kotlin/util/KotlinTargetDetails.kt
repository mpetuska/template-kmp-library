package util


enum class NativeHost {
  LINUX,
  OSX,
  WINDOWS,
}

enum class KotlinTargetDetails(
  val presetName: String,
  val hasCoroutines: Boolean,
  vararg val supportedBuildHosts: NativeHost,
) {
  JVM("jvm", true),
  ANDROID("android", false),
  JS("jsIr", true),

  ANDROID_NDK_ARM32("androidNativeArm32", false),
  ANDROID_NDK_ARM64("androidNativeArm64", false, NativeHost.LINUX, NativeHost.OSX),

  IOS_ARM32("iosArm32", true, NativeHost.OSX),
  IOS_ARM64("iosArm64", true, NativeHost.OSX),
  IOS_X64("iosX64", true, NativeHost.OSX),

  WATCHOS_X86("watchosX86", true, NativeHost.OSX),
  WATCHOS_X64("watchosX64", true, NativeHost.OSX),
  WATCHOS_ARM64("watchosArm64", true, NativeHost.OSX),
  WATCHOS_ARM32("watchosArm32", true, NativeHost.OSX),

  TVOS_ARM64("tvosArm64", true, NativeHost.OSX),
  TVOS_X64("tvosX64", true, NativeHost.OSX),

  MACOS_X64("macosX64", true, NativeHost.OSX),

  LINUX_ARM32_HFP("linuxArm32Hfp", false),
  LINUX_MIPS32("linuxMips32", false, NativeHost.LINUX),
  LINUX_MIPSEL32("linuxMipsel32", false, NativeHost.LINUX),
  LINUX_X64("linuxX64", true),
  LINUX_ARM64("linuxArm64", false),

  MINGW_X64("mingwX64", true, NativeHost.WINDOWS),
  MINGW_X32("mingwX86", false, NativeHost.WINDOWS),
}