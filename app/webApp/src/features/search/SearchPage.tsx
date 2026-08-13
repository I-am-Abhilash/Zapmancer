import React, { useState } from 'react';
import './search.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Search, ShieldCheck, Star, MapPin, Sparkles, MessageSquare } from 'lucide-react';
import { Tabs, TabItem } from '../../components/ui/Tabs';

type TalentCategory = 'All' | 'KMP' | 'Compose' | 'Ktor' | 'Wasm';

export const SearchPage: React.FC = () => {
  const [selectedSpecialty, setSelectedSpecialty] = useState<TalentCategory>('All');
  const [query, setQuery] = useState('');

  const specialtyTabs: TabItem<TalentCategory>[] = [
    { id: 'All', label: 'All Engineers' },
    { id: 'KMP', label: 'Kotlin Multiplatform' },
    { id: 'Compose', label: 'Compose Desktop & Mobile' },
    { id: 'Ktor', label: 'Ktor & Microservices' },
    { id: 'Wasm', label: 'WebAssembly & AI' },
  ];

  const talents = [
    {
      id: 't1',
      name: 'Elena Rostova',
      title: 'Senior KMP & WebAssembly Lead',
      specialty: 'Wasm',
      avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
      rating: 5.0,
      reviews: 42,
      rate: '$85 / hr',
      successRate: '100% Success',
      location: 'Berlin, Germany',
      bio: 'Architected high-throughput Ktor backend microservices and Compose Multiplatform clients. Specialist in Kotlin Native compilation to WebAssembly.',
      skills: ['Kotlin', 'Wasm', 'Ktor', 'PostgreSQL', 'Docker', 'KMP']
    },
    {
      id: 't2',
      name: 'David Chen',
      title: 'iOS & Android Native KMP Architect',
      specialty: 'KMP',
      avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
      rating: 4.9,
      reviews: 38,
      rate: '$75 / hr',
      successRate: '98% Success',
      location: 'Toronto, Canada',
      bio: 'Specialized in SwiftUI & Jetpack Compose shared viewmodels with SQLDelight offline persistence and Coroutines async streams.',
      skills: ['KMP', 'SwiftUI', 'Compose', 'Coroutines', 'SQLDelight']
    },
    {
      id: 't3',
      name: 'Marcus Vance',
      title: 'Backend Systems & Ktor Core Engineer',
      specialty: 'Ktor',
      avatar: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=150&q=80',
      rating: 4.95,
      reviews: 29,
      rate: '$90 / hr',
      successRate: '100% Success',
      location: 'Austin, TX',
      bio: 'Exposed ORM maintainer and Ktor WebSocket engine developer. Expert in database connection pooling, Gorse AI pipelines, and Dockerized deployments.',
      skills: ['Ktor', 'PostgreSQL', 'Exposed ORM', 'Gorse AI', 'Kotlin']
    },
    {
      id: 't4',
      name: 'Sophia Al-Mansoor',
      title: 'UI/UX & Compose Multiplatform Lead',
      specialty: 'Compose',
      avatar: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=150&q=80',
      rating: 5.0,
      reviews: 51,
      rate: '$80 / hr',
      successRate: '100% Success',
      location: 'London, UK',
      bio: 'Crafts responsive multiplatform design systems with dynamic Light & Dark themes, custom canvas animations, and fluid micro-interactions.',
      skills: ['Compose UI', 'Material 3', 'Canvas', 'Design Systems', 'Kotlin']
    }
  ];

  const filteredTalents = talents.filter((t) => {
    const matchesSearch =
      t.name.toLowerCase().includes(query.toLowerCase()) ||
      t.title.toLowerCase().includes(query.toLowerCase()) ||
      t.bio.toLowerCase().includes(query.toLowerCase()) ||
      t.skills.some((s) => s.toLowerCase().includes(query.toLowerCase()));
    const matchesSpecialty = selectedSpecialty === 'All' || t.specialty === selectedSpecialty;
    return matchesSearch && matchesSpecialty;
  });

  return (
    <div className="search-page">
      <Header isLoggedIn={true} />

      <main className="search-main">
        
        {/* Title Banner */}
        <div className="search-header">
          <div className="search-badge">
            <Sparkles className="w-3.5 h-3.5" /> Talent Network
          </div>
          <h1 className="search-title">Find & Hire Top Talent</h1>
          <p className="search-subtitle">Discover vetted Kotlin Multiplatform, Mobile, and Web engineers ready for your next project contract.</p>
        </div>

        {/* Search & Specialty Filter Controls */}
        <div className="search-filter-card">
          
          <div className="relative">
            <Search className="w-5 h-5 text-mute absolute left-4 top-3.5" />
            <input
              type="text"
              placeholder="Search by engineer name, skills, title, or tech stack..."
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              className="search-input"
            />
          </div>

          <div>
            <Tabs
              tabs={specialtyTabs}
              activeTab={selectedSpecialty}
              onChange={(id) => setSelectedSpecialty(id)}
            />
          </div>

        </div>

        {/* Talent Cards Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {filteredTalents.map((t) => (
            <div
              key={t.id}
              className="talent-card-box"
            >
              <div className="space-y-4">
                <div className="flex items-start justify-between gap-4">
                  <div className="flex items-center gap-4">
                    <img
                      src={t.avatar}
                      alt={t.name}
                      className="talent-avatar"
                    />
                    <div>
                      <h3 className="font-extrabold text-lg text-ink flex items-center gap-1.5">
                        <Link to="/profile/1" className="talent-name">
                          {t.name}
                        </Link>
                        <ShieldCheck className="w-4 h-4 text-brand-green shrink-0" />
                      </h3>
                      <p className="text-xs font-semibold text-mute">{t.title}</p>
                      <div className="flex items-center gap-3 text-[11px] text-mute pt-1">
                        <span className="flex items-center gap-1"><MapPin className="w-3 h-3 text-brand-green" /> {t.location}</span>
                        <span>&bull;</span>
                        <span className="flex items-center gap-1 text-brand-green font-bold"><Star className="w-3 h-3 fill-current" /> {t.rating} ({t.reviews})</span>
                      </div>
                    </div>
                  </div>

                  <div className="text-right shrink-0">
                    <div className="talent-rate">{t.rate}</div>
                    <div className="text-[11px] font-bold text-brand-green bg-brand-green/10 px-2 py-0.5 rounded-full inline-block mt-0.5">
                      {t.successRate}
                    </div>
                  </div>
                </div>

                <p className="text-xs text-mute leading-relaxed line-clamp-3">
                  {t.bio}
                </p>

                <div className="flex flex-wrap gap-2 pt-2 border-t border-hairline">
                  {t.skills.map((s) => (
                    <span
                      key={s}
                      className="talent-skill-chip"
                    >
                      {s}
                    </span>
                  ))}
                </div>
              </div>

              <div className="pt-4 border-t border-hairline flex items-center justify-between gap-3">
                <Link
                  to="/profile/1"
                  className="text-xs font-bold uppercase tracking-[0.05em] text-mute hover:text-ink transition-colors"
                >
                  View Profile
                </Link>

                <Link
                  to="/messages"
                  className="talent-btn-primary"
                >
                  <MessageSquare className="w-3.5 h-3.5" /> Contact & Hire
                </Link>
              </div>

            </div>
          ))}
        </div>

      </main>

      <Footer />
    </div>
  );
};
