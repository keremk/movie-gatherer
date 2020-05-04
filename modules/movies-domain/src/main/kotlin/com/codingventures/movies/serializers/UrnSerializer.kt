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

    override fun deserialize(decoder: Decoder): Urn {
        val urnString = decoder.decodeString()
        val parts = urnString.split(":")
        if (parts.count() < 3) {
            throw MalformedUrnException("Text '$urnString' is an unknown urn format")
        }

        if (parts[0].isEmpty() || parts[1].isEmpty() || parts[2].isEmpty()) {
            throw MalformedUrnException("Text '$urnString' is missing some fields")
        }

        return Urn(
            org = parts[0],
            type = parts[1],
            id = parts[2]
        )
    }
}

