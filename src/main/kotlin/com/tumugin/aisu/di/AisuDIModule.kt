package com.tumugin.aisu.di

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.tumugin.aisu.app.client.AisuHTTPClient
import com.tumugin.aisu.app.graphql.KtorGraphQLServer
import com.tumugin.aisu.domain.adminAuth0.AdminAuth0Repository
import com.tumugin.aisu.domain.adminUser.AdminUserRepository
import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.app.csrf.CSRFRepository
import com.tumugin.aisu.domain.app.database.JDBCConnectionRepository
import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import com.tumugin.aisu.domain.app.session.SessionKVSRepository
import com.tumugin.aisu.domain.auth0.Auth0Repository
import com.tumugin.aisu.domain.auth0.Auth0UserInfoRepository
import com.tumugin.aisu.domain.auth0.Auth0UserRepository
import com.tumugin.aisu.domain.cheki.ChekiRepository
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupRepository
import com.tumugin.aisu.domain.group.GroupRepository
import com.tumugin.aisu.domain.idol.IdolRepository
import com.tumugin.aisu.domain.regulation.RegulationRepository
import com.tumugin.aisu.domain.user.UserRepository
import com.tumugin.aisu.infra.app.config.AppConfigRepositoryImpl
import com.tumugin.aisu.infra.app.csrf.CSRFRepositoryImpl
import com.tumugin.aisu.infra.app.database.JDBCConnectionRepositoryImpl
import com.tumugin.aisu.infra.app.database.RedisPoolRepositoryImpl
import com.tumugin.aisu.infra.app.session.SessionKVSRepositoryImpl
import com.tumugin.aisu.infra.auth0.AdminAuth0RepositoryImpl
import com.tumugin.aisu.infra.auth0.Auth0RepositoryImpl
import com.tumugin.aisu.infra.auth0.Auth0UserInfoRepositoryImpl
import com.tumugin.aisu.infra.repository.exposed.repository.*
import org.koin.core.context.startKoin
import org.koin.dsl.module
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.util.Pool

object AisuDIModule {
  private val aisuDatabaseModule = module {
    // NOTE: DBへのコネクションはアプリケーション全体で常に同じものを参照したいのでsingleにする
    single<JDBCConnectionRepository> { JDBCConnectionRepositoryImpl(get()) }
    single<RedisPoolRepository<Pool<Jedis>>> { RedisPoolRepositoryImpl(get()) }
    single<SessionKVSRepository> { SessionKVSRepositoryImpl(get()) }
    single<CSRFRepository> { CSRFRepositoryImpl(get()) }
  }

  private val aisuConfigModule = module {
    // NOTE: コンフィグは起動後に変化することはないのでsingleにする
    single<AppConfigRepository> { AppConfigRepositoryImpl() }
  }

  private val aisuTestingConfigModule = module {
    single<AppConfigRepository> { AppConfigRepositoryImpl(isTesting = true) }
  }

  private val aisuModule = module {
    factory<UserRepository> { UserRepositoryImpl() }
    factory<RegulationRepository> { RegulationRepositoryImpl() }
    factory<IdolRepository> { IdolRepositoryImpl() }
    factory<GroupRepository> { GroupRepositoryImpl() }
    factory<FavoriteGroupRepository> { FavoriteGroupRepositoryImpl() }
    factory<ChekiRepository> { ChekiRepositoryImpl() }
    factory<AdminUserRepository> { AdminUserRepositoryImpl() }
    factory<Auth0UserInfoRepository> { Auth0UserInfoRepositoryImpl() }
    factory<Auth0UserRepository> { Auth0UserRepositoryImpl() }
    factory<Auth0Repository> { Auth0RepositoryImpl() }
    factory<AdminAuth0Repository> { AdminAuth0RepositoryImpl() }
    factory { AisuHTTPClient() }
    single { jacksonObjectMapper() }
    single(createdAtStart = true) { KtorGraphQLServer.getGraphQLServer(get()) }
  }

  fun start() {
    startKoin {
      modules(aisuConfigModule, aisuDatabaseModule, aisuModule)
    }
  }

  fun startTesting() {
    startKoin {
      modules(aisuTestingConfigModule, aisuDatabaseModule, aisuModule)
    }
  }
}
