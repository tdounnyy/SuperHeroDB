package felix.duan.superherodb.ui.widget

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import felix.duan.superherodb.repo.SuperHeroRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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