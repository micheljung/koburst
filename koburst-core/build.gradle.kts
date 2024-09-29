plugins {
  id("buildlogic.kotlin-common-conventions")
}

dependencies {
  api(project(":koburst-api"))
  implementation(libs.kotlinx.coroutines.core)
}
