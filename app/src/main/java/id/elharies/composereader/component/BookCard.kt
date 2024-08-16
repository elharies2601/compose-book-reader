package id.elharies.composereader.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import id.elharies.composereader.model.Book
import id.elharies.composereader.utils.constans.ConstStatusReading
import id.elharies.composereader.utils.extension.buildImageReq

@Composable
fun BookCard(
    modifier: Modifier = Modifier,
    book: Book = Book(),
    onClickDetail: (Book) -> Unit = {}
) {
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics

    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp
    // .width(screenWidth.dp - (2 * spacing))

    Card(
        modifier = modifier
            .width(240.dp)
            .wrapContentHeight()
            .clickable { onClickDetail(book) }
            .padding(start = 4.dp, end = 4.dp, bottom = 4.dp),
        shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.width(screenWidth.dp - (spacing * 2))
        ) {
            TopSection(modifier, linkImage = book.linkImage, rating = book.rating)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = book.author,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            StatusItem(modifier = modifier.align(Alignment.End), status = book.statusReading)
        }
    }
}

@Composable
private fun StatusItem(
    modifier: Modifier = Modifier,
    status: String = ConstStatusReading.notStarted
) {
    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp),
        color = MaterialTheme.colorScheme.tertiary,
        modifier = modifier.heightIn(40.dp)
    ) {
        Text(
            text = status,
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 17.sp),
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onTertiary,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewStatusItem() {
    StatusItem()
}

@Composable
private fun TopSection(
    modifier: Modifier = Modifier,
    linkImage: String = "https://static.electronicsweekly.com/wp-content/uploads/2021/10/14135005/Flutter-Apprentice-book.png",
    rating: Double = 0.0,
    isFavorite: Boolean = false
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        // TODO: Need change to asyncimage
        val image = linkImage.buildImageReq(LocalContext.current)
        AsyncImage(
            model = image,
            contentDescription = "Image Book",
            modifier = Modifier
                .width(150.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(topStart = 16.dp)),
            contentScale = ContentScale.Crop
        )
//        val image = painterResource(id = R.drawable.temp_image_book)
//
//        Image(
//            painter = image,
//            contentDescription = "Image Temp",
//            modifier = Modifier
//                .width(150.dp)
//                .height(180.dp)
//                .aspectRatio(image.intrinsicSize.width / image.intrinsicSize.height)
//                .clip(RoundedCornerShape(topStart = 16.dp)),
//            contentScale = ContentScale.Crop
//        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite"
            )
            Spacer(modifier = modifier.height(16.dp))
            RatingReadBar(modifier = modifier, valueRating = rating)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTopSection() {
    TopSection()
}

@Preview(name = "BookCard", showBackground = true)
@Composable
private fun PreviewBookCard() {
    BookCard(book = Book(title = "Flutter in Action Flutter in Action Flutter in Action Flutter in Action Flutter in Action", author = "Eric Withmill"))
}