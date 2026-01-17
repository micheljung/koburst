plugins {
  alias(libs.plugins.gitVersioning)
  alias(libs.plugins.nexusPublish)
}

group = "io.koburst"
version = "0.0.0-SNAPSHOT"
gitVersioning.apply {
  refs {
    branch(".+") {
      version = "\${ref.slug}-SNAPSHOT"
    }
    tag("(?<version>.*)") {
      version = "\${ref.version}"
    }
  }
  rev {
    version = "\${commit}"
  }
}

nexusPublishing {
  repositories {
    // see https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/#configuration
    sonatype {
      nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
      snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
      username = System.getenv("SONATYPE_USERNAME")
      password = System.getenv("SONATYPE_PASSWORD")
    }
  }
}
