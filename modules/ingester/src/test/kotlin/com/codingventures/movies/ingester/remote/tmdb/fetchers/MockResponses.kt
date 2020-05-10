package com.codingventures.movies.ingester.remote.tmdb.fetchers

import com.codingventures.movies.ingester.reader.Endpoint
import com.codingventures.movies.ingester.reader.FetchTask
import com.codingventures.movies.ingester.reader.ServiceProvider
import com.codingventures.movies.ingester.reader.TaskType
import io.ktor.http.ContentType
import io.ktor.http.headersOf

val mockBadRequestTask = FetchTask(
    taskType = TaskType.MovieDetails,
    serviceProvider = ServiceProvider.TMDB,
    endpoint = Endpoint(
        path = "3/invalid",
        params = mapOf("append_to_response" to listOf("credits,release_dates"))
    )
)

val mockUnexpectedResponseTask = FetchTask(
    taskType = TaskType.MovieDetails,
    serviceProvider = ServiceProvider.TMDB,
    endpoint = Endpoint(
        path = "3/unexpected",
        params = mapOf("append_to_response" to listOf("credits,release_dates"))
    )
)

val mockMissingFieldsTask = FetchTask(
    taskType = TaskType.MovieDetails,
    serviceProvider = ServiceProvider.TMDB,
    endpoint = Endpoint(
        path = "3/missingfields",
        params = mapOf("append_to_response" to listOf("credits,release_dates"))
    )
)

val mockHeaders = headersOf(
    "Content-Type" to listOf(ContentType.Application.Json.toString())
)

val mockUnexpectedMovieDetails = """
    {
        "id": null
    }
""".trimIndent()

val mockMissingFields = """
    {
        "adult": false,
        "backdrop_path": "/ftODZXaXpWtV5XFD8gS9n9KwLDr.jpg",
        "belongs_to_collection": {
            "id": 681327,
            "name": "Spenser Collection",
            "poster_path": "/943yCMe4jz7X7WL4N08GFXzHPIi.jpg",
            "backdrop_path": null
        },
        "budget": 0,
        "id": 1000
    }
""".trimIndent()

val mockMovieList = """
    {
        "page": 1,
        "total_results": 10000,
        "total_pages": 500,
        "results": [
            {
                "popularity": 458.421,
                "vote_count": 3295,
                "video": false,
                "poster_path": "/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg",
                "id": 419704,
                "adult": false,
                "backdrop_path": "/5BwqwxMEjeFtdknRV792Svo0K1v.jpg",
                "original_language": "en",
                "original_title": "Ad Astra",
                "genre_ids": [
                    18,
                    878
                ],
                "title": "Ad Astra",
                "vote_average": 6,
                "overview": "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.",
                "release_date": "2019-09-17"
            },
            {
                "popularity": 219.819,
                "vote_count": 1627,
                "video": false,
                "poster_path": "/wlfDxbGEsW58vGhFljKkcR5IxDj.jpg",
                "id": 545609,
                "adult": false,
                "backdrop_path": "/1R6cvRtZgsYCkh8UFuWFN33xBP4.jpg",
                "original_language": "en",
                "original_title": "Extraction",
                "genre_ids": [
                    28,
                    18,
                    53
                ],
                "title": "Extraction",
                "vote_average": 7.5,
                "overview": "Tyler Rake, a fearless mercenary who offers his services on the black market, embarks on a dangerous mission when he is hired to rescue the kidnapped son of a Mumbai crime lord…",
                "release_date": "2020-04-24"
            },
            {
                "popularity": 172.265,
                "vote_count": 3610,
                "video": false,
                "poster_path": "/h4VB6m0RwcicVEZvzftYZyKXs6K.jpg",
                "id": 495764,
                "adult": false,
                "backdrop_path": "/uozb2VeD87YmhoUP1RrGWfzuCrr.jpg",
                "original_language": "en",
                "original_title": "Birds of Prey (and the Fantabulous Emancipation of One Harley Quinn)",
                "genre_ids": [
                    28,
                    35,
                    80
                ],
                "title": "Birds of Prey (and the Fantabulous Emancipation of One Harley Quinn)",
                "vote_average": 7.2,
                "overview": "Harley Quinn joins forces with a singer, an assassin and a police detective to help a young girl who had a hit placed on her after she stole a rare diamond from a crime lord.",
                "release_date": "2020-02-05"
            },
            {
                "popularity": 169.659,
                "vote_count": 853,
                "video": false,
                "poster_path": "/33VdppGbeNxICrFUtW2WpGHvfYc.jpg",
                "id": 481848,
                "adult": false,
                "backdrop_path": "/9sXHqZTet3Zg5tgcc0hCDo8Tn35.jpg",
                "original_language": "en",
                "original_title": "The Call of the Wild",
                "genre_ids": [
                    12,
                    18,
                    10751
                ],
                "title": "The Call of the Wild",
                "vote_average": 7.3,
                "overview": "Buck is a big-hearted dog whose blissful domestic life is turned upside down when he is suddenly uprooted from his California home and transplanted to the exotic wilds of the Yukon during the Gold Rush of the 1890s. As the newest rookie on a mail delivery dog sled team—and later its leader—Buck experiences the adventure of a lifetime, ultimately finding his true place in the world and becoming his own master.",
                "release_date": "2020-02-19"
            },
            {
                "popularity": 169.051,
                "vote_count": 2099,
                "video": false,
                "poster_path": "/8WUVHemHFH2ZIP6NWkwlHWsyrEL.jpg",
                "id": 338762,
                "adult": false,
                "backdrop_path": "/ocUrMYbdjknu2TwzMHKT9PBBQRw.jpg",
                "original_language": "en",
                "original_title": "Bloodshot",
                "genre_ids": [
                    28,
                    878
                ],
                "title": "Bloodshot",
                "vote_average": 7.1,
                "overview": "After he and his wife are murdered, marine Ray Garrison is resurrected by a team of scientists. Enhanced with nanotechnology, he becomes a superhuman, biotech killing machine—'Bloodshot'. As Ray first trains with fellow super-soldiers, he cannot recall anything from his former life. But when his memories flood back and he remembers the man that killed both him and his wife, he breaks out of the facility to get revenge, only to discover that there's more to the conspiracy than he thought.",
                "release_date": "2020-03-05"
            },
            {
                "popularity": 167.549,
                "vote_count": 205,
                "video": false,
                "poster_path": "/c01Y4suApJ1Wic2xLmaq1QYcfoZ.jpg",
                "id": 618344,
                "adult": false,
                "backdrop_path": "/sQkRiQo3nLrQYMXZodDjNUJKHZV.jpg",
                "original_language": "en",
                "original_title": "Justice League Dark: Apokolips War",
                "genre_ids": [
                    28,
                    12,
                    16,
                    14,
                    878
                ],
                "title": "Justice League Dark: Apokolips War",
                "vote_average": 8.9,
                "overview": "Earth is decimated after intergalactic tyrant Darkseid has devastated the Justice League in a poorly executed war by the DC Super Heroes. Now the remaining bastions of good – the Justice League, Teen Titans, Suicide Squad and assorted others – must regroup, strategize and take the war to Darkseid in order to save the planet and its surviving inhabitants.",
                "release_date": "2020-05-05"
            },
            {
                "popularity": 167.086,
                "vote_count": 3658,
                "video": false,
                "poster_path": "/aQvJ5WPzZgYVDrxLX4R6cLJCEaQ.jpg",
                "id": 454626,
                "adult": false,
                "backdrop_path": "/stmYfCUGd8Iy6kAMBr6AmWqx8Bq.jpg",
                "original_language": "en",
                "original_title": "Sonic the Hedgehog",
                "genre_ids": [
                    28,
                    35,
                    878,
                    10751
                ],
                "title": "Sonic the Hedgehog",
                "vote_average": 7.6,
                "overview": "Based on the global blockbuster videogame franchise from Sega, Sonic the Hedgehog tells the story of the world’s speediest hedgehog as he embraces his new home on Earth. In this live-action adventure comedy, Sonic and his new best friend team up to defend the planet from the evil genius Dr. Robotnik and his plans for world domination.",
                "release_date": "2020-02-12"
            },
            {
                "popularity": 142.437,
                "vote_count": 6,
                "video": false,
                "poster_path": "/oiFxdnsQyGs3DBLzHzW92GrDR6w.jpg",
                "id": 682071,
                "adult": false,
                "backdrop_path": "/6I6ZhQrX4L4KIxSzlgg8zc1BSLr.jpg",
                "original_language": "en",
                "original_title": "To the Beat! Back 2 School",
                "genre_ids": [
                    35,
                    18,
                    10751
                ],
                "title": "To the Beat! Back 2 School",
                "vote_average": 2.9,
                "overview": "Sisterhood is tested, rivalries heat up and new bonds are formed when students go back to their performing arts school to compete for an all-expense paid summer scholarship program to a prestigious Conservatory of Fine Arts.",
                "release_date": "2020-03-10"
            },
            {
                "popularity": 129.395,
                "vote_count": 7050,
                "video": false,
                "poster_path": "/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
                "id": 496243,
                "adult": false,
                "backdrop_path": "/ApiBzeaa95TNYliSbQ8pJv4Fje7.jpg",
                "original_language": "ko",
                "original_title": "기생충",
                "genre_ids": [
                    35,
                    18,
                    53
                ],
                "title": "Parasite",
                "vote_average": 8.5,
                "overview": "All unemployed, Ki-taek's family takes peculiar interest in the wealthy and glamorous Parks for their livelihood until they get entangled in an unexpected incident.",
                "release_date": "2019-05-30"
            },
            {
                "popularity": 128.4,
                "vote_count": 4436,
                "video": false,
                "poster_path": "/db32LaOibwEliAmSL2jjDF6oDdj.jpg",
                "id": 181812,
                "adult": false,
                "backdrop_path": "/jOzrELAzFxtMx2I4uDGHOotdfsS.jpg",
                "original_language": "en",
                "original_title": "Star Wars: The Rise of Skywalker",
                "genre_ids": [
                    28,
                    12,
                    878
                ],
                "title": "Star Wars: The Rise of Skywalker",
                "vote_average": 6.5,
                "overview": "The surviving Resistance faces the First Order once again as the journey of Rey, Finn and Poe Dameron continues. With the power and knowledge of generations behind them, the final battle begins.",
                "release_date": "2019-12-18"
            },
            {
                "popularity": 115.538,
                "vote_count": 4693,
                "video": false,
                "poster_path": "/AuGiPiGMYMkSosOJ3BQjDEAiwtO.jpg",
                "id": 530915,
                "adult": false,
                "backdrop_path": "/2lBOQK06tltt8SQaswgb8d657Mv.jpg",
                "original_language": "en",
                "original_title": "1917",
                "genre_ids": [
                    28,
                    18,
                    36,
                    53,
                    10752
                ],
                "title": "1917",
                "vote_average": 8,
                "overview": "At the height of the First World War, two young British soldiers must cross enemy territory and deliver a message that will stop a deadly attack on hundreds of soldiers.",
                "release_date": "2019-12-25"
            },
            {
                "popularity": 101.913,
                "vote_count": 9262,
                "video": false,
                "poster_path": "/qa6HCwP4Z15l3hpsASz3auugEW6.jpg",
                "id": 920,
                "adult": false,
                "backdrop_path": "/sd4xN5xi8tKRPrJOWwNiZEile7f.jpg",
                "original_language": "en",
                "original_title": "Cars",
                "genre_ids": [
                    12,
                    16,
                    35,
                    10751
                ],
                "title": "Cars",
                "vote_average": 6.8,
                "overview": "Lightning McQueen, a hotshot rookie race car driven to succeed, discovers that life is about the journey, not the finish line, when he finds himself unexpectedly detoured in the sleepy Route 66 town of Radiator Springs. On route across the country to the big Piston Cup Championship in California to compete against two seasoned pros, McQueen gets to know the town's offbeat characters.",
                "release_date": "2006-06-08"
            },
            {
                "popularity": 98.742,
                "vote_count": 3746,
                "video": false,
                "poster_path": "/y95lQLnuNKdPAzw9F9Ab8kJ80c3.jpg",
                "id": 38700,
                "adult": false,
                "backdrop_path": "/upUy2QhMZEmtypPW3PdieKLAHxh.jpg",
                "original_language": "en",
                "original_title": "Bad Boys for Life",
                "genre_ids": [
                    28,
                    80,
                    53
                ],
                "title": "Bad Boys for Life",
                "vote_average": 7.2,
                "overview": "Marcus and Mike are forced to confront new threats, career changes, and midlife crises as they join the newly created elite team AMMO of the Miami police department to take down the ruthless Armando Armas, the vicious leader of a Miami drug cartel.",
                "release_date": "2020-01-15"
            },
            {
                "popularity": 97.728,
                "vote_count": 627,
                "video": false,
                "poster_path": "/8ZMrZGGW65ePWIgRn1260nA1uUm.jpg",
                "id": 539537,
                "adult": false,
                "backdrop_path": "/x80ZIVGUJ6plcUBcgVZ6DPKT7vc.jpg",
                "original_language": "en",
                "original_title": "Fantasy Island",
                "genre_ids": [
                    14,
                    27,
                    878
                ],
                "title": "Fantasy Island",
                "vote_average": 6.1,
                "overview": "A group of contest winners arrive at an island hotel to live out their dreams, only to find themselves trapped in nightmare scenarios.",
                "release_date": "2020-02-12"
            },
            {
                "popularity": 95.861,
                "vote_count": 12213,
                "video": false,
                "poster_path": "/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg",
                "id": 475557,
                "adult": false,
                "backdrop_path": "/f5F4cRhQdUbyVbB5lTNCwUzD6BP.jpg",
                "original_language": "en",
                "original_title": "Joker",
                "genre_ids": [
                    80,
                    18,
                    53
                ],
                "title": "Joker",
                "vote_average": 8.2,
                "overview": "During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure.",
                "release_date": "2019-10-02"
            },
            {
                "popularity": 92.842,
                "vote_count": 1033,
                "video": false,
                "poster_path": "/gzlbb3yeVISpQ3REd3Ga1scWGTU.jpg",
                "id": 443791,
                "adult": false,
                "backdrop_path": "/ww7eC3BqSbFsyE5H5qMde8WkxJ2.jpg",
                "original_language": "en",
                "original_title": "Underwater",
                "genre_ids": [
                    28,
                    27,
                    878,
                    53
                ],
                "title": "Underwater",
                "vote_average": 6.4,
                "overview": "After an earthquake destroys their underwater station, six researchers must navigate two miles along the dangerous, unknown depths of the ocean floor to make it to safety in a race against time.",
                "release_date": "2020-01-08"
            },
            {
                "popularity": 87.731,
                "vote_count": 3884,
                "video": false,
                "poster_path": "/pThyQovXQrw2m0s9x82twj48Jq4.jpg",
                "id": 546554,
                "adult": false,
                "backdrop_path": "/AbRYlvwAKHs0YuyNO6NX9ofq4l6.jpg",
                "original_language": "en",
                "original_title": "Knives Out",
                "genre_ids": [
                    35,
                    80,
                    18,
                    9648,
                    53
                ],
                "title": "Knives Out",
                "vote_average": 7.8,
                "overview": "When renowned crime novelist Harlan Thrombey is found dead at his estate just after his 85th birthday, the inquisitive and debonair Detective Benoit Blanc is mysteriously enlisted to investigate. From Harlan's dysfunctional family to his devoted staff, Blanc sifts through a web of red herrings and self-serving lies to uncover the truth behind Harlan's untimely death.",
                "release_date": "2019-11-27"
            },
            {
                "popularity": 85.542,
                "vote_count": 220,
                "video": false,
                "poster_path": "/x0g9tzgZKKmNEtwcjS3aF4kduRi.jpg",
                "id": 689723,
                "adult": false,
                "backdrop_path": "/tuqv0jb0o7sBOivgkagKJHuO6X4.jpg",
                "original_language": "en",
                "original_title": "Dangerous Lies",
                "genre_ids": [
                    53
                ],
                "title": "Dangerous Lies",
                "vote_average": 6.4,
                "overview": "After losing her waitressing job, Katie Franklin takes a job as a caretaker to a wealthy elderly man in his sprawling, empty Chicago estate. The two grow close, but when he unexpectedly passes away and names Katie as his sole heir, she and her husband Adam are pulled into a complex web of lies, deception, and murder. If she's going to survive, Katie will have to question everyone's motives — even the people she loves.",
                "release_date": "2020-04-30"
            },
            {
                "popularity": 82.154,
                "vote_count": 13529,
                "video": false,
                "poster_path": "/6FfCtAuVAW8XJjZ7eWeLibRLWTw.jpg",
                "id": 11,
                "adult": false,
                "backdrop_path": "/zqkmTXzjkAgXmEWLRsY4UpTWCeo.jpg",
                "original_language": "en",
                "original_title": "Star Wars",
                "genre_ids": [
                    28,
                    12,
                    878
                ],
                "title": "Star Wars",
                "vote_average": 8.2,
                "overview": "Princess Leia is captured and held hostage by the evil Imperial forces in their effort to take over the galactic Empire. Venturesome Luke Skywalker and dashing captain Han Solo team together with the loveable robot duo R2-D2 and C-3PO to rescue the beautiful princess and restore peace and justice in the Empire.",
                "release_date": "1977-05-25"
            },
            {
                "popularity": 80.02,
                "vote_count": 261,
                "video": false,
                "poster_path": "/jC1PNXGET1ZZQyrJvdFhPfXdPP1.jpg",
                "id": 597219,
                "adult": false,
                "backdrop_path": "/deTb672Jh4HGh48x4MVwHXIytQU.jpg",
                "original_language": "en",
                "original_title": "The Half of It",
                "genre_ids": [
                    35,
                    10749
                ],
                "title": "The Half of It",
                "vote_average": 7.4,
                "overview": "Shy, straight-A student Ellie is hired by sweet but inarticulate jock Paul, who needs help wooing the most popular girl in school. But their new and unlikely friendship gets tricky when Ellie discovers she has feelings for the same girl.",
                "release_date": "2020-05-01"
            }
        ]
    } 
""".trimIndent()

val mockMovieDetails = """
    {
        "adult": false,
        "backdrop_path": "/ftODZXaXpWtV5XFD8gS9n9KwLDr.jpg",
        "belongs_to_collection": {
            "id": 681327,
            "name": "Spenser Collection",
            "poster_path": "/943yCMe4jz7X7WL4N08GFXzHPIi.jpg",
            "backdrop_path": null
        },
        "budget": 0,
        "genres": [
            {
                "id": 35,
                "name": "Comedy"
            },
            {
                "id": 28,
                "name": "Action"
            },
            {
                "id": 53,
                "name": "Thriller"
            }
        ],
        "homepage": "https://www.netflix.com/title/81005492",
        "id": 581600,
        "imdb_id": "tt8629748",
        "original_language": "en",
        "original_title": "Spenser Confidential",
        "overview": "Spenser, a former Boston patrolman who just got out from prison, teams up with Hawk, an aspiring fighter, to unravel the truth behind the death of two police officers.",
        "popularity": 36.585,
        "poster_path": "/fePczipv6ZzDO2uoww4vTAu2Sq3.jpg",
        "production_companies": [
            {
                "id": 20153,
                "logo_path": null,
                "name": "Film 44",
                "origin_country": "US"
            },
            {
                "id": 333,
                "logo_path": "/5xUJfzPZ8jWJUDzYtIeuPO4qPIa.png",
                "name": "Original Film",
                "origin_country": "US"
            },
            {
                "id": 8537,
                "logo_path": null,
                "name": "Closest to the Hole Productions",
                "origin_country": "US"
            },
            {
                "id": 119509,
                "logo_path": null,
                "name": "Leverage Entertainment",
                "origin_country": "US"
            }
        ],
        "production_countries": [
            {
                "iso_3166_1": "US",
                "name": "United States of America"
            }
        ],
        "release_date": "2020-03-06",
        "revenue": 0,
        "runtime": 110,
        "spoken_languages": [
            {
                "iso_639_1": "en",
                "name": "English"
            },
            {
                "iso_639_1": "es",
                "name": "Español"
            }
        ],
        "status": "Released",
        "tagline": "The law has limits. They don't.",
        "title": "Spenser Confidential",
        "video": false,
        "vote_average": 6.7,
        "vote_count": 931,
        "credits": {
            "cast": [
                {
                    "cast_id": 1,
                    "character": "Spenser",
                    "credit_id": "5c62c6a30e0a267dfd981c3f",
                    "gender": 2,
                    "id": 13240,
                    "name": "Mark Wahlberg",
                    "order": 0,
                    "profile_path": "/bTEFpaWd7A6AZVWOqKKBWzKEUe8.jpg"
                },
                {
                    "cast_id": 34,
                    "character": "Hawk",
                    "credit_id": "5cf686de0e0a267993ca87dc",
                    "gender": 2,
                    "id": 1447932,
                    "name": "Winston Duke",
                    "order": 1,
                    "profile_path": "/MhBiZbryibwuoEtPL9Ns8pYHC1.jpg"
                },
                {
                    "cast_id": 2,
                    "character": "Henry",
                    "credit_id": "5c62c703c3a368745ed78705",
                    "gender": 2,
                    "id": 1903,
                    "name": "Alan Arkin",
                    "order": 2,
                    "profile_path": "/mhACkTNrnwR4E115h8EfCZ263zH.jpg"
                },
                {
                    "cast_id": 9,
                    "character": "Cissy Davis",
                    "credit_id": "5c62c84ac3a36825e7d5529b",
                    "gender": 1,
                    "id": 96316,
                    "name": "Iliza Shlesinger",
                    "order": 3,
                    "profile_path": "/cLbuKIJOmojOL0z9nLcJhWzq8Ge.jpg"
                },
                {
                    "cast_id": 8,
                    "character": "Captain Boylan",
                    "credit_id": "5c62c82bc3a3684f9ed80a28",
                    "gender": 2,
                    "id": 33241,
                    "name": "Michael Gaston",
                    "order": 4,
                    "profile_path": null
                },
                {
                    "cast_id": 5,
                    "character": "Driscoll",
                    "credit_id": "5c62c7e60e0a267de89816a1",
                    "gender": 2,
                    "id": 71913,
                    "name": "Bokeem Woodbine",
                    "order": 5,
                    "profile_path": "/pnPyA5pJn94zUzuLWTNkGBNZxza.jpg"
                },
                {
                    "cast_id": 6,
                    "character": "Wayne Cosgrove",
                    "credit_id": "5c62c7f7c3a3684f90d7d9ba",
                    "gender": 2,
                    "id": 1231717,
                    "name": "Marc Maron",
                    "order": 6,
                    "profile_path": "/zgUiWlWf2JvVYRO7aX9WUAuwYQR.jpg"
                },
                {
                    "cast_id": 12,
                    "character": "Bentwood",
                    "credit_id": "5c62c8aac3a3684f97d7ca51",
                    "gender": 2,
                    "id": 97446,
                    "name": "James DuMont",
                    "order": 7,
                    "profile_path": "/8DLNFj3a4SJWdtw5daPBQm0qrOO.jpg"
                },
                {
                    "cast_id": 7,
                    "character": "'Squeeb'",
                    "credit_id": "5c62c805c3a3684fa4d847e5",
                    "gender": 2,
                    "id": 1821863,
                    "name": "Post Malone",
                    "order": 8,
                    "profile_path": "/iEiu7QhZsqXyBctfr2SlpEuqCZi.jpg"
                },
                {
                    "cast_id": 4,
                    "character": "Betty",
                    "credit_id": "5c62c7ca0e0a267dfd981e45",
                    "gender": 1,
                    "id": 13023,
                    "name": "Colleen Camp",
                    "order": 9,
                    "profile_path": "/ykT8mEYZM57T6JH5RMIMaJQpReb.jpg"
                },
                {
                    "cast_id": 50,
                    "character": "Letitia",
                    "credit_id": "5e63609755c92600135e7c9d",
                    "gender": 1,
                    "id": 1370849,
                    "name": "Hope Olaide Wilson",
                    "order": 10,
                    "profile_path": null
                },
                {
                    "cast_id": 51,
                    "character": "Macklin",
                    "credit_id": "5e6360a7357c000011355d3d",
                    "gender": 2,
                    "id": 60509,
                    "name": "Kip Weeks",
                    "order": 11,
                    "profile_path": null
                },
                {
                    "cast_id": 52,
                    "character": "Terrence Graham",
                    "credit_id": "5e6360bc459ad600135710df",
                    "gender": 0,
                    "id": 1942131,
                    "name": "Brandon Scales",
                    "order": 12,
                    "profile_path": "/y954SAZj3Zov7PrinOcanx8y2q9.jpg"
                },
                {
                    "cast_id": 53,
                    "character": "Federal Agent Burton",
                    "credit_id": "5e6360d4459ad60013571114",
                    "gender": 1,
                    "id": 2559002,
                    "name": "Ayana Brown",
                    "order": 13,
                    "profile_path": "/cAmmH9ek1UM0Sdo6w2crtTkoMlI.jpg"
                },
                {
                    "cast_id": 54,
                    "character": "Federal Agent Fingle",
                    "credit_id": "5e6360e7459ad600185675da",
                    "gender": 2,
                    "id": 1732327,
                    "name": "Dustin Tucker",
                    "order": 14,
                    "profile_path": "/9wVwGqOPOCkO6I1zFwiPi0caaEf.jpg"
                },
                {
                    "cast_id": 55,
                    "character": "Minimart Clerk",
                    "credit_id": "5e636108357c00001335d7b0",
                    "gender": 0,
                    "id": 2195271,
                    "name": "Josh Cronin",
                    "order": 15,
                    "profile_path": "/iBKg7FYR2hlL3jkMc36xR99nN9E.jpg"
                },
                {
                    "cast_id": 56,
                    "character": "Scott Traylor",
                    "credit_id": "5e6361193e01ea0013e9b0c9",
                    "gender": 1,
                    "id": 1779986,
                    "name": "Alfred Briere",
                    "order": 16,
                    "profile_path": "/v4ivthGHETkShtvbQnnP98G36Zg.jpg"
                },
                {
                    "cast_id": 57,
                    "character": "Laurie Boylan",
                    "credit_id": "5e63612d357c00001335d800",
                    "gender": 0,
                    "id": 557599,
                    "name": "Rebecca Gibel",
                    "order": 17,
                    "profile_path": null
                },
                {
                    "cast_id": 58,
                    "character": "Gloria Weisnewski",
                    "credit_id": "5e63615d357c00001335d835",
                    "gender": 1,
                    "id": 1633527,
                    "name": "Alexandra Vino",
                    "order": 18,
                    "profile_path": "/syLrDdr9AXHIxrvEnq7AyMRPLcG.jpg"
                },
                {
                    "cast_id": 59,
                    "character": "Mrs. Weisnewski",
                    "credit_id": "5e6361b0459ad6001a56b07a",
                    "gender": 1,
                    "id": 944107,
                    "name": "Patty Ross",
                    "order": 19,
                    "profile_path": "/qXY7bippfggRoHJMfuPepaP964V.jpg"
                },
                {
                    "cast_id": 60,
                    "character": "Terrence and Letitia's Son",
                    "credit_id": "5e636202357c00001634db87",
                    "gender": 0,
                    "id": 2559009,
                    "name": "Chad Nehemiah Reed-Lopes",
                    "order": 20,
                    "profile_path": null
                },
                {
                    "cast_id": 61,
                    "character": "Terrence and Letitia's Son",
                    "credit_id": "5e63622c3e01ea0013e9b500",
                    "gender": 0,
                    "id": 2559011,
                    "name": "Chaz Jeremiah Reed-Lopes",
                    "order": 21,
                    "profile_path": null
                },
                {
                    "cast_id": 62,
                    "character": "Lego's Owner",
                    "credit_id": "5e6362bc357c00001335dc3b",
                    "gender": 2,
                    "id": 2559012,
                    "name": "Anthony 'Ace' Thomas",
                    "order": 22,
                    "profile_path": "/dZxlbeSACcPdNfyX4spPXMCktVg.jpg"
                },
                {
                    "cast_id": 63,
                    "character": "Boxing Trainer",
                    "credit_id": "5e6362d7357c00001135613a",
                    "gender": 2,
                    "id": 1335461,
                    "name": "Johnny Alves",
                    "order": 23,
                    "profile_path": "/fpSzBe918GyFj4QatyJWM7ZRLsn.jpg"
                },
                {
                    "cast_id": 64,
                    "character": "Billy",
                    "credit_id": "5e6362f7459ad6001156bab4",
                    "gender": 2,
                    "id": 1753545,
                    "name": "Rich Skinner",
                    "order": 24,
                    "profile_path": "/A4YCTFzLLbAQCyoup9u4dnLh5gj.jpg"
                },
                {
                    "cast_id": 65,
                    "character": "'Skinny'",
                    "credit_id": "5e63630a55c92600135e830a",
                    "gender": 0,
                    "id": 2559014,
                    "name": "Trevor Robertson",
                    "order": 25,
                    "profile_path": null
                },
                {
                    "cast_id": 66,
                    "character": "'Big Boy'",
                    "credit_id": "5e636320357c0000193573c9",
                    "gender": 2,
                    "id": 105271,
                    "name": "Donald Cerrone",
                    "order": 26,
                    "profile_path": "/lRIa01PrI9M6ARwg70xSLvQYoLR.jpg"
                },
                {
                    "cast_id": 67,
                    "character": "Hipster",
                    "credit_id": "5e636330357c00001634dde0",
                    "gender": 2,
                    "id": 2559015,
                    "name": "Christopher Weigel",
                    "order": 27,
                    "profile_path": "/yEsLXLW0WKl9wztAm0qXlQpN9tx.jpg"
                },
                {
                    "cast_id": 68,
                    "character": "Archie",
                    "credit_id": "5e63634b357c00001634ddf0",
                    "gender": 2,
                    "id": 1903695,
                    "name": "Arthur Wahlberg",
                    "order": 28,
                    "profile_path": "/dFIvtGxR76I16nCFBtrTybSqk7O.jpg"
                },
                {
                    "cast_id": 69,
                    "character": "Marcelo",
                    "credit_id": "5e63635a3e01ea0013e9b8f5",
                    "gender": 2,
                    "id": 43010,
                    "name": "Thomas Rosales Jr.",
                    "order": 29,
                    "profile_path": "/ffDrcpKGvjPgpcjd0t9T6EtQ2hz.jpg"
                },
                {
                    "cast_id": 70,
                    "character": "Boylan's Daughter",
                    "credit_id": "5e6363df55c92600155cad93",
                    "gender": 0,
                    "id": 2559016,
                    "name": "Stephanie Rodgers",
                    "order": 30,
                    "profile_path": null
                },
                {
                    "cast_id": 71,
                    "character": "Driscoll's Son",
                    "credit_id": "5e6363f2357c0000113563df",
                    "gender": 0,
                    "id": 2331658,
                    "name": "Miles Walters",
                    "order": 31,
                    "profile_path": null
                },
                {
                    "cast_id": 72,
                    "character": "Harbor Health Club Receptionist",
                    "credit_id": "5e63640b55c92600155cae73",
                    "gender": 0,
                    "id": 2559017,
                    "name": "Paris Nixon",
                    "order": 32,
                    "profile_path": null
                },
                {
                    "cast_id": 73,
                    "character": "Mimi",
                    "credit_id": "5e636418459ad60018567b31",
                    "gender": 0,
                    "id": 2559018,
                    "name": "Marilyn Leung",
                    "order": 33,
                    "profile_path": null
                },
                {
                    "cast_id": 74,
                    "character": "Baptist Minister",
                    "credit_id": "5e63642a459ad6001a56bb0b",
                    "gender": 0,
                    "id": 1863669,
                    "name": "Ricardo Pitts-Wiley",
                    "order": 34,
                    "profile_path": "/s8uVYMl3H8Tm9JYDOuGxqtmpI1w.jpg"
                },
                {
                    "cast_id": 75,
                    "character": "Cissy's Date",
                    "credit_id": "5e636441357c0000193576c4",
                    "gender": 0,
                    "id": 1933049,
                    "name": "Bo Cleary",
                    "order": 35,
                    "profile_path": null
                },
                {
                    "cast_id": 76,
                    "character": "News Anchor #1",
                    "credit_id": "5e636495357c00001634dedc",
                    "gender": 0,
                    "id": 2559019,
                    "name": "Jim Boyd",
                    "order": 36,
                    "profile_path": null
                },
                {
                    "cast_id": 77,
                    "character": "News Anchor #2",
                    "credit_id": "5e6364aa357c00001135651a",
                    "gender": 2,
                    "id": 2559020,
                    "name": "Bianca de la Garza",
                    "order": 37,
                    "profile_path": "/mOW2jLGNeAESx1p2hUwphNPjFMF.jpg"
                },
                {
                    "cast_id": 78,
                    "character": "Reporter at Letitia's",
                    "credit_id": "5e6364d4459ad60018567c9f",
                    "gender": 0,
                    "id": 1435827,
                    "name": "Jenny Johnson",
                    "order": 38,
                    "profile_path": null
                },
                {
                    "cast_id": 79,
                    "character": "Crime Scene Reporter",
                    "credit_id": "5e6364fb357c0000193578c3",
                    "gender": 0,
                    "id": 2559021,
                    "name": "Alberto Vasallo III",
                    "order": 39,
                    "profile_path": null
                },
                {
                    "cast_id": 80,
                    "character": "Field Reporter",
                    "credit_id": "5e63651555c92600195c8373",
                    "gender": 0,
                    "id": 2559023,
                    "name": "Va Lynda K. Robinson",
                    "order": 40,
                    "profile_path": null
                },
                {
                    "cast_id": 81,
                    "character": "Vegan Chef",
                    "credit_id": "5e63653455c92600155cb34b",
                    "gender": 0,
                    "id": 2559026,
                    "name": "Joe Carresi",
                    "order": 41,
                    "profile_path": null
                },
                {
                    "cast_id": 82,
                    "character": "Vegan Waitress",
                    "credit_id": "5e63656255c92600155cb470",
                    "gender": 0,
                    "id": 2559030,
                    "name": "Sam Schechter",
                    "order": 42,
                    "profile_path": null
                },
                {
                    "cast_id": 83,
                    "character": "Vegan Diner #1",
                    "credit_id": "5e636574357c00001335e39d",
                    "gender": 1,
                    "id": 2559032,
                    "name": "Marina Varano",
                    "order": 43,
                    "profile_path": "/YFqVhLA72XXdG8hwf9gbrCwP92.jpg"
                },
                {
                    "cast_id": 84,
                    "character": "Vegan Diner #2",
                    "credit_id": "5e63658655c92600175cbd9d",
                    "gender": 0,
                    "id": 1572024,
                    "name": "Kallie Tabor",
                    "order": 44,
                    "profile_path": null
                },
                {
                    "cast_id": 85,
                    "character": "Piano Player",
                    "credit_id": "5e6365963e01ea0013e9bebb",
                    "gender": 0,
                    "id": 2559033,
                    "name": "Dennis Montgomery III",
                    "order": 45,
                    "profile_path": null
                },
                {
                    "cast_id": 86,
                    "character": "Choir Singer",
                    "credit_id": "5e6365ad55c92600195c8512",
                    "gender": 0,
                    "id": 2559034,
                    "name": "Mikelyn Roderick",
                    "order": 46,
                    "profile_path": null
                },
                {
                    "cast_id": 87,
                    "character": "Van Driver",
                    "credit_id": "5e6365cb357c000011356717",
                    "gender": 0,
                    "id": 2216014,
                    "name": "Andresito Germosen De La Cruz",
                    "order": 47,
                    "profile_path": "/9u2V57E2Oj74Po4htwE45sAqL4A.jpg"
                },
                {
                    "cast_id": 88,
                    "character": "Truck School Student",
                    "credit_id": "5e63661b3e01ea0013e9bffc",
                    "gender": 0,
                    "id": 6942,
                    "name": "Eric Weinstein",
                    "order": 48,
                    "profile_path": "/yPYIjSrc1SLiVHAsBaB5RAOhINh.jpg"
                },
                {
                    "cast_id": 89,
                    "character": "Batallion Chief Foley",
                    "credit_id": "5e636631459ad60018567e92",
                    "gender": 0,
                    "id": 2559037,
                    "name": "Jeffrey Vincent Thompson",
                    "order": 49,
                    "profile_path": null
                },
                {
                    "cast_id": 90,
                    "character": "Newscaster",
                    "credit_id": "5e636649357c000019357cfc",
                    "gender": 0,
                    "id": 1971236,
                    "name": "Josh Brogadir",
                    "order": 50,
                    "profile_path": null
                },
                {
                    "cast_id": 91,
                    "character": "Detective Mulligan",
                    "credit_id": "5e6366693e01ea0011e98f76",
                    "gender": 0,
                    "id": 1293730,
                    "name": "Kevin McCormick",
                    "order": 51,
                    "profile_path": null
                },
                {
                    "cast_id": 92,
                    "character": "Salon Girl",
                    "credit_id": "5e63667a357c000019357d53",
                    "gender": 0,
                    "id": 1735636,
                    "name": "Leah Procito",
                    "order": 52,
                    "profile_path": "/qfweXm8hw3MqGPoIo778DoTTx12.jpg"
                },
                {
                    "cast_id": 93,
                    "character": "Driscoll's Buddy",
                    "credit_id": "5e63668e357c00001634e31c",
                    "gender": 0,
                    "id": 2559038,
                    "name": "Nick Cyr",
                    "order": 53,
                    "profile_path": null
                },
                {
                    "cast_id": 94,
                    "character": "Trinitario #1",
                    "credit_id": "5e6366b4459ad6001156bf49",
                    "gender": 0,
                    "id": 2559041,
                    "name": "Regis Prograis",
                    "order": 54,
                    "profile_path": null
                },
                {
                    "cast_id": 95,
                    "character": "Trinitario #2",
                    "credit_id": "5e6366ca3e01ea0011e99088",
                    "gender": 0,
                    "id": 2559042,
                    "name": "Jason A. Martinez",
                    "order": 55,
                    "profile_path": null
                },
                {
                    "cast_id": 96,
                    "character": "Trinitario #3",
                    "credit_id": "5e6366e155c92600195c872c",
                    "gender": 0,
                    "id": 2527559,
                    "name": "Brandon M. Shaw",
                    "order": 56,
                    "profile_path": null
                },
                {
                    "cast_id": 97,
                    "character": "Trinitario #4",
                    "credit_id": "5e6367093e01ea0015e93c0e",
                    "gender": 0,
                    "id": 2559044,
                    "name": "Jason Molina",
                    "order": 57,
                    "profile_path": null
                },
                {
                    "cast_id": 98,
                    "character": "Trinitario #5",
                    "credit_id": "5e63672455c92600175cbf5c",
                    "gender": 0,
                    "id": 2559046,
                    "name": "Carlos Escarfullery",
                    "order": 58,
                    "profile_path": null
                },
                {
                    "cast_id": 99,
                    "character": "Police Officer #1",
                    "credit_id": "5e63673c55c92600155cba84",
                    "gender": 2,
                    "id": 550900,
                    "name": "Carrick O'Quinn",
                    "order": 59,
                    "profile_path": "/y6P1IRcdqc3eBQWqPaja26qxgu2.jpg"
                },
                {
                    "cast_id": 100,
                    "character": "Police Officer #2",
                    "credit_id": "5e636752459ad6001a56c6b0",
                    "gender": 2,
                    "id": 1226495,
                    "name": "Wayne Dalglish",
                    "order": 60,
                    "profile_path": null
                },
                {
                    "cast_id": 101,
                    "character": "Police Officer #3",
                    "credit_id": "5e6367643e01ea0017e96efa",
                    "gender": 0,
                    "id": 1702790,
                    "name": "Adrian M. Mompoint",
                    "order": 61,
                    "profile_path": null
                },
                {
                    "cast_id": 102,
                    "character": "Police Officer #4",
                    "credit_id": "5e636774357c00001634e5c0",
                    "gender": 0,
                    "id": 2559049,
                    "name": "Sal Mirabella Jr.",
                    "order": 62,
                    "profile_path": null
                },
                {
                    "cast_id": 103,
                    "character": "Police Officer #5",
                    "credit_id": "5e63678a55c92600135e874d",
                    "gender": 0,
                    "id": 2559050,
                    "name": "Jeff Bouffard",
                    "order": 63,
                    "profile_path": null
                },
                {
                    "cast_id": 104,
                    "character": "Police Officer #6",
                    "credit_id": "5e63679d357c00001634e5f8",
                    "gender": 0,
                    "id": 2559051,
                    "name": "Dennis Pietrantonio",
                    "order": 64,
                    "profile_path": null
                },
                {
                    "cast_id": 105,
                    "character": "Officer Billy",
                    "credit_id": "5e6367b9357c000019357f32",
                    "gender": 0,
                    "id": 1759621,
                    "name": "Billy Concha",
                    "order": 65,
                    "profile_path": null
                },
                {
                    "cast_id": 106,
                    "character": "Prison Guard #1",
                    "credit_id": "5e6367de55c92600175cc015",
                    "gender": 2,
                    "id": 1745913,
                    "name": "Cary 'Big Shug' Guy",
                    "order": 66,
                    "profile_path": null
                },
                {
                    "cast_id": 107,
                    "character": "Prison Guard #2",
                    "credit_id": "5e6367ed55c92600155cbc76",
                    "gender": 0,
                    "id": 2501129,
                    "name": "Kevin Molis",
                    "order": 67,
                    "profile_path": null
                },
                {
                    "cast_id": 108,
                    "character": "Prison Guard #3",
                    "credit_id": "5e63681455c92600135e88c0",
                    "gender": 0,
                    "id": 2151364,
                    "name": "Pat O'Shea",
                    "order": 68,
                    "profile_path": null
                },
                {
                    "cast_id": 109,
                    "character": "Construction Worker #1",
                    "credit_id": "5e63684d357c00001335e963",
                    "gender": 0,
                    "id": 2559053,
                    "name": "Lawrence Duran",
                    "order": 69,
                    "profile_path": null
                },
                {
                    "cast_id": 110,
                    "character": "Construction Worker #2",
                    "credit_id": "5e63687d357c000019357fb7",
                    "gender": 0,
                    "id": 2559054,
                    "name": "Mark 'Cowboy' Schotz",
                    "order": 70,
                    "profile_path": null
                },
                {
                    "cast_id": 111,
                    "character": "Construction Worker #3",
                    "credit_id": "5e6368943e01ea0013e9c36a",
                    "gender": 0,
                    "id": 2559055,
                    "name": "Thanhminh Tan Thai",
                    "order": 71,
                    "profile_path": null
                }
            ],
            "crew": [
                {
                    "credit_id": "5e635d4855c92600155ca322",
                    "department": "Writing",
                    "gender": 2,
                    "id": 4723,
                    "job": "Screenplay",
                    "name": "Brian Helgeland",
                    "profile_path": "/f3oRiFyMZbq2HMzN71sHahYpfdW.jpg"
                },
                {
                    "credit_id": "5e635eb955c92600195c73ac",
                    "department": "Production",
                    "gender": 1,
                    "id": 7494,
                    "job": "Casting",
                    "name": "Sheila Jaffe",
                    "profile_path": null
                },
                {
                    "credit_id": "5c62cbce9251412fbffc87ca",
                    "department": "Art",
                    "gender": 2,
                    "id": 11411,
                    "job": "Production Design",
                    "name": "Neil Spisak",
                    "profile_path": null
                },
                {
                    "credit_id": "5c62cbdd0e0a267df198112d",
                    "department": "Art",
                    "gender": 0,
                    "id": 20585,
                    "job": "Set Decoration",
                    "name": "Karen O'Hara",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636a3c3e01ea0013e9c658",
                    "department": "Sound",
                    "gender": 2,
                    "id": 8653,
                    "job": "Sound Mixer",
                    "name": "Edward Tise",
                    "profile_path": null
                },
                {
                    "credit_id": "5e63691355c92600195c8ba0",
                    "department": "Art",
                    "gender": 2,
                    "id": 11508,
                    "job": "Supervising Art Director",
                    "name": "Dan Webster",
                    "profile_path": null
                },
                {
                    "credit_id": "5c62cad39251412fb4fca823",
                    "department": "Production",
                    "gender": 2,
                    "id": 11874,
                    "job": "Producer",
                    "name": "Neal H. Moritz",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636c7955c92600195c985e",
                    "department": "Camera",
                    "gender": 2,
                    "id": 12575,
                    "job": "Second Unit Director of Photography",
                    "name": "Patrick Loungway",
                    "profile_path": null
                },
                {
                    "credit_id": "5e635ea355c92600155ca636",
                    "department": "Sound",
                    "gender": 0,
                    "id": 13225,
                    "job": "Music Supervisor",
                    "name": "Liza Richardson",
                    "profile_path": null
                },
                {
                    "credit_id": "5c62caf10e0a267de6982edd",
                    "department": "Production",
                    "gender": 2,
                    "id": 13240,
                    "job": "Producer",
                    "name": "Mark Wahlberg",
                    "profile_path": "/bTEFpaWd7A6AZVWOqKKBWzKEUe8.jpg"
                },
                {
                    "credit_id": "5e635cee459ad6001a56a089",
                    "department": "Camera",
                    "gender": 2,
                    "id": 15559,
                    "job": "Director of Photography",
                    "name": "Tobias A. Schliessler",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636cf23e01ea0013e9cc9c",
                    "department": "Sound",
                    "gender": 2,
                    "id": 42035,
                    "job": "Supervising Sound Editor",
                    "name": "Sean McCormack",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636cd8357c000011357264",
                    "department": "Sound",
                    "gender": 2,
                    "id": 42036,
                    "job": "Supervising Sound Editor",
                    "name": "Kami Asgar",
                    "profile_path": null
                },
                {
                    "credit_id": "5c62cb799251412fc4fc8f1c",
                    "department": "Sound",
                    "gender": 2,
                    "id": 18264,
                    "job": "Original Music Composer",
                    "name": "Steve Jablonsky",
                    "profile_path": null
                },
                {
                    "credit_id": "5e635d1f55c92600175cb54a",
                    "department": "Writing",
                    "gender": 2,
                    "id": 19451,
                    "job": "Characters",
                    "name": "Robert B. Parker",
                    "profile_path": null
                },
                {
                    "credit_id": "5e6368d3357c00001335eb55",
                    "department": "Crew",
                    "gender": 2,
                    "id": 23285,
                    "job": "Stunt Coordinator",
                    "name": "Jeffrey J. Dashnaw",
                    "profile_path": "/5Sw0YV4U8Yg7BpN8yVX7vVc8y8x.jpg"
                },
                {
                    "credit_id": "5e636b6d55c92600135e92ef",
                    "department": "Directing",
                    "gender": 2,
                    "id": 23285,
                    "job": "Second Unit Director",
                    "name": "Jeffrey J. Dashnaw",
                    "profile_path": "/5Sw0YV4U8Yg7BpN8yVX7vVc8y8x.jpg"
                },
                {
                    "credit_id": "5c62c8fb0e0a267dfd982064",
                    "department": "Directing",
                    "gender": 2,
                    "id": 36602,
                    "job": "Director",
                    "name": "Peter Berg",
                    "profile_path": "/1pR16H0zqRG49ns8cYykYpodgGF.jpg"
                },
                {
                    "credit_id": "5c62cab2c3a3684fa4d84c36",
                    "department": "Production",
                    "gender": 2,
                    "id": 36602,
                    "job": "Producer",
                    "name": "Peter Berg",
                    "profile_path": "/1pR16H0zqRG49ns8cYykYpodgGF.jpg"
                },
                {
                    "credit_id": "5e636c9a55c92600135e966e",
                    "department": "Sound",
                    "gender": 0,
                    "id": 113073,
                    "job": "Sound Re-Recording Mixer",
                    "name": "Paul Massey",
                    "profile_path": null
                },
                {
                    "credit_id": "5e635d35459ad6001a56a131",
                    "department": "Writing",
                    "gender": 0,
                    "id": 67976,
                    "job": "Screenplay",
                    "name": "Sean O'Keefe",
                    "profile_path": null
                },
                {
                    "credit_id": "5cc8c4789251415d3ef9479f",
                    "department": "Costume & Make-Up",
                    "gender": 2,
                    "id": 107372,
                    "job": "Makeup Department Head",
                    "name": "Howard Berger",
                    "profile_path": "/fz0Z8ia0Iaq4SS6NfVAcFIAeU2F.jpg"
                },
                {
                    "credit_id": "5c62cb1e0e0a266dac97a8f1",
                    "department": "Production",
                    "gender": 2,
                    "id": 84220,
                    "job": "Executive Producer",
                    "name": "Bill Bannerman",
                    "profile_path": null
                },
                {
                    "credit_id": "5e635fa4459ad6001156b92c",
                    "department": "Production",
                    "gender": 2,
                    "id": 84220,
                    "job": "Unit Production Manager",
                    "name": "Bill Bannerman",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636c5b357c000019358690",
                    "department": "Crew",
                    "gender": 2,
                    "id": 84220,
                    "job": "Aerial Coordinator",
                    "name": "Bill Bannerman",
                    "profile_path": null
                },
                {
                    "credit_id": "5e635e0e3e01ea0013e9a740",
                    "department": "Editing",
                    "gender": 2,
                    "id": 111456,
                    "job": "Editor",
                    "name": "Michael L. Sale",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636966357c00001335ed3a",
                    "department": "Art",
                    "gender": 2,
                    "id": 138379,
                    "job": "Art Direction",
                    "name": "Paul Richards",
                    "profile_path": null
                },
                {
                    "credit_id": "5e635dc5357c00001634ced4",
                    "department": "Production",
                    "gender": 2,
                    "id": 143894,
                    "job": "Executive Producer",
                    "name": "Eric Heffron",
                    "profile_path": null
                },
                {
                    "credit_id": "5e635fd5459ad6001856731c",
                    "department": "Directing",
                    "gender": 2,
                    "id": 143894,
                    "job": "First Assistant Director",
                    "name": "Eric Heffron",
                    "profile_path": null
                },
                {
                    "credit_id": "5c62cabfc3a3684f97d7cccc",
                    "department": "Production",
                    "gender": 2,
                    "id": 183044,
                    "job": "Producer",
                    "name": "Stephen Levinson",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636caa459ad60018568b6d",
                    "department": "Sound",
                    "gender": 2,
                    "id": 579405,
                    "job": "Sound Re-Recording Mixer",
                    "name": "Beau Borders",
                    "profile_path": null
                },
                {
                    "credit_id": "5e635d04459ad6001a56a0d0",
                    "department": "Costume & Make-Up",
                    "gender": 1,
                    "id": 1318814,
                    "job": "Costume Designer",
                    "name": "Virginia B. Johnson",
                    "profile_path": null
                },
                {
                    "credit_id": "5e6369fd3e01ea0011e99527",
                    "department": "Art",
                    "gender": 2,
                    "id": 1335066,
                    "job": "Property Master",
                    "name": "Andrew Petrotta",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636e5f357c000011357617",
                    "department": "Sound",
                    "gender": 0,
                    "id": 1338484,
                    "job": "Sound Designer",
                    "name": "Chris Terhune",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636d0f357c000019358849",
                    "department": "Sound",
                    "gender": 0,
                    "id": 1338484,
                    "job": "Supervising Sound Editor",
                    "name": "Chris Terhune",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636a9d357c00001634eb38",
                    "department": "Lighting",
                    "gender": 0,
                    "id": 1367672,
                    "job": "Gaffer",
                    "name": "Bob E. Krattiger",
                    "profile_path": null
                },
                {
                    "credit_id": "5e6369e4459ad6001156c56b",
                    "department": "Directing",
                    "gender": 1,
                    "id": 1367679,
                    "job": "Script Supervisor",
                    "name": "Kelly Cronin",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636d40357c00001335f290",
                    "department": "Sound",
                    "gender": 2,
                    "id": 1372207,
                    "job": "Foley Artist",
                    "name": "Leslie Bloome",
                    "profile_path": "/4JBCrVTUdrFYcZd2EsMUqUZsW8c.jpg"
                },
                {
                    "credit_id": "5e636cc03e01ea0011e99c29",
                    "department": "Sound",
                    "gender": 0,
                    "id": 1389133,
                    "job": "Supervising Sound Editor",
                    "name": "Will Files",
                    "profile_path": null
                },
                {
                    "credit_id": "5cc8c45a92514120dbf87b4b",
                    "department": "Costume & Make-Up",
                    "gender": 0,
                    "id": 1407009,
                    "job": "Hair Department Head",
                    "name": "Johnny Villanueva",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636b243e01ea0017e978ee",
                    "department": "Camera",
                    "gender": 2,
                    "id": 1420326,
                    "job": "Still Photographer",
                    "name": "Daniel C. McFadden",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636aba357c00001634eba9",
                    "department": "Camera",
                    "gender": 2,
                    "id": 1451256,
                    "job": "Key Grip",
                    "name": "Kurt Grossi",
                    "profile_path": null
                },
                {
                    "credit_id": "5e6368fb55c92600155cc027",
                    "department": "Crew",
                    "gender": 1,
                    "id": 1530870,
                    "job": "Post Production Supervisor",
                    "name": "Lisa Rodgers",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636ae8459ad6001a56d207",
                    "department": "Visual Effects",
                    "gender": 2,
                    "id": 1558086,
                    "job": "Special Effects Supervisor",
                    "name": "Matt Kutcher",
                    "profile_path": null
                },
                {
                    "credit_id": "5e635fea55c92600195c77fb",
                    "department": "Directing",
                    "gender": 0,
                    "id": 1560964,
                    "job": "Second Assistant Director",
                    "name": "Craig Comstock",
                    "profile_path": null
                },
                {
                    "credit_id": "5e635f92459ad600185672c4",
                    "department": "Production",
                    "gender": 0,
                    "id": 1608789,
                    "job": "Unit Production Manager",
                    "name": "Haley Sweet",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636a4c357c00001634ea99",
                    "department": "Sound",
                    "gender": 2,
                    "id": 1618896,
                    "job": "Boom Operator",
                    "name": "Luis Antonio Landrau",
                    "profile_path": null
                },
                {
                    "credit_id": "5c62cb2d0e0a267df1981017",
                    "department": "Production",
                    "gender": 0,
                    "id": 1691839,
                    "job": "Executive Producer",
                    "name": "John Logan Pierson",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636d573e01ea0013e9cd1c",
                    "department": "Sound",
                    "gender": 1,
                    "id": 1769801,
                    "job": "Foley Artist",
                    "name": "Joanna Fang",
                    "profile_path": "/suthDA38A8FTBl50jwZduP4eeBd.jpg"
                },
                {
                    "credit_id": "5e636d833e01ea0011e99e40",
                    "department": "Visual Effects",
                    "gender": 0,
                    "id": 1981979,
                    "job": "Visual Effects Supervisor",
                    "name": "Troy Moore",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636a1c357c000011356d47",
                    "department": "Art",
                    "gender": 0,
                    "id": 2195106,
                    "job": "Graphic Designer",
                    "name": "Trey Shaffer",
                    "profile_path": null
                },
                {
                    "credit_id": "5c62ca770e0a267de8981963",
                    "department": "Writing",
                    "gender": 0,
                    "id": 2240787,
                    "job": "Novel",
                    "name": "Ace Atkins",
                    "profile_path": null
                },
                {
                    "credit_id": "5e635d83357c000011355338",
                    "department": "Production",
                    "gender": 0,
                    "id": 2240788,
                    "job": "Producer",
                    "name": "Toby Ascher",
                    "profile_path": null
                },
                {
                    "credit_id": "5e63695155c92600135e8cc2",
                    "department": "Art",
                    "gender": 0,
                    "id": 2253925,
                    "job": "Set Designer",
                    "name": "Chris Biddle",
                    "profile_path": null
                },
                {
                    "credit_id": "5e6369a855c92600155cc202",
                    "department": "Art",
                    "gender": 0,
                    "id": 2293298,
                    "job": "Art Direction",
                    "name": "Bill King",
                    "profile_path": null
                },
                {
                    "credit_id": "5e636a663e01ea0017e97767",
                    "department": "Costume & Make-Up",
                    "gender": 0,
                    "id": 2559062,
                    "job": "Costume Supervisor",
                    "name": "Sloane True",
                    "profile_path": null
                }
            ]
        },
        "release_dates": {
            "results": [
                {
                    "iso_3166_1": "US",
                    "release_dates": [
                        {
                            "certification": "R",
                            "iso_639_1": "",
                            "note": "Netflix",
                            "release_date": "2020-03-06T00:00:00.000Z",
                            "type": 4
                        }
                    ]
                },
                {
                    "iso_3166_1": "AU",
                    "release_dates": [
                        {
                            "certification": "MA15+",
                            "iso_639_1": null,
                            "note": "Netflix",
                            "release_date": "2020-03-06T00:00:00.000Z",
                            "type": 4
                        }
                    ]
                },
                {
                    "iso_3166_1": "SG",
                    "release_dates": [
                        {
                            "certification": "M18",
                            "iso_639_1": "",
                            "note": "Netflix",
                            "release_date": "2020-03-06T00:00:00.000Z",
                            "type": 4
                        }
                    ]
                },
                {
                    "iso_3166_1": "FR",
                    "release_dates": [
                        {
                            "certification": "",
                            "iso_639_1": "",
                            "note": "Netflix",
                            "release_date": "2020-03-06T00:00:00.000Z",
                            "type": 4
                        }
                    ]
                },
                {
                    "iso_3166_1": "DE",
                    "release_dates": [
                        {
                            "certification": "16",
                            "iso_639_1": "",
                            "note": "Netflix",
                            "release_date": "2020-03-06T00:00:00.000Z",
                            "type": 4
                        }
                    ]
                },
                {
                    "iso_3166_1": "TR",
                    "release_dates": [
                        {
                            "certification": "18",
                            "iso_639_1": null,
                            "note": "Netflix",
                            "release_date": "2020-03-06T00:00:00.000Z",
                            "type": 4
                        }
                    ]
                },
                {
                    "iso_3166_1": "BR",
                    "release_dates": [
                        {
                            "certification": "16",
                            "iso_639_1": "",
                            "note": "Netflix",
                            "release_date": "2020-03-06T00:00:00.000Z",
                            "type": 4
                        }
                    ]
                },
                {
                    "iso_3166_1": "ES",
                    "release_dates": [
                        {
                            "certification": "",
                            "iso_639_1": "",
                            "note": "Netflix",
                            "release_date": "2020-03-06T00:00:00.000Z",
                            "type": 4
                        }
                    ]
                }
            ]
        }
    }
""".trimIndent()

val mockPersonDetails = """
    {
        "birthday": "1923-10-04",
        "known_for_department": "Acting",
        "deathday": "2008-04-05",
        "id": 10017,
        "name": "Charlton Heston",
        "images": {
            "profiles": [
                {
                    "iso_639_1": null,
                    "aspect_ratio": 0.66666666666667,
                    "vote_count": 0,
                    "height": 3000,
                    "vote_average": 0,
                    "file_path": "/cqAac7fwnXhaSkYCuLneUaLrABE.jpg",
                    "width": 2000
                },
                {
                    "iso_639_1": null,
                    "aspect_ratio": 0.66666666666667,
                    "vote_count": 1,
                    "height": 2100,
                    "vote_average": 5.312,
                    "file_path": "/31Z0NOxctaaLPk7Nq60eeQaDHJa.jpg",
                    "width": 1400
                },
                {
                    "iso_639_1": null,
                    "aspect_ratio": 0.66666666666667,
                    "vote_count": 1,
                    "height": 3000,
                    "vote_average": 5.312,
                    "file_path": "/yqmVz8wDDpb49SYdNNrmuEm9YL0.jpg",
                    "width": 2000
                },
                {
                    "iso_639_1": null,
                    "aspect_ratio": 0.66626506024096,
                    "vote_count": 0,
                    "height": 1660,
                    "vote_average": 0,
                    "file_path": "/h19IAwRMISiOKO8DblAK4gEshom.jpg",
                    "width": 1106
                }
            ]
        },
        "also_known_as": [],
        "gender": 2,
        "biography": "From Wikipedia, the free encyclopedia.\n\nCharlton Heston (October 4, 1923 – April 5, 2008) was an American actor of film, theatre and television. Heston is known for heroic roles in films such as The Ten Commandments, Planet of the Apes and Ben-Hur, for which he won the Academy Award for Best Actor. Heston was also known for his political activism. In the 1950s and 1960s he was one of a handful of Hollywood actors to speak openly against racism and was an active supporter of the Civil Rights Movement. Initially a moderate Democrat, he later supported conservative Republican policies and was president of the National Rifle Association from 1998 to 2003.",
        "popularity": 4.99,
        "place_of_birth": "Wilmette, Illinois, USA",
        "profile_path": "/yqmVz8wDDpb49SYdNNrmuEm9YL0.jpg",
        "adult": false,
        "imdb_id": "nm0000032",
        "homepage": null
    }
""".trimIndent()