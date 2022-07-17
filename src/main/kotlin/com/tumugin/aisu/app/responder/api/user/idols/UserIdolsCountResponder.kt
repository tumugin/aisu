package com.tumugin.aisu.app.responder.api.user.idols

import com.tumugin.aisu.app.responder.BaseResponder
import com.tumugin.aisu.app.responder.ResponseStatusType

/**
 * @url /user/idols/{idolId}/count
 */
@kotlinx.serialization.Serializable
class UserIdolsCountResponder : BaseResponder {
  override val status = ResponseStatusType.SUCCESS
}
