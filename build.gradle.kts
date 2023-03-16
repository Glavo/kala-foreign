plugins {
    java
}

allprojects {
    group = "org.glavo.kala"
    version = "0.1.0" + "-SNAPSHOT"

    apply {
        plugin("java-library")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.glavo:classfile:0.2.0")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    }



    tasks.compileJava {
        sourceCompatibility = "19"
        targetCompatibility = sourceCompatibility

        options.compilerArgs.add("--enable-preview")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

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
