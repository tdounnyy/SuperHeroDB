package felix.duan.superherodb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import felix.duan.superherodb.ui.theme.SuperHeroDBTheme
import felix.duan.superherodb.ui.widget.HeroListPage
import felix.duan.superherodb.ui.widget.HeroProfilePage
import felix.duan.superherodb.ui.widget.HomePage
import felix.duan.superherodb.ui.widget.SearchPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SuperHeroDBTheme {
                SuperHeroDBApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun SuperHeroDBApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomePage(
                onBrowseClick = { navController.navigate("hero_list") },
                onSearchClick = { navController.navigate("search") }
            )
        }
        composable("hero_list") {
            HeroListPage(
                onItemClick = { navController.navigate("profile/$it") },
            )
        }
        composable("search") {
            SearchPage(onItemClick = { navController.navigate("profile/$it") })
        }
        composable("profile/{id}") { backStackEntry ->
            val heroId = backStackEntry.arguments?.getString("id") ?: ""
            HeroProfilePage(id = heroId)
        }
    }
}

