plugins {
  // TODO use version catalog?
  // https://plugins.gradle.org/plugin/org.gradle.kotlin.kotlin-dsl
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
}

dependencies {
//  implementation(libs.jandex.gradle.plugin)
  implementation(libs.kotlin.gradle.plugin)
  implementation(libs.kotlin.allopen.plugin)
}
