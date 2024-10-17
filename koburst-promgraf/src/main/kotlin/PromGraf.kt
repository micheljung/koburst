package io.koburst.promgraf

import io.koburst.api.MeterRegistryProvider
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.util.logging.*
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import org.testcontainers.Testcontainers
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import java.util.*
import kotlin.io.path.createTempFile
import kotlin.io.path.writeText


object PromGrafModule

val PromGraf = createApplicationPlugin(name = "PromGraf") {
  val network = Network.newNetwork()
  val port = application.attributes[AttributeKey("port")] as Int
  runBlocking {
    joinAll(
      async {
        startPrometheus(application.log, port, network)
      },
      async {
        startGrafana(application.log, network)
      },
    )
  }

  application.routing {
    get("/metrics") {
      val meterRegistry = ServiceLoader.load(MeterRegistryProvider::class.java).singleOrNull()
        ?.getMeterRegistry()
        ?: error("No MeterRegistryProvider found")

      check(meterRegistry is PrometheusMeterRegistry) { "MeterRegistry is not a PrometheusMeterRegistry." }
      call.respond(meterRegistry.scrape())
    }
  }
}

private fun startPrometheus(log: Logger, port: Int, network: Network) {
  val prometheusYml = createTempFile("prometheus", ".yml").apply {
    writeText(
      """
        |global:
        |  scrape_interval: 5s
        |scrape_configs:
        |  - job_name: 'koburst'
        |    static_configs:
        |      - targets: ['host.testcontainers.internal:$port']
        """.trimMargin(),
    )
  }

  val container = GenericContainer("prom/prometheus:latest")
    .withNetwork(network)
    .withNetworkAliases("prometheus")
    .withAccessToHost(true)
    .withExposedPorts(9090)
    .withFileSystemBind(
      prometheusYml.toString(),
      "/etc/prometheus/prometheus.yml",
      BindMode.READ_ONLY,
    ).apply {
      start()
    }
  Testcontainers.exposeHostPorts(port)
  val mappedPort = container.getMappedPort(9090)
  log.info("Prometheus started on http://${container.host}:$mappedPort")
}

private fun startGrafana(log: Logger, network: Network) {
  val grafanaIni = createTempFile("grafana", ".ini").apply {
    writeText(
      """
        |[auth.anonymous]
        |enabled = true
        |org_role = Admin
        |
        |[auth.basic]
        |enabled = false
        """.trimMargin(),
    )
  }

  val container = GenericContainer("grafana/grafana:latest")
    .withNetwork(network)
    .withExposedPorts(3000)
    .withFileSystemBind(
      grafanaIni.toString(),
      "/etc/grafana/grafana.ini",
      BindMode.READ_ONLY,
    )
    .withClasspathResourceMapping(
      "/grafana/datasources",
      "/etc/grafana/provisioning/datasources",
      BindMode.READ_ONLY,
    )
    .withClasspathResourceMapping(
      "/grafana/dashboards",
      "/etc/grafana/provisioning/dashboards",
      BindMode.READ_ONLY,
    )
    .apply {
      start()
    }
  log.info("Grafana started on http://${container.host}:${container.getMappedPort(3000)}")
}

class PrometheusMeterRegistryProvider : MeterRegistryProvider {

  override fun getMeterRegistry() = prometheusMeterRegistry

  companion object {
    private val prometheusMeterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
  }
}
