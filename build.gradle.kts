import org.kohsuke.github.GHReleaseBuilder
import org.kohsuke.github.GitHub

plugins {
    id("net.frozenblock.triangle.core")
    id("net.frozenblock.triangle.common") version("+") apply(false)
    id("net.frozenblock.triangle.fabric") version("+") apply(false)
    id("net.frozenblock.triangle.neoforge") version("+") apply(false)
    id("net.mehvahdjukaar.candlelight") version("+") apply(false)

    id("org.quiltmc.gradle.licenser") version("+") apply(false)
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.kohsuke:github-api:1.326")
    }
}

val frozenlib_version: String by project

val changelogText = run {
    val split = file("CHANGELOG.txt").readText().split("-----------------")
    check(split.size == 2) { "Malformed changelog" }
    split[1].trim()
}

fun mainJarTask(project: Project) =
    if (project.tasks.names.contains("shadowJar")) project.tasks.named("shadowJar")
    else project.tasks.named("jar")

val githubRelease by tasks.registering {
    val fabricJar = mainJarTask(project(":pt-fabric"))
    val neoforgeJar = mainJarTask(project(":pt-neoforge"))
    dependsOn(fabricJar, neoforgeJar)

    val token = env["GITHUB_TOKEN"]
    val repository = mod.repository.get()
    val tag = project(":pt-fabric").version.toString()
    val releaseTitle = "Particle Tweaks $tag"
    val isPrerelease = mod.releaseType.get() != "release"
    val commitish = env["GITHUB_SHA"]

    onlyIf { !token.isNullOrEmpty() }

    doLast {
        val github = GitHub.connectUsingOAuth(token)
        val repo = github.getRepository(repository)

        repo.getReleaseByTagName(tag)?.delete()

        val releaseBuilder = GHReleaseBuilder(repo, tag)
        releaseBuilder.name(releaseTitle)
        releaseBuilder.body(changelogText)
        releaseBuilder.prerelease(isPrerelease)
        if (commitish != null) releaseBuilder.commitish(commitish)

        val release = releaseBuilder.create()
        release.uploadAsset(fabricJar.get().outputs.files.singleFile, "application/java-archive")
        release.uploadAsset(neoforgeJar.get().outputs.files.singleFile, "application/java-archive")
    }
}

val publishMod by tasks.registering {
    dependsOn(tasks.named("upload"))
    dependsOn(githubRelease)
}

subprojects {
    apply(plugin = "net.frozenblock.triangle.core")
    apply(plugin = "net.mehvahdjukaar.candlelight")

    val mavenUrl = env["MAVEN_URL"]
    val mavenUsername = env["MAVEN_USERNAME"]
    val mavenPassword = env["MAVEN_PASSWORD"]

    if (mavenUrl != null && mavenUsername != null && mavenPassword != null) {
        upload {
            maven {
                repositories {
                    maven(mavenUrl) {
                        name = "FrozenBlock"
                        credentials {
                            username = mavenUsername
                            password = mavenPassword
                        }
                    }
                }
            }
        }
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(listOf("-Xmaxerrs", "4000"))
        options.release.set(25)
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }

    dependencies {
        compileOnly("net.mehvahdjukaar:candlelight:+")
        compileOnly("net.frozenblock:frozenlib-common:${frozenlib_version}")
    }

    if (project.name != "pt-common") {
        afterEvaluate {
            tasks.findByName("compileJava")?.dependsOn(":pt-common:candleLightTransform")
        }
    }

    repositories {
        maven("https://maven.frozenblock.net/release") {
            name = "FrozenBlock"
        }
        maven("https://maven.frozenblock.net/snapshot") {
            name = "FrozenBlock Snapshot"
        }

        exclusiveContent {
            forRepository {
                maven("https://repo.spongepowered.org/repository/maven-public") {
                    name = "Sponge"
                }
            }
            filter { includeGroupAndSubgroups("org.spongepowered") }
        }
        maven("https://maven.minecraftforge.net/") {
            name = "Forge"
        }
        maven("https://registry.somethingcatchy.net/repository/maven-releases/") { // Candlelight & Triangle
            name = "SomethingCatchy (MehVahdJukaar)"
        }

        maven("https://maven.quiltmc.org/repository/release") {
            name = "Quilt"
        }
        maven("https://maven.jamieswhiteshirt.com/libs-release") {
            name = "JamiesWhiteShirt"
            content {
                includeGroup("com.jamieswhiteshirt")
            }
        }
        maven("https://maven.shedaniel.me/") {
            name = "Shedaniel"
        }
        maven("https://maven.terraformersmc.com") {
            name = "TerraformersMC"
            content {
                includeGroup("com.terraformersmc")
            }
        }

        exclusiveContent {
            forRepository {
                maven("https://api.modrinth.com/maven") {
                    name = "Modrinth"
                }
            }
            filter {
                includeGroup("maven.modrinth")
            }
        }
        maven("https://jitpack.io") {
            name = "Jitpack"
        }
        mavenCentral()
    }

    tasks {
        withType(JavaCompile::class) {
            options.encoding = "UTF-8"
            options.release.set(25)
            options.isFork = true
            options.isIncremental = true
        }

        withType(Test::class) {
            maxParallelForks = Runtime.getRuntime().availableProcessors().div(2)
        }
    }
}
