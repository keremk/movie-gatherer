package com.codingventures.movies.domain

import kotlinx.serialization.Serializable

@Serializable
enum class LanguageCode(val code: String) {
    fr("fr"),
    de("de"),
    en("en"),
    it("it"),
    es("es"),
    tr("tr"),
    ja("ja"),
    zh("zh"),
    ko("ko"),
    unspecified("unspecified");

    companion object {
        private val values = LanguageCode.values()
        fun fromString(value: String): LanguageCode {
            val language = values.firstOrNull { it.code == value }
            return language ?: unspecified
        }
    }
}