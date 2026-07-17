import { Link, useNavigate } from 'react-router-dom'
import HobbyCard from '../components/HobbyCard'
import { useAppContext } from '../context/AppContext'

export default function Favorites() {
  const navigate = useNavigate()
  const { hobbies, favorites, user } = useAppContext()

  if (!user) {
    return (
      <div className="py-20 text-center">
        <h1 className="font-heading text-3xl font-semibold text-primary">Save hobbies for later</h1>
        <p className="mt-2 text-on-surface-variant">Sign in to build your favorites list.</p>
        <button className="btn-primary mt-6" onClick={() => navigate('/auth')}>Sign in</button>
      </div>
    )
  }

  const saved = hobbies.filter(hobby => favorites.some(favorite => favorite.hobbyId === Number(hobby.id)))

  return (
    <div>
      <h1 className="font-heading text-3xl md:text-4xl font-semibold text-primary">My favorites</h1>
      <p className="mt-2 text-on-surface-variant">Hobbies you have saved for later.</p>
      {saved.length ? (
        <div className="mt-8 grid gap-6 md:grid-cols-2 lg:grid-cols-3">
          {saved.map(hobby => <HobbyCard key={hobby.id} hobby={hobby} />)}
        </div>
      ) : (
        <div className="mt-8 rounded-2xl bg-surface-container-low p-12 text-center">
          <p className="text-on-surface-variant">Nothing saved yet.</p>
          <Link className="btn-primary mt-5 inline-block" to="/discover">Browse hobbies</Link>
        </div>
      )}
    </div>
  )
}
