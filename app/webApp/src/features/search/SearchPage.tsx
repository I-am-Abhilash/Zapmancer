import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Search, Filter, ShieldCheck, Star, MapPin, DollarSign, ArrowUpRight, Code, User } from 'lucide-react';

export const SearchPage: React.FC = () => {
  const [tab, setTab] = useState<'talents' | 'projects'>('talents');
  const [query, setQuery] = useState('');

  const talents = [
    {
      id: 't1',
      name: 'Elena Rostova',
      title: 'Senior KMP & WebAssembly Lead',
      avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
      rating: 5.0,
      reviews: 42,
      rate: '$85 / hr',
      location: 'Berlin, Germany',
      bio: 'Architected high-throughput Ktor backend microservices and Compose Multiplatform clients.',
      skills: ['Kotlin', 'Wasm', 'Ktor', 'PostgreSQL', 'Docker']
    },
    {
      id: 't2',
      name: 'David Chen',
      title: 'iOS & Android Native KMP Developer',
      avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
      rating: 4.9,
      reviews: 38,
      rate: '$75 / hr',
      location: 'Toronto, Canada',
      bio: 'Specialized in SwiftUI & Jetpack Compose shared viewmodels with SQLDelight persistence.',
      skills: ['KMP', 'SwiftUI', 'Compose', 'Coroutines']
    }
  ];

  const projects = [
    {
      id: 'p1',
      title: 'Real-Time WebSockets Chat Core in Ktor',
      client: 'Zapmancer Labs',
      budget: '$2,800',
      skills: ['Ktor', 'WebSockets', 'PostgreSQL']
    },
    {
      id: 'p2',
      title: 'Gorse AI Recommendation Engine Pipeline Integration',
      client: 'Retail AI',
      budget: '$5,000',
      skills: ['Gorse', 'Kotlin', 'Go']
    }
  ];

  return (
    <div className="min-h-screen flex flex-col bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 transition-colors">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        <div>
          <h1 className="text-3xl font-extrabold tracking-tight">Marketplace Search & Discovery</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Find top engineering talent or explore active bounty contracts.</p>
        </div>

        {/* Search Bar & Tabs */}
        <div className="bg-white dark:bg-slate-900 p-6 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-sm space-y-4">
          <div className="relative">
            <Search className="w-5 h-5 text-slate-400 absolute left-4 top-3.5" />
            <input
              type="text"
              placeholder="Search by engineer name, skills, title, or project scope..."
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              className="w-full pl-12 pr-4 py-3 rounded-xl border border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-800 text-sm font-medium focus:ring-2 focus:ring-indigo-500"
            />
          </div>

          <div className="flex items-center gap-4 pt-2 border-t border-slate-100 dark:border-slate-800">
            <button
              onClick={() => setTab('talents')}
              className={`flex items-center gap-2 px-5 py-2.5 rounded-xl text-xs font-extrabold transition-all ${
                tab === 'talents'
                  ? 'bg-indigo-600 text-white shadow-sm'
                  : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400'
              }`}
            >
              <User className="w-4 h-4" /> Find Freelancers & Talent
            </button>
            <button
              onClick={() => setTab('projects')}
              className={`flex items-center gap-2 px-5 py-2.5 rounded-xl text-xs font-extrabold transition-all ${
                tab === 'projects'
                  ? 'bg-indigo-600 text-white shadow-sm'
                  : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400'
              }`}
            >
              <Code className="w-4 h-4" /> Find Project Bounties
            </button>
          </div>
        </div>

        {/* Results */}
        {tab === 'talents' ? (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {talents.map((t) => (
              <div key={t.id} className="bg-white dark:bg-slate-900 p-6 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-sm hover:border-indigo-500 transition-all space-y-4">
                <div className="flex items-start justify-between gap-4">
                  <div className="flex items-center gap-4">
                    <img src={t.avatar} alt={t.name} className="w-14 h-14 rounded-2xl object-cover border border-indigo-500/30" />
                    <div>
                      <h3 className="font-bold text-lg text-slate-900 dark:text-white flex items-center gap-2">
                        <Link to="/profile/1" className="hover:text-indigo-600">{t.name}</Link>
                        <ShieldCheck className="w-4 h-4 text-emerald-500" />
                      </h3>
                      <p className="text-xs text-slate-500 dark:text-slate-400 font-medium">{t.title}</p>
                    </div>
                  </div>
                  <span className="text-lg font-extrabold text-emerald-600 dark:text-emerald-400">{t.rate}</span>
                </div>

                <p className="text-xs text-slate-600 dark:text-slate-300 leading-relaxed">{t.bio}</p>

                <div className="flex flex-wrap gap-2 pt-2 border-t border-slate-100 dark:border-slate-800">
                  {t.skills.map((s) => (
                    <span key={s} className="px-2.5 py-1 rounded-md bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 text-xs font-semibold">
                      {s}
                    </span>
                  ))}
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="space-y-4">
            {projects.map((p) => (
              <div key={p.id} className="bg-white dark:bg-slate-900 p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm flex items-center justify-between gap-4">
                <div>
                  <h3 className="font-bold text-lg text-slate-900 dark:text-white hover:text-indigo-600">
                    <Link to={`/projects/${p.id}`}>{p.title}</Link>
                  </h3>
                  <div className="flex items-center gap-2 text-xs text-slate-400 mt-1">
                    <span>{p.client}</span> &bull; <span>Verified Escrow</span>
                  </div>
                </div>
                <div className="text-right">
                  <div className="text-lg font-extrabold text-emerald-600">{p.budget}</div>
                  <Link to={`/projects/${p.id}`} className="text-xs font-bold text-indigo-600 hover:underline">View Bounties &rarr;</Link>
                </div>
              </div>
            ))}
          </div>
        )}

      </main>

      <Footer />
    </div>
  );
};
