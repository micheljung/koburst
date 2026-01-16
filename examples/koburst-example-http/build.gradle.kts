plugins {
  id("buildlogic.kotlin-common-conventions")
}

dependencies {
  implementation(project(":koburst-core"))
  implementation(libs.ktor.server.netty)
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.cio)
  implementation(libs.ktor.client.cio.jvm)
}
