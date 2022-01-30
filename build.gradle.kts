import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"
}

group = "me.maksim"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}

val nativeBuildDestination = "build_executables"
val installingScriptsFolder = "InstallingScript"

task("copyBuildArtifacts") {
    dependsOn("createDistributable")
        doLast{
            if (Os.isFamily(Os.FAMILY_WINDOWS)) {
                copyArtifacts("$installingScriptsFolder/win")
            } else if (Os.isFamily(Os.FAMILY_MAC)) {
                copyArtifacts("$installingScriptsFolder/mac")
            } else {
                println("Unavailable OS")
            }
        }
}

tasks.build {
    finalizedBy("copyBuildArtifacts")
}

fun copyArtifacts(fromPath: String) {
    copy {
        println("Copying Build Artifacts")
        from(fromPath)
        include("**/*.*")
        into(layout.buildDirectory.dir("$nativeBuildDestination/main/app/TestsDiploma"))
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            packageName = "TestsDiploma"
            packageVersion = "1.0.0"
            description = "TestsDiploma description"
            copyright = "MCAL"

            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            outputBaseDir.set(project.buildDir.resolve(nativeBuildDestination))
            macOS {
                iconFile.set(project.file("src/main/resources/icon.icns"))
            }
            windows {
                dirChooser = true
                iconFile.set(project.file("src/main/resources/logo.ico"))
            }
            linux {
                iconFile.set(project.file("src/main/resources/icon.ico"))
            }
        }
    }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}