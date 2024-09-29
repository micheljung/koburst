package io.koburst.api

interface Session {

  /**
   * Returns the value associated with the given key, or `null` if no value is associated with the
   * given key.
   */
  fun <T> tryGet(key: String): T?

  /**
   * Retrieves the non-value associated with the given key. Throws an exception if the value is
   * `null` or if no value is associated with the given key.
   */
  fun <T> get(key: String): T
}
