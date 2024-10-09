package io.koburst.example.grpc

import io.koburst.core.BaseUser
import io.koburst.core.ScenarioDsl.rampUsers
import io.koburst.core.ScenarioDsl.scenario
import java.util.function.Supplier
import kotlin.time.Duration.Companion.seconds

object SimpleExample {
  @JvmStatic
  fun main(args: Array<String>) {
    scenario("Simple example") {
      rampUsers(1) {
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
