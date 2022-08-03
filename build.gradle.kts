plugins {
    id("java")
    id("com.github.breadmoirai.github-release") version "2.4.1"
}

group = "io.github.singlerr"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

githubRelease{

}