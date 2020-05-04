package com.codingventures.movies.domain

import kotlinx.serialization.Serializable

// https://en.wikipedia.org/wiki/ISO_3166-1

@Serializable
enum class CountryCode(val code: String) {
    FR("FR"),
    DE("DE"),
    IT("IT"),
    ES("ES"),
    TR("TR"),
    JA("JP"),
    KR("KR"),
    CN("CN"),
    US("US"),
    GB("GB"),
    Unspecified("Unspecified");

    companion object {
        private val values = CountryCode.values()
        fun fromString(value: String): CountryCode {
            val country = values.firstOrNull { it.code == value }
            return country ?: Unspecified
        }
    }
}