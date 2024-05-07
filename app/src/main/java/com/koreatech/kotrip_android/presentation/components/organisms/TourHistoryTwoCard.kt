package com.koreatech.kotrip_android.presentation.components.organisms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koreatech.kotrip_android.data.model.response.HistoryDataResponseDto
import com.koreatech.kotrip_android.model.history.TourHistoryInfo

@Composable
fun TourHistoryTwoCard(
    one: HistoryDataResponseDto? = null,
    onClickedOne: ((String) -> Unit)? = null,
    two: HistoryDataResponseDto? = null,
    onClickedTwo: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        if (one != null) {
            TourHistoryCard(
                tourHistoryInfo = one,
                onClick = { onClickedOne?.invoke(one.uuid) },
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .padding(end = 4.dp)
            )
        }

        if (two != null) {
            TourHistoryCard(
                tourHistoryInfo = two,
                onClick = { onClickedTwo?.invoke(two.uuid) },
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(start = 4.dp)
            )
        }
    }

}

@Preview
@Composable
fun TourHistoryTwoCardPreview() {
}

@Preview
@Composable
fun TourHistoryTwoCardTwoNullPreview() {
}