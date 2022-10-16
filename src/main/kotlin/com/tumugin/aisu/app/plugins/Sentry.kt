package com.tumugin.aisu.app.plugins

import com.tumugin.aisu.domain.app.config.AppConfigRepository
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import io.sentry.Sentry
import io.sentry.SpanStatus
import io.sentry.kotlin.SentryContext
import kotlinx.coroutines.withContext
import org.koin.core.Koin

fun Application.configureSentry(koin: Koin) {
  val appConfigRepository = koin.get<AppConfigRepository>()

  if (appConfigRepository.appConfig.appConfigSentryDsn.value.isBlank()) {
    return
  }

  Sentry.init { options ->
    options.dsn = appConfigRepository.appConfig.appConfigSentryDsn.value
    options.environment = appConfigRepository.appConfig.appEnvironment.name.lowercase()
  }

  intercept(ApplicationCallPipeline.Call) {
    withContext(SentryContext()) {
      val transaction = Sentry.startTransaction("Ktor Request", call.request.path())
      Sentry.setTag("path", call.request.path())
      Sentry.setTag("method", call.request.httpMethod.value)
      Sentry.setTag("host", call.request.host())
      Sentry.setTag("protocol", call.request.origin.scheme)
      Sentry.setTag("user_id", call.sessions.get<UserAuthSession>()?.userId.toString())
      Sentry.setTag("admin_user_id", call.sessions.get<AdminUserAuthSession>()?.adminUserId.toString())
      Sentry.setExtra("query", call.request.queryString())
      try {
        proceed()
        transaction.status = SpanStatus.OK
      } catch (e: NotFoundException) {
        transaction.apply {
          this.throwable = e
          this.status = SpanStatus.NOT_FOUND
        }
        throw e
      } catch (e: BadRequestException) {
        transaction.apply {
          this.throwable = e
          this.status = SpanStatus.INVALID_ARGUMENT
        }
        throw e
      } catch (e: Exception) {
        transaction.apply {
          this.throwable = e
          this.status = SpanStatus.INTERNAL_ERROR
        }
        Sentry.captureException(e)
        throw e
      } finally {
        transaction.finish()
      }
    }
  }
}
