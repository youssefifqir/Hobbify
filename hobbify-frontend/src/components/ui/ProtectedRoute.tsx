import { Navigate } from 'react-router-dom'
import { useAppContext } from '../../context/AppContext'
import type { ReactNode } from 'react'

const isAdmin = (roles: string[]) => roles.some(r => r === 'ROLE_ADMIN' || r === 'ADMIN')

export function ProtectedRoute({ children, adminOnly = false }: { children: ReactNode; adminOnly?: boolean }) {
  const { user, loading } = useAppContext()

  if (loading) return null

  if (!user) return <Navigate to="/auth" replace />

  if (adminOnly && !isAdmin(user.roles)) {
    return <Navigate to="/" replace />
  }

  return <>{children}</>
}

// Admins manage the catalog, not a personal hobby list — keep them out of the
// consumer "my hobbies" pages and back in the admin panel. Anonymous visitors
// are left alone so each page's own "sign in to continue" fallback still shows.
export function BlockAdminRoute({ children }: { children: ReactNode }) {
  const { user, loading } = useAppContext()

  if (loading) return null
  if (user && isAdmin(user.roles)) return <Navigate to="/admin" replace />

  return <>{children}</>
}
