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
import com.koreatech.kotrip_android.model.trip.CityInfo

@Composable
fun CityTwoCard(
    one: CityInfo? = null,
    onClickedOne: ((cityInfo: CityInfo) -> Unit)? = null,
    two: CityInfo? = null,
    onClickedTwo: ((cityInfo: CityInfo) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {

        if (one != null) {
            CityCard(
                cityInfo = one,
                onClick = { onClickedOne?.invoke(it) },
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .padding(end = 4.dp)
            )
        }

        if (two != null) {
            CityCard(
                cityInfo = two,
                onClick = { onClickedTwo?.invoke(it) },
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(start = 4.dp)
            )
        }
    }

}
