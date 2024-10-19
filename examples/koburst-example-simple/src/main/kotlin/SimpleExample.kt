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
      rampUsers(10) {
        userSupplier = Supplier { PrintHelloUser() }
      }
    }
  }
}

class PrintHelloUser : BaseUser() {

  override suspend fun execute() {
    pause(1.seconds, 10.seconds)
    logger.info("[${id}] Hello, Koburst!")
  }
}
