package com.tumugin.aisu.usecase.app

import org.koin.core.component.KoinComponent
import org.slf4j.LoggerFactory

class ApplicationBootstrap : KoinComponent {
  private val logger = LoggerFactory.getLogger(ApplicationBootstrap::class.java)

  suspend fun bootstrap() {
    logger.info("Application bootstrap started")

    logger.info("Database preparation started")
    Database().prepareDatabase()
    logger.info("Database prepared")
  }
}
