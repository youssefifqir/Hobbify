import { useState } from 'react'
import type { FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { updateProfile } from '../lib/api'
import { useAppContext } from '../context/AppContext'
import { useToast } from '../context/ToastContext'

export default function Profile() {
  const navigate = useNavigate()
  const { user, hobbies, progress, refreshAccount, signOut } = useAppContext()
  const { showToast } = useToast()
  const [firstName, setFirstName] = useState(user?.firstName ?? '')
  const [lastName, setLastName] = useState(user?.lastName ?? '')
  const [saving, setSaving] = useState(false)

  if (!user) {
    return (
      <div className="py-20 text-center">
        <h1 className="font-heading text-3xl font-semibold text-primary">My profile</h1>
        <p className="mt-2 text-on-surface-variant">Sign in to view and edit your profile.</p>
        <button className="btn-primary mt-6" onClick={() => navigate('/auth')}>Sign in to continue</button>
      </div>
    )
  }

  const hobbiesStarted = hobbies.filter(hobby =>
    hobby.stages.some(stage => stage.steps.some(step => progress.some(item => item.stepId === Number(step.id))))
  ).length
  const completedSteps = progress.filter(item => item.completed).length

  const save = async (event: FormEvent) => {
    event.preventDefault()
    setSaving(true)
    try {
      await updateProfile(firstName, lastName)
      await refreshAccount()
      showToast('Profile updated.', 'success')
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Unable to update profile.')
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="max-w-2xl mx-auto">
      <h1 className="font-heading text-3xl md:text-4xl font-semibold text-primary mb-8">My profile</h1>

      <div className="card p-7">
        <div className="flex items-center gap-4">
          <div className="flex h-16 w-16 items-center justify-center rounded-full bg-secondary-container text-2xl font-heading font-semibold text-secondary shrink-0">
            {user.firstName.charAt(0)}{user.lastName.charAt(0)}
          </div>
          <div>
            <h2 className="font-heading text-lg font-semibold text-primary">{user.firstName} {user.lastName}</h2>
            <p className="text-sm text-on-surface-variant">{user.email}</p>
          </div>
        </div>

        <div className="mt-6 grid grid-cols-2 gap-4">
          <div className="rounded-xl bg-surface-container-low p-4 text-center">
            <p className="font-heading text-2xl font-semibold text-secondary">{hobbiesStarted}</p>
            <p className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mt-1">Hobbies</p>
          </div>
          <div className="rounded-xl bg-surface-container-low p-4 text-center">
            <p className="font-heading text-2xl font-semibold text-secondary">{completedSteps}</p>
            <p className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mt-1">Steps completed</p>
          </div>
        </div>

        <form onSubmit={save} className="mt-8 grid gap-4 sm:grid-cols-2">
          <label className="text-sm font-semibold text-on-surface">
            First name
            <input value={firstName} onChange={e => setFirstName(e.target.value)} className="mt-2 w-full rounded-xl bg-surface-container-low border-none p-3 text-sm font-normal outline-none focus:ring-2 focus:ring-secondary transition-shadow" />
          </label>
          <label className="text-sm font-semibold text-on-surface">
            Last name
            <input value={lastName} onChange={e => setLastName(e.target.value)} className="mt-2 w-full rounded-xl bg-surface-container-low border-none p-3 text-sm font-normal outline-none focus:ring-2 focus:ring-secondary transition-shadow" />
          </label>
          <button disabled={saving} className="btn-primary sm:col-span-2 disabled:opacity-60">{saving ? 'Saving…' : 'Save profile'}</button>
        </form>

        <button
          onClick={async () => { await signOut(); navigate('/') }}
          className="mt-8 text-sm font-semibold text-error hover:underline"
        >
          Sign out
        </button>
      </div>
    </div>
  )
}
