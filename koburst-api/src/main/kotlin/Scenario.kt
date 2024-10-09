package io.koburst.api

import io.koburst.api.trait.Named
import io.micrometer.core.instrument.MeterRegistry

interface Scenario : Named {
  val meterRegistry: MeterRegistry

  val users: Map<Int, User>

  suspend fun stop()

  /** Delays the coroutine until the requested number of users are ready. */
  suspend fun waitForUsers(count: Int)
}
