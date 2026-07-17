import { useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import { deleteAdminHobby } from '../../lib/api'
import { useAppContext } from '../../context/AppContext'
import { useToast } from '../../context/ToastContext'
import { getHobbyImage } from '../../lib/hobbyImages'

type Tab = 'ALL' | 'PUBLISHED' | 'DRAFT'

export default function AdminContentLibrary() {
  const { hobbies, refreshCatalog } = useAppContext()
  const { showToast } = useToast()
  const [tab, setTab] = useState<Tab>('ALL')
  const [category, setCategory] = useState('All')
  const [search, setSearch] = useState('')

  const categories = useMemo(() => [...new Set(hobbies.map(h => h.category))].sort(), [hobbies])
  const published = hobbies.filter(h => (h.status ?? 'PUBLISHED') === 'PUBLISHED').length
  const drafts = hobbies.filter(h => h.status === 'DRAFT').length
  const totalSteps = hobbies.reduce((sum, h) => sum + h.stages.reduce((s, st) => s + st.steps.length, 0), 0)

  const filtered = hobbies.filter(h => {
    if (tab === 'PUBLISHED' && (h.status ?? 'PUBLISHED') !== 'PUBLISHED') return false
    if (tab === 'DRAFT' && h.status !== 'DRAFT') return false
    if (category !== 'All' && h.category !== category) return false
    if (search && !h.name.toLowerCase().includes(search.toLowerCase())) return false
    return true
  })

  const remove = async (id: string, name: string) => {
    if (!confirm(`Delete "${name}"? This cannot be undone.`)) return
    try {
      await deleteAdminHobby(id)
      await refreshCatalog()
      showToast('Hobby deleted.', 'success')
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Unable to delete hobby.')
    }
  }

  return (
    <div>
      <div className="flex flex-wrap items-start justify-between gap-4 mb-8">
        <div>
          <h1 className="font-heading text-2xl font-semibold text-primary mb-1">Content Library</h1>
          <p className="text-sm text-on-surface-variant max-w-lg">Manage your collection of hobbies. Create, edit, and curate the experiences that inspire our community.</p>
        </div>
        <Link to="/admin/content/new" className="btn-primary">
          Create New Hobby
          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
            <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 4.5L21 12m0 0l-7.5 7.5M21 12H3" />
          </svg>
        </Link>
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <div className="card p-5">
          <div className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mb-1">Total Hobbies</div>
          <div className="font-heading text-2xl font-semibold text-primary">{hobbies.length}</div>
        </div>
        <div className="card p-5">
          <div className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mb-1">Published</div>
          <div className="font-heading text-2xl font-semibold text-secondary">{published}</div>
        </div>
        <div className="card p-5">
          <div className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mb-1">Drafts</div>
          <div className="font-heading text-2xl font-semibold text-primary">{drafts}</div>
        </div>
        <div className="card p-5">
          <div className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mb-1">Total Guide Steps</div>
          <div className="font-heading text-2xl font-semibold text-primary">{totalSteps}</div>
        </div>
      </div>

      <div className="card p-6">
        <div className="flex flex-wrap items-center gap-3 mb-6">
          <div className="flex items-center gap-1 bg-surface-container-low rounded-full p-1">
            {(['ALL', 'PUBLISHED', 'DRAFT'] as Tab[]).map(t => (
              <button
                key={t}
                onClick={() => setTab(t)}
                className={`px-4 py-1.5 rounded-full text-sm font-semibold transition-all ${tab === t ? 'bg-surface-container-lowest shadow-sm text-primary' : 'text-on-surface-variant'}`}
              >
                {t === 'ALL' ? 'All' : t === 'PUBLISHED' ? 'Published' : 'Drafts'}
              </button>
            ))}
          </div>
          <select
            value={category}
            onChange={e => setCategory(e.target.value)}
            className="px-4 py-2 rounded-full bg-surface-container-low text-sm font-semibold outline-none focus:ring-2 focus:ring-secondary/30 transition-all"
          >
            <option value="All">Category: All</option>
            {categories.map(c => <option key={c} value={c}>{c}</option>)}
          </select>
          <div className="relative flex-1 min-w-[200px]">
            <svg className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-on-surface-variant" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
            <input
              type="text"
              value={search}
              onChange={e => setSearch(e.target.value)}
              placeholder="Search hobbies..."
              className="w-full pl-10 pr-4 py-2 rounded-full bg-surface-container-low text-sm outline-none focus:ring-2 focus:ring-secondary/30 transition-all"
            />
          </div>
          <span className="text-xs text-on-surface-variant whitespace-nowrap">Showing {filtered.length} of {hobbies.length}</span>
        </div>

        <div className="overflow-x-auto -mx-6 px-6">
          <table className="w-full text-sm min-w-[760px]">
            <thead>
              <tr className="text-left text-xs font-semibold text-on-surface-variant uppercase tracking-wider border-b border-outline-variant/30">
                <th className="pb-3 pr-4">Hobby Name</th>
                <th className="pb-3 px-4">Category</th>
                <th className="pb-3 px-4">Status</th>
                <th className="pb-3 px-4">Guide</th>
                <th className="pb-3 pl-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(h => {
                const status = h.status ?? 'PUBLISHED'
                return (
                  <tr key={h.id} className="border-b border-outline-variant/20 last:border-0">
                    <td className="py-4 pr-4">
                      <div className="flex items-center gap-3">
                        <div className="w-11 h-11 rounded-xl overflow-hidden shrink-0 bg-surface-container-high">
                          <img src={getHobbyImage(h)} alt={h.name} className="w-full h-full object-cover" />
                        </div>
                        <div className="min-w-0">
                          <div className="font-semibold text-on-surface truncate">{h.name}</div>
                          <div className="text-xs text-on-surface-variant">Skill: {h.difficulty}</div>
                        </div>
                      </div>
                    </td>
                    <td className="py-4 px-4">
                      <span className="badge bg-tertiary-fixed text-primary">{h.category}</span>
                    </td>
                    <td className="py-4 px-4">
                      <span className={`inline-flex items-center gap-1.5 font-semibold text-xs ${status === 'PUBLISHED' ? 'text-secondary' : status === 'IN_REVIEW' ? 'text-primary' : 'text-on-surface-variant'}`}>
                        <span className={`w-1.5 h-1.5 rounded-full ${status === 'PUBLISHED' ? 'bg-secondary' : status === 'IN_REVIEW' ? 'bg-primary' : 'bg-on-surface-variant'}`} />
                        {status === 'PUBLISHED' ? 'Published' : status === 'IN_REVIEW' ? 'In review' : 'Draft'}
                      </span>
                    </td>
                    <td className="py-4 px-4 text-on-surface-variant">{h.stages.length} stages &middot; {h.stages.reduce((s, st) => s + st.steps.length, 0)} steps</td>
                    <td className="py-4 pl-4">
                      <div className="flex items-center justify-end gap-4">
                        <Link to={`/hobby/${h.id}`} className="text-on-surface-variant hover:text-on-surface" aria-label="View">
                          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M2.036 12.322a1.012 1.012 0 010-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178z" />
                            <path strokeLinecap="round" strokeLinejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                          </svg>
                        </Link>
                        <Link to={`/admin/content/${h.id}/edit`} className="text-secondary hover:opacity-70" aria-label="Edit">
                          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931z" />
                          </svg>
                        </Link>
                        <button onClick={() => remove(h.id, h.name)} className="text-error hover:opacity-70" aria-label="Delete">
                          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
                          </svg>
                        </button>
                      </div>
                    </td>
                  </tr>
                )
              })}
              {filtered.length === 0 && (
                <tr>
                  <td colSpan={5} className="py-12 text-center text-on-surface-variant">No hobbies match your filters.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}
