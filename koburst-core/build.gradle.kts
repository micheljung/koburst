plugins {
  id("buildlogic.kotlin-common-conventions")
  id("buildlogic.maven-publish-conventions")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(project(":koburst-api"))
  implementation(project(":koburst-ui"))
  compileOnly(project(":koburst-promgraf"))

  api(libs.kotlinx.coroutines.core)
  implementation(libs.ktor.serialization.kotlinx.protobuf)
  implementation(libs.ktor.server.content.negotiation)
  implementation(libs.ktor.server.metrics.micrometer)
  implementation(libs.ktor.server.netty)
  implementation(libs.ktor.server.websocket)
  implementation(libs.kotlinx.serialization.protobuf)
  implementation(libs.logback.classic)
  compileOnly(libs.micrometer.registry.prometheus)
}
