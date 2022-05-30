package com.tumugin.aisu.di

import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.app.database.JDBCConnectionRepository
import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import com.tumugin.aisu.domain.app.session.SessionKVSRepository
import com.tumugin.aisu.domain.regulation.RegulationRepository
import com.tumugin.aisu.domain.user.UserRepository
import com.tumugin.aisu.infra.app.config.AppConfigRepositoryImpl
import com.tumugin.aisu.infra.app.database.JDBCConnectionRepositoryImpl
import com.tumugin.aisu.infra.app.database.RedisPoolRepositoryImpl
import com.tumugin.aisu.infra.app.session.SessionKVSRepositoryImpl
import com.tumugin.aisu.infra.repository.exposed.repository.RegulationRepositoryImpl
import com.tumugin.aisu.infra.repository.exposed.repository.UserRepositoryImpl
import org.koin.core.context.startKoin
import org.koin.dsl.module
import redis.clients.jedis.JedisPool

object AisuDIModule {
  private val aisuDatabaseModule = module {
    // NOTE: DBへのコネクションはアプリケーション全体で常に同じものを参照したいのでsingleにする
    single<JDBCConnectionRepository> { JDBCConnectionRepositoryImpl(get()) }
    single<RedisPoolRepository<JedisPool>> { RedisPoolRepositoryImpl(get()) }
    single<SessionKVSRepository> { SessionKVSRepositoryImpl(get()) }
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
