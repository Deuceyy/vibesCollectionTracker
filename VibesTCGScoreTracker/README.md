# Vibes TCG Score Tracker

A beautiful, modern iOS app for tracking game scores and state during Vibes TCG (Pudgy Penguins Trading Card Game) matches.

![iOS 17+](https://img.shields.io/badge/iOS-17%2B-blue)
![SwiftUI](https://img.shields.io/badge/SwiftUI-brightgreen)
![Swift 5.9+](https://img.shields.io/badge/Swift-5.9%2B-orange)

## 🎮 Features

### Core Gameplay Tracking
- **Two-Player Support**: Symmetric UI for head-to-head matches
- **Cards in Play**: Track primary win condition (15+ cards with Baron control)
- **Deck Remaining**: Monitor deck size (starts at 52, deck-out = loss)
- **Total Vibe**: Track Vibe values for Vibe Checks
- **Baron Control**: Visual indicator showing who controls the Baron

### Smart UI
- **Adaptive Layout**: Optimized for both portrait and landscape orientations
- **Rotatable Panels**: Each player can view their stats upright
- **Editable Names**: Tap to customize player names
- **Quick Adjustments**: Large +1, +5, -1, -5 buttons for fast updates
- **Win Detection**: Automatic victory banner when conditions are met

### Extra Tools
- **Coin Flip**: Randomly determine first player (assigns Baron control)
- **Dice Roller**: Built-in d6, d20, and coin flip utility
- **Action History**: View last 10 actions with timestamps
- **Undo Function**: Roll back mistakes instantly
- **Reset Game**: Start fresh with one tap

### Visual Design
- **Pudgy Penguins Theme**: Icy blues, vibrant purples, warm oranges
- **Dark Mode**: Beautiful in low-light environments
- **Smooth Animations**: Spring physics and transitions
- **No Clutter**: Clean, focused interface

## 📱 Requirements

- iOS 17.0 or later
- iPhone or iPad
- Xcode 15.0+ (for development)

## 🚀 Quick Start

### Option 1: Open in Xcode

1. Clone or download this repository
2. Open `VibesTCGScoreTracker.xcodeproj` in Xcode
3. Select your target device (iPhone or iPad simulator)
4. Press `⌘R` to build and run

### Option 2: Create New Xcode Project

If you don't have the `.xcodeproj` file:

1. Open Xcode
2. Create a new project:
   - Choose **iOS** → **App**
   - Product Name: `VibesTCGScoreTracker`
   - Interface: **SwiftUI**
   - Language: **Swift**
   - Minimum iOS: **17.0**
3. Replace the default files with the provided source files:
   - Copy all files from this folder into your project
   - Maintain the folder structure (Models, Views, Theme)
4. Build and run!

## 📂 Project Structure

```
VibesTCGScoreTracker/
├── App.swift                    # App entry point
├── Models/
│   ├── GameState.swift         # @Observable game state manager
│   ├── PlayerData.swift        # Player data structure
│   └── GameAction.swift        # Action history for undo
├── Views/
│   ├── ContentView.swift       # Main adaptive layout
│   ├── PlayerPanelView.swift   # Reusable player panel
│   ├── BaronControlView.swift  # Baron control indicator
│   └── DiceRollerView.swift    # Dice rolling utility
└── Theme/
    └── Colors.swift            # Pudgy Penguins color scheme
```

## 🎯 How to Use

### Starting a Game

1. **Set Player Names**: Tap the pencil icon next to each player name to customize
2. **Coin Flip**: Tap "Coin Flip" to randomly decide who goes first (assigns Baron control)
3. **Track Actions**: Use the +/- buttons to adjust Cards in Play, Deck, and Vibe values

### During the Game

- **Draw Cards**: Tap "Draw" button or "-1" to decrease deck count
- **Add Cards to Play**: Use +1/+5 buttons to track Penguins/Relics/Rods played
- **Vibe Checks**: Adjust Total Vibe as cards enter/leave play
- **Switch Baron**: Tap the Baron control button to toggle control
- **Undo Mistakes**: Open History and tap "Undo" to reverse last action

### Winning the Game

The app automatically detects when a player wins:
- ✅ Has 15+ Cards in Play
- ✅ Controls the Baron
- 🎉 Win banner appears with celebration

### Other Tools

- **Dice Roller**: Tap to open d6/d20 roller for card effects
- **History**: Review recent actions with timestamps
- **Reset**: Start a new game (keeps player names)

## 🎨 Customization

### Colors

Edit `Theme/Colors.swift` to customize the color scheme:
- `penguinBlue`: Icy light blue accent
- `vibesPurple`: Vibrant purple for Vibe stats
- `penguinOrange`: Warm orange for Baron/warnings
- `oceanDeep`/`oceanMid`: Background gradients

### Game Rules

Edit `Models/PlayerData.swift` and `Models/GameState.swift` to adjust:
- Starting deck size (default: 52)
- Win condition threshold (default: 15 cards)
- Maximum history size (default: 10 actions)

## 🔧 Technical Details

### Architecture
- **SwiftUI**: Pure SwiftUI with no UIKit dependencies
- **@Observable**: Modern iOS 17+ observation framework
- **No External Dependencies**: Completely self-contained
- **MVVM Pattern**: Clean separation of concerns

### Performance
- Lightweight and fast
- Smooth 60fps animations
- Minimal memory footprint
- No networking or data persistence

### Supported Orientations
- Portrait (default)
- Landscape (adaptive layout)
- Player panels rotate to face each player

## 🐛 Troubleshooting

**Build errors?**
- Ensure iOS deployment target is set to 17.0+
- Verify Swift version is 5.9+

**Layout issues?**
- Test on different device sizes in simulator
- Check orientation locks in device settings

**App crashes?**
- Check Xcode console for error messages
- Ensure all files are properly added to target

## 📝 Game Rules Reference

Based on official Vibes TCG rules:

- **Win Condition**: First player to reach 15 cards in play while controlling Baron
- **Deck-Out Loss**: Drawing from empty deck = instant loss
- **Baron Control**: Shared game piece, changes hands during Vibe Checks
- **Vibe Checks**: Compare Total Vibe values to win prizes/Baron
- **Starting Deck**: 52 cards

## 🤝 Contributing

Feel free to fork and customize for your playgroup! Some ideas:
- Add support for 3-4 players
- Track additional stats (prizes won, turns played)
- Add sound effects and haptics
- Create custom card art backgrounds

## 📄 License

MIT License - feel free to use, modify, and distribute!

## 🐧 About Vibes TCG

Vibes TCG is the trading card game featuring Pudgy Penguins characters. This app is an unofficial fan-made tracker and is not affiliated with or endorsed by Pudgy Penguins.

---

Made with ❄️ for the Vibes TCG community
