package com.edu6tron.spiritualcompanion.nativepreview.util

/** Pure offline helpers shared by the native practice dashboard and its tests. */
object PracticeMath {
  fun remainingToNextMala(count: Int, malaSize: Int = 108): Int {
    require(malaSize > 0) { "malaSize must be positive" }
    val normalized = count.coerceAtLeast(0) % malaSize
    return if (normalized == 0 && count > 0) malaSize else malaSize - normalized
  }
}
