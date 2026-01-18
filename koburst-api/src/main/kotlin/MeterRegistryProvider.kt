package io.github.micheljung.koburst.api

import io.micrometer.core.instrument.MeterRegistry

interface MeterRegistryProvider {
  fun getMeterRegistry(): MeterRegistry
}
