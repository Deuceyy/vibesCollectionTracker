# Quick Start Guide - Vibes TCG Score Tracker

## 🎯 Getting the App Running in 2 Minutes

### Method 1: Open Existing Project (Easiest)

1. **Download/Clone** this repository
2. **Navigate** to the `VibesTCGScoreTracker` folder
3. **Double-click** `VibesTCGScoreTracker.xcodeproj`
4. **Wait** for Xcode to index the project (~30 seconds)
5. **Select a simulator** (iPhone 15 Pro recommended) from the device menu
6. **Press** `⌘R` or click the Play button
7. **Done!** The app will launch in the simulator

### Method 2: Create Fresh Project (If above doesn't work)

1. **Open Xcode** and select "Create New Project"
2. Choose:
   - Platform: **iOS**
   - Template: **App**
   - Interface: **SwiftUI**
   - Language: **Swift**
   - Product Name: **VibesTCGScoreTracker**
   - Minimum iOS: **17.0**
3. **Delete** the default `ContentView.swift` and `VibesTCGScoreTrackerApp.swift`
4. **Drag and drop** all files from this folder into your project:
   - Keep the folder structure (Models, Views, Theme)
   - Check "Copy items if needed"
   - Ensure "Add to targets" includes your app target
5. **Build and run** with `⌘R`

## 📱 Testing the App

### First Launch Checklist

Once the app opens, try these features:

1. ✅ **Tap player names** → Edit them
2. ✅ **Tap "Coin Flip"** → See banner showing first player
3. ✅ **Press +1/+5 buttons** → Numbers update with animation
4. ✅ **Tap Baron control** → Crown switches sides
5. ✅ **Increase cards to 15+** → Win banner appears
6. ✅ **Open History** → See action log
7. ✅ **Tap Undo** → Last action reverses
8. ✅ **Open Dice Roller** → Roll d6/d20
9. ✅ **Rotate device** → Layout adapts to landscape

### Sample Game Flow

```
1. Start app
2. Coin Flip → "Player 1 goes first"
3. Player 1 draws (Deck: 52 → 51)
4. Player 1 plays card (Cards: 0 → 1)
5. ... continue playing ...
6. Player 1 reaches 15 cards
7. Player 1 controls Baron
8. WIN BANNER appears! 🎉
```

## 🎨 Customization Quick Tips

### Change Colors

Edit `Theme/Colors.swift`:

```swift
// Make Baron gold instead of orange
static let penguinOrange = Color(red: 1.0, green: 0.84, blue: 0.0)

// Make background darker
static let oceanDeep = Color(red: 0.05, green: 0.1, blue: 0.2)
```

### Adjust Win Condition

Edit `Models/PlayerData.swift`:

```swift
// Change from 15 to 20 cards
var canWin: Bool {
    cardsInPlay >= 20  // Changed from 15
}
```

### Change Starting Deck Size

Edit `Models/PlayerData.swift`:

```swift
init(id: Int, name: String = "") {
    self.id = id
    self.name = name.isEmpty ? "Player \(id + 1)" : name
    self.cardsInPlay = 0
    self.deckRemaining = 60  // Changed from 52
    self.totalVibe = 0
}
```

## 🐛 Common Issues

### "Cannot find 'GameState' in scope"

**Fix:** Ensure all files are added to your target:
1. Select file in Xcode
2. Open File Inspector (right panel)
3. Check "Target Membership" → Your app should be checked

### "iOS 17.0 unavailable"

**Fix:** Update deployment target:
1. Select project in Xcode navigator
2. Select your target
3. General → Minimum Deployments → Set to iOS 17.0

### Layout looks wrong in simulator

**Fix:** Test on recommended devices:
- iPhone 15 Pro (best)
- iPhone 14 Pro
- iPad Pro 12.9"

### Build succeeds but app crashes immediately

**Fix:** Check Console (⌘⇧C) for error. Common causes:
- Missing SwiftUI import
- @Observable not available (needs iOS 17+)
- Observation framework not imported

## 🚀 Next Steps

### Add to Your Device

1. Connect iPhone/iPad to Mac
2. Select your device in Xcode device menu
3. Sign the app with your Apple ID:
   - Project settings → Signing & Capabilities
   - Team: Your Apple ID
4. Build and run to your device!

### Test with Real Cards

Play an actual Vibes TCG game and use the app to:
- Track real card counts
- Monitor deck sizes
- Handle Vibe Checks
- Declare winners

### Share with Friends

1. Archive the app (Product → Archive)
2. Distribute via TestFlight or direct install
3. Or just share the source code!

## 📖 In-App Help

The app is designed to be self-explanatory:
- Large buttons with clear labels
- Color coding (green = good, red = warning)
- Baron control shows current controller
- Win detection is automatic

### Button Guide

| Button | Function |
|--------|----------|
| +1, +5 | Increase stat |
| -1, -5 | Decrease stat |
| Draw | Decrease deck by 1 |
| Pencil | Edit player name |
| Crown | Toggle Baron control |
| Coin | Random first player |
| Dice | Open dice roller |
| History | View/undo actions |
| Reset | Start new game |

## 💡 Pro Tips

1. **Landscape mode**: Great for sitting across from opponent
2. **History**: Review last 10 actions anytime
3. **Undo**: Fix mistakes without manual adjustment
4. **Coin flip**: Use at start of every game for fairness
5. **Dice roller**: Keep it open in slide-over for quick rolls

## 🎓 Code Learning

Want to learn SwiftUI? This project demonstrates:
- `@Observable` macro (iOS 17+)
- Adaptive layouts with environment values
- SwiftUI animations and transitions
- Clean MVVM architecture
- Reusable view components
- Custom color schemes
- Sheet presentations
- Button styles and gradients

Each file is heavily commented for learning!

## 🤝 Need Help?

- Check the README.md for full documentation
- Read inline code comments for details
- Test in multiple simulators
- Try portrait AND landscape modes
- Join the Vibes TCG community for gameplay questions

---

**Ready to track some Vibes TCG games? Let's go! 🐧❄️**
