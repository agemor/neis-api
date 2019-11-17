group = "kr.go.neis.api"
version = "4.0.0"

plugins {

    kotlin("jvm") version "1.3.50"

    id("org.jetbrains.dokka") version "0.9.17"
    id("fr.coppernic.versioning") version "3.1.2"
    id("com.jfrog.bintray") version "1.8.4"

    `maven-publish`
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib")) 
}

tasks {
    dokka {
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"
        moduleName = rootProject.name
    }
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
    dependsOn(tasks.dokka)
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

publishing {
    publications {
        create<MavenPublication>("lib") {

            groupId = "neis-api"
            artifactId = "kr.go.neis.api"
            version = project.versioning.info.display

            from(components["java"])
            artifact(dokkaJar)
            artifact(sourcesJar)

            pom.withXml {
                asNode().apply {
                    appendNode("name", rootProject.name)
                }
            }

        }
    }
}

bintray {
    // Getting bintray user and key from properties file or command line
    user = if (project.hasProperty("bintray_user")) project.property("bintray_user") as String else ""
    key = if (project.hasProperty("bintray_key")) project.property("bintray_key") as String else ""

    // Automatic publication enabled
    publish = true

    // Set maven publication onto bintray plugin
    setPublications("lib")

    // Configure package
    pkg.apply {
        repo = "maven"
        name = rootProject.name
        setLicenses("MIT")
        issueTrackerUrl = "https://github.com/agemor/neis-api/issues"
        githubRepo = "https://github.com/agemor/neis-api"

        // Configure version
        version.apply {
            name = project.versioning.info.display
            vcsTag = project.versioning.info.tag
        }
    }
}