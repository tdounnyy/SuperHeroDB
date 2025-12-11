package felix.duan.superherodb.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Text("Search")
            }
        }
    }
}