package com.codingventures.movies.domain

import kotlinx.serialization.Serializable

@Serializable
enum class Certification(val value:String) {
    // US Certifications
    Rated_PG13("PG-13"),
    Rated_R("R"),
    Rated_G("G"),
    Rated_PG("PG"),
    Rated_NC17("NC-17"),
    Rated_NR("NR"),

    // CA Certifications
    Rated_18A("18A"),
    Rated_14A("14A"),
    Rated_A("A"),

    // AU Certifications
    Rated_E("E"),
    Rated_R18P("R18+"),
    Rated_RC("RC"),
    Rated_M("M"),
    Rated_MA15P("MA15+"),
    Rated_X18P("X18+"),

    // DE Certifications
    Rated_0("0"),
    Rated_6("6"),
    Rated_12("12"),
    Rated_16("16"),
    Rated_18("18"),

    // FR Certifications
    Rated_U("U"),
    Rated_10("10"),

    // NZ Certifications
    Rated_13("13"),
    Rated_15("15"),

    // IN Certifications
    Rated_UA("UA"),

    // GB Certifications
    Rated_R18("R18"),
    Rated_12A("12A"),

    // NL Certifications
    Rated_AL("AL"),
    Rated_9("9"),

    // BR Certifications
    Rated_L("L"),
    Rated_14("14"),

    // FI Certifications
    Rated_S("S"),
    Rated_K7("K-7"),
    Rated_K12("K-12"),
    Rated_K16("K-16"),
    Rated_K18("K-18"),
    Rated_KK("KK"),

    // BG Certifications
    Rated_B("B"),
    Rated_C("C"),
    Rated_D("D"),
    Rated_X("X"),

    // ES Certifications
    Rated_APTA("APTA"),
    Rated_7("7"),

    // PT Certifications
    Rated_Publicos("Públicos"),
    Rated_M3("M/3"),
    Rated_M6("M/6"),
    Rated_M12("M/12"),
    Rated_M14("M/14"),
    Rated_M16("M/16"),
    Rated_M18("M/18"),
    Rated_P("P"),

    // MY Certifications
    Rated_P13("P13"),
    Rated_18SG("18SG"),
    Rated_18SX("18SX"),
    Rated_18PA("18PA"),
    Rated_18PL("18PL"),

    //CA-QC Certifications
    Rated_13P("13+"),
    Rated_16P("16+"),
    Rated_18P("18+"),

    // SE Certifications
    Rated_Btl("Btl"),
    Rated_11("11"),

    // DK Certifications
    Rated_F("F"),

    // NO Certifications

    // HU Certifications
    Rated_KN("KN"),

    // LT Certifications
    Rated_V("V"),
    Rated_N7("N-7"),
    Rated_N13("N-13"),
    Rated_N16("N-16"),
    Rated_N18("N-18"),

    // RU Certifications
    Rated_0P("0+"),
    Rated_6P("6+"),
    Rated_12P("12+"),

    // PH Certifications
    Rated_R_13("R-13"),
    Rated_R_16("R-16"),
    Rated_R_18("R-18"),

    // IT Specifications
    Rated_T("T"),
    Rated_VM14("VM14"),
    Rated_VM18("VM18"),

    // Unspecified
    Rated_Unspecified("Unspecified");

    companion object {
        private val values = values()
        fun fromString(value: String): Certification {
            val certification = values.firstOrNull { it.value == value }
            return certification ?: Rated_Unspecified
        }
    }
}