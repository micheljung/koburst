package io.github.micheljung.koburst.core

import io.github.micheljung.koburst.api.UserFactory

/**
 * A factory for creating [BaseUser] instances. Using this factory, and [BaseUser], simplifies user
 * creation because its properties are set after instantiation rather than via constructor.
 */
abstract class BaseUserFactory : UserFactory {
  final override fun create(id: Int) = createUser().apply {
    this.id = id
  }

  abstract fun createUser(): BaseUser
}
