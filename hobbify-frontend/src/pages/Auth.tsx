import { useState } from 'react'
import type { FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { login, register, ApiError } from '../lib/api'
import { useAppContext } from '../context/AppContext'
import { useToast } from '../context/ToastContext'
import authBg from '../assets/hobbies/auth-bg.jpg'

export default function Auth() {
  const navigate = useNavigate()
  const { refreshAccount } = useAppContext()
  const { showToast } = useToast()
  const [isLogin, setIsLogin] = useState(true)
  const [firstName, setFirstName] = useState('')
  const [lastName, setLastName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [rememberMe, setRememberMe] = useState(false)
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [justRegistered, setJustRegistered] = useState(false)

  const submit = async (event: FormEvent) => {
    event.preventDefault()
    setError('')
    setSubmitting(true)
    try {
      if (isLogin) {
        await login(email, password, rememberMe)
        const profile = await refreshAccount()
        const isAdmin = profile?.roles.some(r => r === 'ROLE_ADMIN' || r === 'ADMIN') ?? false
        navigate(isAdmin ? '/admin' : '/dashboard')
      } else {
        await register(firstName, lastName, email, password)
        setJustRegistered(true)
        setIsLogin(true)
        setPassword('')
      }
    } catch (reason) {
      const msg = reason instanceof ApiError && reason.status === 401
        ? 'That email and password combination was not recognized.'
        : reason instanceof Error ? reason.message : 'Unable to continue.'
      setError(msg)
      showToast(msg)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="min-h-screen bg-surface flex">
      <section className="hidden lg:flex lg:w-7/12 relative overflow-hidden">
        <img src={authBg} alt="" className="absolute inset-0 w-full h-full object-cover" />
        <div className="absolute inset-0 bg-gradient-to-br from-primary/70 via-primary/30 to-transparent" />
        <div className="relative z-10 flex flex-col justify-end p-16 w-full">
          <div className="grid grid-cols-2 gap-5 max-w-xl">
            <div className="col-span-2 bg-white/15 backdrop-blur-md rounded-2xl p-6 border border-white/20">
              <p className="text-white/70 text-xs font-semibold uppercase tracking-widest mb-2">Hobbify spotlight</p>
              <p className="text-white text-xl font-heading font-semibold leading-snug">
                "Hobbify didn't just give me a hobby, it gave me a community."
              </p>
              <p className="text-white/70 text-sm mt-2">— Sarah Jenkins, member since 2025</p>
            </div>
            <div className="bg-white/15 backdrop-blur-md rounded-2xl p-5 border border-white/20">
              <p className="text-white font-heading font-semibold">12,000+</p>
              <p className="text-white/70 text-sm">Hobbyists learning together</p>
            </div>
            <div className="bg-white/15 backdrop-blur-md rounded-2xl p-5 border border-white/20">
              <p className="text-white font-heading font-semibold">Step-by-step</p>
              <p className="text-white/70 text-sm">Beginner-friendly guides</p>
            </div>
          </div>
        </div>
      </section>

      <section className="flex-1 flex items-center justify-center px-6 py-12">
        <form onSubmit={submit} className="w-full max-w-md">
          <Link to="/" className="font-heading text-xl font-bold text-primary tracking-tight">hobbify</Link>
          <h2 className="mt-8 font-heading text-3xl md:text-4xl font-semibold text-primary">
            {isLogin ? 'Welcome back' : 'Join our community'}
          </h2>
          <p className="mt-2 text-on-surface-variant">
            {isLogin ? 'Sign in to continue your journey.' : 'Start discovering hobbies today — it only takes a minute.'}
          </p>

          {justRegistered && (
            <div className="mt-6 rounded-xl bg-secondary-container text-secondary text-sm font-medium p-4">
              Account created! A team member will review and activate it shortly — you'll be able to sign in once approved.
            </div>
          )}

          {!isLogin && (
            <div className="mt-6 grid gap-4 sm:grid-cols-2">
              <label className="text-sm font-semibold text-on-surface">
                First name
                <input required value={firstName} onChange={e => setFirstName(e.target.value)} className="mt-2 w-full rounded-xl bg-surface-container-low border-none p-3 text-sm font-normal outline-none focus:ring-2 focus:ring-secondary transition-shadow" />
              </label>
              <label className="text-sm font-semibold text-on-surface">
                Last name
                <input required value={lastName} onChange={e => setLastName(e.target.value)} className="mt-2 w-full rounded-xl bg-surface-container-low border-none p-3 text-sm font-normal outline-none focus:ring-2 focus:ring-secondary transition-shadow" />
              </label>
            </div>
          )}

          <label className="mt-5 block text-sm font-semibold text-on-surface">
            Email address
            <input required type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="you@example.com" className="mt-2 w-full rounded-xl bg-surface-container-low border-none p-3 text-sm font-normal outline-none focus:ring-2 focus:ring-secondary transition-shadow" />
          </label>

          <label className="mt-5 block text-sm font-semibold text-on-surface">
            Password
            <input required type="password" minLength={8} value={password} onChange={e => setPassword(e.target.value)} placeholder="••••••••" className="mt-2 w-full rounded-xl bg-surface-container-low border-none p-3 text-sm font-normal outline-none focus:ring-2 focus:ring-secondary transition-shadow" />
          </label>

          {isLogin && (
            <label className="mt-4 flex items-center gap-2.5 text-sm text-on-surface-variant">
              <input checked={rememberMe} onChange={e => setRememberMe(e.target.checked)} type="checkbox" className="w-4 h-4 rounded accent-secondary" />
              Keep me signed in
            </label>
          )}

          {error && <p className="mt-4 text-sm text-error">{error}</p>}

          <button disabled={submitting} className="btn-primary w-full mt-7 py-4 disabled:opacity-60">
            {submitting ? 'Please wait…' : isLogin ? 'Sign in' : 'Create account'}
          </button>

          <p className="mt-6 text-center text-sm text-on-surface-variant">
            {isLogin ? "New to Hobbify?" : 'Already have an account?'}{' '}
            <button
              type="button"
              onClick={() => { setIsLogin(!isLogin); setError(''); setJustRegistered(false) }}
              className="font-semibold text-primary hover:underline"
            >
              {isLogin ? 'Create one' : 'Sign in'}
            </button>
          </p>
        </form>
      </section>
    </div>
  )
}
