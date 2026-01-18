package io.github.micheljung.koburst.example.http

import io.github.micheljung.koburst.core.BaseUser
import io.github.micheljung.koburst.core.ScenarioDsl.rampUsers
import io.github.micheljung.koburst.core.ScenarioDsl.scenario
import java.util.function.Supplier
import kotlin.time.Duration.Companion.seconds

object PromGrafExample {
  @JvmStatic
  fun main(args: Array<String>) {
    scenario("Prometheus & Grafana example") {
      rampUsers(50) {
        during = 10.seconds
        userSupplier = Supplier { PrintHelloUser() }
      }
    }
  }
}

class PrintHelloUser : BaseUser() {

  override suspend fun execute() {
    println("[${id}] Hello, Koburst!")
    while (true) {
      record("sleep") {
        pause(1.seconds, 10.seconds)
      }
    }
  }
}
