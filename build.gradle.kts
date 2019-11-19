// Bintray repository
val bintrayRepo = "neis-api"
val bintrayName = "kr.go.neis.api"
val bintrayUser: String by project
val bintrayKey: String by project
// Module name
val libraryName = "neis-api"

// Artifact information (eg. kr.go.neis.api:neis-api:4.0.0)
val publishedGroupId = "kr.go.neis.api"
val artifact = "neis-api"
val libraryVersion = "4.0.0"

val libraryDescription = "빠르고 가벼운 전국 초,중,고등학교 급식 식단표/학사일정 파서"
val siteUrl = "https://github.com/agemor/neis-api"
val gitUrl = "https://github.com/agemor/neis-api.git"
val developerId = "agemor"
val developerName = "HyunJun Kim"
val developerEmail = "hyunjun.leo.kim@gmail.com"
val licenseName = "The MIT License"
val licenseId = "MIT"
val licenseUrl = "https://opensource.org/licenses/MIT"


plugins {
    kotlin("jvm") version "1.3.50"
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
}


val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

publishing {
    publications {
        create<MavenPublication>("lib") {

            groupId = publishedGroupId
            artifactId = artifact
            version = libraryVersion

            from(components["java"])
            artifact(sourcesJar)

            pom {
                name.set(libraryName)
                description.set(libraryDescription)
                url.set(siteUrl)
                licenses {
                    license {
                        name.set(licenseName)
                        url.set(licenseUrl)
                    }
                }
                developers {
                    developer {
                        id.set(developerId)
                        name.set(developerName)
                        email.set(developerEmail)
                    }
                }
                scm {
                    connection.set(gitUrl)
                    developerConnection.set(gitUrl)
                    url.set(siteUrl)
                }
            }

        }
    }
}

bintray {
    // Getting bintray user and key from properties file or command line
    user = bintrayUser
    key = bintrayKey

    // Automatic publication enabled
    publish = true

    // Set maven publication onto bintray plugin
    setPublications("lib")

    // Configure package
    pkg.apply {
        repo = bintrayRepo
        name = bintrayName
        desc = libraryDescription
        websiteUrl = siteUrl
        vcsUrl = gitUrl
        setLicenses(licenseId)
        publish = true
        publicDownloadNumbers = true

        // Configure version
        version.apply {
            name = libraryVersion
        }
    }
}