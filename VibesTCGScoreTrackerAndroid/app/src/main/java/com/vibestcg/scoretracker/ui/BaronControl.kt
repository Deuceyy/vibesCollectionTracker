package com.vibestcg.scoretracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibestcg.scoretracker.ui.theme.*

@Composable
fun BaronControl(
    baronController: Int,
    player1Name: String,
    player2Name: String,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Baron Control",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Surface(
            onClick = onToggle,
            shape = RoundedCornerShape(16.dp),
            color = CardBackground,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Top/Left player indicator
                Text(
                    text = if (baronController == 0) "👑" else "👤",
                    fontSize = 24.sp
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "⟷",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Tap to Switch",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                // Bottom/Right player indicator
                Text(
                    text = if (baronController == 1) "👑" else "👤",
                    fontSize = 24.sp
                )
            }
        }

        Text(
            text = if (baronController == 0) player1Name else player2Name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PenguinOrange
        )
    }
}
