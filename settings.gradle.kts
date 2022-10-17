rootProject.name = "kala-foreign"

val modules = listOf(
    "core",
    "processor"
)

include(modules.map { "${rootProject.name}-$it" })

