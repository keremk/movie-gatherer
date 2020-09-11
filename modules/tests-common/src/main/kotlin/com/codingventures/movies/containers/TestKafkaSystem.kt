package com.codingventures.movies.containers

import com.codingventures.movies.kafka.*
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.KafkaContainer
import java.util.concurrent.atomic.AtomicBoolean

object TestKafkaSystem {
    private const val kafkaSystemVersion = "5.2.1"
    private const val schemaRegistryPort = 8081
    private const val zookeeperUrl = "localhost:2181"

    private val kafkaInitialized = AtomicBoolean(false)
    private val schemaRegistryInitialized = AtomicBoolean(false)

    private val kafka: KafkaContainer by lazy {
        KafkaContainer(kafkaSystemVersion)
            .withEnv(
                mapOf(
                    "delete.topic.enable" to "true",
                    "auto.create.topics.enable" to "true"
                )
            )
    }

    private class SchemaRegistryContainer(image: String) : GenericContainer<SchemaRegistryContainer>(image)

    private val schemaRegistry: SchemaRegistryContainer by lazy {
        SchemaRegistryContainer("confluentinc/cp-schema-registry:$kafkaSystemVersion")
            .withNetwork(kafka.network)
            .withEnv(
                mapOf(
                    "SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS" to "PLAINTEXT://${kafka.networkAliases[0]}:9092",
                    "SCHEMA_REGISTRY_HOST_NAME" to "schema-registry"
                )
            )
            .withExposedPorts(schemaRegistryPort)
    }

    private fun schemaRegistryUrl(): String {
        val port = schemaRegistry.getMappedPort(schemaRegistryPort)
        val host = schemaRegistry.containerIpAddress

        return "http://$host:$port"
    }

    fun initialize(topics: List<String> = emptyList()): ServerConfig {
        if (kafkaInitialized.compareAndSet(false, true)) {
            kafka.start()
        }

        if (schemaRegistryInitialized.compareAndSet(false, true)) {
            schemaRegistry.start()
        }

        if (!topics.isEmpty()) {
            deleteTopics(topics)
            createTopics(topics)
        }

        return ServerConfig(
            schemaRegistryUrl = schemaRegistryUrl(),
            bootstrapServers = kafka.bootstrapServers
        )
    }

    fun createTopics(topics: List<String>): Int {
        if (!kafka.isRunning) return 0

        topics.forEach {
            val createCommand =
                "/usr/bin/kafka-topics --create --zookeeper $zookeeperUrl --partitions 1 --replication-factor 1 --topic $it"
            val execResult = kafka.execInContainer("/bin/sh", "-c", createCommand)
            if (execResult.exitCode == 0) return execResult.exitCode
        }
        return 1
    }


    fun deleteTopics(topics: List<String>): Int {
        if (!kafka.isRunning) return 0

        val deleteCommand =
            "/usr/bin/kafka-topics --delete --zookeeper $zookeeperUrl --topic ${topics.joinToString(separator = ",")}"
        val execResult = kafka.execInContainer("/bin/sh", "-c", deleteCommand)
        return execResult.exitCode
    }

}