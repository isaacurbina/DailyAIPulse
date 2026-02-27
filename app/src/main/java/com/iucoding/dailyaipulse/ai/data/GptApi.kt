package com.iucoding.dailyaipulse.ai.data

import com.iucoding.dailyaipulse.ai.model.ChatRequest
import com.iucoding.dailyaipulse.ai.model.ChatResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApi {

	@Headers("Content-Type: application/json")
	@POST("v1/chat/completions")
	suspend fun createChatCompletion(
		@Header("Authorization") apiKey: String,
		@Body request: ChatRequest
	): Response<ChatResponse>
}
