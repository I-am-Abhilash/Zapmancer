import React, { useState } from 'react';
import './projects.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Search, ShieldCheck, ArrowRight, Plus, Bookmark, Clock, Award, Layers } from 'lucide-react';

type CategoryId = 'All' | 'KMP' | 'Mobile' | 'Full-Stack' | 'AI';

const CATEGORIES: { id: CategoryId; label: string }[] = [
  { id: 'All', label: 'All' },
  { id: 'KMP', label: 'Kotlin Multiplatform' },
  { id: 'Mobile', label: 'Mobile' },
  { id: 'Full-Stack', label: 'Full-Stack & Ktor' },
  { id: 'AI', label: 'AI & Data' },
];

const PROJECTS = [
  {
    id: '1',
    title: 'Compose Multiplatform Desktop App for Ktor Analytics',
    client: 'Acme AI Systems',
    clientAvatar: 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=120&q=80',
    category: 'KMP' as CategoryId,
    budget: '$3,200',
    budgetType: 'Fixed',
    duration: '3 weeks',
    level: 'Intermediate',
    proposals: 8,
    posted: '2 hours ago',
    description: 'Desktop management dashboard with Compose Multiplatform for Kotlin, integrating Ktor backend REST API and WebSocket streaming endpoints.',
    skills: ['Kotlin', 'Compose', 'Ktor', 'Desktop', 'SQLDelight'],
  },
  {
    id: '2',
    title: 'High-Concurrency PostgreSQL Exposed ORM Migration',
    client: 'Fintech Core',
    clientAvatar: 'https://images.unsplash.com/photo-1557804506-669a67965ba0?auto=format&fit=crop&w=120&q=80',
    category: 'Full-Stack' as CategoryId,
    budget: '$1,800',
    budgetType: 'Fixed',
    duration: '10 days',
    level: 'Expert',
    proposals: 14,
    posted: '5 hours ago',
    description: 'Refactor legacy SQL queries into Exposed ORM DSL with HikariCP pooling, automated migrations, and multi-region read replica fallback.',
    skills: ['PostgreSQL', 'Exposed', 'Ktor', 'SQL', 'HikariCP'],
  },
  {
    id: '3',
    title: 'WebAssembly Component for Real-Time Audio Processing',
    client: 'AudioCraft Labs',
    clientAvatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=120&q=80',
    category: 'KMP' as CategoryId,
    budget: '$4,500',
    budgetType: 'Fixed',
    duration: '1 month',
    level: 'Expert',
    proposals: 5,
    posted: '1 day ago',
    description: 'High-performance Wasm module compiled from Kotlin Native to process web audio streams in real-time with zero latency drops.',
    skills: ['Wasm', 'Kotlin', 'WebAudio', 'C++'],
  },
  {
    id: '4',
    title: 'Native Android Material 3 Design Overhaul',
    client: 'HealthSync Mobile',
    clientAvatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=120&q=80',
    category: 'Mobile' as CategoryId,
    budget: '$65 / hr',
    budgetType: 'Hourly',
    duration: '2–3 weeks',
    level: 'Entry',
    proposals: 19,
    posted: '2 days ago',
    description: 'Modernize Android app views to Material 3, Jetpack Compose adaptive layouts, and dynamic theme switching support.',
    skills: ['Android', 'Jetpack Compose', 'Kotlin', 'Material 3'],
  },
];

export const ProjectListPage: React.FC = () => {
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState<CategoryId>('All');
  const [level, setLevel] = useState('All');
  const [saved, setSaved] = useState<string[]>([]);

  const toggle = (id: string, e: React.MouseEvent) => {
    e.preventDefault();
    setSaved((p) => p.includes(id) ? p.filter((x) => x !== id) : [...p, id]);
  };

  const filtered = PROJECTS.filter((p) => {
    const q = search.toLowerCase();
    return (
      (p.title.toLowerCase().includes(q) || p.description.toLowerCase().includes(q)) &&
      (category === 'All' || p.category === category) &&
      (level === 'All' || p.level === level)
    );
  });

  return (
    <div className="projects-page">
      <Header isLoggedIn={true} />

      <main className="projects-main">

        {/* Header */}
        <div className="projects-header">
          <div className="projects-header-row">
            <div>
              <h1 className="projects-title">Open Contracts</h1>
              <p className="projects-subtitle">Browse verified freelance contracts with milestone escrow and 0% worker fee.</p>
            </div>
            <Link to="/projects/new" className="projects-btn-primary">
              <Plus size={15} /> Post a contract
            </Link>
          </div>
        </div>

        {/* Filters */}
        <div className="projects-filters">
          <div className="projects-search-row">
            <div className="projects-search-wrap">
              <Search size={15} />
              <input
                type="text"
                placeholder="Search contracts..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="projects-search-input"
              />
            </div>
            <select value={level} onChange={(e) => setLevel(e.target.value)} className="projects-select">
              <option value="All">All levels</option>
              <option value="Entry">Entry</option>
              <option value="Intermediate">Intermediate</option>
              <option value="Expert">Expert</option>
            </select>
          </div>

          {/* Category pills */}
          <div className="projects-cats">
            {CATEGORIES.map((c) => (
              <button
                key={c.id}
                onClick={() => setCategory(c.id)}
                className={`projects-cat${category === c.id ? ' active' : ''}`}
              >
                {c.label}
              </button>
            ))}
          </div>
        </div>

        {/* Count */}
        <p className="projects-count">{filtered.length} contract{filtered.length !== 1 ? 's' : ''}</p>

        {/* List */}
        {filtered.length === 0 ? (
          <div className="projects-empty">No contracts match your filters.</div>
        ) : (
          <div className="project-list">
            {filtered.map((p) => {
              const isSaved = saved.includes(p.id);
              return (
                <div key={p.id} className="project-card">
                  {/* Top row */}
                  <div className="project-card-top">
                    <div style={{ flex: 1 }}>
                      <div className="project-card-meta">
                        <img src={p.clientAvatar} alt={p.client} className="project-card-avatar" />
                        <span className="project-card-client">{p.client}</span>
                        <span>·</span>
                        <span className="project-card-verified">
                          <ShieldCheck size={11} /> Verified Escrow
                        </span>
                        <span>·</span>
                        <span>{p.posted}</span>
                      </div>
                      <p className="project-card-title" style={{ marginTop: 6 }}>{p.title}</p>
                    </div>

                    <div style={{ display: 'flex', alignItems: 'flex-start', gap: 10 }}>
                      <div className="project-card-budget-col">
                        <div className="project-card-budget">{p.budget}</div>
                        <div className="project-card-budget-type">{p.budgetType}</div>
                      </div>
                      <button
                        onClick={(e) => toggle(p.id, e)}
                        className={`project-bookmark-btn${isSaved ? ' saved' : ''}`}
                        title={isSaved ? 'Saved' : 'Save'}
                      >
                        <Bookmark size={14} fill={isSaved ? 'currentColor' : 'none'} />
                      </button>
                    </div>
                  </div>

                  {/* Description */}
                  <p className="project-card-desc">{p.description}</p>

                  {/* Footer */}
                  <div className="project-card-footer">
                    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
                      <div className="project-card-attrs">
                        <span className="project-card-attr"><Clock size={12} /> {p.duration}</span>
                        <span>·</span>
                        <span className="project-card-attr"><Award size={12} /> {p.level}</span>
                        <span>·</span>
                        <span className="project-card-attr"><Layers size={12} /> {p.proposals} proposals</span>
                      </div>
                      <div className="project-card-skills">
                        {p.skills.map((s) => <span key={s} className="project-skill-tag">{s}</span>)}
                      </div>
                    </div>

                    <Link to={`/projects/${p.id}/apply`} className="project-card-cta">
                      Submit proposal <ArrowRight size={13} />
                    </Link>
                  </div>
                </div>
              );
            })}
          </div>
        )}

      </main>

      <Footer />
    </div>
  );
};
