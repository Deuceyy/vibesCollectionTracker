package com.vibestcg.scoretracker.data

/**
 * Represents a player in a Vibes TCG game
 */
data class PlayerData(
    val id: Int, // 0 or 1 for the two players
    var name: String = "Player ${id + 1}",
    var cardsInPlay: Int = 0, // Primary score - win at 15+ with Baron control
    var deckRemaining: Int = 52, // Starts at 52, deck-out at 0 is a loss
    var totalVibe: Int = 0 // Sum of Vibe values for Vibe Checks
) {
    /**
     * Reset player to starting state (keep name)
     */
    fun reset() {
        cardsInPlay = 0
        deckRemaining = 52
        totalVibe = 0
    }

    /**
     * Check if this player can win (15+ cards in play, must also control Baron)
     */
    val canWin: Boolean
        get() = cardsInPlay >= 15

    /**
     * Check if this player has lost (tried to draw from empty deck)
     */
    val hasLost: Boolean
        get() = deckRemaining < 0
}
