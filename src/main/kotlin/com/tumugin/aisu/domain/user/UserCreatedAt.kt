@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.tumugin.aisu.domain.user

import kotlin.time.ExperimentalTime

@JvmInline
value class UserCreatedAt(val value: kotlin.time.Instant)
