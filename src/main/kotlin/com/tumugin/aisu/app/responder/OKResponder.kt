package com.tumugin.aisu.app.responder

import kotlinx.serialization.Serializable

@Serializable
data class OKResponder(override val status: String = "ok") : BaseResponder
