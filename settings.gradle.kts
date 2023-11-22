plugins {
    id("com.gradle.common-custom-user-data-gradle-plugin") version "1.12"
    id("com.gradle.enterprise") version "3.15.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

gradleEnterprise {
    if (providers.gradleProperty("agreeToGradleTermsOfService").orNull != "yes") return@gradleEnterprise
    buildScan {
        publishAlways()
        termsOfServiceAgree = "yes"
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
    }
}

rootProject.name = "develocity-connectivity-report"
