import { Routes, Route } from 'react-router-dom';
import { AuthProvider } from './hooks/useAuth';
import Header from './components/Header';
import CollectionPage from './components/CollectionPage';
import ProfilePage from './components/ProfilePage';

export default function App() {
  return (
    <AuthProvider>
      <div className="app">
        <Header />
        <Routes>
          <Route path="/" element={<CollectionPage />} />
          <Route path="/u/:username" element={<ProfilePage />} />
        </Routes>
      </div>
    </AuthProvider>
  );
}
