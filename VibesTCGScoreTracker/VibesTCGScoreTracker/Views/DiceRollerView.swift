//
//  DiceRollerView.swift
//  VibesTCGScoreTracker
//
//  Dice rolling utility (d6, d20, coin flip)
//

import SwiftUI

struct DiceRollerView: View {
    @Environment(\.dismiss) private var dismiss

    @State private var d6Result: Int? = nil
    @State private var d20Result: Int? = nil
    @State private var coinResult: String? = nil
    @State private var isRolling = false

    var body: some View {
        NavigationStack {
            ZStack {
                // Background
                LinearGradient.oceanGradient
                    .ignoresSafeArea()

                ScrollView {
                    VStack(spacing: 24) {
                        // D6 Roller
                        diceCard(
                            title: "D6",
                            icon: "dice.fill",
                            result: d6Result.map { String($0) },
                            resultColor: .penguinBlue
                        ) {
                            rollDice(sides: 6) { result in
                                d6Result = result
                            }
                        }

                        // D20 Roller
                        diceCard(
                            title: "D20",
                            icon: "dice.fill",
                            result: d20Result.map { String($0) },
                            resultColor: .vibesPurple
                        ) {
                            rollDice(sides: 20) { result in
                                d20Result = result
                            }
                        }

                        // Coin Flip
                        diceCard(
                            title: "Coin Flip",
                            icon: "circle.circle.fill",
                            result: coinResult,
                            resultColor: .penguinOrange
                        ) {
                            flipCoin { result in
                                coinResult = result
                            }
                        }
                    }
                    .padding()
                }
            }
            .navigationTitle("Dice Roller")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button("Done") {
                        dismiss()
                    }
                    .foregroundStyle(Color.primaryAccent)
                }
            }
        }
    }

    // MARK: - Dice Card
    @ViewBuilder
    private func diceCard(
        title: String,
        icon: String,
        result: String?,
        resultColor: Color,
        onRoll: @escaping () -> Void
    ) -> some View {
        VStack(spacing: 16) {
            HStack {
                Image(systemName: icon)
                    .font(.title2)
                    .foregroundStyle(resultColor)
                Text(title)
                    .font(.title2.bold())
                    .foregroundStyle(.primary)
                Spacer()
            }

            if let result = result {
                Text(result)
                    .font(.system(size: 64, weight: .bold, design: .rounded))
                    .foregroundStyle(resultColor)
                    .contentTransition(.numericText())
                    .transition(.scale.combined(with: .opacity))
            } else {
                Text("?")
                    .font(.system(size: 64, weight: .bold, design: .rounded))
                    .foregroundStyle(.secondary)
            }

            Button(action: onRoll) {
                Text("Roll")
                    .font(.headline)
                    .foregroundStyle(.white)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(
                        RoundedRectangle(cornerRadius: 12)
                            .fill(resultColor.gradient)
                    )
            }
            .buttonStyle(.plain)
            .disabled(isRolling)
        }
        .padding()
        .background(
            RoundedRectangle(cornerRadius: 20)
                .fill(Color.cardBackground)
        )
    }

    // MARK: - Rolling Logic
    private func rollDice(sides: Int, completion: @escaping (Int) -> Void) {
        isRolling = true

        // Animate rolling
        var count = 0
        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { timer in
            count += 1
            withAnimation(.spring(response: 0.2)) {
                completion(Int.random(in: 1...sides))
            }
            if count >= 8 {
                timer.invalidate()
                isRolling = false
            }
        }
    }

    private func flipCoin(completion: @escaping (String) -> Void) {
        isRolling = true

        // Animate flipping
        var count = 0
        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { timer in
            count += 1
            withAnimation(.spring(response: 0.2)) {
                completion(Bool.random() ? "Heads" : "Tails")
            }
            if count >= 8 {
                timer.invalidate()
                isRolling = false
            }
        }
    }
}

// MARK: - Preview
#Preview {
    DiceRollerView()
}
