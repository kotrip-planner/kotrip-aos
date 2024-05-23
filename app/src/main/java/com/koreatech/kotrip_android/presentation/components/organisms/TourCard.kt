package com.koreatech.kotrip_android.presentation.components.organisms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.model.home.TourInfo

@Composable
fun TourCard(
    tourInfo: TourInfo,
    selectedId: Int,
    onSelectedIdChanged: (tourInfo: TourInfo, id: Int) -> Unit,
    homeTours: List<List<TourInfo>>,
    selectedTours: List<TourInfo>,
    onClick: (tourInfo: TourInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .background(color = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            onClick(tourInfo)
        }
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight(Alignment.CenterVertically)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .background(Color.White)
                .padding(8.dp)
        ) {
            Text(
                text = tourInfo.title,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(tourInfo.imageUrl)
                        .build(),
                    error = painterResource(id = R.drawable.img_empty_tour),
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .alpha(if (selectedTours.contains(tourInfo)) 0.5f else 1f)
                )
                if (selectedTours.contains(tourInfo)) {
                    Image(
                        painter = painterResource(id = R.drawable.img_check),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                    )
                }
                homeTours.forEachIndexed { index, tourInfos ->
                    if (tourInfo in tourInfos) {
                        Text(
                            text = "${index + 1} 일차",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun TourCardPreview() {
//    TourCard(
//        tourTestInfo = sampleTourTestInfo,
//        onClick = {}
//    )
}

