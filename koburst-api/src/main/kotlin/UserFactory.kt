package io.koburst.api

interface UserFactory {
  fun create(id: Int): User
}
