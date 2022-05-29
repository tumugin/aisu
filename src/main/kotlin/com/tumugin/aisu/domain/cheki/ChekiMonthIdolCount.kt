package com.tumugin.aisu.domain.cheki

import com.tumugin.aisu.domain.idol.Idol

data class ChekiMonthIdolCount(val idol: Idol, val chekiCount: ChekiCount, val chekiShotAtMonth: ChekiShotAtMonth) {}
