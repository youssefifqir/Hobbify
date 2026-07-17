import { Link } from 'react-router-dom'
import { useAppContext } from '../../context/AppContext'

export default function AdminDashboard() {
  const { hobbies, progress } = useAppContext()
  const steps = hobbies.flatMap(hobby => hobby.stages.flatMap(stage => stage.steps))
  const published = hobbies.filter(h => (h.status ?? 'PUBLISHED') === 'PUBLISHED').length
  const drafts = hobbies.filter(h => h.status === 'DRAFT').length

  return (
    <div>
      <div className="mb-8">
        <h1 className="font-heading text-2xl font-semibold text-primary mb-1">Platform Health</h1>
        <p className="text-sm text-on-surface-variant">Live totals pulled straight from the Hobbify API.</p>
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
          <div className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mb-1">Guide Steps</div>
          <div className="font-heading text-2xl font-semibold text-primary">{steps.length}</div>
        </div>
      </div>

      <div className="card p-8 flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
        <div>
          <h2 className="font-heading text-xl font-semibold text-primary mb-2">Grow the library</h2>
          <p className="text-on-surface-variant max-w-md">Create a new hobby, or jump into the content library to edit an existing guide.</p>
        </div>
        <div className="flex gap-3 shrink-0">
          <Link to="/admin/content" className="btn-ghost">View library</Link>
          <Link to="/admin/content/new" className="btn-primary">Create hobby</Link>
        </div>
      </div>

      <div className="mt-6 card p-6">
        <h2 className="font-heading text-lg font-semibold text-primary mb-4">Community activity</h2>
        <p className="text-sm text-on-surface-variant">{progress.filter(item => item.completed).length} steps completed by members so far.</p>
      </div>
    </div>
  )
}
