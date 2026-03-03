package com.iucoding.dailyaipulse.ai.model

import com.squareup.moshi.Json

data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double? = null,
    @Json(name = "max_tokens") val maxTokens: Int? = null,
    @Json(name = "response_format") val responseFormat: ResponseFormat? = null
)

data class ResponseFormat(
    val type: String // "text" or "json_object"
)
