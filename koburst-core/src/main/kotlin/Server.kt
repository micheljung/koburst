package io.koburst.core

import io.koburst.core.metrics.LocalMeterRegistry
import io.koburst.core.metrics.Metric
import io.koburst.core.metrics.Metrics
import io.koburst.promgraf.PromGraf
import io.koburst.promgraf.PromGrafModule
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoNumber
import kotlinx.serialization.protobuf.ProtoOneOf
import java.net.ServerSocket

object Server {

  @OptIn(ExperimentalSerializationApi::class)
  fun start(
    meterRegistry: MeterRegistry,
    keepRunning: Boolean,
    applicationCallback: (Application) -> Unit,
  ) {
    val port = getFreePort()
    embeddedServer(CIO, port = port) {
      attributes.put(AttributeKey("port"), port)
      install(MicrometerMetrics) {
        registry = meterRegistry
        meterBinders = emptyList()
      }

      if (isPromGrafAvailable) {
        install(PromGraf)
      }

      if (meterRegistry is LocalMeterRegistry) {
        install(WebSockets) {
          contentConverter = KotlinxWebsocketSerializationConverter(ProtoBuf)
        }

        routing {
          staticResources("/", "static")
        }

        routing {
          webSocket("/ws") {
            try {
              val replayCache = meterRegistry.metricsFlow.replayCache
              sendSerialized(MetricsMessageOneOf(Metrics(replayCache)))
              meterRegistry.metricsFlow
                .drop(replayCache.size)
                .map { Message(MetricMessageOneOf(it)) }
                .collect(::sendSerialized)
            } finally {
              println("WebSocket connection closed")
            }
          }
        }
      }

      applicationCallback(this)
    }.start(wait = keepRunning)
  }

  private val isPromGrafAvailable = classExists(PromGrafModule::class.java.name)

  private fun classExists(className: String) = kotlin.runCatching {
    Class.forName(className)
  }.isSuccess

  private fun getFreePort() = ServerSocket(0).use { it.localPort }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Message(
  @ProtoOneOf
  val payload: MessageOneOf,
)

@Serializable
sealed interface MessageOneOf

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class MetricMessageOneOf(
  @ProtoNumber(1)
  val metric: Metric,
) : MessageOneOf

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class MetricsMessageOneOf(
  @ProtoNumber(2)
  val metrics: Metrics,
) : MessageOneOf
