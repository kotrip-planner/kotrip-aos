package com.koreatech.kotrip_android.presentation.components.organisms

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CustomTextField(
    text: String,
    onTextChanged: (String) -> Unit,
) {
    TextField(
        value = text,
        onValueChange = onTextChanged,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        )
    )
}


@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(
        text = "1234",
        onTextChanged = {}
    )
}
