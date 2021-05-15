package com.codingventures.movies.serializers

import com.codingventures.movies.domain.MalformedUrnException
import com.codingventures.movies.domain.Urn
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = UrnSerializer::class)
object UrnSerializer : KSerializer<Urn> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UrnSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Urn) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Urn = Urn.fromString(decoder.decodeString())
}

