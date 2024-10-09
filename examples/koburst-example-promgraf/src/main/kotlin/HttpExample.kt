package io.koburst.example.http

import io.koburst.core.BaseUser
import io.koburst.core.ScenarioDsl.rampUsers
import io.koburst.core.ScenarioDsl.scenario
import java.util.function.Supplier
import kotlin.time.Duration.Companion.seconds

object HttpExample {
  @JvmStatic
  fun main(args: Array<String>) {
    scenario("Simple example") {
      rampUsers(50) {
        during = 10.seconds
        userSupplier = Supplier { PrintHelloBehavior() }
      }
    }
  }
}

class PrintHelloBehavior : BaseUser() {

  override suspend fun execute() {
    println("[${id}] Hello, Koburst!")
    while (true) {
      record("procrastinate") {
        pause(1.seconds, 10.seconds)
      }
    }
  }
}
