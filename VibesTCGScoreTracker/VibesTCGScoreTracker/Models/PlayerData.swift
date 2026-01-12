//
//  PlayerData.swift
//  VibesTCGScoreTracker
//
//  Data model for a single player's game state
//

import Foundation

/// Represents a player in a Vibes TCG game
struct PlayerData: Identifiable, Codable {
    let id: Int // 0 or 1 for the two players
    var name: String
    var cardsInPlay: Int // Primary score - win at 15+ with Baron control
    var deckRemaining: Int // Starts at 52, deck-out at 0 is a loss
    var totalVibe: Int // Sum of Vibe values for Vibe Checks

    /// Initialize with default starting values
    init(id: Int, name: String = "") {
        self.id = id
        self.name = name.isEmpty ? "Player \(id + 1)" : name
        self.cardsInPlay = 0
        self.deckRemaining = 52
        self.totalVibe = 0
    }

    /// Reset player to starting state (keep name)
    mutating func reset() {
        cardsInPlay = 0
        deckRemaining = 52
        totalVibe = 0
    }

    /// Check if this player has won (15+ cards in play, must also control Baron)
    var canWin: Bool {
        cardsInPlay >= 15
    }

    /// Check if this player has lost (tried to draw from empty deck)
    var hasLost: Bool {
        deckRemaining < 0
    }
}
