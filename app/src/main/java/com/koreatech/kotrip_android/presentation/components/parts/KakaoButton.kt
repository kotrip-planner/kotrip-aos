package com.koreatech.kotrip_android.presentation.components.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.presentation.theme.Black
import com.koreatech.kotrip_android.presentation.theme.Yellow_FEE500

@Composable
fun KakaoButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick
    ) {
        Row(
            modifier = modifier
                .wrapContentSize()
                .background(Yellow_FEE500, shape = RoundedCornerShape(12.dp)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.kakao_speech_bubble),
                contentDescription = null,
                modifier = modifier
                    .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                    .size(20.dp)

            )
            Spacer(modifier = modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.login_kakao),
                fontSize = 20.sp,
                fontStyle = FontStyle.Normal,
                color = Black,
                modifier = Modifier.padding(end = 20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KakaoButtonPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        KakaoButton(
            Modifier,
            onClick = {}
        )
    }
}