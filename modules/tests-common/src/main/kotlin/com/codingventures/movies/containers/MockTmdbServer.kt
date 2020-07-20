package com.codingventures.movies.containers

import com.codingventures.movies.mockdata.*
import mu.KotlinLogging
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.MediaType
import org.mockserver.model.Not
import org.mockserver.model.Parameter
import java.util.concurrent.atomic.AtomicBoolean

private val logger = KotlinLogging.logger {}

enum class DataSet {
    FullHappyPath,
    FailingFetchNonRetriable,
    FailingFetchRetriable,
    All
}

object MockTmdbServer {
    private val initalized = AtomicBoolean(false)

    private val tmdbServer: MockServerContainer by lazy {
        MockServerContainer()
    }

    private var client: MockServerClient? = null

    fun initialize(): MockServerContainer {
        if (initalized.compareAndSet(false, true)) {
            tmdbServer.start()
            val host = tmdbServer.host
            val port = tmdbServer.serverPort
            client = MockServerClient(host, port)

            registerMovieListEndpoints()
            registerMovieDetailEndpoints()
            registerPersonDetailEndpoints()

//            when (dataSet) {
//                DataSet.FullHappyPath -> registerHappyPath()
//                DataSet.FailingFetchNonRetriable -> registerNonRetriable()
//                DataSet.FailingFetchRetriable -> registerRetriable()
//                DataSet.All -> registerAll()
//            }
//
        }

        return tmdbServer
    }

    public fun debugExpectations() {
        val expectations = client?.retrieveActiveExpectations(null)
        logger.debug { expectations }
    }

//    private fun registerAll() {
//        registerHappyPath()
//    }
//
//    private fun registerHappyPath() {
//        registerMovieListEndpoints()
//        registerMovieDetailEndpoints()
//        registerPersonDetailEndpoints()
//    }
//
//    private fun registerNonRetriable() {
//
//    }
//
//    private fun registerRetriable() {
//
//    }
//
    private fun registerMovieListEndpoints() {
        val listPages = arrayOf(
            movieListPage1,
            movieListPage2,
            movieListFailing
        )

        listPages.forEachIndexed() { i, response ->
            client?.`when`(
                request()
                    .withMethod("GET")
                    .withPath("/3/discover/movie")
                    .withQueryStringParameters(
                        Parameter("page", "${i+1}"),
                        Parameter("sort_by", "popularity.desc"),
                        Parameter("api_key", "TestKey")
                    )
            )?.respond(
                response()
                    .withBody(response)
                    .withContentType(MediaType.APPLICATION_JSON)
            )
        }
    }

    private fun registerMovieDetailEndpoints() {
        val movies = mapOf(
            "419704" to movieAdAstra,
            "475557" to movieJoker,
            "496243" to movieParasite,
            "55419704" to movieAdAstraFailing
        )

        movies.forEach { id, details ->
            client?.`when`(
                request()
                    .withMethod("GET")
                    .withPath("/3/movie/$id")
                    .withQueryStringParameters(
                        Parameter("append_to_response", "credits,release_dates"),
                        Parameter("api_key", "TestKey")
                    )
            )?.respond(
                response()
                    .withBody(details)
                    .withContentType(MediaType.APPLICATION_JSON)
            )
        }
    }

    private fun registerPersonDetailEndpoints() {
        val people = mapOf(
            "287" to personBrad,
            "2176" to personTommy,
            "17018" to personRuth,
            "20561" to personJames,
            "73421" to personJoaquin,
            "1545693" to personZazie,
            "57130" to personTodd,
            "20738" to personSong,
            "21684" to personBong,
            "5555287" to personBradFailing
        )

        people.forEach { id, details ->
            client?.`when`(
                request()
                    .withMethod("GET")
                    .withPath("/3/person/$id")
                    .withQueryStringParameters(
                        Parameter("append_to_response", "images"),
                        Parameter("api_key", "TestKey")
                    )
            )?.respond(
                response()
                    .withBody(details)
                    .withContentType(MediaType.APPLICATION_JSON)
            )
        }
    }
}
