import { useEffect, useState } from 'react'
import { getAdminUsers, setUserEnabled, type AdminUser } from '../../lib/api'
import { useToast } from '../../context/ToastContext'

export default function AdminUsers() {
  const { showToast } = useToast()
  const [users, setUsers] = useState<AdminUser[]>([])
  const [search, setSearch] = useState('')
  const [loading, setLoading] = useState(true)

  const load = async () => {
    try {
      setUsers(await getAdminUsers())
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Unable to load users.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { void load() }, [])

  const toggle = async (user: AdminUser) => {
    try {
      await setUserEnabled(user.id, !user.enabled)
      await load()
      showToast(`User ${user.enabled ? 'deactivated' : 'activated'}.`, 'success')
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Unable to update user.')
    }
  }

  const filtered = users.filter(u =>
    !search || `${u.firstName} ${u.lastName} ${u.email}`.toLowerCase().includes(search.toLowerCase())
  )
  const active = users.filter(u => u.enabled).length
  const admins = users.filter(u => u.roles.some(r => r.includes('ADMIN'))).length

  return (
    <div>
      <div className="mb-8">
        <h1 className="font-heading text-2xl font-semibold text-primary mb-1">Users</h1>
        <p className="text-sm text-on-surface-variant max-w-lg">Review new sign-ups, manage access, and keep the community healthy.</p>
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-3 gap-4 mb-8">
        <div className="card p-5">
          <div className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mb-1">Total Users</div>
          <div className="font-heading text-2xl font-semibold text-primary">{users.length}</div>
        </div>
        <div className="card p-5">
          <div className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mb-1">Active</div>
          <div className="font-heading text-2xl font-semibold text-secondary">{active}</div>
        </div>
        <div className="card p-5">
          <div className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mb-1">Admins</div>
          <div className="font-heading text-2xl font-semibold text-primary">{admins}</div>
        </div>
      </div>

      <div className="card p-6">
        <div className="relative max-w-sm mb-6">
          <svg className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-on-surface-variant" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
            <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
          <input
            value={search}
            onChange={e => setSearch(e.target.value)}
            placeholder="Search users..."
            className="w-full pl-10 pr-4 py-2 rounded-full bg-surface-container-low text-sm outline-none focus:ring-2 focus:ring-secondary/30 transition-all"
          />
        </div>

        <div className="overflow-x-auto -mx-6 px-6">
          <table className="w-full text-sm min-w-[560px]">
            <thead>
              <tr className="text-left text-xs font-semibold text-on-surface-variant uppercase tracking-wider border-b border-outline-variant/30">
                <th className="pb-3 pr-4">User</th>
                <th className="pb-3 px-4">Roles</th>
                <th className="pb-3 px-4">Status</th>
                <th className="pb-3 pl-4 text-right">Action</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(user => (
                <tr key={user.id} className="border-b border-outline-variant/20 last:border-0">
                  <td className="py-4 pr-4">
                    <div className="flex items-center gap-3">
                      <div className="w-9 h-9 rounded-full bg-secondary-container text-secondary flex items-center justify-center text-xs font-heading font-semibold shrink-0">
                        {user.firstName.charAt(0)}{user.lastName.charAt(0)}
                      </div>
                      <div className="min-w-0">
                        <div className="font-semibold text-on-surface truncate">{user.firstName} {user.lastName}</div>
                        <div className="text-xs text-on-surface-variant truncate">{user.email}</div>
                      </div>
                    </div>
                  </td>
                  <td className="py-4 px-4">
                    <div className="flex flex-wrap gap-1.5">
                      {user.roles.map(role => (
                        <span key={role} className="badge bg-tertiary-fixed text-primary">{role.replace('ROLE_', '')}</span>
                      ))}
                    </div>
                  </td>
                  <td className="py-4 px-4">
                    <span className={`inline-flex items-center gap-1.5 font-semibold text-xs ${user.enabled ? 'text-secondary' : 'text-on-surface-variant'}`}>
                      <span className={`w-1.5 h-1.5 rounded-full ${user.enabled ? 'bg-secondary' : 'bg-on-surface-variant'}`} />
                      {user.enabled ? 'Active' : 'Pending / inactive'}
                    </span>
                  </td>
                  <td className="py-4 pl-4 text-right">
                    <button
                      onClick={() => toggle(user)}
                      className={`text-xs font-semibold hover:underline ${user.enabled ? 'text-error' : 'text-secondary'}`}
                    >
                      {user.enabled ? 'Deactivate' : 'Activate'}
                    </button>
                  </td>
                </tr>
              ))}
              {!loading && filtered.length === 0 && (
                <tr><td colSpan={4} className="py-12 text-center text-on-surface-variant">No users match your search.</td></tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}
