package com.vibestcg.scoretracker.data

import java.text.SimpleDateFormat
import java.util.*

/**
 * Represents an action taken during the game (for undo/history)
 */
data class GameAction(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val description: String,
    val undoAction: () -> Unit
) {
    /**
     * Format timestamp for display
     */
    val timeString: String
        get() {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
}
