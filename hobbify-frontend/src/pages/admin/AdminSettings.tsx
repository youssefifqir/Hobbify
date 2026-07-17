import { useState } from 'react'

const initialToggles = [
  { id: 'new-signups', label: 'Email me on new sign-ups', description: 'Get notified when a new member joins Hobbify.' },
  { id: 'guide-updates', label: 'Notify on guide completion', description: 'Get notified when a member finishes a guided hobby path.' },
  { id: 'public-library', label: 'Public content library', description: 'Allow the hobby library to be browsed without an account.' },
]

export default function AdminSettings() {
  const [toggles, setToggles] = useState<Record<string, boolean>>({ 'new-signups': true, 'guide-updates': true, 'public-library': false })

  const toggle = (id: string) => setToggles(prev => ({ ...prev, [id]: !prev[id] }))

  return (
    <div>
      <div className="mb-8">
        <h1 className="font-heading text-2xl font-semibold text-primary mb-1">Settings</h1>
        <p className="text-sm text-on-surface-variant">Manage notification preferences and library visibility.</p>
      </div>

      <div className="card p-6 max-w-2xl space-y-1">
        {initialToggles.map(t => (
          <div key={t.id} className="flex items-center justify-between gap-6 py-4 border-b border-outline-variant/20 last:border-0">
            <div>
              <div className="text-sm font-semibold text-on-surface">{t.label}</div>
              <div className="text-xs text-on-surface-variant mt-0.5">{t.description}</div>
            </div>
            <button
              onClick={() => toggle(t.id)}
              aria-pressed={toggles[t.id]}
              className={`relative w-11 h-6 rounded-full shrink-0 transition-colors ${toggles[t.id] ? 'bg-secondary' : 'bg-surface-container-high'}`}
            >
              <span className={`absolute top-0.5 left-0.5 w-5 h-5 rounded-full bg-white shadow transition-transform ${toggles[t.id] ? 'translate-x-5' : ''}`} />
            </button>
          </div>
        ))}
      </div>
    </div>
  )
}
