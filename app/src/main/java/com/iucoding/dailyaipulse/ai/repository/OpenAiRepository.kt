package com.iucoding.dailyaipulse.ai.repository

import com.iucoding.dailyaipulse.ai.data.OpenAIApi
import com.iucoding.dailyaipulse.ai.model.ChatRequest
import com.iucoding.dailyaipulse.ai.model.ChatResponse
import javax.inject.Inject
import javax.inject.Named

class OpenAiRepository @Inject constructor(
    private val openAIApi: OpenAIApi,
	@Named("chatgpt_api_key") private val apiKey: String
){

	suspend fun askGpt(request: ChatRequest): String? {
		val response = openAIApi.createChatCompletion(apiKey = apiKey, request = request)
		return if (response.isSuccessful) {
			response.body()
				?.choices
				?.firstOrNull()
				?.message
				?.content
		} else {
			println("Error: ${response.errorBody()?.string()}")
			null
		}
	}
}
