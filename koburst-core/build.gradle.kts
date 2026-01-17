plugins {
  id("buildlogic.kotlin-common-conventions")
  id("buildlogic.maven-publish-conventions")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(project(":koburst-api"))
  compileOnly(project(":koburst-promgraf"))

  api(libs.kotlinx.coroutines.core)
  api(libs.logback.classic)

  implementation(libs.ktor.serialization.kotlinx.protobuf)
  implementation(libs.ktor.server.content.negotiation)
  implementation(libs.ktor.server.metrics.micrometer)
  implementation(libs.ktor.server.cio)
  implementation(libs.ktor.server.websocket)
  implementation(libs.kotlinx.serialization.protobuf)
  compileOnly(libs.micrometer.registry.prometheus)
}
