package com.tumugin.aisu.app.graphql.params

import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.request.ValidatorPatterns.NumberPattern
import io.konform.validation.Validation
import io.konform.validation.ValidationResult
import io.konform.validation.jsonschema.pattern
import io.ktor.server.plugins.*

val aisuIdsValidator = Validation {
  ID::value required {
    pattern(NumberPattern) hint "ID must be number like value"
  }
}

fun <T> assertValidationResult(validateResult: ValidationResult<T>) {
  if (validateResult.errors.size > 0) {
    throw BadRequestException(validateResult.errors.joinToString("\n"))
  }
}
