package com.limheejin.kakaoimagesearch.api

import com.limheejin.kakaoimagesearch.model.ImageSearchResponse
import com.limheejin.kakaoimagesearch.util.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.Response

interface KakaoSearchApi {
    @GET("v2/search/image")
    suspend fun searchImage(
        @Header("Authorization") apiKey: String = Constants.AUTH_HEADER, // GET 요청에 필요한 주소
        @Query("query") query: String, // 검색을 원하는 질의어
        @Query("sort") sort: String, // 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
        @Query("page") page: Int, // 결과 페이지 번호, 1~50 사이의 값, 기본 값 1
        @Query("size") size: Int // 한 페이지에 보여질 문서 수, 1~80 사이의 값, 기본 값 80
    ): Response<ImageSearchResponse> // ImageSearchResponse 타입을 가지는 Response 클래스를 반환
}