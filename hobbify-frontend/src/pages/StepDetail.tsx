import { useMemo } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { useAppContext } from '../context/AppContext'

export default function StepDetail() {
  const { id, stageId, stepId } = useParams<{ id: string; stageId: string; stepId: string }>()
  const navigate = useNavigate()
  const { hobbies, progress, toggleProgress, user } = useAppContext()
  const hobby = hobbies.find(item => item.id === id)

  const { stage, step, stageIndex, stepIndex } = useMemo(() => {
    if (!hobby) return { stage: null, step: null, stageIndex: -1, stepIndex: -1 }
    const si = hobby.stages.findIndex(s => s.id === stageId)
    if (si === -1) return { stage: null, step: null, stageIndex: -1, stepIndex: -1 }
    const s = hobby.stages[si]
    const sti = s.steps.findIndex(st => st.id === stepId)
    return { stage: s, step: sti >= 0 ? s.steps[sti] : null, stageIndex: si, stepIndex: sti }
  }, [hobby, stageId, stepId])

  if (!hobby || !stage || !step) {
    return (
      <div className="py-16 text-center">
        <h2 className="font-heading text-lg font-semibold mb-4">Step not found</h2>
        <Link to={`/hobby/${id}/guide`} className="btn-primary inline-block">Back to guide</Link>
      </div>
    )
  }

  const completed = progress.some(item => item.stepId === Number(step.id) && item.completed)
  const toggle = async () => { if (!user) { navigate('/auth'); return }; await toggleProgress(step.id, !completed) }

  const prevStep = stepIndex > 0
    ? { stageId: stage.id, stepId: stage.steps[stepIndex - 1].id }
    : stageIndex > 0
      ? { stageId: hobby.stages[stageIndex - 1].id, stepId: hobby.stages[stageIndex - 1].steps.at(-1)?.id }
      : null

  const nextStep = stepIndex < stage.steps.length - 1
    ? { stageId: stage.id, stepId: stage.steps[stepIndex + 1].id }
    : stageIndex < hobby.stages.length - 1
      ? { stageId: hobby.stages[stageIndex + 1].id, stepId: hobby.stages[stageIndex + 1].steps[0]?.id }
      : null

  return (
    <div className="max-w-3xl mx-auto">
      <div className="flex items-center gap-2 text-xs text-on-surface-variant mb-6 overflow-x-auto pb-2">
        <Link to={`/hobby/${hobby.id}`} className="whitespace-nowrap bg-surface-container-low px-2.5 py-1 rounded-full hover:bg-surface-container transition-colors">{hobby.name}</Link>
        <svg className="w-3 h-3 shrink-0 text-outline" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
          <path strokeLinecap="round" strokeLinejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" />
        </svg>
        <Link to={`/hobby/${hobby.id}/guide`} className="whitespace-nowrap bg-surface-container-low px-2.5 py-1 rounded-full hover:bg-surface-container transition-colors">{stage.title}</Link>
        <svg className="w-3 h-3 shrink-0 text-outline" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
          <path strokeLinecap="round" strokeLinejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" />
        </svg>
        <span className="whitespace-nowrap bg-surface-container-low px-2.5 py-1 rounded-full text-primary font-semibold">{step.title}</span>
      </div>

      <div className="rounded-2xl bg-surface-container-low p-7 md:p-10">
        <div className="flex items-center justify-between mb-3">
          <span className="bg-secondary-container text-secondary px-3 py-1 rounded-full text-xs font-semibold flex items-center gap-1.5">
            <svg className="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            {step.estimatedMinutes} min
          </span>
          <span className="text-sm font-semibold text-on-surface-variant">{completed ? 'Completed' : 'In progress'}</span>
        </div>

        <h1 className="font-heading text-2xl md:text-4xl font-semibold text-primary mb-6">{step.title}</h1>
        <div className="text-base md:text-lg text-on-surface-variant leading-relaxed whitespace-pre-line">{step.content}</div>

        <button
          onClick={toggle}
          className={`w-full mt-10 rounded-full px-6 py-4 font-semibold transition-all duration-300 ${
            completed ? 'bg-secondary-container text-secondary border-2 border-secondary' : 'bg-primary text-on-primary shadow-lg active:scale-[0.98]'
          }`}
        >
          {completed ? 'Completed ✓' : 'Mark complete'}
        </button>
      </div>

      <div className="flex items-center justify-between mt-6">
        {prevStep ? (
          <Link to={`/hobby/${hobby.id}/guide/${prevStep.stageId}/${prevStep.stepId}`} className="btn-ghost">
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" />
            </svg>
            Previous
          </Link>
        ) : <span />}
        {nextStep ? (
          <Link to={`/hobby/${hobby.id}/guide/${nextStep.stageId}/${nextStep.stepId}`} className="btn-secondary">
            Next
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" />
            </svg>
          </Link>
        ) : (
          <Link to={`/hobby/${hobby.id}/guide`} className="btn-secondary">Back to guide overview</Link>
        )}
      </div>
    </div>
  )
}
