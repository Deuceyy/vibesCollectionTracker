package com.vibestcg.scoretracker.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibestcg.scoretracker.data.GameState
import com.vibestcg.scoretracker.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(gameState: GameState = remember { GameState() }) {
    var showDiceRoller by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var coinFlipResult by remember { mutableStateOf<String?>(null) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(OceanDeep, OceanMid)
                )
            )
    ) {
        // Main Content
        if (isLandscape) {
            LandscapeLayout(
                gameState = gameState,
                onShowDiceRoller = { showDiceRoller = true },
                onShowHistory = { showHistory = true },
                onResetGame = { showResetDialog = true },
                onCoinFlip = {
                    val winner = gameState.coinFlip()
                    coinFlipResult = gameState.player1.name.takeIf { winner == 0 }
                        ?: gameState.player2.name
                }
            )
        } else {
            PortraitLayout(
                gameState = gameState,
                onShowDiceRoller = { showDiceRoller = true },
                onShowHistory = { showHistory = true },
                onResetGame = { showResetDialog = true },
                onCoinFlip = {
                    val winner = gameState.coinFlip()
                    coinFlipResult = gameState.player1.name.takeIf { winner == 0 }
                        ?: gameState.player2.name
                }
            )
        }

        // Win Banner
        gameState.winningPlayer?.let { winner ->
            WinBanner(
                winner = winner.name,
                onNewGame = { showResetDialog = true }
            )
        }

        // Coin Flip Result
        coinFlipResult?.let { result ->
            LaunchedEffect(result) {
                kotlinx.coroutines.delay(2000)
                coinFlipResult = null
            }
            CoinFlipBanner(playerName = result)
        }
    }

    // Dice Roller Sheet
    if (showDiceRoller) {
        ModalBottomSheet(
            onDismissRequest = { showDiceRoller = false },
            containerColor = OceanDeep
        ) {
            DiceRollerSheet(onDismiss = { showDiceRoller = false })
        }
    }

    // History Sheet
    if (showHistory) {
        ModalBottomSheet(
            onDismissRequest = { showHistory = false },
            containerColor = OceanDeep
        ) {
            HistorySheet(
                gameState = gameState,
                onDismiss = { showHistory = false }
            )
        }
    }

    // Reset Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Game?") },
            text = { Text("This will reset all scores and stats to starting values.") },
            confirmButton = {
                TextButton(onClick = {
                    gameState.resetGame()
                    showResetDialog = false
                }) {
                    Text("Reset", color = AlertRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun PortraitLayout(
    gameState: GameState,
    onShowDiceRoller: () -> Unit,
    onShowHistory: () -> Unit,
    onResetGame: () -> Unit,
    onCoinFlip: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Player 2 (top, rotated 180°)
        Box(modifier = Modifier.rotate(180f)) {
            PlayerPanel(
                player = gameState.player2,
                isBaronController = gameState.baronController == 1,
                onAdjustCards = { gameState.adjustCardsInPlay(1, it) },
                onAdjustDeck = { gameState.adjustDeckRemaining(1, it) },
                onAdjustVibe = { gameState.adjustTotalVibe(1, it) },
                onNameChange = { gameState.updatePlayerName(1, it) }
            )
        }

        // Central controls
        CentralControls(
            gameState = gameState,
            onShowDiceRoller = onShowDiceRoller,
            onShowHistory = onShowHistory,
            onResetGame = onResetGame,
            onCoinFlip = onCoinFlip,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Player 1 (bottom, normal orientation)
        PlayerPanel(
            player = gameState.player1,
            isBaronController = gameState.baronController == 0,
            onAdjustCards = { gameState.adjustCardsInPlay(0, it) },
            onAdjustDeck = { gameState.adjustDeckRemaining(0, it) },
            onAdjustVibe = { gameState.adjustTotalVibe(0, it) },
            onNameChange = { gameState.updatePlayerName(0, it) }
        )
    }
}

@Composable
private fun LandscapeLayout(
    gameState: GameState,
    onShowDiceRoller: () -> Unit,
    onShowHistory: () -> Unit,
    onResetGame: () -> Unit,
    onCoinFlip: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Player 1 (left, rotated -90°)
        Box(
            modifier = Modifier
                .weight(1f)
                .rotate(-90f)
        ) {
            PlayerPanel(
                player = gameState.player1,
                isBaronController = gameState.baronController == 0,
                onAdjustCards = { gameState.adjustCardsInPlay(0, it) },
                onAdjustDeck = { gameState.adjustDeckRemaining(0, it) },
                onAdjustVibe = { gameState.adjustTotalVibe(0, it) },
                onNameChange = { gameState.updatePlayerName(0, it) }
            )
        }

        // Central controls
        CentralControls(
            gameState = gameState,
            onShowDiceRoller = onShowDiceRoller,
            onShowHistory = onShowHistory,
            onResetGame = onResetGame,
            onCoinFlip = onCoinFlip,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )

        // Player 2 (right, rotated 90°)
        Box(
            modifier = Modifier
                .weight(1f)
                .rotate(90f)
        ) {
            PlayerPanel(
                player = gameState.player2,
                isBaronController = gameState.baronController == 1,
                onAdjustCards = { gameState.adjustCardsInPlay(1, it) },
                onAdjustDeck = { gameState.adjustDeckRemaining(1, it) },
                onAdjustVibe = { gameState.adjustTotalVibe(1, it) },
                onNameChange = { gameState.updatePlayerName(1, it) }
            )
        }
    }
}

@Composable
private fun CentralControls(
    gameState: GameState,
    onShowDiceRoller: () -> Unit,
    onShowHistory: () -> Unit,
    onResetGame: () -> Unit,
    onCoinFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Title
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Vibes TCG",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryAccent
            )
            Text(
                text = "Score Tracker",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        // Baron Control
        BaronControl(
            baronController = gameState.baronController,
            player1Name = gameState.player1.name,
            player2Name = gameState.player2.name,
            onToggle = { gameState.toggleBaronControl() }
        )

        // Action Buttons
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ActionButton(
                icon = "🪙",
                label = "Coin Flip",
                color = PenguinOrange,
                onClick = onCoinFlip
            )

            ActionButton(
                icon = "🎲",
                label = "Dice Roller",
                color = VibesPurple,
                onClick = onShowDiceRoller
            )

            ActionButton(
                icon = "🕐",
                label = "History (${gameState.actionHistory.size})",
                color = PenguinBlue,
                onClick = onShowHistory,
                enabled = gameState.actionHistory.isNotEmpty()
            )

            ActionButton(
                icon = "🔄",
                label = "Reset Game",
                color = AlertRed,
                onClick = onResetGame
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: String,
    label: String,
    color: Color,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContainerColor = color.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 16.sp)
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun WinBanner(winner: String, onNewGame: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CardBackground,
            shadowElevation = 30.dp,
            modifier = Modifier.padding(32.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "🎉", fontSize = 80.sp)
                Text(
                    text = "$winner WINS!",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = IceGreen,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "15+ Cards in Play with Baron Control",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = onNewGame,
                    colors = ButtonDefaults.buttonColors(containerColor = IceGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "New Game",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CoinFlipBanner(playerName: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = CardBackground,
            shadowElevation = 10.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🪙", fontSize = 24.sp)
                Text(
                    text = "$playerName goes first!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistorySheet(gameState: GameState, onDismiss: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Action History") },
                navigationIcon = {
                    TextButton(
                        onClick = {
                            gameState.undoLastAction()
                        },
                        enabled = gameState.actionHistory.isNotEmpty()
                    ) {
                        Text("Undo")
                    }
                },
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
        if (gameState.actionHistory.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "🕐", fontSize = 60.sp)
                    Text(
                        text = "No actions yet",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(gameState.actionHistory) { action ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CardBackground
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = action.description,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = action.timeString,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
