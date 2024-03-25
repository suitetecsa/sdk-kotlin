import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    kotlin("jvm") version "1.9.23"
    alias(libs.plugins.detekt)
    `maven-publish`
}

group = "cu.suitetecsa"
version = "1.0.0-alpha02"

publishing {
    // Configure el paquete de salida publicado, un proyecto puede tener múltiples salidas, pero solo una es
    publications {
        create<MavenPublication>("sdk-kotlin") {
            from(components["java"])
        }
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(libs.jsoup)
    implementation(libs.gson)
    implementation(libs.rx.java)

    testImplementation(libs.mockk)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom(
        "$projectDir/config/detekt.yml"
    ) // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

tasks.withType<Detekt>().configureEach {
    reports {
        // observe findings in your browser with structure and code snippets
        html.required.set(true)
        // checkstyle like format mainly for integrations like Jenkins
        xml.required.set(true)
        // similar to the console output, contains issue signature to manually edit baseline files
        txt.required.set(true)
        // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations
        // with GitHub Code Scanning
        sarif.required.set(true)
        // simple Markdown format
        md.required.set(true)
    }
}

// Kotlin DSL
tasks.withType<Detekt>().configureEach {
    jvmTarget = "17"
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "17"
}
