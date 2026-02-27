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
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	private const val BASE_URL = "https://newsapi.org/"

	@Provides
	@Singleton
	fun provideMoshi(): Moshi = Moshi.Builder()
		.add(KotlinJsonAdapterFactory())
		.build()

	@Provides
	@Singleton
	@Named("okhttp_client")
	fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
		.addInterceptor(
			HttpLoggingInterceptor().apply {
				level = HttpLoggingInterceptor.Level.BODY
			}
		)
		.build()

	@Provides
	@Singleton
	@Named("retrofit")
	fun provideRetrofit(
		@Named("okhttp_client") okHttpClient: OkHttpClient,
		moshi: Moshi
	): Retrofit = Retrofit.Builder()
		.baseUrl(BASE_URL)
		.client(okHttpClient)
		.addConverterFactory(MoshiConverterFactory.create(moshi))
		.build()

	@Provides
	@Singleton
	fun provideArticleApi(@Named("retrofit") retrofit: Retrofit): ArticleApi =
		retrofit.create(ArticleApi::class.java)

	@Provides
	@Singleton
	fun provideSourceApi(
		@Named("retrofit") retrofit: Retrofit): SourceApi =
		retrofit.create(SourceApi::class.java)

	@Provides
	@Singleton
	@Named("news_api_key")
	fun provideNewsApiKey(): String = BuildConfig.NEWS_API_KEY

	@Provides
	@Singleton
	@Named("openai_okhttp_client")
	fun provideOpenAiOkHttpClient(
		@Named("gpt_api_key") apiKey: String
	): OkHttpClient {
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
	@Named("openai_retrofit")
	fun provideOpenAiRetrofit(
		@Named("openai_okhttp_client") okHttpClient: OkHttpClient,
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
	fun provideOpenAiApi(
		@Named("openai_retrofit") retrofit: Retrofit
	): GptApi {
		return retrofit.create(GptApi::class.java)
	}

	@Provides
	@Singleton
	@Named("gpt_api_key")
	fun provideChatGptApiKey(): String = BuildConfig.CHATGPT_API_KEY
}
