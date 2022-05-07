package com.tumugin.aisu.usecase.app

object ApplicationBootstrap {
  fun bootstrap() {
    Database.prepareDatabase()
  }
}
