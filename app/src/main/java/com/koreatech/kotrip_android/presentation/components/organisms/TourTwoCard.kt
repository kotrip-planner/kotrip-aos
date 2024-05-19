package com.koreatech.kotrip_android.presentation.components.organisms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koreatech.kotrip_android.model.home.TourInfo

@Composable
fun TourTwoCard(
    selectedId: Int,
    homeTours: List<List<TourInfo>>,
    onSelectedIdChanged: (tourInfo: TourInfo, id: Int) -> Unit,
    selectedTours: List<TourInfo>,
    one: TourInfo? = null,
    onClickedOne: ((tourInfo: TourInfo) -> Unit)? = null,
    two: TourInfo? = null,
    onClickedTwo: ((tourInfo: TourInfo) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {

        if (one != null) {
            TourCard(
                tourInfo = one,
                selectedId = selectedId,
                onSelectedIdChanged = onSelectedIdChanged,
                selectedTours = selectedTours,
                homeTours = homeTours,
                onClick = { tourInfo ->
                    onClickedOne?.invoke(tourInfo) },
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .padding(end = 4.dp)
            )
        }

        if (two != null) {
            TourCard(
                tourInfo = two,
                selectedId = selectedId,
                selectedTours = selectedTours,
                onSelectedIdChanged = onSelectedIdChanged,
                homeTours = homeTours,
                onClick = { tourInfo ->
                    onClickedTwo?.invoke(tourInfo)
                          },
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(start = 4.dp)
            )
        }
    }

}

