package com.codingventures.movies.kafka

import com.sksamuel.hoplite.ConfigLoader
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer

data class ConsumerSettings(
    val maxPollRecords: Int,
    val enableAutoCommit: Boolean,
    val consumerGroupId: String)

data class ServerConfig(
    val bootstrapServers: String,
    val schemaRegistryUrl: String)

data class KafkaTopics(
    val movies: String,
    val people: String,
    val tasks: String,
    val deadLetters: String
)

class KafkaConfigProvider(
    val consumerSettings: ConsumerSettings,
    val serverConfig: ServerConfig,
    val kafkaTopics: KafkaTopics) {

    fun consumerProperties(): Map<String, Any?> {
        return mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to serverConfig.bootstrapServers,
            SCHEMA_REGISTRY_URL to serverConfig.schemaRegistryUrl,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to KafkaAvroDeserializer::class.java,
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG to consumerSettings.maxPollRecords,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to consumerSettings.enableAutoCommit,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
            ConsumerConfig.GROUP_ID_CONFIG to consumerSettings.consumerGroupId,
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to 30000
        )
    }

    fun producerProperties(): Map<String, Any?> {
        return mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to serverConfig.bootstrapServers,
            SCHEMA_REGISTRY_URL to serverConfig.schemaRegistryUrl,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to KafkaAvroSerializer::class.java,
            AUTO_REGISTER_SCHEMAS to true
            )
    }

    companion object {
        const val SCHEMA_REGISTRY_URL = "schema.registry.url"
        const val AUTO_REGISTER_SCHEMAS = "auto.register.schemas"

        fun default(): KafkaConfigProvider = ConfigLoader().loadConfigOrThrow<KafkaConfigProvider>("/kafka-config.yaml")
    }
}