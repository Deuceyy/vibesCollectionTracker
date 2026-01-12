//
//  Colors.swift
//  VibesTCGScoreTracker
//
//  Pudgy Penguins inspired color scheme
//

import SwiftUI

extension Color {
    // MARK: - Pudgy Penguins Brand Colors
    /// Icy light blue (penguin theme)
    static let penguinBlue = Color(red: 0.6, green: 0.85, blue: 1.0)

    /// Deeper arctic blue
    static let arcticBlue = Color(red: 0.4, green: 0.65, blue: 0.9)

    /// Vibrant purple (vibes theme)
    static let vibesPurple = Color(red: 0.7, green: 0.5, blue: 1.0)

    /// Warm orange/coral accent
    static let penguinOrange = Color(red: 1.0, green: 0.6, blue: 0.3)

    /// Deep ocean blue for backgrounds
    static let oceanDeep = Color(red: 0.1, green: 0.15, blue: 0.3)

    /// Lighter ocean for cards/panels
    static let oceanMid = Color(red: 0.15, green: 0.2, blue: 0.35)

    /// Success green
    static let iceGreen = Color(red: 0.4, green: 0.9, blue: 0.7)

    /// Danger/warning red
    static let alertRed = Color(red: 1.0, green: 0.3, blue: 0.3)

    // MARK: - UI Colors
    static let cardBackground = oceanMid
    static let primaryAccent = penguinBlue
    static let secondaryAccent = vibesPurple
    static let warningColor = penguinOrange
}

extension LinearGradient {
    /// Cool penguin-themed gradient
    static let penguinGradient = LinearGradient(
        colors: [Color.penguinBlue, Color.arcticBlue],
        startPoint: .topLeading,
        endPoint: .bottomTrailing
    )

    /// Vibrant vibes gradient
    static let vibesGradient = LinearGradient(
        colors: [Color.vibesPurple, Color.penguinBlue],
        startPoint: .topLeading,
        endPoint: .bottomTrailing
    )

    /// Ocean background gradient
    static let oceanGradient = LinearGradient(
        colors: [Color.oceanDeep, Color.oceanMid],
        startPoint: .top,
        endPoint: .bottom
    )
}
