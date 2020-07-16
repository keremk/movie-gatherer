package com.codingventures.movies.ingester.processor

import com.codingventures.movies.domain.MovieIndustryData
import com.codingventures.movies.domain.FetchTask
import com.codingventures.movies.ingester.remote.tmdb.response.MovieDetails as MovieDetailsResponse
import com.codingventures.movies.ingester.remote.tmdb.response.MovieList as MovieListResponse
import com.codingventures.movies.ingester.remote.tmdb.response.Cast as CastResponse
import com.codingventures.movies.ingester.remote.tmdb.response.Crew as CrewResponse
import com.codingventures.movies.ingester.remote.tmdb.response.PersonDetails as PersonDetailsResponse
import com.codingventures.movies.ingester.remote.tmdb.response.TmdbResponse
import com.codingventures.movies.ingester.remote.tmdb.tasks.movieDetailsFetchTask
import com.codingventures.movies.ingester.remote.tmdb.tasks.personDetailsFetchTask
import com.codingventures.movies.ingester.remote.tmdb.tasks.popularMoviesFetchTask
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord

data class ResponseProcessorException(
    val response: TmdbResponse,
    val inner: Exception
): Exception()

fun processResponse(response: TmdbResponse): Pair<MovieIndustryData?, List<FetchTask>> {
    try {
        return when (response) {
            is MovieListResponse -> processMovieList(response)
            is MovieDetailsResponse -> processMovieDetails(response)
            is PersonDetailsResponse -> processPersonDetails(response)
        }
    } catch (e: Exception) {
        throw ResponseProcessorException(response, e)
    }
}

fun processMovieList(movieList: MovieListResponse) = Pair(
    null,
    listOf(
        movieList.results.map { movieDetailsFetchTask("${it.id}") },
        listOfNotNull(nextPageMovieListTask(movieList.page, movieList.totalPages))
    ).flatten()
)

fun processMovieDetails(movie: MovieDetailsResponse) = Pair(
    mapMovieResponseToMovieDetails(movie),
    personDetailsTasks(movie.credits.crew, movie.credits.cast)
)

fun processPersonDetails(person: PersonDetailsResponse) = Pair(
    mapPersonResponseToPersonDetails(person),
    emptyList<FetchTask>()
)

fun nextPageMovieListTask(currentPage: Int, totalPages: Int): FetchTask? {
    if (currentPage >= totalPages) return null

    if (currentPage >= 3) return null // For debugging, comment out when in production

    val nextPage = currentPage + 1
    return popularMoviesFetchTask(nextPage)
}

fun personDetailsTasks(crew: List<CrewResponse>, cast: List<CastResponse>) = listOf(
        crew.map { it.id }.take(5),
        cast.map { it.id }.take(5)
    )
    .flatten()
    .map { personDetailsFetchTask("$it") }
