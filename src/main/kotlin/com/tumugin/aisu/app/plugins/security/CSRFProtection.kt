package com.tumugin.aisu.app.plugins.security

import com.tumugin.aisu.domain.app.csrf.CSRFRepository
import com.tumugin.aisu.domain.app.csrf.CSRFToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineInterceptor
import org.koin.core.context.GlobalContext

class CsrfProtection(config: Configuration) {
  private val handleValidationFailure = config.failureResponse

  class Configuration {
    internal var failureResponse: PipelineInterceptor<Unit, PipelineCall> = {
      call.response.headers.append("X-CSRF-Rejected", "1")
      call.respond(HttpStatusCode.BadRequest)
    }

    fun handleValidationFailure(handler: PipelineInterceptor<Unit, PipelineCall>) {
      failureResponse = handler
    }
  }

  companion object Feature : BaseRouteScopedPlugin<Configuration, CsrfProtection> {
    override val key = AttributeKey<CsrfProtection>("CsrfProtection")

    override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): CsrfProtection {
      val csrfRepository = GlobalContext.get().get<CSRFRepository>()

      val csrf = CsrfProtection(Configuration().apply(configure))

      pipeline.intercept(ApplicationCallPipeline.Plugins) {
        val isEnabledHttpMethods =
          listOf(HttpMethod.Post, HttpMethod.Patch, HttpMethod.Put, HttpMethod.Delete).contains(
            call.request.httpMethod
          )

        // token values
        val rawFormCsrfToken = try {
          call.receiveParameters()["_csrf"]
        } catch (ex: Exception) {
          null
        }
        val rawHeaderCsrfToken = call.request.headers["X-CSRF-Token"]
        val rawCsrfToken = rawFormCsrfToken ?: rawHeaderCsrfToken

        if (isEnabledHttpMethods) {
          // if token does not exist
          if (rawCsrfToken == null) {
            csrf.handleValidationFailure(this, Unit)
            finish()
            return@intercept
          }

          val csrfToken = CSRFToken(rawCsrfToken)
          // if token is invalid
          if (!csrfRepository.validateTokenExists(csrfToken)) {
            csrf.handleValidationFailure(this, Unit)
            finish()
          }
        }
      }

      return csrf
    }
  }
}
