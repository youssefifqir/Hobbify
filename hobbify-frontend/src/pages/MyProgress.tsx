import { Link, useNavigate } from 'react-router-dom'
import ProgressBar from '../components/ui/ProgressBar'
import { useAppContext } from '../context/AppContext'
import { getHobbyImage } from '../lib/hobbyImages'

export default function MyProgress() {
  const navigate = useNavigate()
  const { user, hobbies, progress } = useAppContext()

  if (!user) {
    return (
      <div className="py-20 text-center">
        <h1 className="font-heading text-3xl font-semibold text-primary">My progress</h1>
        <p className="mt-2 text-on-surface-variant">Sign in to see progress across your hobbies.</p>
        <button className="btn-primary mt-6" onClick={() => navigate('/auth')}>Sign in to continue</button>
      </div>
    )
  }

  const started = hobbies
    .map(hobby => {
      const steps = hobby.stages.flatMap(stage => stage.steps)
      const completed = steps.filter(step => progress.some(item => item.stepId === Number(step.id) && item.completed)).length
      const percent = steps.length ? Math.round((completed / steps.length) * 100) : 0
      return { hobby, completed, total: steps.length, percent }
    })
    .filter(entry => entry.completed > 0)

  return (
    <div>
      <h1 className="font-heading text-3xl md:text-4xl font-semibold text-primary">My journey</h1>
      <p className="mt-2 text-on-surface-variant">Keep growing. Every completed step is saved to your account.</p>

      {started.length > 0 ? (
        <div className="mt-8 space-y-5">
          {started.map(({ hobby, completed, total, percent }) => (
            <Link key={hobby.id} to={`/hobby/${hobby.id}/guide`} className="card p-6 flex items-center gap-5 hover:shadow-lg transition-shadow">
              <div className="w-16 h-16 rounded-xl overflow-hidden shrink-0">
                <img src={getHobbyImage(hobby)} alt="" className="w-full h-full object-cover" />
              </div>
              <div className="min-w-0 flex-1">
                <div className="flex items-start justify-between gap-4">
                  <div className="min-w-0">
                    <p className="text-sm font-semibold text-secondary">{hobby.category}</p>
                    <h2 className="font-heading text-xl font-semibold text-primary truncate">{hobby.name}</h2>
                  </div>
                  <p className="font-heading text-2xl font-semibold text-primary shrink-0">{percent}%</p>
                </div>
                <div className="mt-4">
                  <ProgressBar value={percent} />
                  <p className="mt-1.5 text-xs text-on-surface-variant">{completed} of {total} steps</p>
                </div>
              </div>
            </Link>
          ))}
        </div>
      ) : (
        <div className="mt-8 rounded-2xl bg-surface-container-low p-12 text-center">
          <p className="text-on-surface-variant">You haven't started a hobby yet.</p>
        </div>
      )}

      <section className="mt-10 rounded-3xl bg-primary p-8 text-white flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
        <div>
          <h2 className="font-heading text-xl font-semibold mb-2">Feeling adventurous?</h2>
          <p className="text-white/75 max-w-md">Browse more hobbies and start your next great obsession.</p>
        </div>
        <Link to="/discover" className="shrink-0 rounded-full bg-white text-primary px-6 py-3 font-semibold hover:opacity-90 transition-all active:scale-95">
          Explore hobbies
        </Link>
      </section>
    </div>
  )
}
