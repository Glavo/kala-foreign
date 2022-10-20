dependencies {
    implementation(project(":kala-foreign-core"))

    // https://mvnrepository.com/artifact/org.javassist/javassist
    implementation("org.javassist:javassist:3.29.2-GA")
}

tasks.jar {
    manifest.attributes("Main-Class" to "kala.foreign.processor.Processor")
}