package io.koburst.prometheus

import io.ktor.server.application.*
import org.testcontainers.containers.GenericContainer

object PrometheusModule

val Prometheus = createApplicationPlugin(name = "Prometheus") {
  GenericContainer("prom/prometheus:latest")
    .withExposedPorts(9090)
    .start()
}
