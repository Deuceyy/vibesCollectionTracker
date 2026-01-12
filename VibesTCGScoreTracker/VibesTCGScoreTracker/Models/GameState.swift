//
//  GameState.swift
//  VibesTCGScoreTracker
//
//  Main game state manager using @Observable (iOS 17+)
//

import Foundation
import Observation

/// Main observable game state for Vibes TCG tracking
@Observable
class GameState {
    // MARK: - Player State
    var player1 = PlayerData(id: 0, name: "Player 1")
    var player2 = PlayerData(id: 1, name: "Player 2")

    // MARK: - Baron Control
    /// Which player controls the Baron (0 or 1). First player starts with Baron.
    var baronController: Int = 0

    // MARK: - History & Undo
    private(set) var actionHistory: [GameAction] = []
    private let maxHistorySize = 10

    // MARK: - Computed Properties
    var currentBaronPlayer: PlayerData {
        baronController == 0 ? player1 : player2
    }

    var winningPlayer: PlayerData? {
        if player1.canWin && baronController == 0 {
            return player1
        } else if player2.canWin && baronController == 1 {
            return player2
        }
        return nil
    }

    var losingPlayer: PlayerData? {
        if player1.hasLost { return player1 }
        if player2.hasLost { return player2 }
        return nil
    }

    // MARK: - Player Actions
    func adjustCardsInPlay(player: Int, by amount: Int) {
        let oldValue = player == 0 ? player1.cardsInPlay : player2.cardsInPlay
        if player == 0 {
            player1.cardsInPlay += amount
        } else {
            player2.cardsInPlay += amount
        }
        let newValue = player == 0 ? player1.cardsInPlay : player2.cardsInPlay
        addAction(
            description: "\(playerName(player)) Cards: \(oldValue) → \(newValue)",
            undo: { [weak self] in
                if player == 0 {
                    self?.player1.cardsInPlay = oldValue
                } else {
                    self?.player2.cardsInPlay = oldValue
                }
            }
        )
    }

    func adjustDeckRemaining(player: Int, by amount: Int) {
        let oldValue = player == 0 ? player1.deckRemaining : player2.deckRemaining
        if player == 0 {
            player1.deckRemaining += amount
        } else {
            player2.deckRemaining += amount
        }
        let newValue = player == 0 ? player1.deckRemaining : player2.deckRemaining
        addAction(
            description: "\(playerName(player)) Deck: \(oldValue) → \(newValue)",
            undo: { [weak self] in
                if player == 0 {
                    self?.player1.deckRemaining = oldValue
                } else {
                    self?.player2.deckRemaining = oldValue
                }
            }
        )
    }

    func adjustTotalVibe(player: Int, by amount: Int) {
        let oldValue = player == 0 ? player1.totalVibe : player2.totalVibe
        if player == 0 {
            player1.totalVibe += amount
        } else {
            player2.totalVibe += amount
        }
        let newValue = player == 0 ? player1.totalVibe : player2.totalVibe
        addAction(
            description: "\(playerName(player)) Vibe: \(oldValue) → \(newValue)",
            undo: { [weak self] in
                if player == 0 {
                    self?.player1.totalVibe = oldValue
                } else {
                    self?.player2.totalVibe = oldValue
                }
            }
        )
    }

    // MARK: - Baron Control
    func toggleBaronControl() {
        let oldController = baronController
        baronController = baronController == 0 ? 1 : 0
        addAction(
            description: "Baron → \(playerName(baronController))",
            undo: { [weak self] in
                self?.baronController = oldController
            }
        )
    }

    // MARK: - Game Management
    func resetGame() {
        player1.reset()
        player2.reset()
        baronController = 0
        actionHistory.removeAll()
    }

    func coinFlip() -> Int {
        let winner = Int.random(in: 0...1)
        baronController = winner
        addAction(
            description: "Coin flip: \(playerName(winner)) goes first!",
            undo: { [weak self] in
                self?.baronController = winner == 0 ? 1 : 0
            }
        )
        return winner
    }

    // MARK: - History Management
    private func addAction(description: String, undo: @escaping () -> Void) {
        let action = GameAction(description: description, undoAction: undo)
        actionHistory.insert(action, at: 0)
        if actionHistory.count > maxHistorySize {
            actionHistory.removeLast()
        }
    }

    func undoLastAction() {
        guard let lastAction = actionHistory.first else { return }
        lastAction.undoAction()
        actionHistory.removeFirst()
    }

    // MARK: - Helpers
    private func playerName(_ playerIndex: Int) -> String {
        playerIndex == 0 ? player1.name : player2.name
    }
}
