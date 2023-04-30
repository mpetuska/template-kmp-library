package test

actual object Env {
  actual operator fun get(key: String): String?  = System.getenv(key)
}