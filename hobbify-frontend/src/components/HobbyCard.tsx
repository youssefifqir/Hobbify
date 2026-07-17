import { Link } from 'react-router-dom'
import type { Hobby } from '../types'
import { getHobbyImage } from '../lib/hobbyImages'

const costLabel = { LOW: '$ Cost', MEDIUM: '$$ Cost', HIGH: '$$$ Cost' }
const spaceLabel = { MINIMAL: 'Minimal space', MODERATE: 'Moderate space', DEDICATED: 'Dedicated space' }
const timeLabel = { LIGHT: 'Light', MODERATE: 'Moderate', INTENSIVE: 'Intensive' }

export default function HobbyCard({ hobby }: { hobby: Hobby }) {
  return (
    <Link
      to={`/hobby/${hobby.id}`}
      className="group block bg-surface-container-lowest rounded-[24px] p-4 shadow-sm hover:shadow-lg transition-all duration-300 hover:-translate-y-1"
    >
      <div className="aspect-[4/3] rounded-[20px] overflow-hidden mb-5">
        <img
          src={getHobbyImage(hobby)}
          alt={hobby.name}
          className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
        />
      </div>
      <div className="px-1">
        <div className="flex items-start justify-between gap-2 mb-2">
          <h3 className="font-heading text-lg font-semibold text-primary leading-tight">{hobby.name}</h3>
          <span className="badge bg-secondary-container text-secondary shrink-0">{hobby.category}</span>
        </div>
        <div className="flex flex-wrap gap-2 mb-3">
          <span className="chip chip-inactive">{costLabel[hobby.cost]}</span>
          <span className="chip chip-inactive">{spaceLabel[hobby.space]}</span>
          <span className="chip chip-inactive">{timeLabel[hobby.time]}</span>
        </div>
        <p className="text-sm text-on-surface-variant line-clamp-2">{hobby.description}</p>
      </div>
    </Link>
  )
}
