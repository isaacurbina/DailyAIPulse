package com.iucoding.dailyaipulse.ai.model

data class ChatResponse(
    val id: String,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage?
)
