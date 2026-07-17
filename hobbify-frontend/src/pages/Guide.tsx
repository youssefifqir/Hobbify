import { Link, useNavigate, useParams } from 'react-router-dom'
import { useAppContext } from '../context/AppContext'
import ProgressBar from '../components/ui/ProgressBar'

export default function Guide() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { hobbies, progress, toggleProgress, user } = useAppContext()
  const hobby = hobbies.find(item => item.id === id)

  if (!hobby) {
    return (
      <div className="py-16 text-center">
        <h2 className="font-heading text-lg font-semibold mb-4">Guide not found</h2>
        <Link to="/discover" className="btn-primary inline-block">Browse hobbies</Link>
      </div>
    )
  }

  const allSteps = hobby.stages.flatMap(stage => stage.steps)
  const completedCount = allSteps.filter(step => progress.some(item => item.stepId === Number(step.id) && item.completed)).length
  const percent = allSteps.length ? Math.round((completedCount / allSteps.length) * 100) : 0

  const mark = async (stepId: string, completed: boolean) => {
    if (!user) { navigate('/auth'); return }
    await toggleProgress(stepId, completed)
  }

  return (
    <div className="max-w-3xl mx-auto">
      <Link to={`/hobby/${hobby.id}`} className="text-sm text-on-surface-variant hover:text-on-surface flex items-center gap-1 mb-6">
        <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
          <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" />
        </svg>
        Back to {hobby.name}
      </Link>

      <section className="mb-10">
        <h1 className="font-heading text-3xl md:text-4xl font-semibold text-primary mb-2">{hobby.name}</h1>
        <p className="text-on-surface-variant mb-6">Complete each step at your own pace — your progress is saved automatically.</p>

        <div className="bg-surface-container-low p-6 rounded-2xl">
          <div className="flex justify-between items-end mb-3">
            <span className="text-sm font-semibold text-on-surface-variant">Overall progress</span>
            <span className="font-heading text-2xl font-semibold text-primary">
              {completedCount}<span className="text-on-surface-variant/50 text-sm">/{allSteps.length} steps</span>
            </span>
          </div>
          <ProgressBar value={percent} />
          <p className="mt-4 text-xs text-on-surface-variant leading-relaxed">
            {allSteps.length === 0
              ? "This guide doesn't have any steps yet — check back soon."
              : completedCount < allSteps.length
                ? `You're doing great! Complete ${allSteps.length - completedCount} more step${allSteps.length - completedCount !== 1 ? 's' : ''} to finish.`
                : 'All steps complete! Great work.'}
          </p>
        </div>
      </section>

      <section className="space-y-4">
        {hobby.stages.length === 0 && (
          <div className="rounded-2xl border border-dashed border-outline-variant/40 p-8 text-center text-on-surface-variant">
            This hobby's guide hasn't been written yet. Check back soon.
          </div>
        )}
        {hobby.stages.map((stage, stageIndex) => {
          const stageCompleted = stage.steps.filter(step => progress.some(item => item.stepId === Number(step.id) && item.completed)).length
          return (
            <div key={stage.id} className="rounded-2xl border border-outline-variant/30 bg-surface-container-lowest overflow-hidden">
              <div className="flex items-center gap-4 p-5 border-b border-outline-variant/20">
                <span className="w-10 h-10 rounded-full bg-secondary-container text-secondary flex items-center justify-center font-heading font-semibold shrink-0">
                  {stageIndex + 1}
                </span>
                <div>
                  <p className="text-xs font-semibold uppercase tracking-widest text-secondary">Stage {stageIndex + 1}</p>
                  <h2 className="font-heading text-xl font-semibold text-primary">{stage.title}</h2>
                  <span className="text-xs font-semibold text-on-surface-variant">{stageCompleted}/{stage.steps.length} steps complete</span>
                </div>
              </div>
              <div className="p-5 space-y-2">
                {stage.steps.map((step, stepIndex) => {
                  const completed = progress.some(item => item.stepId === Number(step.id) && item.completed)
                  return (
                    <div key={step.id} className="flex items-center gap-3 rounded-xl bg-surface-container-low p-4">
                      <button
                        onClick={() => mark(step.id, !completed)}
                        className={`w-7 h-7 rounded-full shrink-0 flex items-center justify-center transition-all duration-200 active:scale-90 ${
                          completed ? 'bg-secondary text-white' : 'border-2 border-outline-variant hover:border-primary'
                        }`}
                        aria-label={completed ? 'Mark incomplete' : 'Mark complete'}
                      >
                        {completed && (
                          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2.5}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M4.5 12.75l6 6 9-13.5" />
                          </svg>
                        )}
                      </button>
                      <Link to={`/hobby/${hobby.id}/guide/${stage.id}/${step.id}`} className="min-w-0 flex-1">
                        <p className={completed ? 'line-through text-on-surface-variant' : 'font-semibold text-on-surface'}>
                          {stepIndex + 1}. {step.title}
                        </p>
                        <p className="text-xs text-on-surface-variant mt-0.5">{step.estimatedMinutes} min</p>
                      </Link>
                      <svg className="w-4 h-4 text-on-surface-variant shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                        <path strokeLinecap="round" strokeLinejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" />
                      </svg>
                    </div>
                  )
                })}
                {stage.steps.length === 0 && <p className="text-sm text-on-surface-variant py-2">No steps yet for this stage.</p>}
              </div>
            </div>
          )
        })}
      </section>
    </div>
  )
}
