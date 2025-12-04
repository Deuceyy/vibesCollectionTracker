# VibesTracker - Pudgy Penguins TCG Collection Tracker

A collection tracker for Vibes TCG with cloud sync, user accounts, and shareable profiles.

## Features

- 🐧 Track all card variants (Normal, Foil, Arctic, Sketch)
- ☁️ Cloud sync with Google sign-in
- 🔗 Shareable profile links (`/u/username`)
- 📊 Playset & Master set completion tracking
- 🔍 Advanced filtering and sorting
- 📱 Mobile responsive

## Quick Setup

### 1. Create a Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Create a project" (or use an existing one)
3. Give it a name (e.g., "vibes-tracker")
4. Disable Google Analytics (optional, not needed)
5. Click "Create project"

### 2. Enable Authentication

1. In Firebase Console, go to **Build → Authentication**
2. Click "Get started"
3. Go to **Sign-in method** tab
4. Click **Google**, enable it, add your email as support email
5. Click **Save**

### 3. Create Firestore Database

1. Go to **Build → Firestore Database**
2. Click "Create database"
3. Choose **Start in test mode** (we'll secure it later)
4. Select a location close to your users
5. Click "Enable"

### 4. Get Your Firebase Config

1. Go to **Project Settings** (gear icon)
2. Scroll down to "Your apps"
3. Click the web icon `</>`
4. Register app with a nickname (e.g., "vibes-tracker-web")
5. Copy the `firebaseConfig` values

### 5. Configure the App

1. Copy `.env.example` to `.env`:
   ```bash
   cp .env.example .env
   ```

2. Fill in your Firebase config values in `.env`:
   ```
   VITE_FIREBASE_API_KEY=AIzaSy...
   VITE_FIREBASE_AUTH_DOMAIN=your-project.firebaseapp.com
   VITE_FIREBASE_PROJECT_ID=your-project-id
   VITE_FIREBASE_STORAGE_BUCKET=your-project.appspot.com
   VITE_FIREBASE_MESSAGING_SENDER_ID=123456789
   VITE_FIREBASE_APP_ID=1:123456789:web:abcdef
   ```

### 6. Install & Run

```bash
npm install
npm run dev
```

Open http://localhost:5173

## Deploy to Vercel (Recommended)

1. Push your code to GitHub

2. Go to [Vercel](https://vercel.com) and import your repo

3. Add environment variables in Vercel dashboard:
   - Go to Settings → Environment Variables
   - Add all the `VITE_FIREBASE_*` variables

4. Deploy!

Your site will be live at `your-project.vercel.app`

## Deploy to Netlify

1. Push your code to GitHub

2. Go to [Netlify](https://netlify.com) and import your repo

3. Build settings:
   - Build command: `npm run build`
   - Publish directory: `dist`

4. Add environment variables in Site Settings → Environment Variables

5. Deploy!

## Firestore Security Rules

Once you're ready for production, update your Firestore rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can read any profile, but only write their own
    match /users/{userId} {
      allow read: if true;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Collections are readable by anyone, writable by owner
    match /collections/{userId} {
      allow read: if true;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

## Custom Domain

In Vercel/Netlify, go to Settings → Domains to add your custom domain.

## Tech Stack

- React 18 + Vite
- Firebase Auth (Google sign-in)
- Firestore (database)
- React Router (routing)

## License

MIT
