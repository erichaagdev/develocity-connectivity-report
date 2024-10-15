package dev.erichaag

data class Repository(
    val name: String,
    val stars: Int,
    val archived: Boolean,
    val url: Any,
    val readme: String?,
    val settingsGradle: String?,
    val settingsGradleKts: String?,
    val pomXml: String?,
    val develocityXml: String?,
    val gradleEnterpriseXml: String?,
    val extensionsXml: String?,
    val lastCommit: String,
) {

    fun hasBadge() = hasGradleEnterpriseBadge() || hasDevelocityBadge()

    fun hasGradleEnterpriseBadge() = readme != null && readme.contains("Revved up by Gradle Enterprise")

    fun hasDevelocityBadge() = readme != null && readme.contains("Revved up by Develocity")

    fun isGradle() = settingsGradle != null || settingsGradleKts != null

    fun isMaven() = pomXml != null

    fun hasPlugin() = settingsContains("com.gradle.enterprise")
            || settingsContains("com.gradle.develocity")
            || settingsContains("io.spring.ge.conventions")
            || settingsContains("io.spring.develocity.conventions")

    fun hasExtension() = develocityXml != null
            || gradleEnterpriseXml != null
            || extensionsXml != null && extensionsXml.contains("develocity-conventions-maven-extension")

    private fun settingsContains(value: String) =
        settingsGradle != null && settingsGradle.contains(value) ||
                settingsGradleKts != null && settingsGradleKts.contains(value)

}
