package dev.erichaag

import com.apollographql.apollo3.ApolloClient
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class GitHubClient(apiKey: String) {

    private val client = ApolloClient.Builder()
        .serverUrl("https://api.github.com/graphql")
        .addHttpHeader("Authorization", "Bearer $apiKey")
        .build()

    fun getRepositories(organizationName: String): List<Repository> {
        return getRepositories(organizationName, emptyList(), null).map { asRepository(it) }
    }

    private tailrec fun getRepositories(organizationName: String, nodes: List<RepositoriesQuery.Node>, endCursor: String?): List<RepositoriesQuery.Node> {
        val response = client.getRepositories(organizationName, endCursor)
        if (!response.pageInfo.hasNextPage) return nodes + response.nodes!!.filterNotNull()
        return getRepositories(organizationName, nodes + response.nodes!!.filterNotNull(), response.pageInfo.endCursor)
    }

    private fun ApolloClient.getRepositories(organizationName: String, endCursor: String?): RepositoriesQuery.Repositories {
        return retry {
            use {
                runBlocking {
                    it.query(RepositoriesQuery(organizationName = organizationName, after = endCursor))
                        .execute().data!!.organization!!.repositories
                }
            }
        }
    }

    /**
     * Retry with incremental backoff. Eventually calls would fail, likely due to rate limiting.
     */
    private fun retry(function: () -> RepositoriesQuery.Repositories): RepositoriesQuery.Repositories {
        val exceptions: MutableList<RuntimeException> = mutableListOf()
        do {
            try {
                return function()
            } catch (e: RuntimeException) {
                TimeUnit.SECONDS.sleep((1 + exceptions.size).toLong())
                exceptions.add(e)
            }
        } while (exceptions.size < 5)
        exceptions.forEach { println(it.message) }
        throw exceptions.last()
    }

    private fun asRepository(node: RepositoriesQuery.Node): Repository {
        return Repository(
            name = node.name,
            stars = node.stargazerCount,
            archived = node.isArchived,
            url = node.url,
            readme = findReadme(node),
            settingsGradle = node.settingsGradle?.onBlob?.text,
            settingsGradleKts = node.settingsGradleKts?.onBlob?.text,
            pomXml = node.pomXml?.onBlob?.text,
            develocityXml = node.develocityXml?.onBlob?.text,
            gradleEnterpriseXml = node.gradleEnterpriseXml?.onBlob?.text,
            extensionsXml = node.extensionsXml?.onBlob?.text,
            lastCommit = node.latestCommit?.onCommit?.committedDate.toString().take(10),
        )
    }

    private fun findReadme(it: RepositoriesQuery.Node): String? {
        return it.readmeMdLower?.onBlob?.text
            ?: it.readmeMdUpper?.onBlob?.text
            ?: it.readmeAdocLower?.onBlob?.text
            ?: it.readmeAdocUpper?.onBlob?.text
    }
}
