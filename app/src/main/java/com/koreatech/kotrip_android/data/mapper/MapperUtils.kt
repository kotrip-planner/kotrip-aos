package com.koreatech.kotrip_android.data.mapper

import com.koreatech.kotrip_android.base.BaseResponse
import com.koreatech.kotrip_android.data.model.response.CityInfoResponseDto
import com.koreatech.kotrip_android.data.model.response.TourBaseInfoResponseDto
import com.koreatech.kotrip_android.data.model.response.TourInfoResponseDto
import com.koreatech.kotrip_android.model.home.TourInfo
import com.koreatech.kotrip_android.model.trip.CityInfo

fun BaseResponse<List<CityInfoResponseDto>>.toCityInfoList() = data.map { it.toCityInfo() }

fun CityInfoResponseDto.toCityInfo() = CityInfo(
    areaId, title, imageUrl, mapX, mapY
)


fun TourInfoResponseDto.toTourInfo() = TourInfo(
    id = id,
    title = title,
    imageUrl = imageUrl,
    address = addr1,
    latitude = mapY,
    longitude = mapX,
    isSelected = false
)