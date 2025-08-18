import cl.franciscosolis.sonatypecentralupload.SonatypeCentralUploadTask
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "2.2.10"
    kotlin("plugin.serialization") version "2.2.10"
    alias(libs.plugins.detekt)
    alias(libs.plugins.sonatype.central.upload)
    alias(libs.plugins.devtools.ksp)
    `maven-publish`
}

group = "io.github.suitetecsa.sdk"
version = "1.0.0-alpha.5"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(libs.jsoup)
    implementation(libs.gson)
    implementation(libs.rx.java)

    testImplementation(libs.mockk)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    implementation(libs.retrofit2)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
    // Adaptador de Coroutines para Retrofit
    implementation(libs.retrofit2.kotlin.coroutines.adapter)
    // Adaptador RxJava para Retrofit
    implementation(libs.adapter.rxjava3)

    implementation(libs.java.jwt)

    ksp(libs.moshi.kotlin.codegen)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockwebserver)

    implementation(libs.bcprov.jdk15on)
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

kotlin {
    jvmToolchain(17)
}

publishing {
    // Configure el paquete de salida publicado, un proyecto puede tener múltiples salidas, pero solo una es
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set(project.name)
                description.set("A tool designed to interact with ETECSA services.")
                url.set("https://github.com/suitetecsa/sdk-kotlin")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://github.com/suitetecsa/sdk-kotlin/blob/master/LICENSE")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("lesclaz")
                        name.set("Lesly Cintra")
                        email.set("lesclaz95@gmail.com")
                    }
                }
                scm {
                    url.set("http://github.com/suitetecsa/sdk-kotlin/tree/master")
                    connection.set("scm:git:git://github.com/suitetecsa/sdk-kotlin.git")
                    developerConnection.set("scm:git:ssh://github.com/suitetecsa/sdk-kotlin.git")
                }
            }
        }
    }
}

tasks.named<SonatypeCentralUploadTask>("sonatypeCentralUpload") {
    dependsOn("jar", "sourcesJar", "javadocJar", "generatePomFileForMavenPublication")

    username = System.getenv("SONATYPE_USERNAME")
    password = System.getenv("SONATYPE_PASSWORD")

    archives = files(
        tasks.named("jar"),
        tasks.named("sourcesJar"),
        tasks.named("javadocJar"),
    )
    pom = file(
        tasks.named("generatePomFileForMavenPublication").get().outputs.files.single()
    )

    signingKey = System.getenv("SIGNING_KEY")
    signingKeyPassphrase = System.getenv("SIGNING_KEY_PASSPHRASE")
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom(
        "$projectDir/config/detekt.yml"
    ) // point to your custom config defining rules to run, overwriting default behavior
    baseline =
        file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
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
