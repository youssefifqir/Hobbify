import { Outlet, useLocation } from 'react-router-dom'
import Header from './Header'
import MobileNav from './MobileNav'

export default function Layout() {
  const loc = useLocation()
  const hideHeader = loc.pathname === '/' || loc.pathname === '/auth'

  return (
    <div className={`min-h-screen bg-surface ${hideHeader ? '' : 'pb-28 md:pb-0'}`}>
      {!hideHeader && <Header />}
      <main className={hideHeader ? '' : 'max-w-7xl mx-auto px-4 py-6'}>
        <Outlet />
      </main>
      {!hideHeader && <MobileNav />}
    </div>
  )
}
