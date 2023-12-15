import com.gradle.enterprise.gradleplugin.internal.extension.BuildScanExtensionWithHiddenFeatures

plugins {
    id("com.gradle.common-custom-user-data-gradle-plugin") version "1.12"
    id("com.gradle.enterprise") version "3.16.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

val isCI = providers.environmentVariable("CI").isPresent

gradleEnterprise {
    server = "https://ge.solutions-team.gradle.com"
    buildScan {
        capture { isTaskInputFiles = true }
        isUploadInBackground = !isCI
        publishAlways()
        this as BuildScanExtensionWithHiddenFeatures
        publishIfAuthenticated()
        obfuscation {
            ipAddresses { addresses -> addresses.map { "0.0.0.0" } }
        }
    }
}

buildCache {
    local {
        isEnabled = true
    }

    remote(gradleEnterprise.buildCache) {
        isEnabled = true
        isPush = isCI
    }
}

rootProject.name = "develocity-connectivity-report"
