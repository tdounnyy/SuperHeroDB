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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import felix.duan.superherodb.viewmodel.HeroListViewModel
import felix.duan.superherodb.viewmodel.HeroPagingListViewModel

@Composable
fun SearchPage(onItemClick: (id: String) -> Unit, modifier: Modifier = Modifier.Companion) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val viewModel: HeroPagingListViewModel = viewModel()
    val superHeroes by viewModel.superHeroes.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val hasMore by viewModel.hasMore.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            viewModel.refreshHeroes(searchQuery)
        } else {
            viewModel.clearData()
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
                if (it.isNotEmpty()) {
                    viewModel.refreshHeroes(it)
                } else {
                    viewModel.clearData()
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

        // 基于搜索结果显示内容
        when {
            error != null -> {
                Box(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Companion.Center)
                ) {
                    Text("Error: $error")
                }
            }

            superHeroes.isEmpty() && searchQuery.isNotEmpty() -> {
                Box(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Companion.Center)
                ) {
                    Text("No hero found for $searchQuery")
                }
            }

            superHeroes.isEmpty() -> {
                Box(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Companion.Center)
                ) {
                    Text("Search super heroes by name")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.Companion.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(superHeroes) { hero ->
                        HeroListCard(
                            hero = hero,
                            onClick = { onItemClick(hero.id) }
                        )
                    }

                    // show loading indicator
                    if (isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    } else if (hasMore && superHeroes.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                // load more trigger
                                LaunchedEffect(Unit) {
                                    viewModel.loadMoreHeroes()
                                }
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}