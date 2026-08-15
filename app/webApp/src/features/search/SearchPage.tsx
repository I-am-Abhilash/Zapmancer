import React, { useState } from 'react';
import './search.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Search, ShieldCheck, Star, MapPin, MessageSquare } from 'lucide-react';

type TalentCategory = 'All' | 'Dev' | 'AI' | 'Design' | 'DevOps' | 'Growth';

const CATEGORIES: { id: TalentCategory; label: string }[] = [
  { id: 'All', label: 'All' },
  { id: 'Dev', label: 'Software & Web Dev' },
  { id: 'AI', label: 'AI & Agents' },
  { id: 'Design', label: 'UI/UX Design' },
  { id: 'DevOps', label: 'DevOps & Cloud' },
  { id: 'Growth', label: 'Growth & Marketing' },
];

const TALENTS = [
  {
    id: 't1',
    name: 'Elena Rostova',
    title: 'Senior Full-Stack & WebAssembly Lead',
    specialty: 'Dev',
    avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
    rating: 5.0,
    reviews: 42,
    rate: '$85 / hr',
    successRate: '100% Success',
    location: 'Berlin, Germany',
    bio: 'Architected high-throughput backend microservices, React web applications, and WebAssembly audio processing components for enterprise clients.',
    skills: ['TypeScript', 'React', 'Node.js', 'Wasm', 'PostgreSQL', 'Docker'],
  },
  {
    id: 't2',
    name: 'Dr. Lucas Meyer',
    title: 'AI Agent & RAG Pipeline Specialist',
    specialty: 'AI',
    avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
    rating: 5.0,
    reviews: 38,
    rate: '$95 / hr',
    successRate: '100% Success',
    location: 'Zurich, Switzerland',
    bio: 'Specialized in vector embeddings, LangChain RAG pipelines, LLM fine-tuning, and Gorse AI recommendation clusters for enterprise-scale deployments.',
    skills: ['Python', 'OpenAI', 'LangChain', 'PGVector', 'Gorse AI', 'PyTorch'],
  },
  {
    id: 't3',
    name: 'Sophia Al-Mansoor',
    title: 'Principal UI/UX & Design Systems Lead',
    specialty: 'Design',
    avatar: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=150&q=80',
    rating: 5.0,
    reviews: 51,
    rate: '$80 / hr',
    successRate: '100% Success',
    location: 'London, UK',
    bio: 'Crafts responsive multiplatform design systems in Figma with dynamic Light & Dark themes, custom canvas animations, and fluid micro-interactions.',
    skills: ['Figma', 'UI/UX Design', 'Design Tokens', 'Tailwind', 'Prototyping'],
  },
  {
    id: 't4',
    name: 'Marcus Vance',
    title: 'DevOps, Ktor & Cloud Systems Engineer',
    specialty: 'DevOps',
    avatar: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=150&q=80',
    rating: 4.95,
    reviews: 29,
    rate: '$90 / hr',
    successRate: '100% Success',
    location: 'Austin, TX',
    bio: 'Cloud infra maintainer and backend WebSockets engineer. Expert in Kubernetes, CI/CD pipelines, database connection pooling, and Dockerized deployments.',
    skills: ['DevOps', 'Kubernetes', 'Ktor', 'PostgreSQL', 'Docker', 'AWS'],
  },
];

export const SearchPage: React.FC = () => {
  const [category, setCategory] = useState<TalentCategory>('All');
  const [query, setQuery] = useState('');

  const filtered = TALENTS.filter((t) => {
    const q = query.toLowerCase();
    return (
      (t.name.toLowerCase().includes(q) || t.title.toLowerCase().includes(q) ||
       t.bio.toLowerCase().includes(q) || t.skills.some((s) => s.toLowerCase().includes(q))) &&
      (category === 'All' || t.specialty === category)
    );
  });

  return (
    <div className="search-page">
      <Header isLoggedIn={true} />

      <main className="search-main">

        {/* Header */}
        <div className="search-header">
          <h1 className="search-title">Find Talent</h1>
          <p className="search-subtitle">Browse vetted developers, AI builders, designers, and growth experts available for contract work.</p>
        </div>

        {/* Filters */}
        <div className="search-filters">
          <div className="search-input-wrap">
            <Search size={15} />
            <input
              type="text"
              placeholder="Search by name, skills, or specialty..."
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              className="search-input"
            />
          </div>
          <div className="search-cats">
            {CATEGORIES.map((c) => (
              <button
                key={c.id}
                onClick={() => setCategory(c.id)}
                className={`search-cat${category === c.id ? ' active' : ''}`}
              >
                {c.label}
              </button>
            ))}
          </div>
        </div>

        <p className="search-count">{filtered.length} talent{filtered.length !== 1 ? 's' : ''} found</p>

        {filtered.length === 0 ? (
          <div className="search-empty">No talent matches your search.</div>
        ) : (
          <div className="talent-list">
            {filtered.map((t) => (
              <div key={t.id} className="talent-card">
                {/* Top */}
                <div className="talent-card-top">
                  <div className="talent-card-identity">
                    <img src={t.avatar} alt={t.name} className="talent-avatar" />
                    <div>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
                        <Link to="/profile/1" className="talent-name">{t.name}</Link>
                        <span className="talent-verified"><ShieldCheck size={11} /> KYC</span>
                      </div>
                      <p className="talent-title-text">{t.title}</p>
                      <div className="talent-meta">
                        <span style={{ display: 'flex', alignItems: 'center', gap: 3 }}>
                          <MapPin size={11} /> {t.location}
                        </span>
                        <span>·</span>
                        <span className="talent-rating">
                          <Star size={11} fill="currentColor" style={{ color: '#f59e0b' }} />
                          {t.rating} ({t.reviews})
                        </span>
                      </div>
                    </div>
                  </div>

                  <div className="talent-rate-col">
                    <div className="talent-rate">{t.rate}</div>
                    <div className="talent-success">{t.successRate}</div>
                  </div>
                </div>

                {/* Bio */}
                <p className="talent-bio">{t.bio}</p>

                {/* Footer */}
                <div className="talent-card-footer">
                  <div className="talent-skills">
                    {t.skills.map((s) => <span key={s} className="talent-skill-tag">{s}</span>)}
                  </div>
                  <div className="talent-card-actions">
                    <Link to="/profile/1" className="talent-link-secondary">View profile</Link>
                    <Link to="/messages" className="talent-btn-primary">
                      <MessageSquare size={13} /> Contact
                    </Link>
                  </div>
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
