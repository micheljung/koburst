package io.github.micheljung.koburst.core

import io.github.micheljung.koburst.api.Interpolation
import kotlin.math.*

object Interpolations {
  val linear = Interpolation { x -> x }
  val exponential = Interpolation { x -> x * x }
  val instant = Interpolation { 1.0 }
  val easInSine = Interpolation { x -> 1 - cos(x * PI / 2) }
  val easeOutSine = Interpolation { x -> sin(x * PI / 2) }
  val easeInQuad = Interpolation { x -> x * x }
  val easeOutQuad = Interpolation { x -> 1 - (1 - x) * (1 - x) }
  val easeInOutQuad =
    Interpolation { x -> if (x < 0.5) 2 * x * x else 1 - (-2 * x + 2).pow(2.0) / 2 }
  val easeInCubic = Interpolation { x -> x * x * x }
  val easeOutCubic = Interpolation { x -> 1 - (1 - x) * (1 - x) * (1 - x) }
  val easeInOutCubic =
    Interpolation { x -> if (x < 0.5) 4 * x * x * x else 1 - (-2 * x + 2).pow(3.0) / 2 }
  val easeInQuart = Interpolation { x -> x * x * x * x }
  val easeOutQuart = Interpolation { x -> 1 - (1 - x) * (1 - x) * (1 - x) * (1 - x) }
  val easeInOutQuart =
    Interpolation { x -> if (x < 0.5) 8 * x * x * x * x else 1 - (-2 * x + 2).pow(4.0) / 2 }
  val easeInQuint = Interpolation { x -> x * x * x * x * x }
  val easeOutQuint = Interpolation { x -> 1 - (1 - x) * (1 - x) * (1 - x) * (1 - x) * (1 - x) }
  val easeInOutQuint =
    Interpolation { x -> if (x < 0.5) 16 * x * x * x * x * x else 1 - (-2 * x + 2).pow(5.0) / 2 }
  val easeInExpo = Interpolation { x -> if (x == 0.0) 0.0 else 2.0.pow(10 * x - 10) }
  val easeOutExpo = Interpolation { x -> if (x == 1.0) 1.0 else 1 - 2.0.pow(-10 * x) }
  val easeInOutExpo = Interpolation { x ->
    if (x == 0.0 || x == 1.0) x else if (x < 0.5) 2.0.pow(20 * x - 10) / 2 else (2 - 2.0.pow(-20 * x + 10)) / 2
  }
  val easeInCirc = Interpolation { x -> 1 - sqrt(1 - x * x) }
  val easeOutCirc = Interpolation { x -> sqrt(1 - (x - 1) * (x - 1)) }
  val easeInOutCirc =
    Interpolation { x -> if (x < 0.5) (1 - sqrt(1 - 4 * x * x)) / 2 else (sqrt(1 - (-2 * x + 2) * (-2 * x + 2)) + 1) / 2 }
}

