plugins {
  id("buildlogic.kotlin-common-conventions")
  id("buildlogic.maven-publish-conventions")
}

dependencies {
  implementation(libs.ktor.server.metrics.micrometer)
}
