package io.koburst.api

import io.koburst.api.trait.Named

/** An action is a small unit of work that can be executed by a [User]. */
interface Action : Named {

  /** Implementations must not be blocking. */
  suspend fun execute()
}
