plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    java
    `maven-publish`
    id("de.chojo.publishdata") version "1.0.4"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://eldonexus.de/repository/maven-public")
    maven("https://eldonexus.de/repository/maven-proxies")
    maven("https://raw.githubusercontent.com/FabioZumbi12/RedProtect/mvn-repo/")
}

dependencies {
    implementation("de.eldoria", "eldo-util", "1.13.9")

    compileOnly("org.spigotmc", "spigot-api", "1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.mojang", "authlib", "1.5.25")
    compileOnly("org.jetbrains", "annotations", "16.0.2")
    compileOnly("com.github.SaberLLC", "Saber-Factions", "2.9.1-RC"){
        exclude("*")
    }
    compileOnly("world.bentobox", "bentobox", "1.16.2-SNAPSHOT")
    compileOnly("com.github.TechFortress", "GriefPrevention", "16.17.1")
    compileOnly("com.github.TownyAdvanced", "Towny", "0.97.1.0")
    compileOnly("com.plotsquared", "PlotSquared-Core", "6.9.0") {
        exclude("com.intellectualsites.paster")
        exclude("net.kyori")
        exclude("org.apache.logging.log4j")

    }
    compileOnly("com.plotsquared", "PlotSquared-Bukkit", "6.9.0") { isTransitive = false } // PlotSquared Bukkit API

    compileOnly("io.github.fabiozumbi12.RedProtect", "RedProtect-Spigot", "8.0.0-SNAPSHOT") {
        exclude("com.github.MilkBowl")
        exclude("com.github.TheBusyBiscuit")
        exclude("com.gmail.nossr50.mcMMO")
        exclude("net.ess3")
        exclude("org.spigotmc")
        exclude("org.spongepowered")
        exclude("com.typesafe")
    }

    testImplementation(platform("org.junit:junit-bom:5.7.2"))
    testImplementation("org.junit.jupiter", "junit-jupiter")
    testImplementation("org.spigotmc", "spigot-api", "1.16.5-R0.1-SNAPSHOT")
    testImplementation("com.github.seeseemelk", "MockBukkit-v1.16", "1.0.0")
}

group = "de.eldoria"
version = "1.3.7"
var mainPackage = "pickmeup"
val shadebase = group as String? + "." + mainPackage + "."

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

publishData {
    useEldoNexusRepos()
    publishComponent("java")
}

publishing {
    publications.create<MavenPublication>("maven") {
        publishData.configurePublication(this)
    }

    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    username = System.getenv("NEXUS_USERNAME")
                    password = System.getenv("NEXUS_PASSWORD")
                }
            }

            name = "EldoNexus"
            url = uri(publishData.getRepository())
        }
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    compileTestJava {
        options.encoding = "UTF-8"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    shadowJar {
        relocate("de.eldoria.eldoutilities", shadebase + "eldoutilities")
        mergeServiceFiles()
        minimize()
    }

    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }

    register<Copy>("copyToServer") {
        val path = project.property("targetDir") ?: "";
        if (path.toString().isEmpty()) {
            println("targetDir is not set in gradle properties")
            return@register
        }
        from(shadowJar)
        destinationDir = File(path.toString())
    }

    build {
        dependsOn(shadowJar)
    }
}

bukkit {
    authors = listOf("RainbowDashLabs")
    main = "de.eldoria.pickmeup.PickMeUp"
    website = "https://www.spigotmc.org/resources/88151/"
    apiVersion = "1.13"
    softDepend = listOf("BentoBox", "RedProtect", "GriefPrevention", "PlotSquared", "Towny", "Factions")
    commands {
        register("pickmeup") {
            description = "Main command of pick me up"
            usage = "Trust the tab completion"
            aliases = listOf("pmu")
        }
    }
}
