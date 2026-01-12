//
//  PlayerPanelView.swift
//  VibesTCGScoreTracker
//
//  Reusable player panel showing name, stats, and controls
//

import SwiftUI

struct PlayerPanelView: View {
    let player: PlayerData
    let isBaronController: Bool
    let onAdjustCards: (Int) -> Void
    let onAdjustDeck: (Int) -> Void
    let onAdjustVibe: (Int) -> Void
    let onNameChange: (String) -> Void

    @State private var isEditingName = false

    var body: some View {
        VStack(spacing: 16) {
            // Player Name (editable)
            if isEditingName {
                TextField("Player Name", text: .init(
                    get: { player.name },
                    set: { onNameChange($0) }
                ))
                .font(.title2.bold())
                .foregroundStyle(Color.primaryAccent)
                .multilineTextAlignment(.center)
                .textFieldStyle(.roundedBorder)
                .padding(.horizontal)
                .onSubmit { isEditingName = false }
            } else {
                HStack {
                    Text(player.name)
                        .font(.title2.bold())
                        .foregroundStyle(Color.primaryAccent)
                    Button(action: { isEditingName = true }) {
                        Image(systemName: "pencil.circle.fill")
                            .foregroundStyle(Color.secondaryAccent)
                    }
                    .buttonStyle(.plain)
                }
            }

            // Baron indicator
            if isBaronController {
                HStack {
                    Image(systemName: "crown.fill")
                        .foregroundStyle(Color.penguinOrange)
                    Text("Controls Baron")
                        .font(.caption.bold())
                        .foregroundStyle(Color.penguinOrange)
                }
                .padding(.horizontal, 12)
                .padding(.vertical, 6)
                .background(
                    Capsule()
                        .fill(Color.penguinOrange.opacity(0.2))
                )
            }

            // Stats Grid
            VStack(spacing: 12) {
                statRow(
                    title: "Cards in Play",
                    value: player.cardsInPlay,
                    color: player.cardsInPlay >= 15 ? .iceGreen : .primaryAccent,
                    increments: [1, 5],
                    onAdjust: onAdjustCards
                )

                Divider().background(Color.primaryAccent.opacity(0.3))

                statRow(
                    title: "Deck Remaining",
                    value: player.deckRemaining,
                    color: player.deckRemaining <= 5 ? .alertRed : .secondaryAccent,
                    increments: [1],
                    onAdjust: onAdjustDeck,
                    showDrawButton: true
                )

                Divider().background(Color.primaryAccent.opacity(0.3))

                statRow(
                    title: "Total Vibe",
                    value: player.totalVibe,
                    color: .vibesPurple,
                    increments: [1, 5],
                    onAdjust: onAdjustVibe
                )
            }
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 20)
                    .fill(Color.cardBackground)
            )
        }
        .padding()
    }

    // MARK: - Stat Row
    @ViewBuilder
    private func statRow(
        title: String,
        value: Int,
        color: Color,
        increments: [Int],
        onAdjust: @escaping (Int) -> Void,
        showDrawButton: Bool = false
    ) -> some View {
        VStack(spacing: 8) {
            Text(title)
                .font(.caption.bold())
                .foregroundStyle(.secondary)

            Text("\(value)")
                .font(.system(size: 48, weight: .bold, design: .rounded))
                .foregroundStyle(color)
                .contentTransition(.numericText())

            HStack(spacing: 12) {
                // Decrease buttons
                ForEach(increments.reversed(), id: \.self) { inc in
                    adjustButton("-\(inc)", color: .red) {
                        onAdjust(-inc)
                    }
                }

                // Special draw button for deck
                if showDrawButton {
                    adjustButton("Draw", color: color) {
                        onAdjust(-1)
                    }
                    .frame(minWidth: 70)
                }

                // Increase buttons
                ForEach(increments, id: \.self) { inc in
                    adjustButton("+\(inc)", color: .green) {
                        onAdjust(inc)
                    }
                }
            }
        }
    }

    // MARK: - Adjust Button
    private func adjustButton(_ label: String, color: Color, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            Text(label)
                .font(.headline)
                .foregroundStyle(.white)
                .frame(minWidth: 50)
                .padding(.vertical, 10)
                .padding(.horizontal, 12)
                .background(
                    RoundedRectangle(cornerRadius: 12)
                        .fill(color.gradient)
                )
        }
        .buttonStyle(.plain)
    }
}

// MARK: - Preview
#Preview {
    PlayerPanelView(
        player: PlayerData(id: 0, name: "Player 1"),
        isBaronController: true,
        onAdjustCards: { _ in },
        onAdjustDeck: { _ in },
        onAdjustVibe: { _ in },
        onNameChange: { _ in }
    )
    .background(Color.oceanDeep)
}
