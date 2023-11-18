plugins {
    id("application")
    id("com.github.ben-manes.versions") version "0.49.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("org.jgrapht:jgrapht-core:1.5.2")
    implementation("commons-codec:commons-codec:1.16.0")
    implementation("org.springframework:spring-context:6.0.13")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

application {
    mainClass = "tech.skagedal.javaaoc.Main"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.compileJava {
    options.compilerArgs.add("--enable-preview")
}
tasks.withType<JavaExec> {
    jvmArgs("--enable-preview")
}
tasks.test {
    jvmArgs("--enable-preview")
    useJUnitPlatform()
}
