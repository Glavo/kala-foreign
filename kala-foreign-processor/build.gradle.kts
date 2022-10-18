dependencies {
    implementation(project(":kala-foreign-core"))

    // https://mvnrepository.com/artifact/javassist/javassist
    implementation("javassist:javassist:3.12.1.GA")
}

tasks.jar {
    manifest.attributes("Main-Class" to "kala.foreign.processor.Processor")
}