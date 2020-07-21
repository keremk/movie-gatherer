package com.codingventures.movies.ingester.config

import com.sksamuel.hoplite.ConfigLoader

data class PipelineConfigProvider(
    val maxNoPages: Int = Int.MAX_VALUE,
    val maxNoCrewRequests: Int = Int.MAX_VALUE,
    val maxNoCastRequests: Int = Int.MAX_VALUE
) {
    companion object {
        fun default(): PipelineConfigProvider {
            return ConfigLoader().loadConfigOrThrow("/pipeline.yaml")
        }
    }
}