package com.app.mycovidapp.di

import android.content.Context
import com.app.mycovidapp.data.remote.api.AppointmentApi
import com.app.nefrovida.data.remote.api.AuthApiService
import com.app.nefrovida.data.remote.api.RefreshAuthenticator
import com.app.nefrovida.data.remote.api.ReportsApi
import com.app.nefrovida.data.repository.AppointmentRepositoryImpl
import com.app.nefrovida.domain.repository.AppointmentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "\"https://api.api-ninjas.com/v1"
//    curl -X GET "https://api.api-ninjas.com/v1/covid19?country=United%20States" \
    -H "X-Api-Key: sDBYS8y8Ty+CICVabrzlYQ==h3Tgkj81YvTbfhNr"

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
    fun provideAppointmentApi(retrofit: Retrofit): AppointmentApi = retrofit.create(AppointmentApi::class.java)

    @Provides
    @Singleton
    fun provideReportsApi(retrofit: Retrofit): ReportsApi = retrofit.create(ReportsApi::class.java)

    @Provides
    @Singleton
    fun provideAppointmentRepository(api: AppointmentApi): AppointmentRepository = AppointmentRepositoryImpl(api)

    private fun createRetrofit(context: Context): Retrofit {
        // Create refresh client without authenticator to avoid infinite loops
        val refreshClient =
            OkHttpClient
                .Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

        // Create logging interceptor for debugging
        val loggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

        // Create main OkHttp client with cookie jar and authenticator
        val okHttpClient =
            OkHttpClient
                .Builder()
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

    fun provideAuthApiService(context: Context): AuthApiService = provideRetrofit(context).create(AuthApiService::class.java)
}
