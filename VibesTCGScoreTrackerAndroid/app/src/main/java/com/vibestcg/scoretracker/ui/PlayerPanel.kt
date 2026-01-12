package com.vibestcg.scoretracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibestcg.scoretracker.data.PlayerData
import com.vibestcg.scoretracker.ui.theme.*

@Composable
fun PlayerPanel(
    player: PlayerData,
    isBaronController: Boolean,
    onAdjustCards: (Int) -> Unit,
    onAdjustDeck: (Int) -> Unit,
    onAdjustVibe: (Int) -> Unit,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditingName by remember { mutableStateOf(false) }
    var nameText by remember { mutableStateOf(player.name) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Player Name (editable)
        if (isEditingName) {
            OutlinedTextField(
                value = nameText,
                onValueChange = { nameText = it },
                modifier = Modifier.fillMaxWidth(0.8f),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = PrimaryAccent,
                    unfocusedTextColor = PrimaryAccent
                ),
                singleLine = true
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = {
                    onNameChange(nameText)
                    isEditingName = false
                }) {
                    Text("Done")
                }
                TextButton(onClick = {
                    nameText = player.name
                    isEditingName = false
                }) {
                    Text("Cancel")
                }
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = player.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryAccent
                )
                IconButton(onClick = {
                    nameText = player.name
                    isEditingName = true
                }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit name",
                        tint = SecondaryAccent
                    )
                }
            }
        }

        // Baron indicator
        if (isBaronController) {
            Surface(
                modifier = Modifier.padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = PenguinOrange.copy(alpha = 0.2f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("👑", fontSize = 16.sp)
                    Text(
                        "Controls Baron",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PenguinOrange
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Grid
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = CardBackground
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatRow(
                    title = "Cards in Play",
                    value = player.cardsInPlay,
                    color = if (player.cardsInPlay >= 15) IceGreen else PrimaryAccent,
                    increments = listOf(1, 5),
                    onAdjust = onAdjustCards
                )

                Divider(color = PrimaryAccent.copy(alpha = 0.3f))

                StatRow(
                    title = "Deck Remaining",
                    value = player.deckRemaining,
                    color = if (player.deckRemaining <= 5) AlertRed else SecondaryAccent,
                    increments = listOf(1),
                    onAdjust = onAdjustDeck,
                    showDrawButton = true
                )

                Divider(color = PrimaryAccent.copy(alpha = 0.3f))

                StatRow(
                    title = "Total Vibe",
                    value = player.totalVibe,
                    color = VibesPurple,
                    increments = listOf(1, 5),
                    onAdjust = onAdjustVibe
                )
            }
        }
    }
}

@Composable
private fun StatRow(
    title: String,
    value: Int,
    color: Color,
    increments: List<Int>,
    onAdjust: (Int) -> Unit,
    showDrawButton: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value.toString(),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrease buttons
            increments.reversed().forEach { inc ->
                AdjustButton(
                    label = "-$inc",
                    color = AlertRed,
                    onClick = { onAdjust(-inc) }
                )
            }

            // Special draw button for deck
            if (showDrawButton) {
                AdjustButton(
                    label = "Draw",
                    color = color,
                    onClick = { onAdjust(-1) },
                    modifier = Modifier.widthIn(min = 70.dp)
                )
            }

            // Increase buttons
            increments.forEach { inc ->
                AdjustButton(
                    label = "+$inc",
                    color = IceGreen,
                    onClick = { onAdjust(inc) }
                )
            }
        }
    }
}

@Composable
private fun AdjustButton(
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
