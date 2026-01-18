package io.koburst.example.http

import io.koburst.core.BaseUser
import io.koburst.core.ScenarioDsl.rampUsers
import io.koburst.core.ScenarioDsl.scenario
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.function.Supplier
import kotlin.time.Duration.Companion.seconds

object HttpExample {
  @JvmStatic
  fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8000) {
      routing {
        get("/") {
          call.respondText("Hello ${call.request.queryParameters["name"] ?: "Anonymous"}")
        }
      }
    }.start(wait = false)

    scenario("HTTP example") {
      rampUsers(50) {
        during = 30.seconds
        userSupplier = Supplier { HttpExampleUser() }
      }
    }
  }
}

class HttpExampleUser : BaseUser() {
  private val name by lazy { "User $id" }

  override suspend fun execute() {
    val client = HttpClient(CIO)
    val response = client.get("http://localhost:8000/") {
      url {
        parameters.append("name", name)
      }
    }
    println("[$id] ${response.bodyAsText()}")
  }
}
