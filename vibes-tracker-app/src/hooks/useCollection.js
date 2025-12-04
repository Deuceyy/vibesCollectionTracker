import { useState, useEffect, useCallback } from 'react';
import { doc, setDoc, onSnapshot } from 'firebase/firestore';
import { db } from '../firebase';
import { useAuth } from './useAuth';
import cardData from '../cardData.json';

const VARIANTS = ['normal', 'foil', 'arctic', 'sketch'];
const LOCAL_STORAGE_KEY = 'vibes_collection_local';

export function useCollection(userId = null) {
  const { user } = useAuth();
  const [collection, setCollection] = useState({});
  const [loading, setLoading] = useState(true);

  // Determine which user's collection to load
  const targetUserId = userId || user?.uid;
  const isOwnCollection = !userId || userId === user?.uid;

  // Load collection from Firestore or localStorage
  useEffect(() => {
    if (targetUserId) {
      // Load from Firestore
      const collectionRef = doc(db, 'collections', targetUserId);
      const unsubscribe = onSnapshot(collectionRef, (docSnap) => {
        if (docSnap.exists()) {
          setCollection(docSnap.data().cards || {});
        } else {
          setCollection({});
        }
        setLoading(false);
      }, (error) => {
        console.error('Error loading collection:', error);
        setLoading(false);
      });

      return () => unsubscribe();
    } else {
      // Load from localStorage for non-logged-in users
      try {
        const saved = localStorage.getItem(LOCAL_STORAGE_KEY);
        if (saved) {
          setCollection(JSON.parse(saved));
        }
      } catch (e) {
        console.error('Failed to load local collection:', e);
      }
      setLoading(false);
    }
  }, [targetUserId]);

  // Save collection
  const saveCollection = useCallback(async (newCollection) => {
    setCollection(newCollection);
    
    if (user && isOwnCollection) {
      // Save to Firestore
      const collectionRef = doc(db, 'collections', user.uid);
      await setDoc(collectionRef, { 
        cards: newCollection,
        updatedAt: new Date().toISOString()
      }, { merge: true });
    } else if (!user) {
      // Save to localStorage
      localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(newCollection));
    }
  }, [user, isOwnCollection]);

  // Get card variants
  const getCardVariants = useCallback((cardId) => {
    return collection[cardId] || { normal: 0, foil: 0, arctic: 0, sketch: 0 };
  }, [collection]);

  // Set variant count
  const setVariantCount = useCallback((cardId, variant, count) => {
    const newCollection = { ...collection };
    
    if (!newCollection[cardId]) {
      newCollection[cardId] = { normal: 0, foil: 0, arctic: 0, sketch: 0 };
    }
    
    newCollection[cardId][variant] = Math.max(0, Math.min(count, 99));
    
    // Clean up empty entries
    const variants = newCollection[cardId];
    if (variants.normal === 0 && variants.foil === 0 && variants.arctic === 0 && variants.sketch === 0) {
      delete newCollection[cardId];
    }
    
    saveCollection(newCollection);
  }, [collection, saveCollection]);

  // Adjust variant count
  const adjustVariant = useCallback((cardId, variant, delta) => {
    const current = getCardVariants(cardId)[variant];
    setVariantCount(cardId, variant, current + delta);
  }, [getCardVariants, setVariantCount]);

  // Get total owned
  const getTotalOwned = useCallback((cardId) => {
    const v = getCardVariants(cardId);
    return v.normal + v.foil + v.arctic + v.sketch;
  }, [getCardVariants]);

  // Check playset (4x normal)
  const hasPlayset = useCallback((cardId) => {
    return getCardVariants(cardId).normal >= 4;
  }, [getCardVariants]);

  // Check master set (1x each variant)
  const hasMasterSet = useCallback((cardId) => {
    const v = getCardVariants(cardId);
    return v.normal >= 1 && v.foil >= 1 && v.arctic >= 1 && v.sketch >= 1;
  }, [getCardVariants]);

  // Calculate stats
  const stats = {
    uniqueCards: cardData.filter(c => getTotalOwned(c.id) > 0).length,
    totalCards: cardData.reduce((sum, c) => sum + getTotalOwned(c.id), 0),
    playsetComplete: cardData.filter(c => hasPlayset(c.id)).length,
    masterComplete: cardData.filter(c => hasMasterSet(c.id)).length,
    totalInSet: cardData.length
  };

  // Import collection
  const importCollection = useCallback((data) => {
    let newCollection = {};
    if (data.collection) {
      newCollection = data.collection;
    } else if (data.cards) {
      newCollection = data.cards;
    } else {
      newCollection = data;
    }
    saveCollection(newCollection);
  }, [saveCollection]);

  // Export collection
  const exportCollection = useCallback(() => {
    return {
      version: 2,
      exportDate: new Date().toISOString(),
      collection: collection
    };
  }, [collection]);

  // Reset collection
  const resetCollection = useCallback(() => {
    saveCollection({});
  }, [saveCollection]);

  return {
    collection,
    loading,
    isOwnCollection,
    getCardVariants,
    setVariantCount,
    adjustVariant,
    getTotalOwned,
    hasPlayset,
    hasMasterSet,
    stats,
    importCollection,
    exportCollection,
    resetCollection
  };
}

export { cardData, VARIANTS };
