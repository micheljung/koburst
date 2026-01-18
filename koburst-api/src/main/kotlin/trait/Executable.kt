package io.github.micheljung.koburst.api.trait

import io.github.micheljung.koburst.api.Scenario

interface Executable {
  suspend fun execute(scenario: Scenario)
}
