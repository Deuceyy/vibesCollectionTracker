# Quick Build Guide - Get APK in 10 Minutes

## ⚡ Fastest Method (Windows/Mac/Linux)

### Step 1: Install Android Studio (5 minutes)
1. Go to https://developer.android.com/studio
2. Download for your OS (Windows/Mac/Linux)
3. Run installer
4. Follow setup wizard (accept defaults)

### Step 2: Open Project (30 seconds)
1. Launch Android Studio
2. Click **"Open"**
3. Navigate to `VibesTCGScoreTrackerAndroid` folder
4. Click "OK"

### Step 3: Wait for Sync (3 minutes)
- Status bar shows "Syncing..."
- Gradle downloads dependencies
- Just wait, it's automatic!
- ☕ Grab a coffee

### Step 4: Build APK (2 minutes)
1. Menu bar: **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
2. Wait for "BUILD SUCCESSFUL" message
3. Click **"locate"** link in notification
4. APK file appears in file browser!

### Step 5: Install on Phone (1 minute)
1. Copy `app-debug.apk` to your phone:
   - USB cable, or
   - Email to yourself, or
   - Google Drive/Dropbox
2. Tap the APK file on your phone
3. Allow installation (may need to enable "Unknown Sources")
4. Tap "Install"
5. ✅ Done! Launch the app!

---

## 🖥️ Alternative: Command Line (No GUI)

### Prerequisites
```bash
# Install Java 17+
# Windows: https://adoptium.net/
# Mac: brew install openjdk@17
# Linux: sudo apt install openjdk-17-jdk
```

### Build Commands

**Windows:**
```cmd
cd VibesTCGScoreTrackerAndroid
gradlew.bat assembleDebug
```

**Mac/Linux:**
```bash
cd VibesTCGScoreTrackerAndroid
chmod +x gradlew
./gradlew assembleDebug
```

### Find APK
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 🧪 Test Without a Phone

### Use Android Emulator
1. In Android Studio: **Device Manager** (phone icon)
2. Click **"Create Virtual Device"**
3. Select **Pixel 6**
4. Download **System Image** (API 34)
5. Click **Finish**
6. Press **Play** button to run app
7. Virtual phone opens with app running!

---

## 📱 Install APK Methods

### Method 1: USB Cable
```bash
# Enable USB debugging on phone
# Connect phone to computer
# Run:
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Method 2: Email
1. Email APK to yourself
2. Open email on phone
3. Download attachment
4. Tap to install

### Method 3: Cloud Storage
1. Upload APK to Google Drive/Dropbox
2. Download on phone
3. Tap to install

### Method 4: QR Code
1. Upload APK to file hosting (e.g., transfer.sh)
2. Generate QR code from URL
3. Scan QR with phone
4. Download and install

---

## ⚠️ Common Issues & Fixes

### Issue: "Gradle sync failed"
**Fix:**
```bash
# Delete cache and retry
rm -rf .gradle build
./gradlew clean assembleDebug
```

### Issue: "SDK not found"
**Fix:**
1. Open Android Studio
2. **Tools** → **SDK Manager**
3. Check **Android 13.0 (API 34)**
4. Click **Apply**
5. Wait for download
6. Retry build

### Issue: "Can't install on phone"
**Fix:**
1. Phone Settings → **Security**
2. Enable **"Install unknown apps"**
3. Choose your file manager
4. Retry installation

### Issue: "Build takes forever"
**Fix:**
- First build downloads ~500MB of dependencies
- Subsequent builds are much faster
- Be patient on first run!

### Issue: "Out of memory"
**Fix:**
Edit `gradle.properties`:
```
org.gradle.jvmargs=-Xmx4096m
```

---

## 📦 APK Details

**Debug APK (for testing):**
- File: `app-debug.apk`
- Size: ~10-15MB
- Signed with debug key
- Can install on any device
- Not for Play Store

**Release APK (for distribution):**
- File: `app-release.apk`
- Needs signing with your keystore
- Required for Play Store
- Optimized and smaller

---

## 🎯 Success Checklist

Before building:
- [ ] Android Studio installed
- [ ] Project opened
- [ ] Gradle sync completed (no errors)

After building:
- [ ] APK file exists at `app/build/outputs/apk/debug/app-debug.apk`
- [ ] File size is 10-15MB
- [ ] No build errors in console

On phone:
- [ ] "Unknown sources" enabled
- [ ] APK transferred to phone
- [ ] Installation successful
- [ ] App launches without crashes
- [ ] All features work

---

## 💡 Pro Tips

1. **First build is slow** - Be patient, it downloads dependencies
2. **Use stable internet** - Gradle needs to download packages
3. **Close other apps** - Free up RAM for build
4. **Enable Developer Options** on phone for easier testing
5. **Keep Android Studio updated** for best experience

---

## 🚀 You're Done!

Your APK is ready to install and test. No Mac needed, no App Store fees, test immediately!

**Next steps:**
1. Install on your phone
2. Play a Vibes TCG game
3. Track scores with the app
4. Share APK with friends!

**Questions?** Check the full README.md for detailed troubleshooting.
