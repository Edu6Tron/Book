import AsyncStorage from "@react-native-async-storage/async-storage";
import { createContext, useCallback, useContext, useEffect, useMemo, useState, type PropsWithChildren } from "react";

const STORAGE_KEY = "spiritual-companion-practice-v1";

type StoredPractice = {
  favourites: string[];
  completedPracticeIds: string[];
  japaCount: number;
};

type SpiritualStore = StoredPractice & {
  hydrated: boolean;
  toggleFavourite: (id: string) => void;
  togglePractice: (id: string) => void;
  incrementJapa: () => void;
  resetJapa: () => void;
};

const defaultValue: SpiritualStore = {
  favourites: [],
  completedPracticeIds: [],
  japaCount: 0,
  hydrated: false,
  toggleFavourite: () => undefined,
  togglePractice: () => undefined,
  incrementJapa: () => undefined,
  resetJapa: () => undefined,
};

const SpiritualContext = createContext<SpiritualStore>(defaultValue);

export function SpiritualProvider({ children }: PropsWithChildren) {
  const [state, setState] = useState<StoredPractice>({
    favourites: [],
    completedPracticeIds: [],
    japaCount: 0,
  });
  const [hydrated, setHydrated] = useState(false);

  useEffect(() => {
    AsyncStorage.getItem(STORAGE_KEY)
      .then((raw) => {
        if (!raw) return;
        const parsed = JSON.parse(raw) as Partial<StoredPractice>;
        setState({
          favourites: Array.isArray(parsed.favourites) ? parsed.favourites : [],
          completedPracticeIds: Array.isArray(parsed.completedPracticeIds) ? parsed.completedPracticeIds : [],
          japaCount: typeof parsed.japaCount === "number" ? parsed.japaCount : 0,
        });
      })
      .catch(() => undefined)
      .finally(() => setHydrated(true));
  }, []);

  const persist = useCallback((next: StoredPractice) => {
    AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(next)).catch(() => undefined);
  }, []);

  const updateState = useCallback((updater: (current: StoredPractice) => StoredPractice) => {
    setState((current) => {
      const next = updater(current);
      persist(next);
      return next;
    });
  }, [persist]);

  const toggleFavourite = useCallback((id: string) => {
    updateState((current) => ({
      ...current,
      favourites: current.favourites.includes(id)
        ? current.favourites.filter((value) => value !== id)
        : [...current.favourites, id],
    }));
  }, [updateState]);

  const togglePractice = useCallback((id: string) => {
    updateState((current) => ({
      ...current,
      completedPracticeIds: current.completedPracticeIds.includes(id)
        ? current.completedPracticeIds.filter((value) => value !== id)
        : [...current.completedPracticeIds, id],
    }));
  }, [updateState]);

  const incrementJapa = useCallback(() => {
    updateState((current) => ({ ...current, japaCount: current.japaCount + 1 }));
  }, [updateState]);

  const resetJapa = useCallback(() => {
    updateState((current) => ({ ...current, japaCount: 0 }));
  }, [updateState]);

  const value = useMemo(() => ({
    ...state,
    hydrated,
    toggleFavourite,
    togglePractice,
    incrementJapa,
    resetJapa,
  }), [state, hydrated, toggleFavourite, togglePractice, incrementJapa, resetJapa]);

  return <SpiritualContext.Provider value={value}>{children}</SpiritualContext.Provider>;
}

export function useSpiritualStore() {
  return useContext(SpiritualContext);
}
