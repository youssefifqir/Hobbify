import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import * as api from '../lib/api'
import type { Achievement, Favorite, Hobby, User, UserProgress } from '../types'
import { useToast } from './ToastContext'

interface AppContextValue {
  user: User | null
  hobbies: Hobby[]
  favorites: Favorite[]
  progress: UserProgress[]
  achievements: Achievement[]
  loading: boolean
  refreshCatalog: () => Promise<void>
  refreshAccount: () => Promise<User | null>
  toggleFavorite: (hobbyId: string) => Promise<void>
  toggleProgress: (stepId: string, completed: boolean) => Promise<void>
  signOut: () => Promise<void>
}

const AppContext = createContext<AppContextValue | null>(null)

export function AppProvider({ children }: { children: React.ReactNode }) {
  const { showToast } = useToast()
  const [user, setUser] = useState<User | null>(null)
  const [hobbies, setHobbies] = useState<Hobby[]>([])
  const [favorites, setFavorites] = useState<Favorite[]>([])
  const [progress, setProgress] = useState<UserProgress[]>([])
  const [achievements, setAchievements] = useState<Achievement[]>([])
  const [loading, setLoading] = useState(true)

  const refreshCatalog = useCallback(async () => {
    setHobbies(await api.getCatalogHobbies())
  }, [])

  const refreshAccount = useCallback(async () => {
    if (!api.hasSession()) {
      setUser(null)
      setFavorites([])
      setProgress([])
      setAchievements([])
      return null
    }
    // The profile fetch determines whether the session is actually valid.
    // Favorites/progress/achievements are best-effort extras — a hiccup in any
    // one of them must not sign out a user who has a perfectly valid session.
    let profile: User
    try {
      profile = await api.getCurrentUser()
      setUser(profile)
    } catch (error) {
      if (error instanceof api.ApiError && error.status === 401) {
        api.clearSession()
      } else if (error instanceof api.ApiError) {
        showToast(error.message)
      }
      setUser(null)
      setFavorites([])
      setProgress([])
      setAchievements([])
      return null
    }

    const [favoritesResult, progressResult, achievementsResult] = await Promise.allSettled([
      api.getFavorites(), api.getProgress(), api.getAchievements(),
    ])
    setFavorites(favoritesResult.status === 'fulfilled' ? favoritesResult.value : [])
    setProgress(progressResult.status === 'fulfilled' ? progressResult.value : [])
    setAchievements(achievementsResult.status === 'fulfilled' ? achievementsResult.value : [])
    return profile
  }, [showToast])

  useEffect(() => {
    Promise.all([refreshCatalog(), refreshAccount()])
      .catch(() => undefined)
      .finally(() => setLoading(false))
  }, [refreshCatalog, refreshAccount])

  const toggleFavorite = useCallback(async (hobbyId: string) => {
    try {
      const existing = favorites.find(item => item.hobbyId === Number(hobbyId))
      if (existing) await api.removeFavorite(existing.id)
      else await api.addFavorite(hobbyId)
      setFavorites(await api.getFavorites())
    } catch (error) {
      showToast(error instanceof Error ? error.message : 'Unable to update favorite')
    }
  }, [favorites, showToast])

  const toggleProgress = useCallback(async (stepId: string, completed: boolean) => {
    try {
      const existing = progress.find(item => item.stepId === Number(stepId))
      await api.saveProgress(existing, stepId, completed)
      setProgress(await api.getProgress())
    } catch (error) {
      showToast(error instanceof Error ? error.message : 'Unable to update progress')
    }
  }, [progress, showToast])

  const signOut = useCallback(async () => {
    try {
      await api.logout()
    } catch {
      // Ignore logout errors — clear session regardless
    }
    setUser(null)
    setFavorites([])
    setProgress([])
    setAchievements([])
  }, [])

  const value = useMemo(() => ({
    user, hobbies, favorites, progress, achievements, loading,
    refreshCatalog, refreshAccount, toggleFavorite, toggleProgress, signOut,
  }), [user, hobbies, favorites, progress, achievements, loading, refreshCatalog, refreshAccount, toggleFavorite, toggleProgress, signOut])

  return <AppContext.Provider value={value}>{children}</AppContext.Provider>
}

export function useAppContext() {
  const context = useContext(AppContext)
  if (!context) throw new Error('useAppContext must be used inside AppProvider')
  return context
}
