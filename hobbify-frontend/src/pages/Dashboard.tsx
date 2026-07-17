import { Link, useNavigate } from 'react-router-dom'
import { useAppContext } from '../context/AppContext'
import ProgressBar from '../components/ui/ProgressBar'
import { getHobbyImage } from '../lib/hobbyImages'

export default function Dashboard() {
  const navigate = useNavigate()
  const { user, hobbies, progress, achievements } = useAppContext()

  if (!user) {
    return (
      <div className="py-20 text-center">
        <h1 className="font-heading text-3xl font-semibold text-primary">Your hobby dashboard</h1>
        <p className="mt-2 text-on-surface-variant">Sign in to track progress across every hobby you start.</p>
        <button onClick={() => navigate('/auth')} className="btn-primary mt-6">Sign in to continue</button>
      </div>
    )
  }

  const active = hobbies.filter(hobby =>
    hobby.stages.some(stage => stage.steps.some(step => progress.some(item => item.stepId === Number(step.id))))
  )

  return (
    <div>
      <h1 className="font-heading text-3xl md:text-4xl font-semibold text-primary">Keep growing, {user.firstName}.</h1>
      <p className="mt-2 text-on-surface-variant">Your live progress across every learning path.</p>

      <section className="mt-8">
        <h2 className="font-heading text-xl font-semibold text-primary mb-4">Hobbies in progress</h2>
        {active.length > 0 ? (
          <div className="grid gap-5 md:grid-cols-2">
            {active.map(hobby => {
              const steps = hobby.stages.flatMap(stage => stage.steps)
              const completed = steps.filter(step => progress.some(item => item.stepId === Number(step.id) && item.completed)).length
              const percent = steps.length ? Math.round((completed / steps.length) * 100) : 0
              return (
                <Link key={hobby.id} to={`/hobby/${hobby.id}/guide`} className="card p-5 flex gap-4 hover:shadow-lg transition-shadow">
                  <div className="w-20 h-20 rounded-xl overflow-hidden shrink-0">
                    <img src={getHobbyImage(hobby)} alt="" className="w-full h-full object-cover" />
                  </div>
                  <div className="min-w-0 flex-1">
                    <p className="text-xs font-semibold text-secondary">{hobby.category}</p>
                    <h3 className="font-heading text-lg font-semibold text-primary truncate">{hobby.name}</h3>
                    <div className="mt-3">
                      <ProgressBar value={percent} />
                      <p className="mt-1.5 text-xs text-on-surface-variant">{completed} of {steps.length} steps complete</p>
                    </div>
                  </div>
                </Link>
              )
            })}
          </div>
        ) : (
          <div className="rounded-2xl bg-surface-container-low p-10 text-center">
            <p className="text-on-surface-variant">No learning paths started yet.</p>
            <Link className="btn-primary mt-5 inline-block" to="/discover">Discover hobbies</Link>
          </div>
        )}
      </section>

      <section className="mt-12">
        <div className="flex items-center justify-between mb-4">
          <h2 className="font-heading text-xl font-semibold text-primary">Achievements</h2>
          {achievements.length > 0 && (
            <span className="text-xs font-semibold text-on-surface-variant">
              {achievements.filter(a => a.earned).length}/{achievements.length} unlocked
            </span>
          )}
        </div>
        {achievements.length > 0 ? (
          <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
            {achievements.map(item => (
              <div key={item.id} className={`card p-5 flex items-start gap-3 ${item.earned ? '' : 'opacity-50'}`}>
                <span className={`w-10 h-10 rounded-full flex items-center justify-center shrink-0 text-lg ${item.earned ? 'bg-secondary-container' : 'bg-surface-container-high grayscale'}`}>
                  {item.iconUrl || '🏅'}
                </span>
                <div className="min-w-0">
                  <h3 className="font-semibold text-on-surface">{item.name}</h3>
                  <p className="mt-1 text-sm text-on-surface-variant">{item.description}</p>
                  {!item.earned && <p className="mt-1 text-xs text-on-surface-variant/70">Keep going to unlock!</p>}
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="rounded-2xl bg-surface-container-low p-8 text-center text-on-surface-variant">
            Complete steps to start earning achievements.
          </div>
        )}
      </section>

      <section className="mt-12 rounded-3xl bg-primary p-8 md:p-10 text-white flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
        <div>
          <h2 className="font-heading text-2xl font-semibold mb-2">Feeling adventurous today?</h2>
          <p className="text-white/75 max-w-md">Browse more hobbies and start your next great obsession.</p>
        </div>
        <Link to="/discover" className="shrink-0 rounded-full bg-white text-primary px-6 py-3 font-semibold hover:opacity-90 transition-all active:scale-95">
          Explore hobbies
        </Link>
      </section>
    </div>
  )
}
