package com.koreatech.kotrip_android.presentation.components.organisms

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.koreatech.kotrip_android.model.home.TourInfo

@Composable
fun TourAddDialog(
    context: Context,
    tourInfo: TourInfo?,
    visible: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onButtonClick: (tourInfo: TourInfo) -> Unit,
) {

    if (visible) {
        CustomAlertDialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "${tourInfo?.title}\n를 추가하시겠습니까?")

                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { onButtonClick(tourInfo!!) },
                    colors = ButtonDefaults.buttonColors(Color.Black)
                ) {
                    Text(text = "추가하기", color = Color.White)
                }
            }

        }
    }
}