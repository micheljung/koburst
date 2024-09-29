package io.koburst.api.trait

import io.koburst.api.Session

interface Executable : Named {
  fun execute(session: Session)
}
