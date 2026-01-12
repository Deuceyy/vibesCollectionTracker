package com.vibestcg.scoretracker.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Main game state manager for Vibes TCG tracking
 */
class GameState {
    // MARK: Player State
    var player1 by mutableStateOf(PlayerData(id = 0, name = "Player 1"))
        private set

    var player2 by mutableStateOf(PlayerData(id = 1, name = "Player 2"))
        private set

    // MARK: Baron Control
    var baronController by mutableStateOf(0) // Which player controls the Baron (0 or 1)
        private set

    // MARK: History & Undo
    private val _actionHistory = mutableStateListOf<GameAction>()
    val actionHistory: List<GameAction> = _actionHistory
    private val maxHistorySize = 10

    // MARK: Computed Properties
    val currentBaronPlayer: PlayerData
        get() = if (baronController == 0) player1 else player2

    val winningPlayer: PlayerData?
        get() {
            return when {
                player1.canWin && baronController == 0 -> player1
                player2.canWin && baronController == 1 -> player2
                else -> null
            }
        }

    val losingPlayer: PlayerData?
        get() {
            return when {
                player1.hasLost -> player1
                player2.hasLost -> player2
                else -> null
            }
        }

    // MARK: Player Actions
    fun adjustCardsInPlay(player: Int, amount: Int) {
        val oldValue = if (player == 0) player1.cardsInPlay else player2.cardsInPlay

        if (player == 0) {
            player1 = player1.copy(cardsInPlay = player1.cardsInPlay + amount)
        } else {
            player2 = player2.copy(cardsInPlay = player2.cardsInPlay + amount)
        }

        val newValue = if (player == 0) player1.cardsInPlay else player2.cardsInPlay
        addAction(
            description = "${playerName(player)} Cards: $oldValue → $newValue"
        ) {
            if (player == 0) {
                player1 = player1.copy(cardsInPlay = oldValue)
            } else {
                player2 = player2.copy(cardsInPlay = oldValue)
            }
        }
    }

    fun adjustDeckRemaining(player: Int, amount: Int) {
        val oldValue = if (player == 0) player1.deckRemaining else player2.deckRemaining

        if (player == 0) {
            player1 = player1.copy(deckRemaining = player1.deckRemaining + amount)
        } else {
            player2 = player2.copy(deckRemaining = player2.deckRemaining + amount)
        }

        val newValue = if (player == 0) player1.deckRemaining else player2.deckRemaining
        addAction(
            description = "${playerName(player)} Deck: $oldValue → $newValue"
        ) {
            if (player == 0) {
                player1 = player1.copy(deckRemaining = oldValue)
            } else {
                player2 = player2.copy(deckRemaining = oldValue)
            }
        }
    }

    fun adjustTotalVibe(player: Int, amount: Int) {
        val oldValue = if (player == 0) player1.totalVibe else player2.totalVibe

        if (player == 0) {
            player1 = player1.copy(totalVibe = player1.totalVibe + amount)
        } else {
            player2 = player2.copy(totalVibe = player2.totalVibe + amount)
        }

        val newValue = if (player == 0) player1.totalVibe else player2.totalVibe
        addAction(
            description = "${playerName(player)} Vibe: $oldValue → $newValue"
        ) {
            if (player == 0) {
                player1 = player1.copy(totalVibe = oldValue)
            } else {
                player2 = player2.copy(totalVibe = oldValue)
            }
        }
    }

    fun updatePlayerName(player: Int, name: String) {
        if (player == 0) {
            player1 = player1.copy(name = name)
        } else {
            player2 = player2.copy(name = name)
        }
    }

    // MARK: Baron Control
    fun toggleBaronControl() {
        val oldController = baronController
        baronController = if (baronController == 0) 1 else 0
        addAction(
            description = "Baron → ${playerName(baronController)}"
        ) {
            baronController = oldController
        }
    }

    // MARK: Game Management
    fun resetGame() {
        player1.reset()
        player2.reset()
        player1 = player1.copy() // Trigger recomposition
        player2 = player2.copy()
        baronController = 0
        _actionHistory.clear()
    }

    fun coinFlip(): Int {
        val winner = (0..1).random()
        baronController = winner
        addAction(
            description = "Coin flip: ${playerName(winner)} goes first!"
        ) {
            baronController = if (winner == 0) 1 else 0
        }
        return winner
    }

    // MARK: History Management
    private fun addAction(description: String, undo: () -> Unit) {
        val action = GameAction(
            description = description,
            undoAction = undo
        )
        _actionHistory.add(0, action)
        if (_actionHistory.size > maxHistorySize) {
            _actionHistory.removeAt(_actionHistory.size - 1)
        }
    }

    fun undoLastAction() {
        if (_actionHistory.isNotEmpty()) {
            val lastAction = _actionHistory.removeAt(0)
            lastAction.undoAction()
        }
    }

    // MARK: Helpers
    private fun playerName(playerIndex: Int): String {
        return if (playerIndex == 0) player1.name else player2.name
    }
}
