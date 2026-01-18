package io.koburst.api

import io.koburst.api.trait.Named
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.Logger

interface Scenario : Named {
  val meterRegistry: MeterRegistry

  val users: Map<Int, User>

  /**
   * If true, the application will keep running after the scenario finishes. This allows inspecting
   * the metrics after the scenario has finished.
   */
  val keepRunning: Boolean

  val logger: Logger

  suspend fun stop()

  /** Delays the coroutine until the requested number of users are ready. */
  suspend fun waitForUsers(count: Int)
}
