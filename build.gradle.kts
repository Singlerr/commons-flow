import java.util.*

plugins {
    java
    `maven-publish`
    signing
    id("com.github.breadmoirai.github-release") version "2.4.1"
}

val author = "Singlerr"
group = "io.github.singlerr"
version = "1.0.7"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
java {
    withSourcesJar()
    withJavadocJar()
}
ext["signing.keyId"] = null
ext["signing.password"] = null
ext["signing.secretKeyRingFile"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null

val secretPropsFile = project.rootProject.file("./.gradle/gradle.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}
fun getExtraString(name: String) = ext[name]?.toString()
githubRelease {
    token { System.getenv("GITHUB_TOKEN") }
    owner.set(author)
    repo.set(rootProject.name)
    tagName.set("v${version}")
    targetCommitish.set("master")
    releaseName.set("v${version}")
    generateReleaseNotes.set(true)
    releaseAssets.setFrom(tasks.jar.get().destinationDirectory.asFile.get().listFiles())
}
publishing {
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }
    publications.create<MavenPublication>("maven"){
        groupId = project.group.toString()
        artifactId = rootProject.name
        version = project.version.toString()
    }
    publications.withType<MavenPublication> {
        artifact(tasks.getByName("javadocJar"))
        pom {
            name.set("commons-flow")
            description.set("A java library for handle exceptions with flow")
            url.set("https://github.com/Singlerr/commons-flow")
            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("singlerr")
                    name.set("Singlerr")
                    email.set("singlerr@naver.com")
                }
            }
            scm {
                url.set("https://github.com/Singlerr/commons-flow")
            }

        }
    }
}

signing {
    sign(publishing.publications)
}