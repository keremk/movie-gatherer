package com.codingventures.movies.containers

import org.testcontainers.containers.GenericContainer

// Using my version of MockServerContainer, because the testcontainers version use
// old versions of MockServer which does not match the versions of the MockServer clients
class MockServerContainer @JvmOverloads constructor(version: String = MockServerContainer.Companion.VERSION) :
    GenericContainer<MockServerContainer?>("mockserver/mockserver:mockserver-$version") {
    val endpoint: String
        get() = String.format("http://%s:%d", host, getMappedPort(MockServerContainer.Companion.PORT))

    val serverPort: Int
        get() = getMappedPort(MockServerContainer.Companion.PORT)

    init {
        withCommand("-logLevel INFO -serverPort " + MockServerContainer.Companion.PORT)
        addExposedPorts(MockServerContainer.Companion.PORT)
    }

    companion object {
        const val VERSION = "5.10.0"
        const val PORT = 1080
    }
}