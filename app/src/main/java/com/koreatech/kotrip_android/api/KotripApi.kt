package com.koreatech.kotrip_android.api

import com.koreatech.kotrip_android.base.BaseResponse
import com.koreatech.kotrip_android.data.model.request.LoginRequestDto
import com.koreatech.kotrip_android.data.model.request.RefreshRequestDto
import com.koreatech.kotrip_android.data.model.response.AuthResponseDto
import com.koreatech.kotrip_android.data.model.response.CityInfoResponseDto
import com.koreatech.kotrip_android.data.model.response.TourBaseInfoResponseDto
import com.koreatech.kotrip_android.data.model.response.TourInfoResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface KotripApi {
    /**
     * 로그인 api 호출
     */
    @POST("api/login")
    suspend fun login(
        @Body loginRequestDto: LoginRequestDto,
    ): BaseResponse<AuthResponseDto>

    /**
     * 리프래스 토큰
     */
    @POST("api/reissue")
    suspend fun refreshToken(
        @Body refreshRequestDto: RefreshRequestDto,
    ): BaseResponse<AuthResponseDto>

    /**
     * 26개 도시 호출
     */
    @GET("city")
    suspend fun getCity(): BaseResponse<List<CityInfoResponseDto>>

    /**
     * 선정된 도시의 관광지 호출
     */
    @GET("tour")
    suspend fun getTour(
        @Query("cityId") cityId: Int,
        @Query("page") page: Int
    ): BaseResponse<TourBaseInfoResponseDto>
}
