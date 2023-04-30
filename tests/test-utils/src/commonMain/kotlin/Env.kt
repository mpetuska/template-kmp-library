package test

expect object Env {
  operator fun get(key: String): String?
}