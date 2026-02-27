package com.iucoding.dailyaipulse.ai.repository

import com.iucoding.dailyaipulse.ai.data.GptApi
import com.iucoding.dailyaipulse.ai.model.AiSummaryData
import com.iucoding.dailyaipulse.ai.model.ChatRequest
import com.iucoding.dailyaipulse.ai.model.Message
import javax.inject.Inject
import javax.inject.Named

class OpenAiRepository @Inject constructor(
	private val gptApi: GptApi,
	@Named("gpt_api_key") private val apiKey: String
) {

	suspend fun getArticleSummary(articleTitles: List<String>): AiSummaryData {
		if (apiKey.isBlank()) {
			return AiSummaryData(
				summary = "It was a good business day with no significant news",
				investingSentiment = "Good"
			)
		}

		val request = ChatRequest(
			model = "gpt-4o-mini",
			messages = listOf(
				Message(
					"system",
					"You are a financial assistant that summarizes news and analyzes investing sentiment."
				),
				Message(
					"user",
					"""
						Based on the following article titles, please do two things:

						1. Provide a short, high-level summary of the day.
						2. Indicate the overall investing sentiment and whether it's positive, negative or neutral.

						Format your response strictly as valid JSON like this:


						{
							"summary": "...",
							"investingSentiment": "..."
						}

						Here are the article titles:
						${articleTitles.joinToString()}
					""".trimIndent()
				)
			),
			temperature = 0.5
		)
		val response = gptApi.createChatCompletion(apiKey = apiKey, request = request)
		return if (response.isSuccessful) {
			val summary = response.body()
				?.choices
				?.firstOrNull()
				?.message
				?.content
			AiSummaryData(
				summary = summary,
				investingSentiment = "Good"
			)

		} else {
			println("Error: ${response.errorBody()?.string()}")
			AiSummaryData(
				summary = response.errorBody()?.string(),
				investingSentiment = "Good"
			)
		}
	}
}
