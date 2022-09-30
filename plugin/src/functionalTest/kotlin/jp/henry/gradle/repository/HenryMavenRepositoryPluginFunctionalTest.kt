/* (C) 2022 Henry, Inc. */
package jp.henry.gradle.repository

import kotlin.test.Test
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder

class HenryMavenRepositoryPluginFunctionalTest {
  @get:Rule val tempFolder = TemporaryFolder()

  private fun getProjectDir() = tempFolder.root
  private fun getBuildFile() = getProjectDir().resolve("build.gradle.kts")
  private fun getSettingsFile() = getProjectDir().resolve("settings.gradle.kts")

  @Test
  fun `can run task`() {
    getSettingsFile().writeText("")
    getBuildFile()
        .writeText(
            """
import jp.henry.gradle.repository.packages
plugins {
    id("jp.henry.maven.repository")
}

repositories {
    packages("henry-apis")
}
""")

    val runner = GradleRunner.create()
    runner.forwardOutput()
    runner.withPluginClasspath()
    runner.withArguments("build")
    runner.withProjectDir(getProjectDir())

    runner.build()
  }

  @Test
  fun `can run task with organization name`() {
    getSettingsFile().writeText("")
    getBuildFile()
        .writeText(
            """
import jp.henry.gradle.repository.packages
plugins {
    id("jp.henry.maven.repository")
}

repositories {
    packages(
        org = "organization",
        repo = "repository",
        groupRegex = "com\\.example.*"
    )
}
""")

    val runner = GradleRunner.create()
    runner.forwardOutput()
    runner.withPluginClasspath()
    runner.withArguments("build")
    runner.withProjectDir(getProjectDir())

    runner.build()
  }
}
