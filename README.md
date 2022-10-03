# henry-maven-repository

[![semantic-release: angular](https://img.shields.io/badge/semantic--release-angular-e10079?logo=semantic-release)](https://github.com/semantic-release/semantic-release)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v?label=Plugin+Portal&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fjp%2Fhenry%2Fgradle%2Fplugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/jp.henry.maven.repository)

Make it easier downloading artifacts from GitHub Packages.

This Gradle plugin is inspired by the [wantedly-maven-repository](https://github.com/wantedly/maven-repository).

## How to use

Make sure you're using Kotlin to write Gradle build script.

Apply the plugin to your build script, import the extension method and use it as `MavenArtifactRepository` like below:

```kotlin
import jp.henry.gradle.repository.packages
plugins {
    id("jp.henry.maven.repository")
}

repositories {
    packages(
        org = "organization",
        repo = "repo",
        groupRegex = """jp\.henry.*"""
    )
}
```

Then provide a GitHub access token via `GITHUB_TOKEN` environment variable.
For instance, your configuration of GitHub Actions will be like below:

```yaml
steps:
  - uses: actions/checkout@v3
  - run: |
      ./gradlew build 
    env:
      # The secrets.GITHUB_TOKEN has no permission to access other repos, so
      # we need to use PAT (Personal Access Token) or GitHub App token.
      GITHUB_TOKEN: ${{ secrets.PERSONAL_ACCESS_TOKEN }}
```

For the build in your local, you can store the token in the `app/src/main/resources/local.properties` file.

## How to develop

First, run `yarn init` to download NPM packages to install Git hooks.

Then simply run the Gradle wrapper by `./gradlew build`, it will resolve dependencies and build the project.

Note that this project uses [spotless](https://github.com/diffplug/spotless) to format code and build scripts.
So when you make some changes, format them by `./gradlew spotlessApply`.
It will be automatically executed if you've installed Git hooks by `yarn init`.

## License

Copyright &copy; 2022 [Henry, Inc.](https://corp.henry-app.jp/)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
