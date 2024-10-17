import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.get

plugins {
  `java-library`
  `maven-publish`
  signing
}

group = rootProject.group
version = rootProject.version

java {
  withSourcesJar()
  withJavadocJar()
}

val publicationName = "kotlin"

publishing {
  repositories {
    maven {
      name = "OSSRH"
      setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
      credentials {
        username = System.getenv("OSSRH_USERNAME")
        password = System.getenv("OSSRH_TOKEN")
      }
    }
  }

  publications {
    create<MavenPublication>(publicationName) {
      // Use this temporarily
      groupId = "io.github.micheljung"

      from(components["kotlin"])
      artifact(tasks["sourcesJar"])
      artifact(tasks["javadocJar"])

      pom {
        name.set("KoBurst")
        description.set("Load testing library for Kotlin developers")
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

signing {
  useGpgCmd()
  sign(publishing.publications[publicationName])
}
