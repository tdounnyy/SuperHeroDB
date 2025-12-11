package felix.duan.superherodb.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import felix.duan.superherodb.model.SuperHeroData
import felix.duan.superherodb.repo.SuperHeroRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SearchPage(onItemClick: (id: String) -> Unit, modifier: Modifier = Modifier.Companion) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<SuperHeroData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            isLoading = true
            val results = SuperHeroRepo.searchLocal(searchQuery)
            searchResults = results
            isLoading = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                isLoading = true
                // TODO: 2025/12/10 (duanyufei) debounce
                CoroutineScope(Dispatchers.Main).launch {
                    val results = SuperHeroRepo.searchLocal(searchQuery)
                    searchResults = results
                    isLoading = false
                }
            },
            label = { Text("Search for superheroes") },
            placeholder = { Text("by name") },
            shape = RoundedCornerShape(20.dp),
            singleLine = true,
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp)
        )

        // HeroListPage based on search results
        if (isLoading) {
            Box(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Companion.Center)
            ) {
                Text("Searching...")
            }
        } else if (searchResults.isEmpty() && searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Companion.Center)
            ) {
                Text("No heroes found for \"$searchQuery\"")
            }
        } else if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Companion.Center)
            ) {
                Text("Search for superheroes by name")
            }
        } else {
            LazyColumn(
                modifier = Modifier.Companion.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(searchResults) { hero ->
                    HeroListCard(
                        hero = hero,
                        onClick = { onItemClick(hero.id) }
                    )
                }
            }
        }
    }
}