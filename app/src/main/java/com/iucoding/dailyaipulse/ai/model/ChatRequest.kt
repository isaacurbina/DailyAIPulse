package com.iucoding.dailyaipulse.ai.model

data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double? = null,
    val max_tokens: Int? = null
)
