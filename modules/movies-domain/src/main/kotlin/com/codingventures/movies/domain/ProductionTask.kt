package com.codingventures.movies.domain

data class ProductionTask(
    val movieIndustryData: MovieIndustryData?,
    val additionalTasks: List<FetchTask>
)
