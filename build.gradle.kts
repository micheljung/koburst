plugins {
  alias(libs.plugins.gitVersioning)
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
