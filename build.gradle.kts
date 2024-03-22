plugins {
    kotlin("jvm") version "1.9.22"
}

val joglversion = "2.4.0"

group = "fortytwo.luxembourg"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jogamp.org/deployment/maven")
}

dependencies {
    implementation("org.jogamp.gluegen:gluegen-rt-main:$joglversion")
    implementation("org.jogamp.jogl:jogl-all-main:$joglversion")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}