package com.koreatech.kotrip_android.presentation.components.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.presentation.theme.Orange_FFCD4C

@Composable
fun KotripScheduleButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .border(
                    width = 1.dp,
                    color = Orange_FFCD4C,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 5.dp, vertical = 2.dp)
        ) {
            Image(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(15.dp)
            )

            Spacer(modifier = Modifier.width(2.dp))

            Text(
                text = stringResource(id = R.string.home_add_schedule),
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun KotripScheduleButtonPreview() {
    KotripScheduleButton() {

    }
}