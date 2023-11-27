package dev.erichaag

fun main() {
    val apiKey = System.getenv()["GITHUB_API_KEY"] ?: throw RuntimeException("Environment variable 'GITHUB_API_KEY' is required.")
    val client = GitHubClient(apiKey = apiKey)

    val report = report {
        organization("spring-projects") {
            exclude(name = "gradle-plugins", reason = "no activity since 2017")
            exclude(name = "spring-boot-data-geode", reason = "project EOL")
            exclude(name = "spring-data", reason = "empty repository")
            exclude(name = "spring-data-bom", reason = "maintainers declined PR: https://github.com/spring-projects/spring-data-bom/pull/185#issuecomment-1827968368")
            exclude(name = "spring-data-book", reason = "no activity since 2019")
            exclude(name = "spring-data-envers", reason = "merged with Spring Data JPA")
            exclude(name = "spring-data-gemfire", reason = "no activity since 2021")
            exclude(name = "spring-data-geode", reason = "project EOL")
            exclude(name = "spring-data-r2dbc", reason = "merged with Spring Data Relational")
            exclude(name = "spring-integration-flow", reason = "no activity since 2019")
            exclude(name = "spring-integration-splunk", reason = "no activity since 2017")
            exclude(name = "spring-loaded", reason = "no activity since 2021")
            exclude(name = "spring-petclinic", reason = "reference Spring Boot project")
            exclude(name = "spring-session-data-geode", reason = "project EOL")
            exclude(name = "spring-test-data-geode", reason = "project EOL")
        }
        organization("spring-cloud")
        organization("apache")
    }

    printReport(client, report)
}
