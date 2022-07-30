package com.tumugin.aisu.domain.cheki

import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.idol.IdolId

data class ChekiIdolCount(val idol: Idol?, val idolId: IdolId, val chekiCount: ChekiCount) {}
