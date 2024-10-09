import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.get

plugins {
  `maven-publish`
}

group = rootProject.group
version = rootProject.version

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
    }
  }
}
