package id.elharies.composereader.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import id.elharies.composereader.R
import id.elharies.composereader.utils.extension.buildImageReq

@Composable
fun RoundedImage(
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    linkImage: String = ""
) {
    AsyncImage(
        model = linkImage.buildImageReq(LocalContext.current),
        contentDescription = "Rounded Image",
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Preview(name = "RoundedImage", showBackground = true)
@Composable
private fun PreviewRoundedImage() {
    RoundedImage()
}