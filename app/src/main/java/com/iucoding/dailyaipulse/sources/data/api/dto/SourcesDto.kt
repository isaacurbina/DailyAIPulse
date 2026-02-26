package com.iucoding.dailyaipulse.sources.data.api.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SourceDto(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null
)

@JsonClass(generateAdapter = true)
data class SourcesResponseDto(
    val status: String? = null,
    val sources: List<SourceDto>? = null
)
