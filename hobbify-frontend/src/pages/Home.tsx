import { useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import HobbyCard from '../components/HobbyCard'
import SearchBar from '../components/ui/SearchBar'
import Chip from '../components/ui/Chip'
import { SkeletonGrid } from '../components/ui/Skeleton'
import { useAppContext } from '../context/AppContext'
import type { CostTier, SpaceNeeded, TimeCommitment } from '../types'

const costOptions: CostTier[] = ['LOW', 'MEDIUM', 'HIGH']
const spaceOptions: SpaceNeeded[] = ['MINIMAL', 'MODERATE', 'DEDICATED']
const timeOptions: TimeCommitment[] = ['LIGHT', 'MODERATE', 'INTENSIVE']
const labels: Record<string, string> = {
  LOW: '$', MEDIUM: '$$', HIGH: '$$$',
  MINIMAL: 'Minimal', MODERATE: 'Moderate', DEDICATED: 'Dedicated',
  LIGHT: 'Light', INTENSIVE: 'Intensive',
}

export default function Home() {
  const navigate = useNavigate()
  const { hobbies, loading } = useAppContext()
  const [search, setSearch] = useState('')
  const [category, setCategory] = useState('')
  const [cost, setCost] = useState<CostTier | ''>('')
  const [space, setSpace] = useState<SpaceNeeded | ''>('')
  const [time, setTime] = useState<TimeCommitment | ''>('')

  const categories = useMemo(() => [...new Set(hobbies.map(hobby => hobby.category))].sort(), [hobbies])

  const filtered = hobbies.filter(hobby => {
    const query = search.toLowerCase()
    return (!query || hobby.name.toLowerCase().includes(query) || hobby.description.toLowerCase().includes(query))
      && (!category || hobby.category === category)
      && (!cost || hobby.cost === cost)
      && (!space || hobby.space === space)
      && (!time || hobby.time === time)
  })

  const surpriseMe = () => {
    if (!hobbies.length) return
    const pick = hobbies[Math.floor(Math.random() * hobbies.length)]
    navigate(`/hobby/${pick.id}`)
  }

  return (
    <div>
      <section className="mb-8 max-w-2xl">
        <h1 className="font-heading text-3xl md:text-5xl font-semibold text-primary">Discover your next passion.</h1>
        <p className="mt-3 text-lg text-on-surface-variant">Browse published learning paths built by the Hobbify community.</p>
      </section>

      <div className="mb-6 max-w-xl">
        <SearchBar value={search} onChange={setSearch} placeholder="Search hobbies..." />
      </div>

      {categories.length > 0 && (
        <div className="mb-5 flex flex-wrap gap-2">
          {categories.map(item => (
            <Chip key={item} label={item} size="md" active={category === item} onClick={() => setCategory(category === item ? '' : item)} />
          ))}
        </div>
      )}

      <div className="mb-10 flex flex-wrap items-center gap-2 text-sm">
        <span className="text-on-surface-variant font-semibold mr-1">Cost:</span>
        {costOptions.map(o => <Chip key={o} label={labels[o]} variant="cost" active={cost === o} onClick={() => setCost(cost === o ? '' : o)} />)}
        <span className="text-on-surface-variant font-semibold ml-3 mr-1">Space:</span>
        {spaceOptions.map(o => <Chip key={o} label={labels[o]} variant="space" active={space === o} onClick={() => setSpace(space === o ? '' : o)} />)}
        <span className="text-on-surface-variant font-semibold ml-3 mr-1">Time:</span>
        {timeOptions.map(o => <Chip key={o} label={labels[o]} variant="time" active={time === o} onClick={() => setTime(time === o ? '' : o)} />)}
      </div>

      {loading ? (
        <SkeletonGrid count={6} />
      ) : filtered.length ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filtered.map(hobby => <HobbyCard key={hobby.id} hobby={hobby} />)}
        </div>
      ) : (
        <div className="rounded-2xl bg-surface-container-low p-12 text-center">
          <h2 className="font-heading text-2xl font-semibold">No hobbies found</h2>
          <p className="mt-2 text-on-surface-variant">Try a different search or check back when new content is published.</p>
        </div>
      )}

      {hobbies.length > 0 && (
        <section className="mt-14 rounded-3xl bg-primary p-8 md:p-12 text-white flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
          <div>
            <h2 className="font-heading text-2xl font-semibold mb-2">Not sure where to start?</h2>
            <p className="text-white/75 max-w-md">Let us pick a hobby for you — sometimes the best passion finds you by surprise.</p>
          </div>
          <button onClick={surpriseMe} className="shrink-0 rounded-full bg-white text-primary px-6 py-3 font-semibold flex items-center gap-2 hover:opacity-90 transition-all active:scale-95">
            Surprise me
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 4.5L21 12m0 0l-7.5 7.5M21 12H3" />
            </svg>
          </button>
        </section>
      )}
    </div>
  )
}
