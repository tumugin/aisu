package com.tumugin.aisu.app.plugins.security

import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.app.config.AppEnvironment
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnlyDebugRoute(config: Configuration) : KoinComponent {
  val appConfigRepository by inject<AppConfigRepository>()

  internal fun interceptPipelineInRoute(route: Route, onlyDebug: Boolean) {
    route.addPhase(PhaseAfterRoutes)
    route.insertPhaseBefore(PhaseAfterRoutes, PhaseInRoute)
    route.intercept(PhaseInRoute) {
      call.attributes.put(ApplyOnlyDebugCheck, onlyDebug)
    }
  }

  class Configuration {}

  companion object Feature : BaseApplicationPlugin<Application, Configuration, OnlyDebugRoute> {
    override val key = AttributeKey<OnlyDebugRoute>("OnlyDebugCheck")
    val ApplyOnlyDebugCheck = AttributeKey<Boolean>("ApplyOnlyDebugCheck")
    private val PhaseInRoute = PipelinePhase("OnlyDebugCheckInRoute")
    private val PhaseAfterRoutes = PipelinePhase("OnlyDebugCheckAfterRoutes")

    override fun install(pipeline: Application, configure: Configuration.() -> Unit): OnlyDebugRoute {
      val onlyDebugRoute = OnlyDebugRoute(Configuration().apply(configure))
      val routing = pipeline.routing { }
      routing.insertPhaseAfter(ApplicationCallPipeline.Plugins, PhaseAfterRoutes)

      routing.intercept(PhaseAfterRoutes) {
        val isDebugProtectedRoute = call.attributes.getOrNull(ApplyOnlyDebugCheck) ?: false
        if (isDebugProtectedRoute && onlyDebugRoute.appConfigRepository.appConfig.appEnvironment == AppEnvironment.PRODUCTION) {
          call.respond(HttpStatusCode.NotFound)
          finish()
        }
      }

      return onlyDebugRoute
    }
  }
}

fun Route.onlyDebugRoute(build: Route.() -> Unit) {
  val route = createChild(DebugOnlyRouteSelector())
  application.plugin(OnlyDebugRoute).interceptPipelineInRoute(route, true)
  route.build()
}

internal class DebugOnlyRouteSelector : RouteSelector() {
  override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
    return RouteSelectorEvaluation.Constant
  }
}
