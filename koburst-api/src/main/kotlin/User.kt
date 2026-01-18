package io.github.micheljung.koburst.api

import io.github.micheljung.koburst.api.trait.Executable

interface User : Executable {
  val id: Int
  val scenario: Scenario
}
