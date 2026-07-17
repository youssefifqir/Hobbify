import { Link } from 'react-router-dom'
import HobbyCard from '../components/HobbyCard'
import { useAppContext } from '../context/AppContext'
import { heroImages } from '../lib/hobbyImages'

const steps = [
  {
    title: '1. Discover',
    body: 'Browse by category or filter by your time, space, and budget to find the perfect match.',
    icon: 'M11.42 15.17L17.25 21A2.652 2.652 0 0021 17.25l-5.877-5.877M11.42 15.17l2.496-3.03c.317-.384.74-.626 1.208-.766M11.42 15.17l-4.655 5.653a2.548 2.548 0 11-3.586-3.586l6.837-5.63m5.108-.233c.55-.164 1.163-.188 1.743-.14a4.5 4.5 0 004.486-6.336l-3.276 3.277a3.004 3.004 0 01-2.25-2.25l3.276-3.276a4.5 4.5 0 00-6.336 4.486c.091 1.076-.071 2.264-.904 2.95l-.102.085m-1.745 1.437L5.909 7.5H4.5L2.25 3.75l1.5-1.5L7.5 4.5v1.409l4.26 4.26m-1.745 1.437l1.745-1.437m6.615 8.206L15.75 15.75M4.867 19.125h.008v.008h-.008v-.008z',
  },
  {
    title: '2. Guide',
    body: 'Unlock a step-by-step beginner blueprint with a friendly mentor watching your back.',
    icon: 'M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0018 18a8.967 8.967 0 00-6 2.292m0-14.25v14.25',
  },
  {
    title: '3. Master',
    body: 'Track your progress, celebrate milestones, and join a community cheering you on.',
    icon: 'M18 18.72a9.094 9.094 0 003.741-.479 3 3 0 00-4.682-2.72m.94 3.198l.001.031c0 .225-.012.447-.037.666A11.944 11.944 0 0112 21c-2.17 0-4.207-.576-5.963-1.584A6.062 6.062 0 016 18.719m12 0a5.971 5.971 0 00-.941-3.197m0 0A5.995 5.995 0 0012 12.75a5.995 5.995 0 00-5.058 2.772m0 0a3 3 0 00-4.681 2.72 8.986 8.986 0 003.74.477m.94-3.197a5.971 5.971 0 00-.94 3.197M15 6.75a3 3 0 11-6 0 3 3 0 016 0zm6 3a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0zm-13.5 0a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0z',
  },
]

export default function Welcome() {
  const { hobbies } = useAppContext()
  const featured = hobbies.slice(0, 3)

  return (
    <div className="min-h-screen bg-surface overflow-x-hidden">
      <header className="sticky top-0 z-40 bg-surface/80 backdrop-blur-lg">
        <div className="max-w-7xl mx-auto flex items-center justify-between px-5 md:px-12 py-5">
          <span className="font-heading text-xl font-bold text-primary tracking-tight">hobbify</span>
          <nav className="flex items-center gap-3">
            <Link to="/discover" className="hidden sm:inline nav-link">Discover</Link>
            <Link to="/auth" className="btn-primary px-5 py-2 text-sm">Sign in</Link>
          </nav>
        </div>
      </header>

      <main>
        <section className="relative px-5 md:px-12 pt-8 pb-16 overflow-hidden">
          <div className="absolute -top-24 -right-12 w-72 h-72 bg-secondary-container/40 rounded-full blur-3xl -z-10" />
          <div className="absolute top-1/3 -left-20 w-80 h-80 bg-tertiary-fixed/30 rounded-full blur-3xl -z-10" />

          <div className="max-w-7xl mx-auto grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
            <div className="max-w-xl">
              <span className="badge bg-secondary-container text-secondary mb-6 inline-block">Friendly mentor, in your pocket</span>
              <h1 className="font-heading text-4xl md:text-6xl font-semibold text-primary leading-tight">
                Your next <span className="text-secondary">passion</span> starts here.
              </h1>
              <p className="mt-6 text-lg text-on-surface-variant leading-relaxed">
                Discover hobbies that fit your life, your space, and your budget. We provide the step-by-step guides — you bring the curiosity.
              </p>
              <div className="mt-9 flex flex-col sm:flex-row gap-3">
                <Link to="/discover" className="btn-primary px-8 py-4 text-base justify-center">
                  Browse hobbies
                  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                    <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 4.5L21 12m0 0l-7.5 7.5M21 12H3" />
                  </svg>
                </Link>
                <Link to="/auth" className="btn-secondary px-8 py-4 text-base justify-center">Sign up / Log in</Link>
              </div>
            </div>

            <div className="relative h-[280px] sm:h-[380px] lg:h-[440px]">
              <div className="absolute top-0 right-0 w-3/4 h-3/4 rounded-[2.5rem] overflow-hidden shadow-2xl rotate-3 hover:rotate-0 transition-transform duration-700">
                <img src={heroImages.pottery} alt="Pottery workshop" className="w-full h-full object-cover" />
              </div>
              <div className="absolute bottom-0 left-0 w-1/2 h-1/2 rounded-[2rem] overflow-hidden shadow-xl -rotate-6 hover:rotate-0 transition-transform duration-700 border-4 border-surface">
                <img src={heroImages.urbanGardening} alt="Urban gardening" className="w-full h-full object-cover" />
              </div>
            </div>
          </div>
        </section>

        <section className="max-w-7xl mx-auto px-5 md:px-12 py-16">
          <div className="flex flex-col md:flex-row justify-between items-start md:items-end gap-4 mb-10">
            <div>
              <h2 className="font-heading text-2xl md:text-3xl font-semibold text-primary mb-2">Start small, grow big</h2>
              <p className="text-on-surface-variant max-w-xl">Curated hobbies loved by the community — perfect for a weekend start.</p>
            </div>
            <Link to="/discover" className="text-secondary font-semibold text-sm flex items-center gap-1 hover:gap-2 transition-all shrink-0">
              See all hobbies
              <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M17.25 8.25L21 12m0 0l-3.75 3.75M21 12H3" />
              </svg>
            </Link>
          </div>

          {featured.length > 0 ? (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {featured.map(hobby => <HobbyCard key={hobby.id} hobby={hobby} />)}
            </div>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {[
                { title: 'Pottery', body: 'Master the wheel and create beautiful ceramics from scratch.', image: heroImages.pottery, badge: 'Artistic' },
                { title: 'Urban Gardening', body: 'Transform your small space into a thriving green sanctuary.', image: heroImages.urbanGardening, badge: 'Nature' },
                { title: 'Sourdough Baking', body: 'The art of natural fermentation, made simple and rewarding.', image: heroImages.sourdough, badge: 'Culinary' },
              ].map(item => (
                <div key={item.title} className="bg-surface-container-lowest rounded-[24px] p-4 shadow-sm">
                  <div className="aspect-[4/3] rounded-[20px] overflow-hidden mb-5">
                    <img src={item.image} alt={item.title} className="w-full h-full object-cover" />
                  </div>
                  <div className="px-1">
                    <span className="badge bg-tertiary-fixed text-primary mb-2 inline-block">{item.badge}</span>
                    <h3 className="font-heading text-lg font-semibold text-primary mb-2">{item.title}</h3>
                    <p className="text-sm text-on-surface-variant">{item.body}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </section>

        <section className="bg-surface-container-low py-16 md:py-24">
          <div className="max-w-7xl mx-auto px-5 md:px-12">
            <div className="text-center max-w-2xl mx-auto mb-14">
              <h2 className="font-heading text-2xl md:text-3xl font-semibold text-primary mb-3">The Hobbify path</h2>
              <p className="text-on-surface-variant">Starting something new shouldn't be stressful. We break it down into three simple steps.</p>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-10">
              {steps.map(step => (
                <div key={step.title} className="flex flex-col items-center text-center">
                  <div className="w-16 h-16 rounded-2xl bg-primary text-on-primary flex items-center justify-center mb-5">
                    <svg className="w-7 h-7" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                      <path strokeLinecap="round" strokeLinejoin="round" d={step.icon} />
                    </svg>
                  </div>
                  <h3 className="font-heading text-lg font-semibold text-primary mb-2">{step.title}</h3>
                  <p className="text-sm text-on-surface-variant leading-relaxed max-w-xs">{step.body}</p>
                </div>
              ))}
            </div>
          </div>
        </section>

        <section className="max-w-7xl mx-auto px-5 md:px-12 py-16 md:py-24">
          <div className="relative rounded-[2.5rem] overflow-hidden min-h-[340px] flex items-center justify-center text-center px-6">
            <div className="absolute inset-0">
              <img src={heroImages.filmPhotography} alt="" className="w-full h-full object-cover opacity-70" />
              <div className="absolute inset-0 bg-gradient-to-b from-primary/50 to-primary/90" />
            </div>
            <div className="relative z-10 max-w-xl py-16">
              <h2 className="font-heading text-3xl md:text-4xl font-semibold text-on-primary mb-4">Start your story today.</h2>
              <p className="text-white/80 mb-8">Join a growing community of makers and hobbyists finding their next passion.</p>
              <div className="flex flex-col sm:flex-row justify-center gap-3">
                <Link to="/auth" className="px-8 py-4 bg-on-primary text-primary rounded-full font-semibold hover:opacity-90 transition-all">Join Hobbify free</Link>
                <Link to="/discover" className="px-8 py-4 border border-white/30 text-on-primary rounded-full font-semibold hover:bg-white/10 transition-all">Explore hobbies</Link>
              </div>
            </div>
          </div>
        </section>
      </main>

      <footer className="border-t border-outline-variant/30 py-10 px-5 md:px-12">
        <div className="max-w-7xl mx-auto flex flex-col sm:flex-row justify-between items-center gap-4 text-sm text-on-surface-variant">
          <span className="font-heading font-semibold text-primary">hobbify</span>
          <span>© 2026 Hobbify. Discover your next passion.</span>
        </div>
      </footer>
    </div>
  )
}
