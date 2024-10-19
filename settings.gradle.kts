pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
  plugins {
  }
}
plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    // All repositories should be defined here, never in projects
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
  }
}

rootProject.name = "koburst"
include(
  "koburst-api",
  "koburst-core",
  "koburst-promgraf",
  "koburst-ui",
  "examples:koburst-example-simple",
  "examples:koburst-example-http",
  "examples:koburst-example-promgraf",
)
