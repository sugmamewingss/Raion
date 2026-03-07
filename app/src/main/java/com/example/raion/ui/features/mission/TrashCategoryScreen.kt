package com.example.raion.ui.features.mission

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.ui.features.auth.components.WaveBackground
import com.example.raion.ui.theme.DesignTokens

@Composable
fun TrashCategoryScreen(
    categoryType: String, // "organic" or "recycle"
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val isOrganic = categoryType.lowercase() == "organic"
    val title = if (isOrganic) "Sampah Organik" else "Sampah Daur Ulang"

    val items = if (isOrganic) {
        listOf(
            Pair(R.drawable.sampahbuah, "Sampah Buah"),
            Pair(R.drawable.sampahsayur, "Sampah Sayur"),
            Pair(R.drawable.sampahhewani, "Sampah Hewani")
        )
    } else {
        listOf(
            Pair(R.drawable.sampahkaleng, "Kaleng"),
            Pair(R.drawable.sampahkertas, "Kertas"),
            Pair(R.drawable.sampahplastik, "Plastik")
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            WaveBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // TopBar dengan Progress 50%
                TrashTopBar(progress = 0.5f, onBackClick = onBackClick)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // Judul Kategori
                    Text(
                        text = title,
                        color = DesignTokens.Colors.OrangePrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Daftar Item
                    items.forEach { (imageRes, itemName) ->
                        TrashItemCard(
                            imageRes = imageRes,
                            title = itemName,
                            onClick = { onItemClick(itemName) }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
