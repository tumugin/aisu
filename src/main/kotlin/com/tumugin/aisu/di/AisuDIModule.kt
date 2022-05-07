package com.tumugin.aisu.di

import com.tumugin.aisu.infra.app.config.AppConfigRepositoryImpl
import com.tumugin.aisu.infra.app.database.JDBCConnectionRepositoryImpl
import org.koin.core.context.startKoin
import org.koin.dsl.module

object AisuDIModule {
  private val aisuModule = module {
    // NOTE: DBへのコネクションはアプリケーション全体で常に同じものを参照したいのでsingleにする
    single { JDBCConnectionRepositoryImpl(get()) }
    // NOTE: コンフィグは起動後に変化することはないのでsingleにする
    single { AppConfigRepositoryImpl() }
  }

  fun start() {
    startKoin {
      modules(aisuModule)
    }
  }
}
