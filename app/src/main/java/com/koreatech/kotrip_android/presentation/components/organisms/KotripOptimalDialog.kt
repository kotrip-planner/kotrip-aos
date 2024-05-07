package com.koreatech.kotrip_android.presentation.components.organisms

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.kotrip_android.presentation.theme.Orange_FF9800
import com.koreatech.kotrip_android.presentation.utils.showToast

@Composable
fun KotripOptimalDialog(
    context: Context,
    visible: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCreate: (title: String) -> Unit,
) {
    var textFieldValue by remember {
        mutableStateOf("")
    }

    if (visible) {
        CustomAlertDialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(
                        vertical = 12.dp,
                        horizontal = 6.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "최적의 일정",
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "나만의 여행 일정 제목을 선정해주세요.",
                    fontSize = 12.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    text = textFieldValue,
                    onTextChanged = { newText ->
                        textFieldValue = newText
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        onCreate(textFieldValue)
                    },
                    colors = ButtonDefaults.buttonColors(Orange_FF9800)
                ) {
                    Text(
                        text = "일정 생성하기",
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun KotripOptimalDialogPreview() {
    KotripOptimalDialog(
        context = LocalContext.current,
        visible = true,
        onDismissRequest = {},
        onCreate = {

        }
    )
}