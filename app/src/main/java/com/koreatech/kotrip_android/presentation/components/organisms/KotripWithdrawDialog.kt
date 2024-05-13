package com.koreatech.kotrip_android.presentation.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.kotrip_android.presentation.theme.Orange_FF9800

@Composable
fun KotripWithdrawDialog(
    visible: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onComplete: () -> Unit,
) {
    if (visible) {
        CustomAlertDialog(
            onDismissRequest = onDismissRequest
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "정말 탈퇴하시겠습니까?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(30.dp))
                Row {
                    Button(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(Orange_FF9800)
                    ) {
                        Text(text = "취소")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = onComplete,
                        colors = ButtonDefaults.buttonColors(Orange_FF9800)
                    ) {
                        Text(text = "확인")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KotripWithdrawDialogPreview() {
    KotripWithdrawDialog(
        visible = true,
        onDismissRequest = {},
        onCancel = {},
        onComplete = {}
    )
}