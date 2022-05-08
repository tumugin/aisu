package com.tumugin.aisu.app.request

import io.konform.validation.Validation
import io.ktor.server.plugins.*

interface BaseRequest<T> {
  val validator: Validation<T>

  fun validate(request: T) {
    val validateResult = this.validator.validate(request)
    if (validateResult.errors.size > 0) {
      throw BadRequestException(validateResult.errors.joinToString("\n"))
    }
  }
}
