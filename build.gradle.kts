plugins {
    id("application")
    id("com.apollographql.apollo3") version "3.8.2"
    embeddedKotlin("jvm")
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "dev.erichaag.MainKt"
}

apollo {
    service("service") {
        packageName = "dev.erichaag"
        generateOptionalOperationVariables = false
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.apollographql.apollo3:apollo-runtime:3.8.4")
    implementation("com.jakewharton.picnic:picnic:0.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
}
