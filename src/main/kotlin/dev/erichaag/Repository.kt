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
    val gradleEnterpriseXml: String?,
    val lastCommit: String,
) {

    fun hasBadge() = readme != null && readme.contains("Revved") && readme.contains("Gradle") && readme.contains("Enterprise")

    fun isGradle() = settingsGradle != null || settingsGradleKts != null

    fun isMaven() = pomXml != null

    fun hasPlugin() = settingsGradle != null && settingsGradle.contains("com.gradle.enterprise") ||
            settingsGradleKts != null && settingsGradleKts.contains("com.gradle.enterprise")

    fun hasExtension() = gradleEnterpriseXml != null
}
