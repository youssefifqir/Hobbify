import pottery from '../assets/hobbies/pottery.jpg'
import urbanGardening from '../assets/hobbies/urban-gardening.jpg'
import sourdough from '../assets/hobbies/sourdough.jpg'
import breadBaking from '../assets/hobbies/bread-baking.jpg'
import filmPhotography from '../assets/hobbies/film-photography.jpg'
import nightCycling from '../assets/hobbies/night-cycling.jpg'

const gallery = [pottery, urbanGardening, sourdough, breadBaking, filmPhotography, nightCycling]

const keywordImage: [RegExp, string][] = [
  [/pottery|ceramic|clay|wheel/i, pottery],
  [/garden|plant|botany|balcony/i, urbanGardening],
  [/sourdough/i, sourdough],
  [/bread|bak|pastry|culinary/i, breadBaking],
  [/photo|camera|film/i, filmPhotography],
  [/cycl|bike|bicycle/i, nightCycling],
]

function hashString(value: string) {
  let hash = 0
  for (let i = 0; i < value.length; i++) hash = (hash * 31 + value.charCodeAt(i)) >>> 0
  return hash
}

export function getHobbyImage(hobby: { id: string; name: string; category?: string; image?: string }) {
  if (hobby.image) return hobby.image
  const haystack = `${hobby.name} ${hobby.category ?? ''}`
  for (const [pattern, image] of keywordImage) {
    if (pattern.test(haystack)) return image
  }
  return gallery[hashString(hobby.id || hobby.name) % gallery.length]
}

export const heroImages = { pottery, urbanGardening, sourdough, breadBaking, filmPhotography, nightCycling }
