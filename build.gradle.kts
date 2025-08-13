import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    alias(libs.plugins.shadow)
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
    implementation(libs.bstats.bukkit)
    compileOnly(libs.spigot.api)
    // Shaded in by Bukkit
    compileOnly(libs.netty.all)
    compileOnly(libs.placeholderapi)
}

group = "kernitus.plugin.OldCombatMechanics"
version = "1.13"
description = "OldCombatMechanics"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get().toInt()))
    }
}

sourceSets {
    named("main") {
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

tasks.named("assemble") {
    dependsOn("shadowJar")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveFileName.set("${project.name}-${project.version}.jar")
    dependencies {
        relocate("org.bstats", "kernitus.plugin.OldCombatMechanics.lib.bstats")
    }
}