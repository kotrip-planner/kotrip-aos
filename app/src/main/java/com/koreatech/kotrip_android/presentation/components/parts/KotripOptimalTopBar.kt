package com.koreatech.kotrip_android.presentation.components.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.presentation.theme.Black
import com.koreatech.kotrip_android.presentation.theme.Orange_FFCD4C

@Composable
fun KotripOptimalTopBar(
    cityTitle: String,
    scheduleContent: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = 20.dp, horizontal = 16.dp)
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = cityTitle,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp, color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Image(
                painter = painterResource(id = R.drawable.three_line),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .size(30.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = scheduleContent,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

