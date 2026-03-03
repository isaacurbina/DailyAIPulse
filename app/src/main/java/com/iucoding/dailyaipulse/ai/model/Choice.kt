package com.iucoding.dailyaipulse.ai.model

import com.squareup.moshi.Json

data class Choice(
    val index: Int,
    val message: MessageResponse,
    @Json(name = "finish_reason") val finishReason: String?
)
