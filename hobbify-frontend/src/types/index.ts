export type CostTier = 'LOW' | 'MEDIUM' | 'HIGH'
export type SpaceNeeded = 'MINIMAL' | 'MODERATE' | 'DEDICATED'
export type TimeCommitment = 'LIGHT' | 'MODERATE' | 'INTENSIVE'
export type Difficulty = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED'
export type ContentStatus = 'DRAFT' | 'IN_REVIEW' | 'PUBLISHED'

export interface Step {
  id: string
  ref?: string
  title: string
  content: string
  estimatedMinutes: number
  completed?: boolean
}

export interface Stage {
  id: string
  ref?: string
  title: string
  order: number
  steps: Step[]
}

export interface Hobby {
  id: string
  ref?: string
  name: string
  description: string
  category: string
  cost: CostTier
  space: SpaceNeeded
  time: TimeCommitment
  difficulty: Difficulty
  icon?: string
  image?: string
  status?: ContentStatus
  stages: Stage[]
}

export interface User {
  id: string
  email: string
  firstName: string
  lastName: string
  roles: string[]
  createdDate: string
}

export interface Favorite {
  id: number
  hobbyId: number
}

export interface UserProgress {
  id: number
  stepId: number
  completed: boolean
  completedAt?: string
}

export type AchievementType = 'ONBOARDING' | 'MILESTONE' | 'STREAK' | 'EXPLORER' | 'MASTERY'

export interface Achievement {
  id: number
  name: string
  description: string
  type: AchievementType
  threshold: number
  iconUrl?: string
  earned: boolean
  earnedAt?: string
}

export type FilterKey = 'cost' | 'space' | 'time' | 'difficulty' | 'category'
