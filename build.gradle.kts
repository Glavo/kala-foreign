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
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    }

    tasks.compileJava {
        options.release.set(19)
        options.compilerArgs.addAll(listOf(
            "--enable-preview",
        ))
    }

    tasks.test {
        useJUnitPlatform()
    }
}