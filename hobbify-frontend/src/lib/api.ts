import type { Achievement, Difficulty, Favorite, Hobby, SpaceNeeded, TimeCommitment, User, UserProgress } from '../types'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api/v1'
const ACCESS_TOKEN_KEY = 'hobbify.access-token'
const REFRESH_TOKEN_KEY = 'hobbify.refresh-token'

interface PageResponse<T> {
  content: T[]
}

interface CatalogHobby {
  id: number
  ref: string
  name: string
  description: string
  category: string
  costTier: Hobby['cost']
  spaceNeeded: SpaceNeeded
  timeCommitment: TimeCommitment
  difficulty: Difficulty
  icon?: string
  imageData?: string
  status?: Hobby['status']
  stages?: CatalogStage[]
}

interface CatalogStage {
  id: number
  ref: string
  title: string
  order: number
  steps: CatalogStep[]
}

interface CatalogStep {
  id: number
  ref: string
  title: string
  content: string
  order: number
  estimatedMinutes: number
}

interface ApiFavorite {
  id: number
  hobbyId: number
}

interface ApiProgress {
  id: number
  stepId: number
  completed: boolean
  completedAt?: string
}

interface ApiAchievement {
  id: number
  name: string
  description: string
  type: Achievement['type']
  threshold: number
  iconUrl?: string
  earned: boolean
  earnedAt?: string
}

export class ApiError extends Error {
  readonly status: number

  constructor(message: string, status: number) {
    super(message)
    this.status = status
  }
}

function toHobby(hobby: CatalogHobby): Hobby {
  return {
    id: String(hobby.id),
    ref: hobby.ref,
    name: hobby.name,
    description: hobby.description,
    category: hobby.category,
    cost: hobby.costTier,
    space: hobby.spaceNeeded,
    time: hobby.timeCommitment,
    difficulty: hobby.difficulty,
    icon: hobby.icon,
    image: hobby.imageData,
    status: hobby.status,
    stages: (hobby.stages ?? []).map(stage => ({
      id: String(stage.id),
      ref: stage.ref,
      title: stage.title,
      order: Number(stage.order),
      steps: stage.steps.map(step => ({
        id: String(step.id),
        ref: step.ref,
        title: step.title,
        content: step.content,
        estimatedMinutes: Number(step.estimatedMinutes ?? 0),
      })),
    })),
  }
}

export function hasSession() {
  return Boolean(localStorage.getItem(ACCESS_TOKEN_KEY))
}

export function clearSession() {
  localStorage.removeItem(ACCESS_TOKEN_KEY)
  localStorage.removeItem(REFRESH_TOKEN_KEY)
}

function saveSession(tokens: { access_token: string; refresh_token: string }) {
  localStorage.setItem(ACCESS_TOKEN_KEY, tokens.access_token)
  localStorage.setItem(REFRESH_TOKEN_KEY, tokens.refresh_token)
}

async function refreshSession() {
  const refreshToken = localStorage.getItem(REFRESH_TOKEN_KEY)
  if (!refreshToken) return false

  const response = await fetch(`${API_BASE_URL}/auth/refresh`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ refreshToken }),
  })
  if (!response.ok) {
    clearSession()
    return false
  }
  saveSession(await response.json())
  return true
}

async function request<T>(path: string, options: RequestInit = {}, retry = true): Promise<T> {
  const headers = new Headers(options.headers)
  const token = localStorage.getItem(ACCESS_TOKEN_KEY)
  if (token) headers.set('Authorization', `Bearer ${token}`)
  if (options.body) headers.set('Content-Type', 'application/json')

  const response = await fetch(`${API_BASE_URL}${path}`, { ...options, headers })
  // An expired/invalid access token falls through this backend's JWT filter as
  // anonymous rather than rejected outright, so Spring Security's authorization
  // check denies it as 403, not 401 — the silent-refresh retry has to cover both,
  // or every access-token expiry (every 15 min) surfaces as a hard, unhandled error.
  if ((response.status === 401 || response.status === 403) && retry && token && await refreshSession()) {
    return request<T>(path, options, false)
  }
  if (!response.ok) {
    const payload = await response.json().catch(() => null) as { message?: string } | null
    throw new ApiError(payload?.message ?? `Request failed (${response.status})`, response.status)
  }
  if (response.status === 204 || response.headers.get('content-length') === '0') return undefined as T
  return response.json() as Promise<T>
}

export async function login(email: string, password: string, rememberMe: boolean) {
  const tokens = await request<{ access_token: string; refresh_token: string }>('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password, rememberMe }),
  })
  saveSession(tokens)
}

export async function register(firstName: string, lastName: string, email: string, password: string) {
  await request<void>('/auth/register', {
    method: 'POST',
    body: JSON.stringify({ firstName, lastName, email, password, confirmPassword: password }),
  })
}

export async function logout() {
  try {
    await request<void>('/auth/logout', { method: 'POST' })
  } finally {
    clearSession()
  }
}

export const getCurrentUser = () => request<User>('/users/me')

export const updateProfile = (firstName: string, lastName: string) =>
  request<void>('/users/profile', { method: 'PATCH', body: JSON.stringify({ firstName, lastName }) })

export async function getCatalogHobbies() {
  const hobbies = await request<CatalogHobby[]>('/catalog/hobbies')
  return hobbies.map(toHobby)
}

export async function getCatalogHobby(id: string) {
  return toHobby(await request<CatalogHobby>(`/catalog/hobbies/${id}`))
}

export async function getFavorites() {
  const page = await request<PageResponse<ApiFavorite>>('/favorites?size=200&sortBy=createdDate&sortDir=desc')
  return page.content.map<Favorite>(item => ({ id: item.id, hobbyId: item.hobbyId }))
}

export const addFavorite = (hobbyId: string) => request<ApiFavorite>('/favorites', {
  method: 'POST',
  body: JSON.stringify({ hobbyId: Number(hobbyId), createdAt: new Date().toISOString() }),
})

export const removeFavorite = (id: number) => request<void>(`/favorites/${id}`, { method: 'DELETE' })

export async function getProgress() {
  const page = await request<PageResponse<ApiProgress>>('/userprogresss?size=200&sortBy=createdDate&sortDir=desc')
  return page.content.map<UserProgress>(item => ({
    id: item.id,
    stepId: item.stepId,
    completed: item.completed,
    completedAt: item.completedAt,
  }))
}

export function saveProgress(existing: UserProgress | undefined, stepId: string, completed: boolean) {
  const body = JSON.stringify({ stepId: Number(stepId), completed, completedAt: completed ? new Date().toISOString() : null })
  return existing
    ? request<ApiProgress>(`/userprogresss/${existing.id}`, { method: 'PUT', body })
    : request<ApiProgress>('/userprogresss', { method: 'POST', body })
}

export async function getAchievements() {
  const items = await request<ApiAchievement[]>('/achievements/mine')
  return items.map<Achievement>(item => ({
    id: item.id, name: item.name, description: item.description,
    type: item.type, threshold: item.threshold, iconUrl: item.iconUrl,
    earned: item.earned, earnedAt: item.earnedAt,
  }))
}

export interface AdminUser {
  id: string
  email: string
  firstName: string
  lastName: string
  roles: string[]
  enabled: boolean
  createdDate: string
}

export const getAdminUsers = async () => (await request<PageResponse<AdminUser>>('/admin/users?size=200&sortBy=createdDate&sortDir=desc')).content
export const setUserEnabled = (id: string, enabled: boolean) => request<void>(`/admin/users/${id}/${enabled ? 'activate' : 'deactivate'}`, { method: 'PUT' })

type HobbyPayload = Pick<Hobby, 'name' | 'description' | 'category' | 'cost' | 'space' | 'time' | 'difficulty' | 'icon' | 'image' | 'status'>
const toHobbyPayload = (hobby: HobbyPayload) => ({
  name: hobby.name,
  description: hobby.description,
  category: hobby.category,
  costTier: hobby.cost,
  spaceNeeded: hobby.space,
  timeCommitment: hobby.time,
  difficulty: hobby.difficulty,
  icon: hobby.icon ?? '',
  imageData: hobby.image ?? '',
  status: hobby.status ?? 'PUBLISHED',
  contentSource: 'MANUAL',
})

export async function createAdminHobby(hobby: HobbyPayload) {
  const created = await request<CatalogHobby>('/hobbys', { method: 'POST', body: JSON.stringify(toHobbyPayload(hobby)) })
  return toHobby(created)
}

export async function updateAdminHobby(id: string, hobby: HobbyPayload) {
  const updated = await request<CatalogHobby>(`/hobbys/${id}`, { method: 'PUT', body: JSON.stringify(toHobbyPayload(hobby)) })
  return toHobby(updated)
}

export const deleteAdminHobby = (id: string) => request<void>(`/hobbys/${id}`, { method: 'DELETE' })

export interface AdminStageResponse { id: number; title: string; order: number }
export interface AdminStepResponse { id: number; title: string; content: string; order: number; estimatedMinutes: number }

export const createAdminStage = (hobbyId: string, title: string, order: number) =>
  request<AdminStageResponse>('/stages', { method: 'POST', body: JSON.stringify({ title, order, hobbyId: Number(hobbyId) }) })

export const updateAdminStage = (id: string, title: string, order: number) =>
  request<AdminStageResponse>(`/stages/${id}`, { method: 'PUT', body: JSON.stringify({ title, order }) })

export const deleteAdminStage = (id: string) => request<void>(`/stages/${id}`, { method: 'DELETE' })

export const createAdminStep = (stageId: string, title: string, content: string, order: number, estimatedMinutes: number) =>
  request<AdminStepResponse>('/steps', { method: 'POST', body: JSON.stringify({ title, content, order, estimatedMinutes, stageId: Number(stageId) }) })

export const updateAdminStep = (id: string, title: string, content: string, order: number, estimatedMinutes: number) =>
  request<AdminStepResponse>(`/steps/${id}`, { method: 'PUT', body: JSON.stringify({ title, content, order, estimatedMinutes }) })

export const deleteAdminStep = (id: string) => request<void>(`/steps/${id}`, { method: 'DELETE' })
