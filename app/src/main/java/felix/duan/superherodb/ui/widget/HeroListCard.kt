package felix.duan.superherodb.ui.widget

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.error
import coil3.request.fallback
import coil3.request.placeholder
import felix.duan.superherodb.R
import felix.duan.superherodb.format
import felix.duan.superherodb.model.SuperHeroData

@Composable
fun HeroListCard(hero: SuperHeroData, onClick: () -> Unit, modifier: Modifier = Modifier.Companion) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            AsyncImage(
                ImageRequest.Builder(LocalContext.current)
                    .data(hero.images.sm)
                    // TODO: 2025/12/9 (duanyufei) replace with proper images
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .fallback(R.mipmap.ic_launcher)
                    .build(),
                contentDescription = hero.name,
                modifier = Modifier.Companion
                    .size(64.dp),
                onState = {
                    if (it is AsyncImagePainter.State.Error) {
                        Log.d("felixx", "image error: $it")
                    }
                }
            )

            // Column on the end with name and details
            Column(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = hero.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = hero.powerStats.format(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}