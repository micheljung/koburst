package io.koburst.api.trait

import io.koburst.api.Scenario

interface Executable {
  suspend fun execute(scenario: Scenario)
}
