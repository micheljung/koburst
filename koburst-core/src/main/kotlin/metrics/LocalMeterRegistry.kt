package io.koburst.core.metrics

import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.Statistic
import io.micrometer.core.instrument.step.StepMeterRegistry
import io.micrometer.core.instrument.step.StepRegistryConfig
import io.micrometer.core.instrument.util.NamedThreadFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.util.concurrent.TimeUnit

class LocalMeterRegistry(config: LocalMetricRegistryConfig, clock: Clock) :
  StepMeterRegistry(config, clock) {

  // TODO only expose a non-mutable flow
  val metricsFlow: MutableSharedFlow<Metric> = MutableSharedFlow(replay = Integer.MAX_VALUE)

  init {
    start(NamedThreadFactory("local-metrics-publisher"))
  }

  override fun publish() {
    runBlocking {
      collectMetrics()
    }
  }

  private suspend fun collectMetrics() {
    withContext(Dispatchers.IO) {
      forEachMeter { meter ->
        if (meter.id.name.startsWith("ktor.")) return@forEachMeter

        val measurements = meter.measure()
        val labels = meter.id.tags.associate { it.key to it.value }
        val timestamp = Instant.now()

        measurements.forEach { measurement ->
          launch {
            metricsFlow.emit(
              Metric(
                name = meter.id.name,
                statistic = measurement.statistic,
                value = measurement.value,
                labels = labels,
                timestamp = timestamp,
              ),
            )
          }
        }
      }
    }
  }

  override fun getBaseTimeUnit(): TimeUnit = TimeUnit.MILLISECONDS
}

@Serializable
data class Metric(
  val name: String,
  val value: Double,
  val labels: Map<String, String>,
  @Serializable(with = InstantSerializer::class)
  val timestamp: Instant,
  val statistic: Statistic,
)

@Serializable
data class Metrics(
  val metrics: List<Metric>,
)

object InstantSerializer : KSerializer<Instant> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("java.time.Instant", PrimitiveKind.LONG)

  override fun serialize(encoder: Encoder, value: Instant) =
    encoder.encodeLong(value.toEpochMilli())

  override fun deserialize(decoder: Decoder): Instant = Instant.ofEpochMilli(decoder.decodeLong())
}

object LocalMetricRegistryConfig : StepRegistryConfig {
  private val map: Map<String, String?> = mutableMapOf(
    "${prefix()}.step" to "PT1s",
  )

  override fun prefix() = "local"

  override fun get(key: String): String? = map[key]

}
