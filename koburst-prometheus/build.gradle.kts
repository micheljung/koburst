plugins {
  id("buildlogic.kotlin-common-conventions")
  id("buildlogic.maven-publish-conventions")
}

dependencies {
  api(project(":koburst-api"))
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.ktor.server.core.jvm)
  implementation(libs.testcontainers)
}
