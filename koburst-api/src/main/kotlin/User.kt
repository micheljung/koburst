package io.koburst.api

import io.koburst.api.trait.Executable

interface User : Executable {
  val id: Int
  val scenario: Scenario
}
