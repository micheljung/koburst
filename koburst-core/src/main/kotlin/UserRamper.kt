package io.koburst.core

import io.koburst.api.Interpolation
import io.koburst.api.UserFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.util.function.Supplier
import kotlin.properties.Delegates
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@ScenarioMarker
class UserRamper(
  private val count: Int,
) {
  var during: Duration by Delegates.notNull()
  var function: Interpolation = Interpolations.linear
  lateinit var userSupplier: Supplier<BaseUser>
  lateinit var userFactory: UserFactory

  // TODO implement this properly
  fun execute() = flow {
    check(count > 0) { "Count must be greater than 0" }
    check(::userFactory.isInitialized xor ::userSupplier.isInitialized) {
      "Make sure to initialize either a userFactory or userSupplier in your scenario"
    }

    if (!::userFactory.isInitialized) {
      userFactory = SupplierBaseUserFactory(userSupplier)
    }

    var rampedUp = 0
    val interval = during.inWholeMilliseconds.toDouble() / count
    var time = 0

    while (rampedUp < count) {
      val x = time / during.inWholeMilliseconds.toDouble()
      val millisTaken = measureTimeMillis {
        val expectedUsers =
          if (time >= during.inWholeMilliseconds) count.toDouble()
          else count * function(x)
        val missingUsers = (expectedUsers - rampedUp).toInt()
        repeat(missingUsers) {
          val user = createUser(rampedUp)
          emit(user)
          rampedUp++
        }
      }
      delay((interval.toLong() - millisTaken).coerceAtLeast(0))
      time += interval.toInt() + millisTaken.toInt()
    }
  }

  private fun createUser(id: Int) = userFactory.create(id)
}
