import org.apache.commons.lang3.SystemUtils

plugins {
	idea
	java
	id("gg.essential.loom") version "0.10.0.+"
	id("dev.architectury.architectury-pack200") version "0.1.3"
	id("com.github.johnrengelman.shadow") version "8.1.1"
	kotlin("jvm") version "2.0.0"
}

val base_group: String by project
val minecraft_version: String by project
val mod_version: String by project
val mixinGroup = "$base_group.mixin"
val mod_id: String by project
val transformerFile = file("src/main/resources/accesstransformer.cfg")

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

loom {
	launchConfigs {
		"client" {
			property("mixin.debug", "true")
			arg("--tweakClass", "org.spongepowered.asm.launch.MixinTweaker")
			arg("--tweakClass", "cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
		}
	}
	runConfigs {
		"client" {
			if (SystemUtils.IS_OS_MAC_OSX) {
				vmArgs.remove("-XstartOnFirstThread")
			}
		}
		remove(getByName("server"))
	}
	forge {
		pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
		mixinConfig("mixins.$mod_id.json")
		if (transformerFile.exists()) {
			println("Installing access transformer")
			accessTransformer(transformerFile)
		}
	}
	mixin {
		defaultRefmapName.set("mixins.$mod_id.refmap.json")
	}
}

tasks.compileJava {
	dependsOn(tasks.processResources)
}

sourceSets.main {
	output.setResourcesDir(sourceSets.main.flatMap { it.java.classesDirectory })
	java.srcDir(layout.projectDirectory.dir("src/main/kotlin"))
	kotlin.destinationDirectory.set(java.destinationDirectory)
}

// Dependencies:

repositories {
	mavenCentral()
	maven("https://repo.spongepowered.org/maven/")
	maven("https://repo.polyfrost.cc/releases")
}

val shadowImpl: Configuration by configurations.creating {
	configurations.implementation.get().extendsFrom(this)
}

dependencies {
	minecraft("com.mojang:minecraft:1.8.9")
	mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
	forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

	shadowImpl(kotlin("stdlib-jdk8"))

	shadowImpl("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
		isTransitive = false
	}
	annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT")

	compileOnly("cc.polyfrost:oneconfig-1.8.9-forge:0.2.2-alpha+")
}

tasks.withType(JavaCompile::class) {
	options.encoding = "UTF-8"
}

tasks.withType(org.gradle.jvm.tasks.Jar::class) {
	archiveBaseName.set(mod_id)
	manifest.attributes.run {
		this["FMLCorePluginContainsFMLMod"] = "true"
		this["ForceLoadAsMod"] = "true"

		// If you don't want mixins, remove these lines
		this["TweakClass"] = "org.spongepowered.asm.launch.MixinTweaker"
		this["MixinConfigs"] = "mixins.$mod_id.json"
		if (transformerFile.exists())
			this["FMLAT"] = "${mod_id}_at.cfg"
	}
}

tasks.processResources {
	inputs.property("version", project.version)
	inputs.property("mcversion", minecraft_version)
	inputs.property("modid", mod_id)
	inputs.property("basePackage", base_group)

	filesMatching(listOf("mcmod.info", "mixins.$mod_id.json")) {
		expand(inputs.properties)
	}

	rename("accesstransformer.cfg", "META-INF/${mod_id}_at.cfg")
}


val remapJar by tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
	archiveClassifier.set("")
	from(tasks.shadowJar)
	input.set(tasks.shadowJar.get().archiveFile)
}

tasks.jar {
	archiveClassifier.set("without-deps")
	destinationDirectory.set(layout.buildDirectory.dir("intermediates"))
	manifest.attributes += mapOf(
		"ModSide" to "CLIENT",
		"TweakOrder" to 0,
		"ForceLoadAsMod" to true,
		"TweakClass" to "cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker"
	)
}

tasks.shadowJar {
	destinationDirectory.set(layout.buildDirectory.dir("intermediates"))
	archiveClassifier.set("non-obfuscated-with-deps")
	configurations = listOf(shadowImpl)
	doLast {
		configurations.forEach {
			println("Copying dependencies into mod: ${it.files}")
		}
	}

	fun relocate(name: String) = relocate(name, "$base_group.deps.$name")
	relocate("cc.polyfrost:oneconfig-wrapper-launchwrapper:1.0.0-beta+")
}

tasks.assemble.get().dependsOn(tasks.remapJar)