import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Layout from './components/layout/Layout'
import AdminLayout from './components/layout/AdminLayout'
import Welcome from './pages/Welcome'
import Auth from './pages/Auth'
import Home from './pages/Home'
import HobbyDetail from './pages/HobbyDetail'
import Guide from './pages/Guide'
import StepDetail from './pages/StepDetail'
import Dashboard from './pages/Dashboard'
import Profile from './pages/Profile'
import MyProgress from './pages/MyProgress'
import Favorites from './pages/Favorites'
import AdminDashboard from './pages/admin/AdminDashboard'
import AdminContentLibrary from './pages/admin/AdminContentLibrary'
import AdminHobbyForm from './pages/admin/AdminHobbyForm'
import AdminUsers from './pages/admin/AdminUsers'
import AdminSettings from './pages/admin/AdminSettings'
import { AppProvider } from './context/AppContext'
import { ProtectedRoute, BlockAdminRoute } from './components/ui/ProtectedRoute'

export default function App() {
  return (
    <BrowserRouter>
      <AppProvider>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<Welcome />} />
          <Route path="/auth" element={<Auth />} />
          <Route path="/discover" element={<Home />} />
          <Route path="/hobby/:id" element={<HobbyDetail />} />
          <Route path="/hobby/:id/guide" element={<Guide />} />
          <Route path="/hobby/:id/guide/:stageId/:stepId" element={<StepDetail />} />
          <Route path="/dashboard" element={<BlockAdminRoute><Dashboard /></BlockAdminRoute>} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/my-progress" element={<BlockAdminRoute><MyProgress /></BlockAdminRoute>} />
          <Route path="/favorites" element={<BlockAdminRoute><Favorites /></BlockAdminRoute>} />
        </Route>
        <Route element={<AdminLayout />}>
          <Route path="/admin" element={<ProtectedRoute adminOnly><AdminDashboard /></ProtectedRoute>} />
          <Route path="/admin/content" element={<ProtectedRoute adminOnly><AdminContentLibrary /></ProtectedRoute>} />
          <Route path="/admin/content/new" element={<ProtectedRoute adminOnly><AdminHobbyForm /></ProtectedRoute>} />
          <Route path="/admin/content/:id/edit" element={<ProtectedRoute adminOnly><AdminHobbyForm /></ProtectedRoute>} />
          <Route path="/admin/users" element={<ProtectedRoute adminOnly><AdminUsers /></ProtectedRoute>} />
          <Route path="/admin/settings" element={<ProtectedRoute adminOnly><AdminSettings /></ProtectedRoute>} />
        </Route>
      </Routes>
      </AppProvider>
    </BrowserRouter>
  )
}
