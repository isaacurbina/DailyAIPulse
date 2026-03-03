package com.iucoding.dailyaipulse.sources.data.repository

import com.iucoding.dailyaipulse.di.NetworkModule
import com.iucoding.dailyaipulse.sources.data.api.SourceApi
import com.iucoding.dailyaipulse.sources.data.api.dto.SourceDto
import com.iucoding.dailyaipulse.sources.data.model.SourceData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourceRepository @Inject constructor(
	private val api: SourceApi,
	@NetworkModule.NewsApiKey private val apiKey: String
) {

	suspend fun getSources(): List<SourceData> {
		val response = api.getSources(country = "us", category = "business", apiKey = apiKey)
		return response.sources?.map { it.toSourceData() } ?: emptyList()
	}
}

private fun SourceDto.toSourceData(): SourceData {
	return SourceData(
		id = id,
		name = name.orEmpty(),
		description = description
	)
}
