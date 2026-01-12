//
//  ContentView.swift
//  VibesTCGScoreTracker
//
//  Main app view with adaptive layout (portrait/landscape)
//

import SwiftUI

struct ContentView: View {
    @State private var gameState = GameState()
    @State private var showDiceRoller = false
    @State private var showHistory = false
    @State private var showResetConfirmation = false
    @State private var showWinBanner = false
    @State private var coinFlipResult: String? = nil

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass
    @Environment(\.verticalSizeClass) private var verticalSizeClass

    var isLandscape: Bool {
        horizontalSizeClass == .regular || verticalSizeClass == .compact
    }

    var body: some View {
        ZStack {
            // Background
            LinearGradient.oceanGradient
                .ignoresSafeArea()

            // Main Content
            if isLandscape {
                landscapeLayout
            } else {
                portraitLayout
            }

            // Win Banner Overlay
            if let winner = gameState.winningPlayer {
                winBannerView(winner: winner)
                    .transition(.scale.combined(with: .opacity))
            }

            // Coin Flip Result
            if let result = coinFlipResult {
                coinFlipBanner(result: result)
                    .transition(.move(edge: .top).combined(with: .opacity))
            }
        }
        .sheet(isPresented: $showDiceRoller) {
            DiceRollerView()
        }
        .sheet(isPresented: $showHistory) {
            historySheet
        }
        .alert("Reset Game?", isPresented: $showResetConfirmation) {
            Button("Cancel", role: .cancel) {}
            Button("Reset", role: .destructive) {
                withAnimation {
                    gameState.resetGame()
                }
            }
        } message: {
            Text("This will reset all scores and stats to starting values.")
        }
        .onChange(of: gameState.winningPlayer) { _, newValue in
            withAnimation(.spring(response: 0.5, dampingFraction: 0.7)) {
                showWinBanner = newValue != nil
            }
        }
    }

    // MARK: - Portrait Layout
    private var portraitLayout: some View {
        VStack(spacing: 0) {
            // Player 2 (top, rotated 180°)
            PlayerPanelView(
                player: gameState.player2,
                isBaronController: gameState.baronController == 1,
                onAdjustCards: { gameState.adjustCardsInPlay(player: 1, by: $0) },
                onAdjustDeck: { gameState.adjustDeckRemaining(player: 1, by: $0) },
                onAdjustVibe: { gameState.adjustTotalVibe(player: 1, by: $0) },
                onNameChange: { gameState.player2.name = $0 }
            )
            .rotationEffect(.degrees(180))

            Spacer()

            // Central controls
            centralControls
                .padding(.vertical)

            Spacer()

            // Player 1 (bottom, normal orientation)
            PlayerPanelView(
                player: gameState.player1,
                isBaronController: gameState.baronController == 0,
                onAdjustCards: { gameState.adjustCardsInPlay(player: 0, by: $0) },
                onAdjustDeck: { gameState.adjustDeckRemaining(player: 0, by: $0) },
                onAdjustVibe: { gameState.adjustTotalVibe(player: 0, by: $0) },
                onNameChange: { gameState.player1.name = $0 }
            )
        }
    }

    // MARK: - Landscape Layout
    private var landscapeLayout: some View {
        HStack(spacing: 0) {
            // Player 1 (left, rotated 90° CCW)
            PlayerPanelView(
                player: gameState.player1,
                isBaronController: gameState.baronController == 0,
                onAdjustCards: { gameState.adjustCardsInPlay(player: 0, by: $0) },
                onAdjustDeck: { gameState.adjustDeckRemaining(player: 0, by: $0) },
                onAdjustVibe: { gameState.adjustTotalVibe(player: 0, by: $0) },
                onNameChange: { gameState.player1.name = $0 }
            )
            .rotationEffect(.degrees(-90))
            .frame(width: UIScreen.main.bounds.height / 2)

            Spacer()

            // Central controls
            centralControls
                .frame(maxWidth: 250)

            Spacer()

            // Player 2 (right, rotated 90° CW)
            PlayerPanelView(
                player: gameState.player2,
                isBaronController: gameState.baronController == 1,
                onAdjustCards: { gameState.adjustCardsInPlay(player: 1, by: $0) },
                onAdjustDeck: { gameState.adjustDeckRemaining(player: 1, by: $0) },
                onAdjustVibe: { gameState.adjustTotalVibe(player: 1, by: $0) },
                onNameChange: { gameState.player2.name = $0 }
            )
            .rotationEffect(.degrees(90))
            .frame(width: UIScreen.main.bounds.height / 2)
        }
    }

    // MARK: - Central Controls
    private var centralControls: some View {
        VStack(spacing: 16) {
            // App Title
            VStack(spacing: 4) {
                Text("Vibes TCG")
                    .font(.title.bold())
                    .foregroundStyle(Color.primaryAccent)
                Text("Score Tracker")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }

            // Baron Control
            BaronControlView(
                baronController: gameState.baronController,
                player1Name: gameState.player1.name,
                player2Name: gameState.player2.name,
                onToggle: { gameState.toggleBaronControl() }
            )

            // Action Buttons
            VStack(spacing: 12) {
                // Coin Flip
                actionButton(
                    icon: "circle.circle.fill",
                    label: "Coin Flip",
                    color: .penguinOrange
                ) {
                    let winner = gameState.coinFlip()
                    coinFlipResult = gameState.playerName(winner)
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                        withAnimation {
                            coinFlipResult = nil
                        }
                    }
                }

                // Dice Roller
                actionButton(
                    icon: "dice.fill",
                    label: "Dice Roller",
                    color: .vibesPurple
                ) {
                    showDiceRoller = true
                }

                // History/Undo
                actionButton(
                    icon: "clock.arrow.circlepath",
                    label: "History (\(gameState.actionHistory.count))",
                    color: .penguinBlue
                ) {
                    showHistory = true
                }
                .disabled(gameState.actionHistory.isEmpty)

                // Reset Game
                actionButton(
                    icon: "arrow.counterclockwise",
                    label: "Reset Game",
                    color: .alertRed
                ) {
                    showResetConfirmation = true
                }
            }
        }
        .padding()
    }

    // MARK: - Action Button
    private func actionButton(icon: String, label: String, color: Color, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            HStack {
                Image(systemName: icon)
                Text(label)
                    .font(.subheadline.bold())
            }
            .foregroundStyle(.white)
            .frame(maxWidth: .infinity)
            .padding(.vertical, 12)
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(color.gradient)
            )
        }
        .buttonStyle(.plain)
    }

    // MARK: - Win Banner
    private func winBannerView(winner: PlayerData) -> some View {
        VStack(spacing: 16) {
            Text("🎉")
                .font(.system(size: 80))

            Text("\(winner.name) WINS!")
                .font(.system(size: 36, weight: .bold, design: .rounded))
                .foregroundStyle(Color.iceGreen)

            Text("15+ Cards in Play with Baron Control")
                .font(.subheadline)
                .foregroundStyle(.secondary)

            Button(action: {
                withAnimation {
                    showResetConfirmation = true
                }
            }) {
                Text("New Game")
                    .font(.headline)
                    .foregroundStyle(.white)
                    .padding(.horizontal, 32)
                    .padding(.vertical, 16)
                    .background(
                        RoundedRectangle(cornerRadius: 12)
                            .fill(Color.iceGreen.gradient)
                    )
            }
            .buttonStyle(.plain)
        }
        .padding(32)
        .background(
            RoundedRectangle(cornerRadius: 24)
                .fill(Color.cardBackground)
                .shadow(color: .iceGreen.opacity(0.5), radius: 30)
        )
        .padding()
    }

    // MARK: - Coin Flip Banner
    private func coinFlipBanner(result: String) -> some View {
        VStack {
            HStack(spacing: 12) {
                Image(systemName: "circle.circle.fill")
                    .font(.title2)
                    .foregroundStyle(Color.penguinOrange)
                Text("\(result) goes first!")
                    .font(.headline)
                    .foregroundStyle(.primary)
            }
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 16)
                    .fill(Color.cardBackground)
                    .shadow(radius: 10)
            )
            .padding()
            Spacer()
        }
    }

    // MARK: - History Sheet
    private var historySheet: some View {
        NavigationStack {
            ZStack {
                LinearGradient.oceanGradient
                    .ignoresSafeArea()

                if gameState.actionHistory.isEmpty {
                    VStack(spacing: 16) {
                        Image(systemName: "clock.arrow.circlepath")
                            .font(.system(size: 60))
                            .foregroundStyle(.secondary)
                        Text("No actions yet")
                            .font(.title3)
                            .foregroundStyle(.secondary)
                    }
                } else {
                    List {
                        ForEach(gameState.actionHistory) { action in
                            HStack {
                                VStack(alignment: .leading, spacing: 4) {
                                    Text(action.description)
                                        .font(.body)
                                    Text(action.timeString)
                                        .font(.caption)
                                        .foregroundStyle(.secondary)
                                }
                                Spacer()
                            }
                            .listRowBackground(Color.cardBackground)
                        }
                    }
                    .scrollContentBackground(.hidden)
                }
            }
            .navigationTitle("Action History")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button("Undo") {
                        withAnimation {
                            gameState.undoLastAction()
                        }
                    }
                    .foregroundStyle(Color.primaryAccent)
                    .disabled(gameState.actionHistory.isEmpty)
                }
                ToolbarItem(placement: .topBarTrailing) {
                    Button("Done") {
                        showHistory = false
                    }
                    .foregroundStyle(Color.primaryAccent)
                }
            }
        }
    }
}

// Helper extension for GameState
extension GameState {
    func playerName(_ index: Int) -> String {
        index == 0 ? player1.name : player2.name
    }
}

// MARK: - Preview
#Preview("Portrait") {
    ContentView()
}

#Preview("Landscape") {
    ContentView()
        .previewInterfaceOrientation(.landscapeLeft)
}
