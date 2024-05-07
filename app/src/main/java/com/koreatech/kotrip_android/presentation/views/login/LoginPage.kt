package com.koreatech.kotrip_android.presentation.views.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.presentation.common.UiState
import com.koreatech.kotrip_android.presentation.components.organisms.LoginTitle
import com.koreatech.kotrip_android.presentation.components.parts.KakaoButton
import com.koreatech.kotrip_android.presentation.theme.Orange_FFCD4C

@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    onClickKakaoLogin: () -> Unit,
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.login_lottie))
    val lottieAnimatable = rememberLottieAnimatable()

    LaunchedEffect(composition) {
        lottieAnimatable.animate(
            composition = composition,
            clipSpec = LottieClipSpec.Frame(0, 1200),
            initialProgress = 0f
        )
    }

    Column {
        Box(
            modifier = modifier
                .fillMaxHeight(0.4f)
                .fillMaxWidth()
                .background(Orange_FFCD4C)
        ) {
            LottieAnimation(
                composition = composition,
                progress = { lottieAnimatable.progress },
                contentScale = ContentScale.FillHeight
            )
            LoginTitle(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.BottomCenter)
                    .offset(y = (40).dp)
            )
        }

        Box(
            modifier = modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            KakaoButton(
                modifier = modifier,
                onClick = onClickKakaoLogin
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        LoginPage() {

        }
    }
}
