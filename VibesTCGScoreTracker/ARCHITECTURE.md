# Architecture Overview - Vibes TCG Score Tracker

## 🏗️ High-Level Structure

This app follows a clean **MVVM** (Model-View-ViewModel) architecture pattern using modern SwiftUI best practices.

```
┌─────────────────────────────────────────┐
│           App.swift (Entry)             │
└─────────────────┬───────────────────────┘
                  │
         ┌────────▼─────────┐
         │  ContentView     │ ◄── Main coordinator
         │  (View Layer)    │
         └────────┬─────────┘
                  │
      ┌───────────┼───────────┐
      │           │           │
┌─────▼──┐  ┌────▼────┐  ┌──▼──────┐
│ Player │  │ Baron   │  │ Dice    │
│ Panel  │  │ Control │  │ Roller  │
└────────┘  └─────────┘  └─────────┘
      │
      │
┌─────▼──────────┐
│   GameState    │ ◄── @Observable state
│  (ViewModel)   │
└────────────────┘
      │
┌─────▼──────────┐
│  PlayerData    │ ◄── Models
│  GameAction    │
└────────────────┘
```

## 📁 File Organization

### **App.swift** - Application Entry Point
- Defines the `@main` entry point
- Sets up the root `WindowGroup`
- Configures dark mode preference
- Minimal and clean - just launches ContentView

### **Models/** - Data Layer

#### **PlayerData.swift**
```swift
struct PlayerData {
    let id: Int              // Player index (0 or 1)
    var name: String         // Editable player name
    var cardsInPlay: Int     // Primary win condition tracker
    var deckRemaining: Int   // Deck-out loss condition
    var totalVibe: Int       // Vibe Check values
}
```

**Purpose:**
- Encapsulates all player-specific game state
- Provides computed properties for win/loss detection
- Includes reset functionality
- Immutable ID for stable identity

**Key Methods:**
- `reset()` - Returns to starting state
- `canWin` - Checks if player meets win condition (≥15 cards)
- `hasLost` - Checks if player decked out

#### **GameAction.swift**
```swift
struct GameAction {
    let id: UUID            // Unique identifier
    let timestamp: Date     // When action occurred
    let description: String // Human-readable text
    let undoAction: () -> Void  // Closure to reverse
}
```

**Purpose:**
- Captures every state change for history log
- Enables undo functionality via stored closures
- Provides timestamps for action log display
- Lightweight and memory-efficient

#### **GameState.swift** - ViewModel
```swift
@Observable
class GameState {
    var player1: PlayerData
    var player2: PlayerData
    var baronController: Int
    private(set) var actionHistory: [GameAction]
}
```

**Purpose:**
- Central source of truth for game state
- Uses `@Observable` macro (iOS 17+) for automatic UI updates
- Manages all game logic and rules
- Maintains action history (max 10 items)
- Provides win/loss detection

**Key Methods:**
- `adjustCardsInPlay(player:by:)` - Update cards with history tracking
- `adjustDeckRemaining(player:by:)` - Draw cards, track deck size
- `adjustTotalVibe(player:by:)` - Modify Vibe totals
- `toggleBaronControl()` - Switch Baron ownership
- `coinFlip()` - Random first player selection
- `resetGame()` - Full game reset
- `undoLastAction()` - Reverse last change

**Design Pattern:**
Uses the **Command pattern** for undo functionality:
```swift
addAction(description: "...") { [weak self] in
    // Undo logic captured in closure
    self?.property = oldValue
}
```

### **Views/** - UI Layer

#### **ContentView.swift** - Main Coordinator
```swift
struct ContentView: View {
    @State private var gameState = GameState()
    @State private var showDiceRoller = false
    @State private var showHistory = false
    @Environment(\.horizontalSizeClass) private var horizontalSizeClass
    @Environment(\.verticalSizeClass) private var verticalSizeClass
}
```

**Responsibilities:**
- Owns the single `GameState` instance
- Manages sheet presentations (dice, history)
- Handles orientation detection
- Switches between portrait/landscape layouts
- Shows win banners and coin flip results
- Coordinates all child views

**Layout Logic:**
```swift
if isLandscape {
    HStack { /* Player panels side-by-side */ }
} else {
    VStack { /* Player panels top/bottom */ }
}
```

**Rotation Strategy:**
- Portrait: Player 2 rotated 180° (upside down)
- Landscape: Player 1 rotated -90° (left), Player 2 rotated +90° (right)
- Central controls always upright

#### **PlayerPanelView.swift** - Player UI Component
```swift
struct PlayerPanelView: View {
    let player: PlayerData
    let isBaronController: Bool
    let onAdjustCards: (Int) -> Void
    let onAdjustDeck: (Int) -> Void
    let onAdjustVibe: (Int) -> Void
    let onNameChange: (String) -> Void
}
```

**Design Pattern:** Pure view component (no state ownership)
- Receives all data via parameters
- Communicates changes via callbacks
- Fully reusable and testable
- No dependency on GameState (loose coupling)

**Features:**
- Editable player name with inline TextField
- Three stat rows (Cards, Deck, Vibe)
- Dynamic color coding based on values
- Baron crown indicator
- Increment/decrement buttons with different sizes

**Visual Hierarchy:**
```
┌─────────────────────────────┐
│      Player Name  ✏️        │
│    👑 Controls Baron         │
├─────────────────────────────┤
│  Cards in Play: 12          │
│  [-5][-1]  [+1][+5]        │
├─────────────────────────────┤
│  Deck Remaining: 45         │
│  [-1]  [Draw]  [+1]        │
├─────────────────────────────┤
│  Total Vibe: 23             │
│  [-5][-1]  [+1][+5]        │
└─────────────────────────────┘
```

#### **BaronControlView.swift** - Baron Toggle
```swift
struct BaronControlView: View {
    let baronController: Int
    let player1Name: String
    let player2Name: String
    let onToggle: () -> Void
}
```

**Purpose:**
- Visual indicator of Baron ownership
- Interactive toggle button
- Shows both players with one crowned
- Spring animation on toggle

**Design:**
```
┌──────────────────────┐
│  Baron Control       │
│  👑 ↔️ 👑            │
│  (tap to switch)     │
│  Player 1            │
└──────────────────────┘
```

#### **DiceRollerView.swift** - Utility Sheet
```swift
struct DiceRollerView: View {
    @State private var d6Result: Int?
    @State private var d20Result: Int?
    @State private var coinResult: String?
    @State private var isRolling = false
}
```

**Features:**
- Three separate rollers (d6, d20, coin)
- Animated rolling effect (rapid number changes)
- Independent state for each roller
- Timer-based animation (0.1s intervals, 8 iterations)
- Prevents spam with `isRolling` flag

**Rolling Algorithm:**
```swift
// Animate 8 rapid changes then stop
Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { timer in
    count += 1
    withAnimation {
        completion(Int.random(in: 1...sides))
    }
    if count >= 8 {
        timer.invalidate()
        isRolling = false
    }
}
```

### **Theme/** - Visual Design

#### **Colors.swift** - Pudgy Penguins Palette
```swift
extension Color {
    static let penguinBlue = Color(red: 0.6, green: 0.85, blue: 1.0)
    static let arcticBlue = Color(red: 0.4, green: 0.65, blue: 0.9)
    static let vibesPurple = Color(red: 0.7, green: 0.5, blue: 1.0)
    static let penguinOrange = Color(red: 1.0, green: 0.6, blue: 0.3)
    static let oceanDeep = Color(red: 0.1, green: 0.15, blue: 0.3)
    static let iceGreen = Color(red: 0.4, green: 0.9, blue: 0.7)
}
```

**Color Usage:**
- **penguinBlue** → Primary accent, card counts
- **vibesPurple** → Vibe stats, secondary actions
- **penguinOrange** → Baron control, warnings
- **oceanDeep/Mid** → Backgrounds, cards
- **iceGreen** → Success, win states
- **alertRed** → Danger, low deck warnings

**Gradients:**
```swift
LinearGradient.penguinGradient  // Blue theme
LinearGradient.vibesGradient    // Purple-blue blend
LinearGradient.oceanGradient    // Background depth
```

## 🔄 Data Flow

### User Action Flow
```
1. User taps +1 button
   ↓
2. PlayerPanelView calls onAdjustCards(1)
   ↓
3. ContentView closure: gameState.adjustCardsInPlay(player: 0, by: 1)
   ↓
4. GameState updates player1.cardsInPlay
   ↓
5. GameState adds action to history with undo closure
   ↓
6. @Observable macro triggers view refresh
   ↓
7. PlayerPanelView re-renders with new value
   ↓
8. Number animates via .contentTransition(.numericText())
```

### Undo Flow
```
1. User taps "Undo" button
   ↓
2. gameState.undoLastAction()
   ↓
3. Retrieves last GameAction from history
   ↓
4. Executes stored undoAction closure
   ↓
5. Closure restores previous state value
   ↓
6. Removes action from history
   ↓
7. @Observable triggers UI update
   ↓
8. View reflects undone state
```

### Win Detection Flow
```
1. Player reaches 15+ cards in play
   ↓
2. GameState.winningPlayer computed property evaluates
   ↓
3. Checks: canWin AND baronController match
   ↓
4. Returns winning player or nil
   ↓
5. ContentView observes change via .onChange(of:)
   ↓
6. Sets showWinBanner = true with animation
   ↓
7. Win banner ZStack overlay appears
   ↓
8. Confetti-style text and "New Game" button shown
```

## 🎨 SwiftUI Patterns Used

### Modern iOS 17+ Features
- **@Observable macro**: Replaces `@StateObject` and `ObservableObject`
- **@State with reference types**: Direct storage of class instances
- **Observation framework**: Automatic dependency tracking

### Adaptive Layout
```swift
@Environment(\.horizontalSizeClass) private var horizontalSizeClass
@Environment(\.verticalSizeClass) private var verticalSizeClass

var isLandscape: Bool {
    horizontalSizeClass == .regular || verticalSizeClass == .compact
}
```

### Animations
- **Spring physics**: `.spring(response: 0.3, dampingFraction: 0.7)`
- **Content transitions**: `.contentTransition(.numericText())`
- **Combined transitions**: `.scale.combined(with: .opacity)`
- **Rotation effects**: `.rotationEffect(.degrees(180))`

### Component Communication
- **Callbacks**: Views pass closures up to parent
- **Bindings**: Two-way data flow for TextField
- **Environment**: Share values down the hierarchy

### State Management
- **Single source of truth**: One `GameState` instance
- **Unidirectional data flow**: State → View → Action → State
- **Immutable where possible**: `let` for properties that don't change

## 🧪 Testing Approach

### Manual Test Checklist
```
✅ Edit player names
✅ Adjust all stats (cards, deck, vibe)
✅ Toggle Baron control
✅ Coin flip randomness
✅ Win detection at 15+ cards + Baron
✅ History log records actions
✅ Undo reverses changes correctly
✅ Reset clears all state
✅ Dice roller randomness
✅ Portrait layout works
✅ Landscape layout works
✅ Device rotation handling
✅ Dark mode appearance
```

### Unit Test Targets
```swift
// Example test cases for GameState
func testPlayerWinCondition() {
    let state = GameState()
    state.adjustCardsInPlay(player: 0, by: 15)
    state.baronController = 0
    XCTAssertNotNil(state.winningPlayer)
}

func testUndoFunctionality() {
    let state = GameState()
    state.adjustCardsInPlay(player: 0, by: 5)
    XCTAssertEqual(state.player1.cardsInPlay, 5)
    state.undoLastAction()
    XCTAssertEqual(state.player1.cardsInPlay, 0)
}
```

## 🚀 Performance Considerations

### Efficiency
- **No unnecessary re-renders**: @Observable tracks dependencies automatically
- **Lightweight models**: Structs for PlayerData, classes only where needed
- **Computed properties**: Win detection is O(1)
- **History limit**: Max 10 actions to prevent memory bloat

### Optimization Opportunities
- Could add Codable conformance for save/load
- Could implement custom Equatable to prevent redundant updates
- Could use @Published explicitly for fine-grained control
- Could add haptic feedback via UINotificationFeedbackGenerator

## 📚 Learning Resources

This codebase demonstrates:
1. **SwiftUI fundamentals**: Views, state, bindings
2. **iOS 17+ APIs**: @Observable, Observation framework
3. **MVVM architecture**: Clear separation of concerns
4. **Functional patterns**: Closures for undo, callbacks
5. **Responsive design**: Adaptive layouts
6. **Animation**: Spring physics, transitions
7. **Component design**: Reusable, composable views
8. **Theme management**: Color extensions, gradients

## 🔮 Future Enhancements

### Potential Features
- [ ] Save/load game state
- [ ] Match history across sessions
- [ ] Statistics tracking (wins per player, average game length)
- [ ] Custom game rules (different win conditions)
- [ ] 3-4 player support
- [ ] iCloud sync between devices
- [ ] Haptic feedback on actions
- [ ] Sound effects (optional)
- [ ] Alternative themes/skins
- [ ] Accessibility improvements (VoiceOver, Dynamic Type)

### Architecture Improvements
- [ ] Add unit tests
- [ ] Extract business logic to separate layer
- [ ] Implement Repository pattern for data persistence
- [ ] Add dependency injection
- [ ] Create protocol-based abstractions for testability

---

**This architecture prioritizes:**
- 📖 Readability and maintainability
- 🎯 Clear separation of concerns
- 🔄 Unidirectional data flow
- 🧩 Reusable, composable components
- 🚀 Modern SwiftUI best practices
