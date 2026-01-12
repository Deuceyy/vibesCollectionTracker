//
//  BaronControlView.swift
//  VibesTCGScoreTracker
//
//  Shows Baron control status and allows toggling
//

import SwiftUI

struct BaronControlView: View {
    let baronController: Int
    let player1Name: String
    let player2Name: String
    let onToggle: () -> Void

    var body: some View {
        VStack(spacing: 12) {
            Text("Baron Control")
                .font(.caption.bold())
                .foregroundStyle(.secondary)

            Button(action: {
                withAnimation(.spring(response: 0.3, dampingFraction: 0.7)) {
                    onToggle()
                }
            }) {
                HStack(spacing: 12) {
                    // Top/Left player indicator
                    Image(systemName: baronController == 0 ? "crown.fill" : "crown")
                        .font(.title2)
                        .foregroundStyle(baronController == 0 ? Color.penguinOrange : .secondary)

                    VStack(spacing: 4) {
                        Image(systemName: "arrow.left.arrow.right")
                            .font(.caption)
                            .foregroundStyle(.secondary)
                        Text("Tap to Switch")
                            .font(.caption2)
                            .foregroundStyle(.secondary)
                    }

                    // Bottom/Right player indicator
                    Image(systemName: baronController == 1 ? "crown.fill" : "crown")
                        .font(.title2)
                        .foregroundStyle(baronController == 1 ? Color.penguinOrange : .secondary)
                }
                .padding()
                .background(
                    RoundedRectangle(cornerRadius: 16)
                        .fill(Color.cardBackground)
                        .shadow(color: .penguinOrange.opacity(0.3), radius: 10)
                )
            }
            .buttonStyle(.plain)

            Text(baronController == 0 ? player1Name : player2Name)
                .font(.caption.bold())
                .foregroundStyle(Color.penguinOrange)
        }
    }
}

// MARK: - Preview
#Preview {
    BaronControlView(
        baronController: 0,
        player1Name: "Player 1",
        player2Name: "Player 2",
        onToggle: {}
    )
    .padding()
    .background(Color.oceanDeep)
}
