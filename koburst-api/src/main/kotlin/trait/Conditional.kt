package io.koburst.api.trait

import io.koburst.api.Session

interface Conditional {
  fun met(session: Session): Boolean
}
