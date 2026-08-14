plugins {
    id("net.frozenblock.triangle.common")
    id("org.quiltmc.gradle.licenser")
}

val githubActions: Boolean = System.getenv("GITHUB_ACTIONS") == "true"
val licenseChecks: Boolean = githubActions

val applyLicenses: Task by tasks

common {
    accessWidener()
}

neoForge {
    accessTransformers {} // Required for transitive AW to apply!
}

tasks {
    license {
        if (licenseChecks) {
            rule(rootProject.file("codeformat/HEADER"))

            include("**/*.java")
        }
    }
}

dependencies {
    compileOnly("net.fabricmc:sponge-mixin:0.17.3+mixin.0.8.7")
    compileOnly("io.github.llamalad7:mixinextras-common:0.5.3")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.3")
}

configurations {
    create("commonJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonResources") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

upload.maven {
    name.set("particletweaks-common")
}
