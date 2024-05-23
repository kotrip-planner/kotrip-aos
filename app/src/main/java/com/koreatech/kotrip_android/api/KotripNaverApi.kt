package com.koreatech.kotrip_android.api

import com.koreatech.kotrip_android.Constants.NAVER_ID_PREFIX
import com.koreatech.kotrip_android.Constants.NAVER_SECRET_PREFIX
import com.koreatech.kotrip_android.data.model.response.NaverDriving5ResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KotripNaverApi {
    @GET("/map-direction/v1/driving")
    suspend fun getDriving5(
        @Header(NAVER_ID_PREFIX) clientId: String,
        @Header(NAVER_SECRET_PREFIX) clientSecret: String,
        @Query("start") start: String,
        @Query("goal") goal: String,
        @Query("waypoints") waypoints: String,
    ): NaverDriving5ResponseDto

    @GET("/map-direction-15/v1/driving")
    suspend fun getDriving15(
        @Header(NAVER_ID_PREFIX) clientId: String,
        @Header(NAVER_SECRET_PREFIX) clientSecret: String,
        @Query("start") start: String,
        @Query("goal") goal: String,
        @Query("waypoints") waypoints: String,
    ): NaverDriving5ResponseDto
}