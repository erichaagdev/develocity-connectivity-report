package dev.erichaag

import com.jakewharton.picnic.TableDsl
import com.jakewharton.picnic.table

fun printReport(client: GitHubClient, report: Report) {
    report.organizations.forEach { organization ->
        val repositories = client.getRepositories(organizationName = organization.name)
        val exclusions = (organization.exclusions + repositories.filter { it.archived }
            .map { ExcludedProject(it.name, "repository is archived") })
            .distinctBy { it.name }

        println("= ${organization.name} =")
        println()
        printGradleReport(repositories, exclusions)
        println()
        printMavenReport(repositories, exclusions)
        println()
        if (exclusions.isNotEmpty()) {
            printExclusions(exclusions)
            println()
        }
    }
}

private fun printExclusions(exclusions: List<ExcludedProject>) {
    println("== Excluded Projects ==")
    println()
    printTable {
        row("name", "reason")
        exclusions.sortedBy { it.name }.forEach { row(it.name, it.reason) }
    }
}

private fun printGradleReport(repositories: List<Repository>, exclusions: List<ExcludedProject>) {
    printReport(
        buildTool = "Gradle",
        repositories = repositories.filter { it.isGradle() },
        exclusions = exclusions,
        columns = listOf("name", "stars", "last commit", "badge", "plugin", "url")
    )
}

private fun printMavenReport(repositories: List<Repository>, exclusions: List<ExcludedProject>) {
    printReport(
        buildTool = "Maven",
        repositories = repositories.filter { it.isMaven() },
        exclusions = exclusions,
        columns = listOf("name", "stars", "last commit", "badge", "extension", "url")
    )
}

private fun printReport(buildTool: String, repositories: List<Repository>, exclusions: List<ExcludedProject>, columns: List<String>) {
    val includedRepositories = repositories.filter { !isExcludedProject(it, exclusions) }
    println("== $buildTool ==")
    println()
    printTable {
        row(*columns.toTypedArray())
        includedRepositories.sortedByDescending { it.stars }.forEach {
            row(
                it.name,
                it.stars,
                it.lastCommit,
                it.hasBadge().toYesNo(),
                (if (it.isGradle()) it.hasPlugin() else it.hasExtension()).toYesNo(),
                it.url
            )
        }
    }
    println()
    println("Connected $buildTool projects: ${includedRepositories.count { it.hasPlugin() || it.hasExtension() }} / ${includedRepositories.size}")
}

private fun Boolean.toYesNo() = if (this) "yes" else "no"

private fun isExcludedProject(repository: Repository, exclusions: List<ExcludedProject>) =
    exclusions.map { it.name }.contains(repository.name)

private fun printTable(content: TableDsl.() -> Unit) {
    table {
        cellStyle {
            paddingRight = 1
        }
        content()
    }.run { println(this) }
}
