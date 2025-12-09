package felix.duan.superherodb.ui.widget

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
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
import felix.duan.superherodb.repo.SuperHeroRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HeroListCard(hero: SuperHeroData, modifier: Modifier = Modifier.Companion) {
    Card(
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
                    .data(hero.image.url)
                    // TODO: 2025/12/9 (duanyufei) replace with proper images
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .fallback(R.mipmap.ic_launcher)
                    .build(),
                // TODO: 2025/12/9 (duanyufei) ERROR 403
                // model = hero.image.url,
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
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                )
                Text(
                    text = hero.powerStats.format(),
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun HomePage(modifier: Modifier = Modifier.Companion) {
    var superHeroes by remember { mutableStateOf<List<SuperHeroData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            isLoading = true
            error = null
            val heroes = SuperHeroRepo.getAllLocally()
            superHeroes = heroes
            isLoading = false
        } catch (e: Exception) {
            error = e.message
            isLoading = false
            Log.e("felixx", "Error loading superheroes", e)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        when {
            isLoading -> {
                Text(
                    text = "Loading...",
                    modifier = Modifier.Companion.padding(16.dp)
                )
            }

            error != null -> {
                Text(
                    text = "Error: $error",
                    modifier = Modifier.Companion.padding(16.dp),
                    textAlign = TextAlign.Companion.Center
                )
            }

            superHeroes.isEmpty() -> {
                Text(
                    text = "No superheroes found. Try searching for some!",
                    modifier = Modifier.Companion.padding(16.dp),
                    textAlign = TextAlign.Companion.Center
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.Companion.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(superHeroes) { hero ->
                        HeroListCard(hero = hero)
                    }
                }
            }
        }
    }
}

@Composable
fun DebugPage(modifier: Modifier = Modifier.Companion) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Text("Debug Page 🐞")
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    SuperHeroRepo.get("69").let {
                        Log.d("felixx", "getById: $it")
                    }
                }
            }) {
            Text("getById")
        }
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    SuperHeroRepo.getAllLocally().let { list ->
                        list.forEach {
                            Log.d("felixx", "getAllLocally: $it")
                        }
                    }
                }
            }) {
            Text("getAllLocally")
        }
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    SuperHeroRepo.search("batman").let {
                        Log.d("felixx", "searchByName: $it")
                    }
                }
            }) {
            Text("searchByName")
        }
    }
}