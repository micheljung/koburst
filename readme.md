# KoBurst: High Performance Load Testing written in Kotlin

Super simple, super small, super generic, super fast, super extensible load testing tool written in
Kotlin for Kotlin.

## Why KoBurst?

With all the load testing tools out there, why would you want to use KoBurst?

* It's not written in Scala, Java, Python, or JavaScript. It's written in Kotlin - the language you
  know and love, and are able to debug.
* It has no fancy abstractions that aim to make your life easier while actually making it harder -
  no need to consult the documentation just to understand how to write an `if` statement, a `for`
  loop, and no roadblocks that prevent you from debugging.
* It's not modular - no need for plugins that are often paid, unmaintained, don't work or don't
  exist.
* No blocking code - all code is based on Kotlin coroutines.

## Super Simple

KoBurst consists of only 9 API classes that are super easy to understand.

### User

A `User` is a single user that will be making requests to your system. Simply implement the method
`execute()` to define the behavior of the user. Just make sure that the code is non-blocking.

### Scenario

A `Scenario` defines how many of which `User`s will be ramped up over what time frame and for how
long the test will run.

## More Features

### UserFactory

Probably not needed, but if you want to create users dynamically, you can your own `UserFactory`.

### Interpolation

Probably not needed, but if you need to implement your own interpolation logic for ramping up users,
this is your Interface to implement.

KoBurst is a high performance load testing tool written in Kotlin. It is designed to be simple to
use, yet powerful enough to handle the most complex of load testing scenarios. KoBurst is built on
top of the popular [Ktor](https://ktor.io) framework and is designed to be highly extensible.
