package com.koreatech.kotrip_android.presentation.components.organisms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.presentation.theme.Black

@Composable
fun LoginTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.bag),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = Modifier.height(100.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(id = R.string.kotrip),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Normal,
                color = Black,
                modifier = Modifier.align(Alignment.End)
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = stringResource(id = R.string.login_title),
                fontSize = 36.sp,
                fontWeight = FontWeight.W900,
                fontStyle = FontStyle.Normal,
                color = Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginTitlePreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        LoginTitle(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        )
    }
}