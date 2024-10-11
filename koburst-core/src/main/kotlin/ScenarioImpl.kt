package io.koburst.core

import io.koburst.api.MeterRegistryProvider
import io.koburst.api.Scenario
import io.koburst.api.User
import io.koburst.core.metrics.LocalMeterRegistry
import io.koburst.core.metrics.LocalMetricRegistryConfig
import io.ktor.util.logging.*
import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.set


internal val log = KtorSimpleLogger("KoBurst")

@ScenarioMarker
class ScenarioImpl(override val name: String) : Scenario {

  val userRampers: MutableList<UserRamper> = mutableListOf()

  override lateinit var meterRegistry: MeterRegistry

  override val users: MutableMap<Int, User> = mutableMapOf()

  lateinit var execution: Job

  private fun addUser(user: User) {
    users[user.id] = user
  }

  private fun removeUser(user: User) {
    users.remove(user.id)
  }

  fun run() {
    check(userRampers.isNotEmpty()) { "No users ramped up" }

    if (!::meterRegistry.isInitialized) {
      meterRegistry = ServiceLoader.load(MeterRegistryProvider::class.java).singleOrNull()
        ?.getMeterRegistry()
        ?: LocalMeterRegistry(LocalMetricRegistryConfig, Clock.SYSTEM)
    }

    Server.start(meterRegistry)

    meterRegistry.gaugeMapSize("koburst.users.count", Tags.empty(), users)

    runBlocking {
      execution = launch {
        userRampers.forEach {
          it.execute().collect { user ->
            addUser(user)
            launch {
              try {
                user.execute(this@ScenarioImpl)
              } catch (e: Exception) {
                log.warn("User ${user.id} failed", e)
              } finally {
                removeUser(user)
              }
            }
          }
        }
      }
    }
  }

  override suspend fun stop() {
    execution.cancelAndJoin()
  }

  override suspend fun waitForUsers(count: Int) {
    coroutineScope {
      // FIXME implement this properly, without delay
      while (users.size < count) {
        delay(100)
      }
    }
  }
}

object ScenarioDsl {
  fun scenario(name: String, init: ScenarioImpl.() -> Unit) = ScenarioImpl(name).apply(init).run()

  fun ScenarioImpl.rampUsers(count: Int, init: UserRamper.() -> Unit) {
    this.userRampers.add(UserRamper(count).apply(init))
  }
}

@DslMarker
annotation class ScenarioMarker
