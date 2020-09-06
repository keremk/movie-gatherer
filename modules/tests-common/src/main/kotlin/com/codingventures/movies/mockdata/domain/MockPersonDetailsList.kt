package com.codingventures.movies.mockdata.domain

import com.codingventures.movies.domain.Gender
import com.codingventures.movies.domain.PersonDetails
import com.codingventures.movies.domain.Picture
import com.codingventures.movies.domain.Urn
import java.time.LocalDate

val mockPersonDetailsList = listOf(
    PersonDetails(
        personUrn = Urn("cv", "person", "117"),
        externalUrns = listOf(
            Urn("imdb", "person", "nm0000032")
        ),
        birthday = LocalDate.of(1923, 10, 4),
        deathday = LocalDate.of(2008, 4, 5),
        knownForDepartment = "Acting",
        name = "Charlton Heston",
        alsoKnownAs = emptyList(),
        gender = Gender.Male,
        biography = "Charlton Heston was an American actor of film, theatre and television.",
        popularity = 4.99,
        placeOfBirth = "Wilmette, Illinois, USA",
        profilePicturePath = "/yqmVz8wDDpb49SYdNNrmuEm9YL0.jpg",
        homepage = null,
        pictures = listOf(
            Picture(
                path = "/h19IAwRMISiOKO8DblAK4gEshom.jpg",
                width = 1106,
                height = 1660
            )
        )
    ),
    PersonDetails(
        personUrn = Urn("cv", "person", "31"),
        externalUrns = listOf(
            Urn("imdb", "person", "nm0000158")
        ),
        birthday = LocalDate.of(1956, 7, 9),
        deathday = null,
        knownForDepartment = "Acting",
        name = "Tom Hanks",
        alsoKnownAs = listOf(
            "Thomas Jeffrey Hanks",
            "Том Хэнкс",
            "توم هانكس",
            "トム・ハンクス"
        ),
        gender = Gender.Male,
        biography = "Thomas Jeffrey Hanks (born July 9, 1956) is an American actor and filmmaker. Known for both his comedic and dramatic roles",
        popularity = 28.915,
        placeOfBirth = "Concord, California, USA",
        profilePicturePath = "/dXlQ1ID6lnR5Av2Awn7OpUhqV8G.jpg",
        homepage = null,
        pictures = listOf(
            Picture(
                path = "/2dBObgFILIfNE6B1WVwK1sWLSKy.jpg",
                width = 1000,
                height = 1500
            ),
            Picture(
                path = "/mKr8PN8sn80LzVaZMg8L52kmakm.jpg",
                width = 625,
                height = 937
            )
        )
    ),
    PersonDetails(
        personUrn = Urn("cv", "person", "1245"),
        externalUrns = listOf(
            Urn("imdb", "person", "nm0424060")
        ),
        birthday = LocalDate.of(1984, 11, 22),
        deathday = null,
        knownForDepartment = "Acting",
        name = "Scarlett Johansson",
        alsoKnownAs = listOf(
            "Scarlett Johanssen",
            "Скарлетт Йоганссон",
            "Скарлетт Йоханссон",
            "Scarlett Ingrid Johansson",
            "스칼릿 조핸슨",
            "سكارليت جوهانسون",
            "史嘉蕾·喬韓森",
            "สการ์เลตต์ โจแฮนส์สัน",
            "スカーレット・ヨハンソン",
            "斯嘉丽·约翰逊",
            "스칼렛 요한슨",
            "Σκάρλετ Τζοχάνσον",
            "اسکارلت جوهانسون"
        ),
        gender = Gender.Female,
        biography = "Scarlett Johansson, born November 22, 1984, is an American actress, model and singer. She made her film debut in North (1994)",
        popularity = 34.256,
        placeOfBirth = "New York City, New York, USA",
        profilePicturePath = "/6NsMbJXRlDZuDzatN2akFdGuTvx.jpg",
        homepage = null,
        pictures = listOf(
            Picture(
                path = "/gxnBaEaCNShBiQXRNICsljONIAO.jpg",
                width = 1000,
                height = 1500
            ),
            Picture(
                path = "/ntpIIUsNSdh7AAg6LxVzIxTRoRZ.jpg",
                width = 1000,
                height = 1500
            ),
            Picture(
                path = "/rzjSlH8UIRtMCz5PLG4sRw1QBCT.jpg",
                width = 2000,
                height = 3000
            )
        )
    ),
    PersonDetails(
        personUrn = Urn("cv", "person", "1769"),
        externalUrns = listOf(
            Urn("imdb", "person", "nm0001068")
        ),
        birthday = LocalDate.of(1971, 5, 14),
        deathday = null,
        knownForDepartment = "Directing",
        name = "Sofia Coppola",
        alsoKnownAs = listOf(
            "Domino Coppola",
            "Sofia Carmina Coppola",
            "Domino",
            "Sofia",
            "София Коппола",
            "โซเฟีย คอปโปลา",
            "蘇菲亞·柯波拉",
            "ソフィア・コッポラ",
            "소피아 코폴라",
            "صوفيا كوبولا"
        ),
        gender = Gender.Female,
        biography = "Sofia Carmina Coppola (born May 14, 1971) is an American screenwriter, film director, actress, and producer.",
        popularity = 1.786,
        placeOfBirth = "New York City, New York, USA",
        profilePicturePath = "/dzHC2LxmarkBxWLhjp2DRa5oCev.jpg",
        homepage = null,
        pictures = listOf(
            Picture(
                path = "/fMnX9ZkbNol6SgIFIKncG07WQr4.jpg",
                width = 1000,
                height = 1500
            ),
            Picture(
                path = "/dzHC2LxmarkBxWLhjp2DRa5oCev.jpg",
                width = 453,
                height = 680
            )
        )
    )
)