package com.iucoding.dailyaipulse.di

import com.iucoding.dailyaipulse.BuildConfig
import com.iucoding.dailyaipulse.ai.data.GptApi
import com.iucoding.dailyaipulse.ai.interceptor.AuthInterceptor
import com.iucoding.dailyaipulse.articles.data.api.ArticleApi
import com.iucoding.dailyaipulse.sources.data.api.SourceApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	private const val BASE_URL = "https://newsapi.org/"

	@Qualifier
	@Retention(AnnotationRetention.BINARY)
	annotation class NewsRetrofit

	@Qualifier
	@Retention(AnnotationRetention.BINARY)
	annotation class OpenAiRetrofit

	@Qualifier
	@Retention(AnnotationRetention.BINARY)
	annotation class NewsOkHttpClient

	@Qualifier
	@Retention(AnnotationRetention.BINARY)
	annotation class OpenAiOkHttpClient

	@Qualifier
	@Retention(AnnotationRetention.BINARY)
	annotation class NewsApiKey

	@Qualifier
	@Retention(AnnotationRetention.BINARY)
	annotation class OpenAiApiKey

	@Provides
	@Singleton
	fun provideMoshi(): Moshi = Moshi.Builder()
		.add(KotlinJsonAdapterFactory())
		.build()

	@Provides
	@Singleton
	@NewsOkHttpClient
	fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
		.addInterceptor(
			HttpLoggingInterceptor().apply {
				level = HttpLoggingInterceptor.Level.BODY
			}
		)
		.build()

	@Provides
	@Singleton
	@NewsRetrofit
	fun provideRetrofit(
		@NewsOkHttpClient okHttpClient: OkHttpClient,
		moshi: Moshi
	): Retrofit = Retrofit.Builder()
		.baseUrl(BASE_URL)
		.client(okHttpClient)
		.addConverterFactory(MoshiConverterFactory.create(moshi))
		.build()

	@Provides
	@Singleton
	fun provideArticleApi(@NewsRetrofit retrofit: Retrofit): ArticleApi =
		retrofit.create(ArticleApi::class.java)

	@Provides
	@Singleton
	fun provideSourceApi(@NewsRetrofit retrofit: Retrofit): SourceApi =
		retrofit.create(SourceApi::class.java)

	@Provides
	@Singleton
	@NewsApiKey
	fun provideNewsApiKey(): String = BuildConfig.NEWS_API_KEY

	@Provides
	@Singleton
	@OpenAiOkHttpClient
	fun provideOpenAiOkHttpClient(@OpenAiApiKey apiKey: String): OkHttpClient {
		return OkHttpClient.Builder()
			.addInterceptor(AuthInterceptor(apiKey))
			.addInterceptor(
				HttpLoggingInterceptor().apply {
					level = HttpLoggingInterceptor.Level.BODY
				}
			)
			.build()
	}

	@Provides
	@Singleton
	@OpenAiRetrofit
	fun provideOpenAiRetrofit(
		@OpenAiOkHttpClient okHttpClient: OkHttpClient,
		moshi: Moshi
	): Retrofit {
		return Retrofit.Builder()
			.baseUrl("https://api.openai.com/")
			.client(okHttpClient)
			.addConverterFactory(MoshiConverterFactory.create(moshi))
			.build()
	}

	@Provides
	@Singleton
	fun provideOpenAiApi(@OpenAiRetrofit retrofit: Retrofit): GptApi {
		return retrofit.create(GptApi::class.java)
	}

	@Provides
	@Singleton
	@OpenAiApiKey
	fun provideChatGptApiKey(): String = BuildConfig.CHATGPT_API_KEY
}
