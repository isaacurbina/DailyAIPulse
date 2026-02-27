package com.iucoding.dailyaipulse.ai.model

data class Choice(
    val index: Int,
    val message: MessageResponse,
    val finish_reason: String?
)
