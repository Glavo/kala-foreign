rootProject.name = "kala-foreign"

val modules = listOf(
    "annotations",
    "core"
)

include(modules.map { "${rootProject.name}-$it" })

