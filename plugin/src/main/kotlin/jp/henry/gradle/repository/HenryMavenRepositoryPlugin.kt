/* (C) 2022 Henry, Inc. */
package jp.henry.gradle.repository

import java.io.File
import java.net.URI
import java.util.Properties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.slf4j.Logger

const val PROPERTIES_FILE_PATH = "local.properties"

data class Credential(val user: String?, val pass: String?)

lateinit var credential: Credential

abstract class HenryMavenRepositoryPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    credential = findCredentials(project.rootProject.file(PROPERTIES_FILE_PATH), project.logger)
  }

  companion object {
    fun findCredentials(file: File, logger: Logger): Credential {
      var user = System.getenv("GITHUB_USER")
      var token = System.getenv("GITHUB_TOKEN")
      if (token.isNullOrBlank()) {
        logger.debug("No GITHUB_TOKEN environment variable found")
        if (file.isFile) {
          file.inputStream().use {
            val prop = Properties()
            prop.load(it)
            user = prop.getProperty("GITHUB_USER")
            token = prop.getProperty("GITHUB_TOKEN")
          }
        } else {
          logger.warn("No properties file found at ${file.absolutePath}")
        }
      }
      if (token.isNullOrBlank()) {
        logger.warn(
            "No GITHUB_TOKEN found, it makes impossible to download artifacts from GitHub Packages")
      }
      return Credential(user, token)
    }
  }
}

fun RepositoryHandler.packages(
    repo: String,
    org: String = "bw-company",
    groupRegex: String = """jp\.henry.*""",
): MavenArtifactRepository = maven {
  // The name of organization should be lowercase letters
  // https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry
  it.url = URI.create("https://maven.pkg.github.com/${org.lowercase()}/${repo}")

  it.credentials { cred ->
    cred.username = credential.user ?: "dummy"
    cred.password = credential.pass ?: ""
  }
  it.content { content -> content.includeGroupByRegex(groupRegex) }
}
