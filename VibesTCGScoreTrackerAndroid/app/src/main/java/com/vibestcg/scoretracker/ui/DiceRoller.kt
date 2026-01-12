package com.vibestcg.scoretracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibestcg.scoretracker.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceRollerSheet(
    onDismiss: () -> Unit
) {
    var d6Result by remember { mutableStateOf<Int?>(null) }
    var d20Result by remember { mutableStateOf<Int?>(null) }
    var coinResult by remember { mutableStateOf<String?>(null) }
    var isRolling by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dice Roller") },
                actions = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = OceanDeep,
                    titleContentColor = PrimaryAccent
                )
            )
        },
        containerColor = OceanDeep
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // D6 Roller
            DiceCard(
                title = "D6",
                icon = "🎲",
                result = d6Result?.toString(),
                resultColor = PenguinBlue,
                enabled = !isRolling
            ) {
                scope.launch {
                    isRolling = true
                    repeat(8) {
                        d6Result = (1..6).random()
                        delay(100)
                    }
                    isRolling = false
                }
            }

            // D20 Roller
            DiceCard(
                title = "D20",
                icon = "🎲",
                result = d20Result?.toString(),
                resultColor = VibesPurple,
                enabled = !isRolling
            ) {
                scope.launch {
                    isRolling = true
                    repeat(8) {
                        d20Result = (1..20).random()
                        delay(100)
                    }
                    isRolling = false
                }
            }

            // Coin Flip
            DiceCard(
                title = "Coin Flip",
                icon = "🪙",
                result = coinResult,
                resultColor = PenguinOrange,
                enabled = !isRolling
            ) {
                scope.launch {
                    isRolling = true
                    repeat(8) {
                        coinResult = if ((0..1).random() == 0) "Heads" else "Tails"
                        delay(100)
                    }
                    isRolling = false
                }
            }
        }
    }
}

@Composable
private fun DiceCard(
    title: String,
    icon: String,
    result: String?,
    resultColor: Color,
    enabled: Boolean,
    onRoll: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = CardBackground
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = icon, fontSize = 24.sp)
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = result ?: "?",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = resultColor
            )

            Button(
                onClick = onRoll,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = enabled,
                colors = ButtonDefaults.buttonColors(containerColor = resultColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Roll",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}
