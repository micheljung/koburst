package io.koburst.example.http

import io.koburst.api.Scenario
import io.koburst.api.User
import io.koburst.api.UserFactory
import io.koburst.core.ScenarioDsl.rampUsers
import io.koburst.core.ScenarioDsl.scenario
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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

    scenario("Simple example") {
      rampUsers(50) {
        during = 10.seconds
        userFactory = object : UserFactory {
          override fun create(id: Int) = HttpExampleUser(id)
        }
      }
    }
  }
}

data class HttpExampleUser(
  override val id: Int,
) : User {
  private val name = "User $id"

  override val scenario: Scenario
    get() = TODO("Not yet implemented")

  override suspend fun execute(scenario: Scenario) {
    val client = HttpClient(CIO)
    val response = client.get("http://localhost:8000/") {
      url {
        parameters.append("name", name)
      }
    }
    println("[$id] ${response.bodyAsText()}")
  }
}
