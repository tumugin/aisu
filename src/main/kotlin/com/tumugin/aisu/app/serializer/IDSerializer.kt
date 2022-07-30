package com.tumugin.aisu.app.serializer

import com.expediagroup.graphql.generator.scalars.ID
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object IDSerializer : KSerializer<ID> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ID", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): ID {
    return ID(decoder.decodeString())
  }

  override fun serialize(encoder: Encoder, value: ID) {
    encoder.encodeString(value.value)
  }
}
