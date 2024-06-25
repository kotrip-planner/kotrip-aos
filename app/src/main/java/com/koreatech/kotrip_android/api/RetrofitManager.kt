package com.koreatech.kotrip_android.api

import com.koreatech.kotrip_android.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {
    const val BASE_URL = BuildConfig.base_url

    private val httpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .build()


    fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getAuthRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getNaverRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://naveropenapi.apigw.ntruss.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()

    inline fun <reified T> create(): T =
        getRetrofit().create<T>(T::class.java)

    inline fun <reified T> createAuth(): T =
        getAuthRetrofit().create(T::class.java)

    inline fun <reified T> createNaver(): T =
        getNaverRetrofit().create(T::class.java)

    object RetrofitServicePool {
        val kotripApi = RetrofitManager.create<KotripApi>()
        val kotripAuthApi = RetrofitManager.createAuth<KotripAuthApi>()
        val kotripNaverApi = RetrofitManager.createNaver<KotripNaverApi>()
    }
}

