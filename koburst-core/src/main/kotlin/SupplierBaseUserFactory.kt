package io.github.micheljung.koburst.core

import java.util.function.Supplier

class SupplierBaseUserFactory(
  private val supplier: Supplier<BaseUser>,
) : BaseUserFactory() {
  override fun createUser() = supplier.get()
}
