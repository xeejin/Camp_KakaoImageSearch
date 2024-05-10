package com.limheejin.kakaoimagesearch.repository

import com.limheejin.kakaoimagesearch.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetWorkClient {

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        // 통신이 안 될 때 디버깅을 위한 용도
        if (BuildConfig.DEBUG)
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            interceptor.level = HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .build()
    }

    private val dustRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client( // json 파일 convert
            createOkHttpClient()
        ).build()

    // retrofit의 create 명령을 이용해서 이미지 검색 api의 인스턴스를 생성
    val ImageNetWork: KaKaoSearchApi = dustRetrofit.create(KaKaoSearchApi::class.java)

}