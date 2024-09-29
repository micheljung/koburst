package io.koburst.core

import io.koburst.api.Interpolation
import kotlin.time.Duration

class UserRamper {
  var during: Duration = Duration.ZERO
  var function: Interpolation = Interpolations.linear

  private suspend fun linearRampUp() {
    val interval = during.inWholeMilliseconds / count
    repeat(count.toInt()) {
      launch { function() }
      delay(interval)
    }
  }

  private suspend fun easeInRampUp() {
    val interval = during.inWholeMilliseconds / (count * count)
    for (i in 1..count) {
      launch { function() }
      delay(i * interval)
    }
  }
}
