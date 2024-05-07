package com.koreatech.kotrip_android.presentation.components.organisms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.presentation.theme.Black
import com.koreatech.kotrip_android.presentation.theme.Gray

@Composable
fun OnboardCard(
    title: String,
    subTitle: String = "",
    warningText: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(Gray, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = subTitle,
                fontStyle = FontStyle.Normal,
                fontSize = 18.sp,
                color = Black
            )
            Image(
                painter = painterResource(id = R.drawable.bag),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = title,
            fontStyle = FontStyle.Normal,
            fontSize = 28.sp,
            color = Black,
            fontWeight = FontWeight.Bold
        )
        if (warningText != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = warningText,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                color = Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardCardPreview() {
    OnboardCard(
        title = "aaaaa",
        subTitle = "ababsd",
        warningText = "asdfasdf",
        modifier = Modifier.padding(16.dp)
    )
}