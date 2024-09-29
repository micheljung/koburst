package io.koburst.core

import io.koburst.api.Interpolation

object Interpolations {
  val linear = object : Interpolation {
    override fun invoke(x: Double): Double = x
  }


}
