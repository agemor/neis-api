group = "kr.go.neis.api"
version = "4.0.0"

plugins {
    kotlin("jvm") version "1.3.50"
    application
}

repositories {
    jcenter() 
}

dependencies {
    implementation(kotlin("stdlib")) 
}

application {
    mainClassName = "kr.go.neis.api.MainKt" 
}