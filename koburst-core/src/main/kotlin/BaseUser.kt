package io.koburst.core

import io.koburst.api.Scenario
import io.koburst.api.User
import io.micrometer.core.instrument.AbstractTimer
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Tags
import io.micrometer.core.instrument.Timer
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.time.withTimeout
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.toJavaDuration

/**
 * A base implementation of [User] that allows `id` to be set after instantiation and provides
 * means to record statistics. Works well with [BaseUserFactory].
 */
abstract class BaseUser : User {
  override var id by Delegates.notNull<Int>()
  override lateinit var scenario: Scenario
  private val timers = mutableMapOf<String, Timer>()

  /** Pause for exactly the specified duration. */
  suspend fun pause(duration: Duration) = delay(duration)

  /** Pause for anywhere between `min` and `max`. */
  suspend fun pause(min: Duration, max: Duration) =
    delay((min.inWholeMilliseconds..max.inWholeMilliseconds).random())

  /**
   * Repeats the block of code for the specified duration. If the time is up, the last block
   * execution will be allowed to finish before stopping. Therefore, this method may take longer
   * than the specified duration.
   *
   * If you need to stop immediately after the duration is up, use [repeat].
   */
  suspend fun repeatFor(duration: Duration, block: suspend () -> Unit) {
    val start = System.currentTimeMillis()
    while (System.currentTimeMillis() - start < duration.inWholeMilliseconds) {
      block()
    }
  }

  /**
   * Repeats the block of code for the specified duration. If the time is up, the block
   * execution will immediately be terminated. Make sure to understand the implications of
   * this behavior before using this method (see [kotlinx.coroutines.withTimeout]).
   *
   * This function catches the `TimeoutCancellationException` thrown by `withTimeout`.
   *
   * If you need to allow the last block execution to finish, use [repeatFor].
   */
  suspend fun repeatForStrict(duration: Duration, block: suspend () -> Unit) {
    try {
      withTimeout(duration.toJavaDuration()) {
        while (true) {
          block()
        }
      }
    } catch (e: TimeoutCancellationException) {
      // Do nothing
    }
  }

  final override suspend fun execute(scenario: Scenario) {
    this.scenario = scenario
    execute()
  }

  // Workaround for https://github.com/micrometer-metrics/micrometer/issues/4455
  protected suspend fun <A> record(
    name: String,
    sla: Duration? = null,
    block: suspend () -> A,
  ): A = try {
    timer("${name}.time", sla).recordSuspend(block).also {
      executionCounter(name, true).increment()
    }
  } catch (e: Exception) {
    executionCounter(name, false).increment()
    throw e
  }

  private fun executionCounter(name: String, ok: Boolean) = scenario.meterRegistry
    .counter(
      "${name}.execution",
      Tags.of(Tag.of("status", if (ok) "ok" else "ko")),
    )

  protected open fun timer(key: String, sla: Duration?) = timers.computeIfAbsent(key) {
    Timer.builder(key).apply {
      sla?.let { sla(it.toJavaDuration()) }
      publishPercentiles(.5, .75, .95, .99)
      publishPercentileHistogram(true)
    }.register(scenario.meterRegistry)
  }

  private suspend fun <A> Timer.recordSuspend(block: suspend () -> A): A =
    when (val timer = this) {
      is AbstractTimer -> timer.recordSuspendInternal(block)
      else -> block()
    }

  private suspend fun <A> AbstractTimer.recordSuspendInternal(block: suspend () -> A): A {
    val clock = scenario.meterRegistry.config().clock()
    val s = clock.monotonicTime()
    return try {
      block()
    } finally {
      val e = clock.monotonicTime()
      record(e - s, TimeUnit.NANOSECONDS)
    }
  }

  abstract suspend fun execute()
}
