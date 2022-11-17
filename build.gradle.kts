val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val prometeus_version: String by project
val koin_version: String by project
val koin_ktor: String by project

plugins {
    application
    kotlin("jvm") version "1.7.20"
    id("io.ktor.plugin") version "2.1.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"
}

group = "samuele794.it"
version = "0.0.1"
application {
    mainClass.set("samuele794.it.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

dependencies {
//    implementation("io.ktor:ktor-bom:2.1.3")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-locations-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-compression-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-id-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-metrics-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-metrics-micrometer-jvm:$ktor_version")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometeus_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-tomcat-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")


    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version"){
        exclude("junit","junit" )
    }

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.7.20")

    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    kotlin("kotlin-reflect")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-jdk14
    implementation("org.slf4j:slf4j-jdk14:2.0.3")

    //https://github.com/DrewCarlson/kjob
    implementation("org.drewcarlson:kjob-core:0.5.0")
    implementation("org.drewcarlson:kjob-api:0.5.0")
    implementation("org.drewcarlson:kjob-kron:0.5.0")
    implementation("org.drewcarlson:kjob-inmem:0.5.0")

    implementation("org.jsoup:jsoup:1.15.3")

    implementation ("io.insert-koin:koin-core:$koin_version")
    // Koin for Ktor
    implementation ("io.insert-koin:koin-ktor:$koin_ktor")
// SLF4J Logger
    implementation ("io.insert-koin:koin-logger-slf4j:$koin_ktor")

// Koin Test features
//    testImplementation ("io.insert-koin:koin-test:$koin_version")
    testImplementation ("io.insert-koin:koin-test-junit5:$koin_version")
}

configurations.all {
    // Exclude JUNIT4 Dedicated Kotlin Annotation
    exclude("org.jetbrains.kotlin", "kotlin-test-junit")
}
