import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `java-gradle-plugin`
  id("com.diffplug.spotless") version "6.11.0"
  id("com.gradle.plugin-publish") version "1.0.0"
  id("org.jetbrains.kotlin.jvm") version "1.7.20"
}

group = "jp.henry.gradle"

repositories { mavenCentral() }

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

kotlin { jvmToolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "1.8" }

testing {
  suites {
    val test by getting(JvmTestSuite::class) { useKotlinTest() }

    val functionalTest by
        registering(JvmTestSuite::class) {
          useKotlinTest()

          dependencies { implementation(project) }
        }
  }
}

gradlePlugin {
  val henryMavenRepository by
      plugins.creating {
        description = "Make it easier downloading artifacts from GitHub Packages."
        displayName = "Henry Maven Repository"
        id = "jp.henry.maven.repository"
        implementationClass = "jp.henry.gradle.repository.HenryMavenRepositoryPlugin"
      }
}

pluginBundle {
  website = "https://github.com/bw-company/henry-maven-repository"
  vcsUrl = "https://github.com/bw-company/henry-maven-repository.git"
  tags = listOf("packages")
}

gradlePlugin.testSourceSets(sourceSets["functionalTest"])

tasks.named<Task>("check") { dependsOn(testing.suites.named("functionalTest")) }

spotless {
  kotlin {
    ktfmt()
    licenseHeader("/* (C) \$YEAR Henry, Inc. */")
  }
  kotlinGradle { ktfmt() }
}

publishing.publications.configureEach {
  (this as MavenPublication).pom {
    url.set("https://github.com/bw-company/henry-maven-repository/")
    scm {
      connection.set("scm:git:git@github.com:bw-company/henry-maven-repository.git")
      developerConnection.set("scm:git:git@github.com:bw-company/henry-maven-repository.git")
      url.set("https://github.com/bw-company/henry-maven-repository/")
    }
    licenses {
      license {
        name.set("Apache License 2.0")
        url.set("https://spdx.org/licenses/Apache-2.0.html")
      }
    }
    inceptionYear.set("2022")
    developers {
      developer {
        id.set("KengoTODA")
        name.set("Kengo TODA")
        email.set("toda_k@henry.jp")
        url.set("https://github.com/KengoTODA/")
        timezone.set("+8")
      }
    }
  }
}
