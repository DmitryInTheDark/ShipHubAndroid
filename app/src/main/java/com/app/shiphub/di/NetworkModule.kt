package com.app.shiphub.di

import com.app.data.UserRepository
import com.app.data.api.AuthApi
import com.app.data.api.ClaimsApi
import com.app.shiphub.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    @Named("ClientWithoutToken")
    fun provideOkHttpClientWithoutToken(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClientWithBaseToken(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        userRepository: UserRepository
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request()
                val builder = request.newBuilder()
                if (userRepository.getJwt().isNotEmpty()) {
                    builder.addHeader("Authorization", "Bearer ${userRepository.getJwt()}")
                }
                chain.proceed(builder.build())
            }
            .build()
    }

    @Provides
    @Singleton
    @Named("RetrofitWithoutToken")
    fun provideRetrofitWithoutToken(
        gsonConverterFactory: GsonConverterFactory,
        @Named("ClientWithoutToken") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitWithToken(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        @Named("RetrofitWithoutToken") retrofit: Retrofit
    ) = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideClaimsApi(retrofit: Retrofit): ClaimsApi = retrofit.create(ClaimsApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): com.app.data.api.UserApi = retrofit.create(com.app.data.api.UserApi::class.java)
}
