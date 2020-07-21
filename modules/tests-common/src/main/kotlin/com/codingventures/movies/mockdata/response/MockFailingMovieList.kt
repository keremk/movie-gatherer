package com.codingventures.movies.mockdata.response

val movieListFailing = """
    {
    "page": 3,
    "total_results": 3,
    "total_pages": 3,
    "results": [
        {
            "popularity": 176.614,
            "vote_count": 3845,
            "video": false,
            "poster_path": "/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg",
            "id": 55419704,
            "adult": false,
            "backdrop_path": "/t4z8OlOEzH7J1JRFUN3rcm6XHNL.jpg",
            "original_language": "en",
            "original_title": "Ad Astra",
            "genre_ids": [
                18,
                878
            ],
            "title": "Ad Astra",
            "vote_average": 6.1,
            "overview": "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.",
            "release_date": "2019-09-17"
        }]
    }    
""".trimIndent()