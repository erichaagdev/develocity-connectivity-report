package dev.erichaag

data class Report(val organizations: List<Organization>)
data class Organization(val name: String, val exclusions: List<ExcludedProject> = emptyList())
data class ExcludedProject(val name: String, val reason: String)

fun report(configure: ReportDsl.() -> Unit): Report {
    val report = ReportDsl().apply(configure)
    return Report(report.organizations)
}

data class ReportDsl(var organizations: MutableList<Organization> = mutableListOf()) {
    fun organization(name: String, configure: OrganizationDsl.() -> Unit = {}) {
        val organization = OrganizationDsl(name).apply(configure)
        organizations.add(Organization(organization.name, organization.excludedProjects))
    }
}

data class OrganizationDsl(var name: String, var excludedProjects: MutableList<ExcludedProject> = mutableListOf()) {
    fun exclude(name: String, reason: String) {
        excludedProjects.add(ExcludedProject(name, reason))
    }
}
