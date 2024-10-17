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
      pom {
        name = "KoBurst"
        description = "Load testing library for Kotlin developers"
        licenses {
          license {
            name = "The Apache License, Version 2.0"
            url = "https://www.gnu.org/licenses/gpl-3.0.txt"
          }
        }
        developers {
          developer {
            id = "michel.jung"
            name = "Michel Jung"
            email = "michel.jung89@gmail.com"
          }
        }
        scm {
          connection = "scm:git:git@github.com:micheljung/koburst.git"
          developerConnection = "scm:git:git@github.com:micheljung/koburst.git"
          url = "https://github.com/micheljung/koburst"
        }
      }
    }
  }
}
