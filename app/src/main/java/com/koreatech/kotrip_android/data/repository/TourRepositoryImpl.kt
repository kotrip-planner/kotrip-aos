package com.koreatech.kotrip_android.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.map
import com.koreatech.kotrip_android.api.KotripApi
import com.koreatech.kotrip_android.data.mapper.toTourInfo
import com.koreatech.kotrip_android.data.model.response.TourInfoResponseDto
import com.koreatech.kotrip_android.model.home.TourInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class TourRepositoryImpl(
    private val kotripApi: KotripApi,
) {
    fun tours(cityId: Int, pageSize: Int): Flow<PagingData<TourInfo>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TourPagingSource(cityId, kotripApi) }
        ).flow
    }
}

class TourPagingSource(
    private val cityId: Int,
    private val kotripApi: KotripApi,
) : PagingSource<Int, TourInfo>() {
    override fun getRefreshKey(state: PagingState<Int, TourInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TourInfo> {
        val page = params.key ?: 0
        val data = kotripApi.getTour(cityId, page).data
        return LoadResult.Page(
            data = data.list.map { it.toTourInfo() },
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (data.list.isEmpty()) null else page + 1
        )
    }
}