package com.koreatech.kotrip_android.presentation.components.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.presentation.theme.Orange_FFCD4C

@Composable
fun KotripButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.width(100.dp),
        colors = ButtonDefaults.buttonColors(Orange_FFCD4C),
        shape = RoundedCornerShape(10.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.airplane),
            contentDescription = null,
            modifier = Modifier.padding(start = 20.dp).size(30.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun KotripButtonPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        KotripButton({})
    }
}