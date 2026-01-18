package io.github.micheljung.koburst.api

interface UserFactory {
  fun create(id: Int): User
}
