package id.elharies.composereader.component

import android.util.Log
import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    enabled: Boolean = true,
    starsColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    onValueChanged: (Double) -> Unit = {}
) {

    var ratingState by remember {
        mutableDoubleStateOf(0.0)
    }

    ratingState = rating

    var selected by remember {
        mutableStateOf(false)
    }

    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        animationSpec = spring(Spring.DampingRatioLowBouncy),
        label = "sizing"
    )

    Row(modifier = modifier) {
        for (i in 1..stars) {
            Icon(
                imageVector = if (i <= ratingState.toInt()) Icons.Default.Star else Icons.Default.StarOutline,
                contentDescription = "rating",
                tint = starsColor,
                modifier = modifier
                    .size(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onValueChanged(i.toDouble())
                                ratingState = i.toDouble()
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        enabled
                    }
            )
        }
    }
}

@Preview(name = "RatingBar", showBackground = true)
@Composable
private fun PreviewRatingBar() {
    RatingBar(rating = 3.5)
}