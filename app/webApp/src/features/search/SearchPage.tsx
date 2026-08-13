import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Search, ShieldCheck, User, Code, Sparkles, ArrowRight } from 'lucide-react';
import { Tabs, TabItem } from '../../components/ui/Tabs';

type SearchTabId = 'talents' | 'projects';

export const SearchPage: React.FC = () => {
  const [tab, setTab] = useState<SearchTabId>('talents');
  const [query, setQuery] = useState('');

  const searchTabs: TabItem<SearchTabId>[] = [
    { id: 'talents', label: 'Find Freelancers & Talent', icon: User },
    { id: 'projects', label: 'Find Project Bounties', icon: Code },
  ];

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
    <div className="min-h-screen flex flex-col bg-canvas text-ink transition-colors duration-200 antialiased font-sans">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        {/* Title Banner */}
        <div className="space-y-1">
          <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
            <Sparkles className="w-3.5 h-3.5" /> Discovery Hub
          </div>
          <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight text-ink">Marketplace Search & Discovery</h1>
          <p className="text-sm text-mute">Find top engineering talent or explore active bounty contracts.</p>
        </div>

        {/* Search Input Container */}
        <div className="bg-surface p-6 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
          <div className="relative">
            <Search className="w-5 h-5 text-mute absolute left-4 top-3.5" />
            <input
              type="text"
              placeholder="Search by engineer name, skills, title, or project scope..."
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              className="w-full pl-12 pr-4 py-3 rounded-full border border-hairline bg-surface-elevated text-sm text-ink placeholder:text-mute focus:outline-none focus:border-brand-green transition-colors"
            />
          </div>

          {/* Central Green Deck Reusable Tabs Component */}
          <div>
            <Tabs
              tabs={searchTabs}
              activeTab={tab}
              onChange={(id) => setTab(id)}
            />
          </div>
        </div>

        {/* Search Results */}
        {tab === 'talents' ? (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {talents.map((t) => (
              <div
                key={t.id}
                className="bg-surface p-6 rounded-xl border border-hairline hover:bg-surface-elevated hover:scale-[1.02] transition-all duration-200 space-y-4 shadow-sm dark:shadow-none"
              >
                <div className="flex items-start justify-between gap-4">
                  <div className="flex items-center gap-4">
                    <img
                      src={t.avatar}
                      alt={t.name}
                      className="w-14 h-14 rounded-full object-cover border border-brand-green/30"
                    />
                    <div>
                      <h3 className="font-bold text-lg text-ink flex items-center gap-2">
                        <Link to="/profile/1" className="hover:text-brand-green transition-colors">
                          {t.name}
                        </Link>
                        <ShieldCheck className="w-4 h-4 text-brand-green" />
                      </h3>
                      <p className="text-xs text-mute font-medium">{t.title}</p>
                    </div>
                  </div>
                  <span className="text-lg font-extrabold text-brand-green">{t.rate}</span>
                </div>

                <p className="text-xs text-mute leading-relaxed">{t.bio}</p>

                <div className="flex flex-wrap gap-2 pt-2 border-t border-hairline">
                  {t.skills.map((s) => (
                    <span
                      key={s}
                      className="px-3 py-1 rounded-full bg-surface-elevated text-brand-green text-xs font-bold border border-hairline"
                    >
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
              <div
                key={p.id}
                className="bg-surface p-6 rounded-xl border border-hairline hover:bg-surface-elevated hover:scale-[1.01] transition-all duration-200 flex flex-col sm:flex-row sm:items-center justify-between gap-4 shadow-sm dark:shadow-none"
              >
                <div className="space-y-1">
                  <h3 className="font-bold text-lg text-ink hover:text-brand-green transition-colors">
                    <Link to={`/projects/${p.id}`}>{p.title}</Link>
                  </h3>
                  <div className="flex items-center gap-2 text-xs text-mute">
                    <span className="font-semibold text-brand-green">{p.client}</span> &bull; <span>Verified Escrow Deposit</span>
                  </div>
                </div>

                <div className="flex items-center justify-between sm:justify-end gap-6 border-t sm:border-t-0 border-hairline pt-3 sm:pt-0">
                  <div className="text-right">
                    <div className="text-lg font-extrabold text-brand-green">{p.budget}</div>
                    <div className="text-xs text-mute">Fixed Bounty</div>
                  </div>
                  <Link
                    to={`/projects/${p.id}`}
                    className="inline-flex items-center gap-1.5 bg-brand-green hover:bg-brand-green-hover text-white font-bold text-xs uppercase tracking-[0.05em] px-5 py-2.5 rounded-full hover:scale-[1.04] transition-all shadow-md shadow-brand-green/20"
                  >
                    View Bounty <ArrowRight className="w-3.5 h-3.5" />
                  </Link>
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
