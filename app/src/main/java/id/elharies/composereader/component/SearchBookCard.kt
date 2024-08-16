package id.elharies.composereader.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import id.elharies.composereader.model.Book

@Composable
fun SearchBookCard(
    modifier: Modifier = Modifier,
    book: Book = Book(),
    onClick: (Book) -> Unit = {}
) {
    Card(
        modifier = modifier
            .clickable { onClick(book) }
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(7.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(4.dp)
        ) {
            val painter =
                ImageRequest.Builder(LocalContext.current).crossfade(true).data(book.linkImage).build()
            AsyncImage(
                model = painter, contentDescription = "Image Card", modifier = Modifier
                    .width(80.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 17.sp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontStyle = FontStyle.Italic,
                        fontSize = 14.sp
                    ),
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.date,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@Preview(name = "SearchBookCard")
@Composable
private fun PreviewSearchBookCard() {
    val temp =
        "https://static.electronicsweekly.com/wp-content/uploads/2021/10/14135005/Flutter-Apprentice-book.png"
    SearchBookCard(book = Book(title = "Book 1 Book 1 Book 1 Book 1", author = "Autohr 1", date = "2020-09-29", linkImage = temp))
}