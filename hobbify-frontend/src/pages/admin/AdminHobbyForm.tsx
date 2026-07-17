import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom'
import {
  createAdminHobby, updateAdminHobby, getCatalogHobby,
  createAdminStage, updateAdminStage, deleteAdminStage,
  createAdminStep, updateAdminStep, deleteAdminStep,
} from '../../lib/api'
import { useAppContext } from '../../context/AppContext'
import { useToast } from '../../context/ToastContext'
import ImageUpload from '../../components/ui/ImageUpload'
import EmojiPicker from '../../components/ui/EmojiPicker'
import type { Hobby, Stage } from '../../types'

type FormValues = Pick<Hobby, 'name' | 'description' | 'category' | 'cost' | 'space' | 'time' | 'difficulty' | 'icon' | 'image' | 'status'>
const empty: FormValues = {
  name: '', description: '', category: '', cost: 'LOW', space: 'MINIMAL', time: 'LIGHT', difficulty: 'BEGINNER',
  icon: '🌱', image: '', status: 'DRAFT',
}

export default function AdminHobbyForm() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const location = useLocation()
  const { hobbies, refreshCatalog } = useAppContext()
  const { showToast } = useToast()

  const [form, setForm] = useState<FormValues>(empty)
  const [saving, setSaving] = useState(false)
  const [stages, setStages] = useState<Stage[]>([])
  const [expanded, setExpanded] = useState<string | null>((location.state as { expandStageId?: string } | null)?.expandStageId ?? null)

  useEffect(() => {
    const hobby = hobbies.find(item => item.id === id)
    if (hobby) {
      setForm({
        name: hobby.name, description: hobby.description, category: hobby.category,
        cost: hobby.cost, space: hobby.space, time: hobby.time, difficulty: hobby.difficulty,
        icon: hobby.icon ?? '🌱', image: hobby.image ?? '', status: hobby.status ?? 'DRAFT',
      })
    }
  }, [hobbies, id])

  const loadStages = async () => {
    if (!id) return
    const hobby = await getCatalogHobby(id)
    setStages(hobby.stages)
  }

  useEffect(() => { void loadStages() }, [id])

  const set = <K extends keyof FormValues>(key: K, value: FormValues[K]) => setForm(current => ({ ...current, [key]: value }))

  const save = async (event: FormEvent, status: FormValues['status']) => {
    event.preventDefault()
    setSaving(true)
    const payload = { ...form, status }
    try {
      if (id) {
        await updateAdminHobby(id, payload)
        await refreshCatalog()
        showToast('Hobby saved.', 'success')
      } else {
        const created = await createAdminHobby(payload)
        await refreshCatalog()
        showToast('Hobby created — now add stages and steps below.', 'success')
        navigate(`/admin/content/${created.id}/edit`)
        return
      }
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Unable to save hobby.')
    } finally {
      setSaving(false)
    }
  }

  const addStage = async () => {
    try {
      if (!id) {
        if (!form.name || !form.category || !form.description) {
          showToast('Fill in the hobby name, category, and description first.')
          return
        }
        const created = await createAdminHobby(form)
        await refreshCatalog()
        const newStage = await createAdminStage(created.id, 'New stage', 1)
        showToast('Hobby created.', 'success')
        navigate(`/admin/content/${created.id}/edit`, { state: { expandStageId: String(newStage.id) } })
        return
      }
      const newStage = await createAdminStage(id, 'New stage', stages.length + 1)
      await loadStages()
      setExpanded(String(newStage.id))
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Unable to add stage.')
    }
  }

  const renameStage = async (stage: Stage, title: string) => {
    if (title === stage.title) return
    try {
      await updateAdminStage(stage.id, title, stage.order)
      await loadStages()
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Unable to update stage.')
    }
  }

  const removeStage = async (stage: Stage) => {
    if (!confirm(`Delete stage "${stage.title}" and all its steps?`)) return
    try {
      await deleteAdminStage(stage.id)
      await loadStages()
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Unable to delete stage.')
    }
  }

  const addStep = async (stage: Stage) => {
    try {
      await createAdminStep(stage.id, 'New step', 'Describe what to do in this step.', stage.steps.length + 1, 15)
      await loadStages()
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Unable to add step.')
    }
  }

  const saveStep = async (stepId: string, title: string, content: string, order: number, estimatedMinutes: number) => {
    try {
      await updateAdminStep(stepId, title, content, order, estimatedMinutes)
      await loadStages()
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Unable to update step.')
    }
  }

  const removeStep = async (stepId: string) => {
    if (!confirm('Delete this step?')) return
    try {
      await deleteAdminStep(stepId)
      await loadStages()
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Unable to delete step.')
    }
  }

  return (
    <form onSubmit={e => save(e, form.status)}>
      <div className="flex flex-wrap items-center justify-between gap-4 mb-8">
        <div>
          <Link to="/admin/content" className="text-xs text-on-surface-variant hover:text-on-surface flex items-center gap-1 mb-2">
            <svg className="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" />
            </svg>
            Content Library
          </Link>
          <h1 className="font-heading text-2xl font-semibold text-primary">{form.name || (id ? 'Edit hobby' : 'Create new hobby')}</h1>
          <p className="text-sm text-on-surface-variant mt-1">{form.description || 'Fill in the details below.'}</p>
        </div>
        <div className="flex items-center gap-3">
          <button type="button" disabled={saving} onClick={e => save(e, 'DRAFT')} className="btn-ghost disabled:opacity-60">Save Draft</button>
          <button type="button" disabled={saving} onClick={e => save(e, 'PUBLISHED')} className="btn-primary disabled:opacity-60">
            {saving ? 'Saving…' : 'Publish Changes'}
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <section className="card p-6 space-y-5 h-fit">
          <h2 className="font-heading text-lg font-semibold flex items-center gap-2">
            <svg className="w-5 h-5 text-secondary" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L6.832 19.82a4.5 4.5 0 01-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 011.13-1.897L16.862 4.487z" />
            </svg>
            Basic Info
          </h2>
          <div>
            <label className="text-sm font-semibold text-on-surface mb-2 block">Hobby Name</label>
            <input required value={form.name} onChange={e => set('name', e.target.value)} placeholder="e.g. Urban Gardening" className="w-full bg-surface-container-low border-none rounded-xl p-3 text-sm outline-none focus:ring-2 focus:ring-secondary transition-shadow" />
          </div>
          <div>
            <label className="text-sm font-semibold text-on-surface mb-2 block">Category</label>
            <input required value={form.category} onChange={e => set('category', e.target.value)} placeholder="e.g. Lifestyle" className="w-full bg-surface-container-low border-none rounded-xl p-3 text-sm outline-none focus:ring-2 focus:ring-secondary transition-shadow" />
          </div>
          <div>
            <label className="text-sm font-semibold text-on-surface mb-2 block">Detailed Description</label>
            <textarea required rows={4} value={form.description} onChange={e => set('description', e.target.value)} placeholder="A beginner-friendly guide to..." className="w-full bg-surface-container-low border-none rounded-xl p-3 text-sm outline-none focus:ring-2 focus:ring-secondary transition-shadow resize-none" />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="text-sm font-semibold text-on-surface mb-2 block">Cost</label>
              <select value={form.cost} onChange={e => set('cost', e.target.value as FormValues['cost'])} className="w-full bg-surface-container-low border-none rounded-xl p-2.5 text-sm outline-none focus:ring-2 focus:ring-secondary transition-shadow">
                <option value="LOW">Low ($)</option>
                <option value="MEDIUM">Medium ($$)</option>
                <option value="HIGH">High ($$$)</option>
              </select>
            </div>
            <div>
              <label className="text-sm font-semibold text-on-surface mb-2 block">Space needed</label>
              <select value={form.space} onChange={e => set('space', e.target.value as FormValues['space'])} className="w-full bg-surface-container-low border-none rounded-xl p-2.5 text-sm outline-none focus:ring-2 focus:ring-secondary transition-shadow">
                <option value="MINIMAL">Minimal</option>
                <option value="MODERATE">Moderate</option>
                <option value="DEDICATED">Dedicated</option>
              </select>
            </div>
            <div>
              <label className="text-sm font-semibold text-on-surface mb-2 block">Time / week</label>
              <select value={form.time} onChange={e => set('time', e.target.value as FormValues['time'])} className="w-full bg-surface-container-low border-none rounded-xl p-2.5 text-sm outline-none focus:ring-2 focus:ring-secondary transition-shadow">
                <option value="LIGHT">Light</option>
                <option value="MODERATE">Moderate</option>
                <option value="INTENSIVE">Intensive</option>
              </select>
            </div>
            <div>
              <label className="text-sm font-semibold text-on-surface mb-2 block">Difficulty</label>
              <select value={form.difficulty} onChange={e => set('difficulty', e.target.value as FormValues['difficulty'])} className="w-full bg-surface-container-low border-none rounded-xl p-2.5 text-sm outline-none focus:ring-2 focus:ring-secondary transition-shadow">
                <option value="BEGINNER">Beginner</option>
                <option value="INTERMEDIATE">Intermediate</option>
                <option value="ADVANCED">Advanced</option>
              </select>
            </div>
          </div>
        </section>

        <section className="card p-6 space-y-5 h-fit">
          <h2 className="font-heading text-lg font-semibold flex items-center gap-2">
            <svg className="w-5 h-5 text-secondary" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5zm10.5-11.25h.008v.008h-.008V8.25zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0z" />
            </svg>
            Media Assets
          </h2>
          <ImageUpload value={form.image ?? ''} onChange={v => set('image', v)} />
          <div>
            <label className="text-sm font-semibold text-on-surface mb-2 block">Icon</label>
            <EmojiPicker value={form.icon ?? ''} onChange={v => set('icon', v)} />
          </div>
          <div className="flex items-center gap-2 pt-2">
            <span className={`w-2 h-2 rounded-full ${form.status === 'PUBLISHED' ? 'bg-secondary' : 'bg-on-surface-variant'}`} />
            <span className="text-sm font-semibold text-on-surface-variant">{form.status === 'PUBLISHED' ? 'Published — visible to members' : 'Draft — hidden from members'}</span>
          </div>
        </section>
      </div>

      <section className="card p-6 mt-6">
        <div className="flex items-center justify-between mb-5">
          <h2 className="font-heading text-lg font-semibold flex items-center gap-2">
            <svg className="w-5 h-5 text-secondary" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M9 6.75V15m6-6v8.25m.503 3.498l4.875-2.437c.381-.19.622-.58.622-1.006V4.82c0-.836-.88-1.38-1.628-1.006l-3.869 1.934c-.317.159-.69.159-1.006 0L9.503 3.752a1.125 1.125 0 00-1.006 0L3.622 6.189C3.24 6.38 3 6.77 3 7.195v10.965c0 .836.88 1.38 1.628 1.006l3.869-1.934c.317-.159.69-.159 1.006 0l4.994 2.497c.317.158.69.158 1.006 0z" />
            </svg>
            Journey Builder
          </h2>
          <button type="button" onClick={addStage} className="w-9 h-9 rounded-full bg-secondary-container text-secondary flex items-center justify-center hover:opacity-80 transition-opacity">+</button>
        </div>

        <div className="space-y-4">
            {stages.map((stage, stageIndex) => {
              const isOpen = expanded === stage.id
              return (
                <div key={stage.id} className="rounded-2xl bg-surface-container-low overflow-hidden">
                  <div className="flex items-center gap-3 p-4">
                    <span className="w-7 h-7 rounded-full bg-primary text-on-primary flex items-center justify-center text-xs font-bold shrink-0">{stageIndex + 1}</span>
                    <input
                      defaultValue={stage.title}
                      onBlur={e => renameStage(stage, e.target.value)}
                      className="flex-1 bg-transparent border-none font-heading font-semibold outline-none focus:bg-surface-container-lowest rounded-lg px-2 py-1 -mx-2"
                    />
                    <span className="text-xs text-on-surface-variant whitespace-nowrap">{stage.steps.length} {stage.steps.length === 1 ? 'step' : 'steps'}</span>
                    <button type="button" onClick={() => removeStage(stage)} className="text-on-surface-variant hover:text-error transition-colors" aria-label="Delete stage">
                      <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}><path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" /></svg>
                    </button>
                    <button type="button" onClick={() => setExpanded(isOpen ? null : stage.id)} className="text-on-surface-variant" aria-label="Toggle stage">
                      <svg className={`w-4 h-4 transition-transform ${isOpen ? 'rotate-180' : ''}`} fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}><path strokeLinecap="round" strokeLinejoin="round" d="M19.5 8.25l-7.5 7.5-7.5-7.5" /></svg>
                    </button>
                  </div>

                  {isOpen && (
                    <div className="px-4 pb-4 space-y-3">
                      <p className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider pt-1">
                        Steps — the tasks a member checks off inside this stage
                      </p>
                      {stage.steps.map((step, stepIndex) => (
                        <div key={step.id} className="rounded-xl bg-surface-container-lowest p-4 space-y-2 border border-outline-variant/20">
                          <div className="flex items-center gap-2">
                            <span className="w-5 h-5 rounded-full bg-secondary-container text-secondary flex items-center justify-center text-[10px] font-bold shrink-0">{stepIndex + 1}</span>
                            <span className="text-xs font-semibold text-on-surface-variant shrink-0">Step {stepIndex + 1}</span>
                            <input
                              defaultValue={step.title}
                              onBlur={e => saveStep(step.id, e.target.value, step.content, stepIndex + 1, step.estimatedMinutes)}
                              className="flex-1 bg-surface-container-low border-none rounded-lg p-2 text-sm font-semibold outline-none focus:ring-2 focus:ring-secondary transition-shadow"
                            />
                            <input
                              type="number"
                              min={0}
                              defaultValue={step.estimatedMinutes}
                              onBlur={e => saveStep(step.id, step.title, step.content, stepIndex + 1, Number(e.target.value))}
                              className="w-20 bg-surface-container-low border-none rounded-lg p-2 text-xs outline-none focus:ring-2 focus:ring-secondary transition-shadow"
                            />
                            <span className="text-xs text-on-surface-variant shrink-0">min</span>
                            <button type="button" onClick={() => removeStep(step.id)} className="text-on-surface-variant hover:text-error transition-colors shrink-0" aria-label="Delete step">
                              <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}><path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" /></svg>
                            </button>
                          </div>
                          <textarea
                            defaultValue={step.content}
                            onBlur={e => saveStep(step.id, step.title, e.target.value, stepIndex + 1, step.estimatedMinutes)}
                            rows={2}
                            placeholder="Step instructions…"
                            className="w-full bg-surface-container-low border-none rounded-lg p-2 text-sm outline-none focus:ring-2 focus:ring-secondary transition-shadow resize-none"
                          />
                        </div>
                      ))}
                      <button type="button" onClick={() => addStep(stage)} className="btn-secondary text-xs px-4 py-2">+ Add step</button>
                    </div>
                  )}
                </div>
              )
            })}
            {stages.length === 0 && <p className="text-sm text-on-surface-variant py-4 text-center">No stages yet. Add one to start the guide.</p>}
        </div>
      </section>
    </form>
  )
}
