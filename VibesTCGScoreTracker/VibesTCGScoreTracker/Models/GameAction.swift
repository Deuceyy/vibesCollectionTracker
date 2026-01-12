//
//  GameAction.swift
//  VibesTCGScoreTracker
//
//  Tracks actions for history log and undo functionality
//

import Foundation

/// Represents an action taken during the game (for undo/history)
struct GameAction: Identifiable {
    let id = UUID()
    let timestamp = Date()
    let description: String
    let undoAction: () -> Void

    /// Format timestamp for display
    var timeString: String {
        let formatter = DateFormatter()
        formatter.timeStyle = .short
        return formatter.string(from: timestamp)
    }
}
