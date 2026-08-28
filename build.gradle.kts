plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
}

group = "dev.kai"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/releases/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")

    compileOnly(libs.lombok)
    compileOnly(libs.packetevents)
    annotationProcessor(libs.lombok)
}

configurations.all {
    resolutionStrategy {
        force("com.google.guava:guava:33.4.0-jre")
        force("com.google.code.gson:gson:2.13.2")
        force("it.unimi.dsi:fastutil:8.5.18")

        eachDependency {
            if (requested.group == "net.kyori"
                    && requested.name == "adventure-text-serializer-ansi"
                    && requested.version.isNullOrBlank()
            ) {
                useVersion("4.17.0")
            }
        }
    }
}

tasks {
    jar {
        enabled = false
    }

    shadowJar {
        archiveFileName = "${rootProject.name}-${project.version}.jar"
        archiveClassifier = null

        manifest {
            attributes["Implementation-Version"] = rootProject.version
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }

    assemble {
        dependsOn(shadowJar)
    }

    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }

    withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }

    defaultTasks("build")
}