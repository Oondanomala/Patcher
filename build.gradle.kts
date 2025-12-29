import gg.essential.gradle.util.noServerRunConfigs
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("jvm")
    id("gg.essential.multi-version")
    id("gg.essential.defaults")
    id("com.gradleup.shadow") version "9.4.+"
}

val modGroup: String by project
val modBaseName: String by project
val modVersion: String by project
version = modVersion
group = modGroup
base.archivesName.set("$modBaseName-$modVersion (${platform.mcVersionStr})")

val accessTransformerName = "patcher1${platform.mcMinor}_at.cfg"

loom {
    noServerRunConfigs()
    forge {
        accessTransformer(rootProject.file("src/main/resources/$accessTransformerName"))
    }
    mixin {
        defaultRefmapName.set("patcher.mixins.refmap.json")
    }
    runConfigs {
        getByName("client") {
            property("fml.coreMods.load", "club.sk1er.patcher.tweaker.PatcherTweaker")
            property("patcher.debugBytecode", "true")
            property("mixin.debug.verbose", "true")
            property("mixin.debug.export", "true")
            property("mixin.dumpTargetOnFailure", "true")
            programArgs("--mixin", "patcher.mixins.json")
        }
    }
    // For some reason loom defaults to tab indentation
    decompilers {
        named("vineflower") {
            options.put("indent-string", "    ")
        }
    }
}

repositories {
    maven("https://repo.essential.gg/repository/maven-public/")
    maven("https://repo.spongepowered.org/repository/maven-public/")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    // Uncomment to launch the game with Essential loaded, for testing.
    // Don't forget to add --tweakClass gg.essential.loader.stage0.EssentialSetupTweaker to the launch CLI arguments and to re-enable transitive dependencies
    //implementation("gg.essential:loader-launchwrapper:1.1.3")

    compileOnly("gg.essential:essential-$platform:4246+g8be73312c") {
        isTransitive = false
    }
    shade("gg.essential:universalcraft-$platform:491")
    shade("gg.essential:elementa:743")
    // 312 has broken transparency, avoid it until it's fixed
    shade("gg.essential:vigilance:306")

    shade("com.github.ben-manes.caffeine:caffeine:2.9.3")
    shade("com.github.char:Koffee:88ba1b0") {
        exclude(module = "asm-commons")
        exclude(module = "asm-tree")
        exclude(module = "asm")
    }

    // TODO: Modern Mixin doesn't work in 1.8
    //  this means we can't use MixinExtras :(
    //shade("org.spongepowered:mixin:0.8.5-SNAPSHOT")
    shade("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        exclude(module = "guava")
        exclude(module = "gson")
        exclude(module = "commons-io")
        exclude(module = "log4j-core")
    }

    modRuntimeOnly("me.djtheredstoner:DevAuth-forge-legacy:1.2.2")
}

sourceSets.main {
    output.setResourcesDir(sourceSets.main.flatMap { it.java.classesDirectory })
}

kotlin {
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_1_9
        apiVersion = KotlinVersion.KOTLIN_1_9
        optIn.add("kotlin.RequiresOptIn")
        freeCompilerArgs.addAll("-Xno-param-assertions", "-Xjvm-default=all-compatibility")
    }
}

tasks {
    processResources {
        rename("(.+_at.cfg)", "META-INF/$1")
    }

    shadowJar {
        archiveVersion.set("")
        archiveClassifier.set("dev")
        configurations = listOf(shade)
    }

    jar {
        dependsOn(shadowJar)
        duplicatesStrategy = DuplicatesStrategy.FAIL

        manifest.attributes(mapOf(
            "FMLCorePlugin" to "club.sk1er.patcher.tweaker.PatcherTweaker",
            "ModSide" to "CLIENT",
            "FMLAT" to accessTransformerName,
            "FMLCorePluginContainsFMLMod" to "Yes, yes it does",
            "ForceLoadAsMod" to true,
            "Main-Class" to "club.sk1er.container.ContainerMessage",
            "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
            "TweakOrder" to "0",
            "MixinConfigs" to "patcher.mixins.json"
        ))
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveVersion.set("")
        archiveClassifier.set("")
    }
}
