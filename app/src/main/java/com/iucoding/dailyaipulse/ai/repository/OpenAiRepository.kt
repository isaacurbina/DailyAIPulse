package com.iucoding.dailyaipulse.ai.repository

import com.iucoding.dailyaipulse.ai.data.GptApi
import com.iucoding.dailyaipulse.ai.model.AiSummaryData
import com.iucoding.dailyaipulse.ai.model.ChatRequest
import com.iucoding.dailyaipulse.ai.model.Message
import com.iucoding.dailyaipulse.ai.model.ResponseFormat
import com.iucoding.dailyaipulse.di.NetworkModule
import com.squareup.moshi.Moshi
import timber.log.Timber
import javax.inject.Inject

class OpenAiRepository @Inject constructor(
	private val gptApi: GptApi,
	@NetworkModule.OpenAiApiKey private val apiKey: String,
	private val moshi: Moshi
) {

	suspend fun getArticleSummary(articleTitles: List<String>): AiSummaryData {
		if (apiKey.isBlank()) {
			return AiSummaryData(
				summary = "No API key provided. Please check your configuration.",
				investingSentiment = "N/A"
			)
		}

		val request = ChatRequest(
			model = "gpt-4o-mini",
			messages = listOf(
				Message(
					"system",
					"You are a financial assistant that summarizes news and analyzes investing sentiment. You must respond with valid JSON."
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
			temperature = 0.5,
			responseFormat = ResponseFormat(type = "json_object")
		)

		return try {
			val response = gptApi.createChatCompletion(request)
			if (response.isSuccessful) {
				val content = response.body()
					?.choices
					?.firstOrNull()
					?.message
					?.content

				if (content != null) {
					val adapter = moshi.adapter(AiSummaryData::class.java)
					adapter.fromJson(content) ?: AiSummaryData(
						summary = "Failed to parse AI response.",
						investingSentiment = "Unknown"
					)
				} else {
					AiSummaryData(
						summary = "Empty response from AI.",
						investingSentiment = "Unknown"
					)
				}
			} else {
				val errorBody = response.errorBody()?.string()
				Timber.e("OpenAI API Error: $errorBody")
				AiSummaryData(
					summary = "Error from AI Service: $errorBody",
					investingSentiment = "Error"
				)
			}
		} catch (e: Exception) {
			Timber.e(e, "Exception during OpenAI API call")
			AiSummaryData(
				summary = "Failed to connect to AI Service: ${e.message}",
				investingSentiment = "Error"
			)
		}
	}
}
