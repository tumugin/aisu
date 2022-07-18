package com.tumugin.aisu.app.plugins.security

import com.tumugin.aisu.domain.app.csrf.CSRFRepository
import com.tumugin.aisu.domain.app.csrf.CSRFToken
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.BaseApplicationPlugin
import io.ktor.server.application.call
import io.ktor.server.application.plugin
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RouteSelector
import io.ktor.server.routing.RouteSelectorEvaluation
import io.ktor.server.routing.RoutingResolveContext
import io.ktor.server.routing.application
import io.ktor.server.routing.routing
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineInterceptor
import io.ktor.util.pipeline.PipelinePhase
import org.koin.core.context.GlobalContext

class CsrfProtection(config: Configuration) {
  private val applyToAllRoutesWithSideEffects = config.applyToAllRoutes
  private val handleValidationFailure = config.failureResponse

  class Configuration {
    internal var applyToAllRoutes = false
    internal var failureResponse: PipelineInterceptor<Unit, ApplicationCall> = {
      call.response.headers.append("X-CSRF-Rejected", "1")
      call.respond(HttpStatusCode.BadRequest)
    }

    fun applyToAllRoutesWithSideEffects() {
      applyToAllRoutes = true
    }

    fun handleValidationFailure(handler: PipelineInterceptor<Unit, ApplicationCall>) {
      failureResponse = handler
    }
  }

  internal fun interceptPipelineInRoute(route: Route, csrfProtected: Boolean) {
    route.addPhase(PhaseAfterRoutes)
    route.insertPhaseBefore(PhaseAfterRoutes, PhaseInRoute)
    route.intercept(PhaseInRoute) {
      call.attributes.put(ApplyCsrfProtection, csrfProtected)
    }
  }

  companion object Feature : BaseApplicationPlugin<Application, Configuration, CsrfProtection> {
    override val key = AttributeKey<CsrfProtection>("CsrfProtection")

    val ApplyCsrfProtection = AttributeKey<Boolean>("ApplyCsrfProtection")
    private val PhaseInRoute = PipelinePhase("CsrfProtectionInRoute")
    private val PhaseAfterRoutes = PipelinePhase("CsrfProtectionAfterRoutes")

    override fun install(pipeline: Application, configure: Configuration.() -> Unit): CsrfProtection {
      val csrfRepository = GlobalContext.get().get<CSRFRepository>()

      val csrf = CsrfProtection(Configuration().apply(configure))
      val routing = pipeline.routing { }
      routing.insertPhaseAfter(ApplicationCallPipeline.Plugins, PhaseAfterRoutes)

      routing.intercept(PhaseAfterRoutes) {
        val applyProtection = call.attributes.getOrNull(ApplyCsrfProtection)
        val isEnabledRoute =
          (csrf.applyToAllRoutesWithSideEffects && applyProtection != false) || applyProtection == true
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

        if (isEnabledRoute && isEnabledHttpMethods) {
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

fun Route.csrfProtection(build: Route.() -> Unit): Route {
  val protectedRoute = createChild(CsrfRouteSelector())

  application.plugin(CsrfProtection).interceptPipelineInRoute(protectedRoute, true)
  protectedRoute.build()
  return protectedRoute
}

fun Route.noCsrfProtection(build: Route.() -> Unit): Route {
  val unprotectedRoute = createChild(CsrfRouteSelector())

  application.plugin(CsrfProtection).interceptPipelineInRoute(unprotectedRoute, csrfProtected = false)
  unprotectedRoute.build()
  return unprotectedRoute
}

internal class CsrfRouteSelector : RouteSelector() {
  override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
    return RouteSelectorEvaluation.Constant
  }
}
