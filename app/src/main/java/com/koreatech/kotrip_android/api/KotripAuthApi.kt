package com.koreatech.kotrip_android.api

import com.google.gson.annotations.SerializedName
import com.koreatech.kotrip_android.Constants.AUTHORIZATION_PREFIX
import com.koreatech.kotrip_android.base.BaseResponse
import com.koreatech.kotrip_android.data.model.request.home.DayGenerateScheduleRequestDto
import com.koreatech.kotrip_android.data.model.request.home.GenerateScheduleRequestDto
import com.koreatech.kotrip_android.data.model.response.HistoryResponseDto
import com.koreatech.kotrip_android.data.model.response.HotelBasicResponseDto
import com.koreatech.kotrip_android.data.model.response.HotelResponseDto
import com.koreatech.kotrip_android.data.model.response.UUIDResponseDto
import com.koreatech.kotrip_android.data.model.response.OptimalRouteResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface KotripAuthApi {
    /**
     * @deprecated
     * 최적의 경로 일정 저장
     * use algorithm : TSP
     */
    @POST("api/schedules")
    suspend fun postTSPOptimalRoute(
        @Header(AUTHORIZATION_PREFIX) token: String,
        @Body generateScheduleRequestDto: GenerateScheduleRequestDto,
    ): BaseResponse<UUIDResponseDto>


    /**
     * 최적의 경로 일정 저장
     * TSP + Greedy
     */
    @POST("api/schedule")
    suspend fun postOptimalRoutes(
        @Header(AUTHORIZATION_PREFIX) token: String,
        @Body generateScheduleRequestDto: GenerateScheduleRequestDto,
    ): BaseResponse<UUIDResponseDto>

    /**
     * 당일치기 경로 저장
     */
    @POST("api/day")
    suspend fun postOptimalRouteDay(
        @Header(AUTHORIZATION_PREFIX) token: String,
        @Body generateScheduleRequestDto: DayGenerateScheduleRequestDto,
    ): BaseResponse<UUIDResponseDto>


    /**
     * UUID 일정 불러오기
     */
    @POST("api/history")
    suspend fun getSchedule(
        @Header(AUTHORIZATION_PREFIX) token: String,
        @Body scheduleRequest: ScheduleRequest,
    ): BaseResponse<OptimalRouteResponseDto>

    /**
     * 히스토리 불러오기
     */
    @GET("api/history")
    suspend fun getHistory(
        @Header(AUTHORIZATION_PREFIX) token: String,
    ): BaseResponse<HistoryResponseDto>

    /**
     * 호텔 가져오기
     */
    @GET("api/hotelSearch")
    suspend fun getHotel(
        @Header(AUTHORIZATION_PREFIX) token: String,
        @Query("mapAX") axLongitude: Double, // 위도
        @Query("mapAY") ayLatitude: Double, // 경도
        @Query("mapBX") bxLongitude: Double,
        @Query("mapBY") byLatitude: Double,
    ): BaseResponse<HotelBasicResponseDto>

    /**
     * 회원 탈퇴
     */
    @DELETE("api/withdrawal")
    suspend fun withdraw(
        @Header(AUTHORIZATION_PREFIX) token: String
    ): BaseResponse<Unit>
}

data class ScheduleRequest(
    @SerializedName("uuid")
    val uuid: String,
)