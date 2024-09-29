package io.koburst.core

import io.koburst.api.trait.Executable
import io.koburst.api.trait.Named
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Scenario(override val name: String) : Named {
  private val executables = mutableListOf<Executable>()

  fun execute() = runBlocking {
    executables.forEach { action ->
      launch { action.execute() }
    }
  }
}

object ScenarioDsl {
  fun scenario(name: String, init: Scenario.() -> Unit) = Scenario(name).apply(init)

  fun rampUsers(count: Long, init: UserRamper.() -> Unit) = UserRamper().apply(init)
}

