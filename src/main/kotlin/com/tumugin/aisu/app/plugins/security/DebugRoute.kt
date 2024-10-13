package com.tumugin.aisu.app.plugins.security

import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.app.config.AppEnvironment
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnlyDebugRoute(configuration: Configuration) : KoinComponent {
  val appConfigRepository by inject<AppConfigRepository>()

  class Configuration {}

  companion object Plugin : BaseRouteScopedPlugin<Configuration, OnlyDebugRoute> {
    override val key = AttributeKey<OnlyDebugRoute>("OnlyDebugRoute")

    override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): OnlyDebugRoute {
      val configuration = Configuration().apply(configure)
      val plugin = OnlyDebugRoute(configuration)
      pipeline.intercept(ApplicationCallPipeline.Plugins) {
        if (plugin.appConfigRepository.appConfig.appEnvironment == AppEnvironment.PRODUCTION) {
          call.respond(HttpStatusCode.NotFound)
          finish()
        }
      }
      return plugin
    }
  }
}
