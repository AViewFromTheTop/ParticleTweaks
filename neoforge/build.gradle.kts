plugins {
    id("net.frozenblock.triangle.neoforge")
    id("org.quiltmc.gradle.licenser")
}

val mod_id: String by project
val mod_version: String by project
val minecraft_version: String by project
val maven_group: String by project
val archives_base_name: String by project

val frozenlib_version: String by project
val cloth_config_version: String by project

val neoforge_version: String by project
val neoforge_loader_version_range: String by project

val neoforgeSnapshotMaven = findProperty("neoforge_snapshot_maven") as String?

base {
    archivesName.set(archives_base_name)
}

val release = findProperty("releaseType") == "stable"

version = getModVersion()
group = maven_group

tasks.jar {
    archiveClassifier.set("neoforge")
}

repositories {
    maven("https://maven.neoforged.net/releases") { name = "NeoForged" }
    if (!neoforgeSnapshotMaven.isNullOrBlank()) {
        maven(neoforgeSnapshotMaven) { name = "NeoForge Snapshots" }
    }
    flatDir {
        dirs("libs")
    }
}

neoforge {
    dependOn(project(":pt-common"))
    accessWidener(project(":pt-common"))
}

neoForge {
    accessTransformers {} // Required for transitive AW to apply!
}

val githubActions: Boolean = System.getenv("GITHUB_ACTIONS") == "true"
val licenseChecks: Boolean = githubActions

val applyLicenses: Task by tasks

tasks {
    license {
        if (licenseChecks) {
            rule(rootProject.file("codeformat/HEADER"))

            include("**/*.java")
        }
    }

    processResources {
        val properties = mapOf("mod_version" to getModVersion())
        inputs.properties(properties)
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(properties)
        }
    }

    withType(JavaCompile::class) {
        options.encoding = "UTF-8"
        options.release = 25
        options.isFork = true
        options.isIncremental = true
    }
}

dependencies {
    //"neoForge"("net.neoforged:neoforge:$neoforge_version")

    // FrozenLib
    implementation("net.frozenblock:frozenlib-neoforge:${frozenlib_version}")

    // Cloth Config (NeoForge edition)
    implementation("me.shedaniel.cloth:cloth-config-neoforge:$cloth_config_version") {
        exclude(group = "net.neoforged")
    }
}

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
val loaderVariants = setOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements")
configurations.all {
    if (name in loaderVariants) {
        attributes {
            attribute(loaderAttribute, "neoforge")
        }
    }
}
sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName).forEach { variant ->
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "neoforge")
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

val changelogText = run {
    val split = rootProject.file("CHANGELOG.txt").readText().split("-----------------")
    check(split.size == 2) { "Malformed changelog" }
    split[1].trim()
}

fun getModVersion(): String {
    var version = "$mod_version-mc$minecraft_version"

    if (release != null && !release) {
        //version += "-unstable"
    }

    return version
}

upload {
    maven {
        name.set("particletweaks-neoforge")
    }

    forEach {
        changelog = changelogText
    }

    curseforge {
        dependencies {
            required("frozenlib")
            optional("cloth-config")
        }
    }

    modrinth {
        dependencies {
            required("frozenlib")
            optional("cloth-config")
        }
    }
}
