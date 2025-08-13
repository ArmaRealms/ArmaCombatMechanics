import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    id("com.gradleup.shadow") version "9.0.1"
}

repositories {
    mavenCentral()
    // Spigot API
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    // Placeholder API for reflection utils
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    // CodeMC Repo for bStats
    maven("https://repo.codemc.org/repository/maven-public/")
    // Netty library from Minecraft
    maven("https://libraries.minecraft.net/")
}

dependencies {
    implementation("org.bstats:bstats-bukkit:3.0.2")
    compileOnly("org.spigotmc:spigot-api:1.21.8-R0.1-SNAPSHOT")
    // Shaded in by Bukkit
    compileOnly("io.netty:netty-all:4.1.93.Final")
    compileOnly("me.clip:placeholderapi:2.11.6")
}

group = "kernitus.plugin.OldCombatMechanics"
version = "1.12.2"
description = "OldCombatMechanics"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

sourceSets {
    main {
        java {
            exclude("kernitus/plugin/OldCombatMechanics/tester/**")
        }
    }
}

// Substitute ${pluginVersion} in plugin.yml with version defined above
tasks.named<Copy>("processResources") {
    inputs.property("pluginVersion", version)
    filesMatching("plugin.yml") {
        expand("pluginVersion" to version)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveFileName.set("${project.name}-${project.version}.jar")
    dependencies {
        relocate("org.bstats", "kernitus.plugin.OldCombatMechanics.lib.bstats")
    }
}