plugins {
    id("net.frozenblock.triangle.neoforge")
    id("org.quiltmc.gradle.licenser")
    checkstyle
}

checkstyle {
    configFile = rootProject.file("checkstyle.xml")
    toolVersion = "10.20.2"
}

val mod_version: String by project
val minecraft_version: String by project
val maven_group: String by project
val archives_base_name: String by project

val frozenlib_version: String by project
val wilderwild_version: String by project
val trailiertales_version: String by project
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

dependencies {
    // FrozenLib
    api("net.frozenblock:frozenlib-neoforge:${frozenlib_version}")?.let {
        accessTransformers(it)
        interfaceInjectionData(it)
    }

    // Wilder Wild
    implementation("net.frozenblock:wilderwild-neoforge:${wilderwild_version}")

    // Trailier Tales
    implementation("net.frozenblock:trailiertales-neoforge:${trailiertales_version}")

    // Cloth Config
    implementation("me.shedaniel.cloth:cloth-config-neoforge:${cloth_config_version}")
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
