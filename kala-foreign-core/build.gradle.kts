dependencies {
    compileOnly("org.glavo.kala:kala-common:0.51.0")
}

val exports = listOf(
    "java.base" to listOf(
        "jdk.internal.access",
        "jdk.internal.misc",
    )
)

tasks.compileJava {
    options.compilerArgs.addAll(exports.flatMap { (mod, pkgs) -> pkgs.map { pkg -> "--add-exports=$mod/$pkg=kala.foreign" } })
}
