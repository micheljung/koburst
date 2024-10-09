plugins {
  id("buildlogic.kotlin-common-conventions")
}

dependencies {
  implementation(project(":koburst-core"))
  implementation(libs.ktor.server.netty)
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.cio)
  implementation("io.ktor:ktor-client-cio-jvm:2.3.12")
}
