import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `java-gradle-plugin`
  id("com.diffplug.spotless") version "7.0.2"
  id("com.gradle.plugin-publish") version "1.3.0"
  id("org.jetbrains.kotlin.jvm") version "2.1.0"
}

group = "jp.henry.gradle"

repositories { mavenCentral() }

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

kotlin { jvmToolchain { languageVersion = JavaLanguageVersion.of("8") } }

java { toolchain { languageVersion = JavaLanguageVersion.of("8") } }

tasks.withType<KotlinCompile> { compilerOptions { jvmTarget = JvmTarget.JVM_1_8 } }

testing {
  suites {
    val test by getting(JvmTestSuite::class) { useJUnit() }

    val functionalTest by
        registering(JvmTestSuite::class) {
          useJUnit()

          dependencies { implementation(project()) }
        }
  }
}

gradlePlugin {
  website = "https://github.com/bw-company/henry-maven-repository"
  vcsUrl = "https://github.com/bw-company/henry-maven-repository.git"

  val henryMavenRepository by
      plugins.creating {
        description = "Make it easier downloading artifacts from GitHub Packages."
        displayName = "Henry Maven Repository"
        id = "jp.henry.maven.repository"
        implementationClass = "jp.henry.gradle.repository.HenryMavenRepositoryPlugin"
        tags = listOf("packages")
      }
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
    url = "https://github.com/bw-company/henry-maven-repository/"
    scm {
      connection = "scm:git:git@github.com:bw-company/henry-maven-repository.git"
      developerConnection = "scm:git:git@github.com:bw-company/henry-maven-repository.git"
      url = "https://github.com/bw-company/henry-maven-repository/"
    }
    licenses {
      license {
        name = "Apache License 2.0"
        url = "https://spdx.org/licenses/Apache-2.0.html"
      }
    }
    inceptionYear = "2022"
    developers {
      developer {
        id = "KengoTODA"
        name = "Kengo TODA"
        email = "toda_k@henry.jp"
        url = "https://github.com/KengoTODA/"
        timezone = "+8"
      }
    }
  }
}
