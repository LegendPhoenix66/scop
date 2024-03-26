import org.gradle.internal.os.OperatingSystem

group = "fortytwo.luxembourg"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.9.22"
}

val lwjglVersion = "3.3.3"
val jomlVersion = "1.10.5"

val lwjglNatives =
    Pair(
        System.getProperty("os.name")!!,
        System.getProperty("os.arch")!!,
    ).let { (name, arch) ->
        when {
            arrayOf("Linux", "SunOS", "Unit").any { name.startsWith(it) } ->
                if (arrayOf("arm", "aarch64").any { arch.startsWith(it) }) {
                    "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
                } else if (arch.startsWith("ppc")) {
                    "natives-linux-ppc64le"
                } else if (arch.startsWith("riscv")) {
                    "natives-linux-riscv64"
                } else {
                    "natives-linux"
                }

            arrayOf("Windows").any { name.startsWith(it) } ->
                "natives-windows"

            else ->
                throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
        }
    }

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-stb")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)

    implementation("org.joml", "joml", jomlVersion)

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
