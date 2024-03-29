package com.tumugin.aisu.testing.seeder

import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.idol.IdolName
import com.tumugin.aisu.domain.idol.IdolRepository
import com.tumugin.aisu.domain.idol.IdolStatus
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IdolSeeder : KoinComponent {
  private val idolRepository by inject<IdolRepository>()

  suspend fun seedIdol(
    userId: UserId,
    idolName: IdolName = IdolName("村崎ゆうな"),
    idolStatus: IdolStatus = IdolStatus.PRIVATE_ACTIVE
  ): Idol {
    return idolRepository.getIdol(
      idolRepository.addIdol(
        userId,
        idolName,
        idolStatus
      ).idolId
    )!!
  }
}
