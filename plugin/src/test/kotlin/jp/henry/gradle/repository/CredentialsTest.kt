/* (C) 2022 Henry, Inc. */
package jp.henry.gradle.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.slf4j.helpers.NOPLogger

class CredentialsTest {
  @get:Rule val tempFolder = TemporaryFolder()

  @Test
  fun `can take values from the properties file`() {
    val file = tempFolder.newFile()
    file.writeText(
        """
            GITHUB_USER=my-github-user
            GITHUB_TOKEN=my-github-token
        """
            .trimIndent())

    val credential = HenryMavenRepositoryPlugin.findCredentials(file, NOPLogger.NOP_LOGGER)
    if (System.getenv("GITHUB_TOKEN").isNullOrBlank()) {
      assertEquals(credential.user, "my-github-user")
      assertEquals(credential.pass, "my-github-token")
    } else {
      assertEquals(credential.user, System.getenv("GITHUB_USER"))
      assertEquals(credential.pass, System.getenv("GITHUB_TOKEN"))
    }
  }
}
