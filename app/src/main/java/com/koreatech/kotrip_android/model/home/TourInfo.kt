package com.koreatech.kotrip_android.model.home

data class TourInfo(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    var isSelected: Boolean,
) {
    fun matchQuery(query: String): Boolean {
        val combinations = listOf(
            "$title",
            "${title.first()}"
        )
        return combinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
