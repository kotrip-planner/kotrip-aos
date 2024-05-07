package com.koreatech.kotrip_android.presentation.sample

import com.koreatech.kotrip_android.data.model.response.OptimalRouteResponseDto
import com.koreatech.kotrip_android.data.model.response.OptimalScheduleResponseDto
import com.koreatech.kotrip_android.data.model.response.OptimalToursResponseDto
import com.koreatech.kotrip_android.model.history.TourHistoryInfo
import com.koreatech.kotrip_android.model.home.TourInfo


val tourHistories = listOf<TourHistoryInfo>(
    TourHistoryInfo(
        title = "해운대",
        imageUrl = "https://drive.google.com/uc?id=1vpsH_TRRBlTOdyGjMhCK0zG5TonSMf92"
    ),
    TourHistoryInfo(
        title = "해운대",
        imageUrl = "https://drive.google.com/uc?id=1vpsH_TRRBlTOdyGjMhCK0zG5TonSMf92"
    ),
    TourHistoryInfo(
        title = "해운대",
        imageUrl = "https://drive.google.com/uc?id=1vpsH_TRRBlTOdyGjMhCK0zG5TonSMf92"
    ),
    TourHistoryInfo(
        title = "해운대",
        imageUrl = "https://drive.google.com/uc?id=1vpsH_TRRBlTOdyGjMhCK0zG5TonSMf92"
    ),
    TourHistoryInfo(
        title = "해운대",
        imageUrl = "https://drive.google.com/uc?id=1vpsH_TRRBlTOdyGjMhCK0zG5TonSMf92"
    ),
)


val optimalTourInfo = TourInfo(
    id = 1,
    title = "해운대",
    imageUrl = "https://drive.google.com/uc?id=1vpsH_TRRBlTOdyGjMhCK0zG5TonSMf92",
    address = "asdfasdf",
    latitude = 0.0,
    longitude = 0.0,
    isSelected = false
)

val optimalTourInfoList: List<TourInfo> = listOf(optimalTourInfo, optimalTourInfo)

val optimalTourList = listOf(
    optimalTourInfoList,
    optimalTourInfoList,
    optimalTourInfoList,
    optimalTourInfoList
)


val optimalToursList = listOf(
    OptimalToursResponseDto(
        id = 452,
        title = "할매탕",
        imageUrl = "http://tong.visitkorea.or.kr/cms/resource/48/3083448_image2_1.jpg",
        latitude = 35.16,
        longitude = 129.16
    ),
    OptimalToursResponseDto(
        id = 453,
        title = "벡스코 상상체험 키즈월드",
        imageUrl = "http://tong.visitkorea.or.kr/cms/resource/30/3083430_image2_1.jpg",
        latitude = 35.17,
        longitude = 129.13
    ),
    OptimalToursResponseDto(
        id = 454,
        title = "용두산 자갈치 관광특구",
        imageUrl = "http://tong.visitkorea.or.kr/cms/resource/46/3049246_image2_1.JPG",
        latitude = 35.10,
        longitude = 129.03
    )
)

val optimalScheduleList = listOf(
    OptimalScheduleResponseDto(
        date = "2024-04-01",
        tours = optimalToursList
    ),
    OptimalScheduleResponseDto(
        date = "2024-04-02",
        tours = optimalToursList
    ),
    OptimalScheduleResponseDto(
        date = "2024-04-03",
        tours = optimalToursList
    )
)

//val optimalRoutes = OptimalRouteResponseDto(optimalScheduleList)
