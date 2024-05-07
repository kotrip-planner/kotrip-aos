package com.koreatech.kotrip_android.presentation.components.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.presentation.theme.Orange_FFCD4C

@Composable
fun KotripPointCard(
    pointText: String,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        border = BorderStroke(width = 1.dp, color = Orange_FFCD4C),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        colors = CardDefaults.cardColors(Color.White),
        modifier = modifier.alpha(
            if (visible) 1f else 0f
        )
    ) {
        Text(
            text = pointText,
            fontSize = 8.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KotripStartPointCardPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        KotripPointCard(
            pointText = stringResource(id = R.string.home_start_point),
            visible = true
        )
    }
}