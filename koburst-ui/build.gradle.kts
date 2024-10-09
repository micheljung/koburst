plugins {
  id("buildlogic.kotlin-common-conventions")
  id("buildlogic.maven-publish-conventions")
}

dependencies {
  api(project(":koburst-api"))
  api(libs.kotlinx.coroutines.core)
}
