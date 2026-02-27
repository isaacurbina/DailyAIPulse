package com.iucoding.dailyaipulse.ai.model

data class Message(
    val role: String,      // "system", "user", "assistant"
    val content: String
)
