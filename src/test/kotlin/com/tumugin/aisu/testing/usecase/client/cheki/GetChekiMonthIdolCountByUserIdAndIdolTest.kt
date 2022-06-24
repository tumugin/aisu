package com.tumugin.aisu.testing.usecase.client.cheki

import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.regulation.Regulation
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.RegulationSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.cheki.GetCheki
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetChekiMonthIdolCountByUserIdAndIdolTest : BaseDatabaseTest() {
  private val getCheki = GetCheki()
  private lateinit var user: User
  private lateinit var idol: Idol
  private lateinit var idolTwo: Idol
  private lateinit var regulation: Regulation
  private lateinit var group: Group

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    idol = IdolSeeder().seedIdol(user.userId)
    idolTwo = IdolSeeder().seedIdol(user.userId)
    group = GroupSeeder().seedGroup(user.userId)
    regulation = RegulationSeeder().seedRegulation(group.groupId, user.userId)
  }

  @Test
  fun testGetChekiMonthIdolCountByUserIdAndIdol() = runTest {
    TODO()
  }
}
