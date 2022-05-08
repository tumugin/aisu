package com.tumugin.aisu.usecase.app

class ApplicationBootstrap {
  fun bootstrap() {
    Database().prepareDatabase()
  }
}
