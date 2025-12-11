package felix.duan.superherodb.ui.widget

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import felix.duan.superherodb.model.SuperHeroData
import felix.duan.superherodb.repo.SuperHeroRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HeroListPage(keyword: String? = null, onItemClick: (id: String) -> Unit, modifier: Modifier = Modifier.Companion) {
    var superHeroes by remember { mutableStateOf<List<SuperHeroData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            CoroutineScope(Dispatchers.Main).launch {
                isLoading = true
                error = null
                val heroes = if (keyword.isNullOrEmpty()) {
                    SuperHeroRepo.getAll()
                } else {
                    SuperHeroRepo.searchLocal(keyword)
                }
                superHeroes = heroes
                isLoading = false
            }
        } catch (e: Exception) {
            error = e.message
            isLoading = false
            Log.e("felixx", "Error loading superheroes", e)
        }
    }

    when {
        isLoading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Companion.Center)
            ) {
                Text(
                    text = "Loading...",
                    modifier = Modifier.Companion.padding(16.dp)
                )
            }
        }

        error != null -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Companion.Center)
            ) {
                Text(
                    text = "Error: $error",
                    modifier = Modifier.Companion.padding(16.dp),
                    textAlign = TextAlign.Companion.Center
                )
            }
        }

        superHeroes.isEmpty() -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Companion.Center)
            ) {
                Text(
                    text = "No superheroes found. Try searching for some!",
                    modifier = Modifier.Companion.padding(16.dp),
                    textAlign = TextAlign.Companion.Center
                )
            }
        }


        else -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.Companion.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier.Companion.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(superHeroes) { hero ->
                        HeroListCard(hero = hero, onClick = { onItemClick(hero.id) })
                    }
                }
            }
        }
    }
}