package com.app.mycovidapp.di

import android.content.Context
import com.app.mycovidapp.data.remote.api.CovidApi
import com.app.mycovidapp.data.repository.CovidRepositoryImpl
import com.app.mycovidapp.domain.repository.CovidRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.api-ninjas.com/v1/"
    private const val API_KEY = "sDBYS8y8Ty+CICVabrzlYQ==h3Tgkj81YvTbfhNr"

    // For physical device, use your computer's IP: "http://192.168.x.x:3001/api/"
    private var retrofit: Retrofit? = null

    @Provides
    @Singleton
    fun provideRetrofit(
        @ApplicationContext context: Context,
    ): Retrofit {
        if (retrofit == null) {
            retrofit = createRetrofit(context)
        }
        return retrofit!!
    }

    @Provides
    @Singleton
    fun provideCovidApi(retrofit: Retrofit): CovidApi = retrofit.create(CovidApi::class.java)

    @Provides
    @Singleton
    fun provideCovidRepository(api: CovidApi): CovidRepository = CovidRepositoryImpl(api)

    private fun createRetrofit(context: Context): Retrofit {
        // Create API key interceptor
        val apiKeyInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .addHeader("X-Api-Key", API_KEY)
                .build()
            chain.proceed(newRequest)
        }

        // Create logging interceptor for debugging
        val loggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

        // Create main OkHttp client with API key interceptor
        val okHttpClient =
            OkHttpClient
                .Builder()
                .addInterceptor(apiKeyInterceptor) // Add API key to all requests
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
