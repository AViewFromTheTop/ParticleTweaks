plugins {
    id("net.frozenblock.triangle.fabric")
    id("org.quiltmc.gradle.licenser")
    checkstyle
}

checkstyle {
    configFile = rootProject.file("checkstyle.xml")
    toolVersion = "10.20.2"
}

val githubActions: Boolean = System.getenv("GITHUB_ACTIONS") == "true"
val licenseChecks: Boolean = githubActions

val fabric_loader_version: String by project

val mod_version: String by project
val minecraft_version: String by project
val maven_group: String by project
val archives_base_name: String by project

val fabric_api_version: String by project
val frozenlib_version: String by project

val modmenu_version: String by project
val cloth_config_version: String by project
val wilderwild_version: String by project
val trailiertales_version: String by project

val sodium_version: String by project
val run_sodium: String by project
val shouldRunSodium = run_sodium == "true"

base {
    archivesName = archives_base_name
}

val release = findProperty("releaseType") == "stable"

version = getModVersion()
group = maven_group

tasks.jar {
    archiveClassifier.set("fabric")
}

fabric {
    dependOn(project(":pt-common"))
    accessWidener(project(":pt-common"))
    dataGen {
        owner = project(":pt-common")
        splitSourceSet("datagen")
    }
}

loom {
    enableTransitiveAccessWideners = true
    interfaceInjection {
        enableDependencyInterfaceInjection = true
    }
}

repositories {
    flatDir {
        dirs("libs")
    }
}

dependencies {
    // Fabric
    implementation("net.fabricmc:fabric-loader:${fabric_loader_version}")
    implementation("net.fabricmc.fabric-api:fabric-api:${fabric_api_version}")

    // FrozenLib
    api("net.frozenblock:frozenlib-fabric:${frozenlib_version}")

    // Mod Menu
    implementation("com.terraformersmc:modmenu:${modmenu_version}")

    // Cloth Config
    implementation("me.shedaniel.cloth:cloth-config-fabric:$cloth_config_version") {
        exclude(group = "net.fabricmc.fabric-api")
        exclude(group = "com.terraformersmc")
    }

    // Wilder Wild
    implementation("net.frozenblock:wilderwild-fabric:$wilderwild_version")

    // Trailier Tales
    compileOnly("net.frozenblock:trailiertales-fabric:$trailiertales_version")

    // Sodium
    if (shouldRunSodium)
        implementation("maven.modrinth:sodium:${sodium_version}")
    else
        compileOnly("maven.modrinth:sodium:${sodium_version}")
}

tasks {
    license {
        if (licenseChecks) {
            rule(rootProject.file("codeformat/HEADER"))

            include("**/*.java")
        }
    }
}

val applyLicenses: Task by tasks
val test: Task by tasks
val runClient: Task by tasks

val sourcesJar: Jar by tasks
val javadocJar: Jar by tasks

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

artifacts {
    archives(sourcesJar)
    archives(javadocJar)
}

fun getModVersion(): String {
    var version = "$mod_version-mc$minecraft_version"

    if (release != null && !release) {
        //version += "-unstable"
    }

    return version
}

val changelogText = run {
    val split = rootProject.file("CHANGELOG.txt").readText().split("-----------------")
    check(split.size == 2) { "Malformed changelog" }
    split[1].trim()
}

upload {
    maven {
        name.set("particletweaks-fabric")
    }

    forEach {
        changelog = changelogText
    }

    curseforge {
        dependencies {
            required("fabric-api")
            required("frozenlib")
            optional("modmenu")
            optional("cloth-config")
            optional("wilder-wild")
            optional("trailier-tales")
        }
    }

    modrinth {
        dependencies {
            required("fabric-api")
            required("frozenlib")
            optional("modmenu")
            optional("cloth-config")
            optional("wilder-wild")
            optional("trailier-tales")
        }
    }
}
