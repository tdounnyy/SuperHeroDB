package felix.duan.superherodb.ui.widget

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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


@Composable
fun HeroProfilePage(id: String, modifier: Modifier = Modifier.Companion) {

    var hero by remember { mutableStateOf<SuperHeroData?>(null) }
    var isLoading by remember { mutableStateOf<Boolean>(true) }
    var isError by remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            SuperHeroRepo.get(id)?.let {
                hero = it
                isError = false
            } ?: run {
                isError = true
            }
            isLoading = false
        }
    }
    if (isLoading) {
        // Centered loading text
        Box(
            modifier = modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Companion.Center)
        ) {
            Text(
                text = "Loading... #$id",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else if (isError) {
        // Centered error text
        Box(
            modifier = modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Companion.Center)
        ) {
            Text(
                text = "No hero found #$id",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Hero image and basic info row
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {

                hero?.let { hero ->
                    Column {
                        Row {
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
                                    .size(120.dp)
                                    .padding(end = 16.dp),
                                onState = {
                                    if (it is AsyncImagePainter.State.Error) {
                                        Log.d("felixx", "image error: $it")
                                    }
                                }
                            )

                            Column {
                                Text(
                                    text = hero.name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.Companion.padding(top = 16.dp)
                                )
                                Text(
                                    text = hero.powerStats.format(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.Companion.padding(top = 4.dp)
                                )
                            }
                        }

                        // Biography section
                        SectionTitle("Biography")
                        BiographySection(hero.biography)

                        // Appearance section
                        SectionTitle("Appearance")
                        AppearanceSection(hero.appearance)

                        // Work section
                        SectionTitle("Work")
                        WorkSection(hero.work)

                        // Connections section
                        SectionTitle("Connections")
                        ConnectionsSection(hero.connections)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.Companion
            .padding(top = 24.dp, bottom = 8.dp)
    )
}

@Composable
private fun BiographySection(biography: SuperHeroData.Biography) {
    Column {
        InfoItem("Full Name", biography.fullName)
        InfoItem("Alter Egos", biography.alterEgos)
        InfoItem("Aliases", biography.aliases.joinToString(", "))
        InfoItem("Place of Birth", biography.placeOfBirth)
        InfoItem("First Appearance", biography.firstAppearance)
        InfoItem("Publisher", biography.publisher ?: "unknown")
        InfoItem("Alignment", biography.alignment)
    }
}

@Composable
private fun AppearanceSection(appearance: SuperHeroData.Appearance) {
    Column {
        InfoItem("Gender", appearance.gender)
        InfoItem("Race", appearance.race ?: "unknown")
        InfoItem("Height", appearance.height.joinToString(", "))
        InfoItem("Weight", appearance.weight.joinToString(", "))
        InfoItem("Eye Color", appearance.eyeColor)
        InfoItem("Hair Color", appearance.hairColor)
    }
}

@Composable
private fun WorkSection(work: SuperHeroData.Work) {
    Column {
        InfoItem("Occupation", work.occupation)
        InfoItem("Base", work.base)
    }
}

@Composable
private fun ConnectionsSection(connections: SuperHeroData.Connections) {
    Column {
        InfoItem("Group Affiliation", connections.groupAffiliation)
        InfoItem("Relatives", connections.relatives)
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    if (value.isNotBlank()) {
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = "$label: ",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.Companion.padding(start = 4.dp)
            )
        }
    }
}

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
                modifier = modifier.fillMaxSize(),
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
            label = { Text("Input to search") },
            placeholder = { Text("Search superheroes...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp)
        )

        // HeroListPage based on search results
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Companion.Center)
            ) {
                Text("Searching...")
            }
        } else if (searchResults.isEmpty() && searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Companion.Center)
            ) {
                Text("No heroes found for \"$searchQuery\"")
            }
        } else if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Companion.Center)
            ) {
                Text("Search for superheroes by name")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
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

@Composable
fun HomePage(
    onBrowseClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Super Hero DB",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onBrowseClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Browse")
            }
            Button(
                onClick = onSearchClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Text("Search")
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
                    SuperHeroRepo.getAll().let { list ->
                        list.forEach {
                            Log.d("felixx", "getAll: $it")
                        }
                    }
                }
            }) {
            Text("getAll")
        }
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    SuperHeroRepo.searchLocal("batman").let {
                        Log.d("felixx", "searchByName: $it")
                    }
                }
            }) {
            Text("searchByName")
        }
    }
}