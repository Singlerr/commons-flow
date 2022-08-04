plugins {
    id("java")
    id("maven-publish")
    id("com.github.breadmoirai.github-release") version "2.4.1"
}

val author = "Singlerr"

group = "io.github.singlerr"
version = "1.0.4"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

java{
    withSourcesJar()
    withJavadocJar()
}


githubRelease{
    token { System.getenv("GITHUB_TOKEN") }
    owner.set(author)
    repo.set(rootProject.name)
    tagName.set("v${version}")
    targetCommitish.set("master")
    releaseName.set("v${version}")
    generateReleaseNotes.set(true)
    releaseAssets.setFrom(tasks.jar.get().destinationDirectory.asFile.get().listFiles())
}