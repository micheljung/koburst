# KoBurst: High Performance Load Testing written in Kotlin

Super simple, super small, super generic, super fast, super extensible load testing tool written in
Kotlin for Kotlin.

## Why KoBurst?

With all the load testing tools out there, why would you want to use KoBurst?

* It's not written in Scala, Java, Python, or JavaScript. It's written in Kotlin - the language you
  know and love, and are able to debug.
* It has no fancy abstractions that aim to make your life easier while actually making it harder -
  no need to consult the documentation just to understand how to write an `if` statement or a `for`
  loop. Neither are there roadblocks that prevent you from debugging.
* It's not modular - no need for plugins that are often paid, unmaintained, don't work or don't
  exist.
* No blocking code - all code is based on Kotlin coroutines.
* No fixed set of metrics. By leveraging Micrometer, KoBurst allows you to collect any metrics you
  want.
* No proprietary visualization. By leveraging Grafana, you're free to visualize and build dashboards
  the way need them to be.

## Why not KoBurst?

* KoBurst is brand new.
* It's not yet documented.
* There's only a single contributor.
* It's for Kotlin developers only.
