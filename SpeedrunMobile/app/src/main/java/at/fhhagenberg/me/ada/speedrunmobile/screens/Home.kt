package at.fhhagenberg.me.ada.speedrunmobile.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(names: List<String>) {
    Column(modifier = Modifier.padding(bottom = 48.dp)) {
        Text(text = "SEARCH BAR")
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = names) { name ->
                Box(modifier = Modifier
                    .size(60.dp)
                    .background(Color.Cyan)
                    .padding(16.dp),
                    contentAlignment = Alignment.Center) {
                    Text(text = name)
                }
            }
        }
    }
}