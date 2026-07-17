import { Link, useNavigate, useParams } from 'react-router-dom'
import { useAppContext } from '../context/AppContext'
import { getHobbyImage } from '../lib/hobbyImages'

const costLabel = { LOW: '$', MEDIUM: '$$', HIGH: '$$$' }
const spaceLabel = { MINIMAL: 'Minimal', MODERATE: 'Moderate', DEDICATED: 'Dedicated' }
const timeLabel = { LIGHT: 'Light', MODERATE: 'Moderate', INTENSIVE: 'Intensive' }
const difficultyLabel = { BEGINNER: 'Beginner', INTERMEDIATE: 'Intermediate', ADVANCED: 'Advanced' }

export default function HobbyDetail() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { hobbies, favorites, progress, toggleFavorite, user } = useAppContext()
  const hobby = hobbies.find(item => item.id === id)

  if (!hobby) {
    return (
      <div className="py-20 text-center">
        <h1 className="font-heading text-2xl font-semibold">Hobby not found</h1>
        <Link className="btn-primary mt-5 inline-block" to="/discover">Browse hobbies</Link>
      </div>
    )
  }

  const steps = hobby.stages.flatMap(stage => stage.steps)
  const completed = steps.filter(step => progress.some(item => item.stepId === Number(step.id) && item.completed)).length
  const favorite = favorites.some(item => item.hobbyId === Number(hobby.id))
  const toggleFav = async () => { if (!user) navigate('/auth'); else await toggleFavorite(hobby.id) }
  const startJourney = () => { if (!user) navigate('/auth'); else navigate(`/hobby/${hobby.id}/guide`) }

  return (
    <div>
      <section className="relative rounded-3xl overflow-hidden min-h-[320px] md:min-h-[400px] flex items-end p-8 md:p-12">
        <img src={getHobbyImage(hobby)} alt={hobby.name} className="absolute inset-0 w-full h-full object-cover" />
        <div className="absolute inset-0 bg-gradient-to-t from-primary/90 via-primary/40 to-transparent" />
        <div className="relative z-10 text-white max-w-2xl">
          <span className="badge bg-white/20 backdrop-blur text-white mb-4 inline-block">{hobby.category}</span>
          <h1 className="font-heading text-3xl md:text-5xl font-semibold leading-tight">{hobby.name}</h1>
          <p className="mt-4 text-white/85 text-base md:text-lg">{hobby.description}</p>
          <div className="mt-8 flex flex-wrap gap-3">
            <button onClick={startJourney} className="rounded-full bg-white text-primary px-6 py-3 font-semibold hover:opacity-90 transition-all active:scale-95">
              {completed ? `Continue journey (${completed}/${steps.length})` : 'Start journey'}
            </button>
            <button onClick={toggleFav} className="rounded-full border border-white/40 text-white px-6 py-3 font-semibold hover:bg-white/10 transition-all active:scale-95">
              {favorite ? '♥ Saved' : '♡ Save for later'}
            </button>
          </div>
        </div>
      </section>

      <div className="mt-8 grid grid-cols-2 md:grid-cols-4 gap-4">
        <div className="card p-5 text-center">
          <p className="font-heading text-2xl font-semibold text-primary">{costLabel[hobby.cost]}</p>
          <p className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mt-1">Cost</p>
        </div>
        <div className="card p-5 text-center">
          <p className="font-heading text-2xl font-semibold text-primary">{spaceLabel[hobby.space]}</p>
          <p className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mt-1">Space</p>
        </div>
        <div className="card p-5 text-center">
          <p className="font-heading text-2xl font-semibold text-primary">{timeLabel[hobby.time]}</p>
          <p className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mt-1">Time</p>
        </div>
        <div className="card p-5 text-center">
          <p className="font-heading text-2xl font-semibold text-primary">{difficultyLabel[hobby.difficulty]}</p>
          <p className="text-xs font-semibold text-on-surface-variant uppercase tracking-wider mt-1">Difficulty</p>
        </div>
      </div>

      <div className="mt-8 grid gap-6 lg:grid-cols-[1fr_300px]">
        <section>
          <h2 className="font-heading text-2xl font-semibold text-primary mb-5">Your learning path</h2>
          <div className="space-y-4">
            {hobby.stages.map((stage, index) => (
              <div key={stage.id} className="card p-6 flex items-start gap-4">
                <span className="w-10 h-10 rounded-full bg-secondary-container text-secondary flex items-center justify-center font-heading font-semibold shrink-0">
                  {index + 1}
                </span>
                <div>
                  <p className="text-xs font-semibold uppercase tracking-widest text-secondary mb-1">Stage {index + 1}</p>
                  <h3 className="font-heading text-xl font-semibold text-primary">{stage.title}</h3>
                  <p className="mt-1 text-sm text-on-surface-variant">{stage.steps.length} steps</p>
                </div>
              </div>
            ))}
            {hobby.stages.length === 0 && (
              <div className="card p-8 text-center text-on-surface-variant">This guide is still being written. Check back soon.</div>
            )}
          </div>
        </section>

        <aside className="rounded-2xl bg-primary p-8 text-white h-fit sticky top-24">
          <p className="text-sm uppercase tracking-widest text-white/60">Journey stats</p>
          <p className="mt-8 font-heading text-4xl font-semibold">{hobby.stages.length}</p>
          <p className="text-white/70">Stages</p>
          <p className="mt-6 font-heading text-4xl font-semibold">{steps.length}</p>
          <p className="text-white/70">Actionable steps</p>
        </aside>
      </div>
    </div>
  )
}
