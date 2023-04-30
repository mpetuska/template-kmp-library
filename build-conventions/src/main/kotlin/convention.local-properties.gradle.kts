import java.util.*

fun File.localProperties() = resolve("local.properties").takeIf(File::exists)?.let {
  Properties().apply {
    it.inputStream().use(::load)
  }.mapKeys { (k, _) -> k.toString() }
}?.toList()?.forEach { (k, v) ->
  project.extra[k] = v
}

rootDir.localProperties()
projectDir.localProperties()