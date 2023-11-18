plugins {
    id("application")
    alias(libs.plugins.com.github.ben.manes.versions)
    alias(libs.plugins.nl.littlerobots.version.catalog.update)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.com.google.guava)
    implementation(libs.org.jgrapht.jgrapht.core)
    implementation(libs.commons.codec)
    implementation(libs.org.springframework.spring.context)

    testImplementation(libs.org.junit.jupiter.junit.jupiter)
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
