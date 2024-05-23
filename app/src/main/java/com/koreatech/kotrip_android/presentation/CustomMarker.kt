package com.koreatech.kotrip_android.presentation

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import coil.compose.AsyncImage
import coil.request.ImageRequest

class CustomMarker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val imageUrl: String
) : AbstractComposeView(context, attrs, defStyleAttr) {
    @Composable
    override fun Content() {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .build(),
                contentDescription = null
            )
        }
    }

//    fun loadImage(url: String) {
//        Timber.e("aaa 어디야 3")
//        Glide.with(context)
//            .load(url)
//            .into(binding.ivMarker)
//    }
}