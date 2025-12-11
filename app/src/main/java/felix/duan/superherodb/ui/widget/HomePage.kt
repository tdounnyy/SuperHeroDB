package felix.duan.superherodb.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import felix.duan.superherodb.repo.LocalRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            modifier = Modifier.Companion.align(Alignment.Companion.Center)
        )

        Column(
            modifier = Modifier.Companion
                .align(Alignment.Companion.BottomCenter)
                .padding(bottom = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onBrowseClick,
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                Text("Browse")
            }
            Button(
                onClick = onSearchClick,
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                Text("Search")
            }
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        LocalRepo.clearAll()
                    }
                },
                modifier = Modifier.Companion.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B0000)
                )
            ) {
                Text("Clear Local DB")
            }
        }
    }
}