package com.codingventures.movies.serializers

import com.codingventures.movies.domain.MalformedUrnException
import com.codingventures.movies.domain.Urn
import kotlinx.serialization.*

@Serializer(forClass = UrnSerializer::class)
object UrnSerializer : KSerializer<Urn> {
    override val descriptor: SerialDescriptor = PrimitiveDescriptor("UrnSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Urn) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Urn = Urn.fromString(decoder.decodeString())
}

