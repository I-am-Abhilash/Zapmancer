import React, { useState } from 'react';
import './projects.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Search, ShieldCheck, ArrowRight, Plus, Bookmark, Clock, Award, Layers, ChevronDown, ChevronUp, RotateCcw } from 'lucide-react';

type ProjectType = 'All' | 'Fixed' | 'Hourly';
type SmartPreset = 'all' | 'best_match' | 'newest' | 'high_budget' | 'low_competition' | 'verified_only';

const AVAILABLE_SKILLS = ['Kotlin', 'Compose', 'Ktor', 'Desktop', 'SQLDelight', 'PostgreSQL', 'Exposed', 'SQL', 'HikariCP', 'Wasm', 'WebAudio', 'Android', 'Jetpack Compose', 'Material 3', 'Docker'];

const PROJECTS = [
  {
    id: '1',
    title: 'Compose Multiplatform Desktop App for Ktor Analytics',
    client: 'Acme AI Systems',
    clientAvatar: 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=120&q=80',
    projectType: 'Fixed' as ProjectType,
    budgetValue: 3200,
    budgetDisplay: '$3,200',
    duration: '3 weeks',
    level: 'Intermediate',
    proposals: 8,
    postedHours: 2,
    postedDisplay: '2 hours ago',
    verified: true,
    clientSpent: 42000,
    clientRating: 4.9,
    description: 'Desktop management dashboard with Compose Multiplatform for Kotlin, integrating Ktor backend REST API and WebSocket streaming endpoints.',
    skills: ['Kotlin', 'Compose', 'Ktor', 'Desktop', 'SQLDelight'],
  },
  {
    id: '2',
    title: 'High-Concurrency PostgreSQL Exposed ORM Migration',
    client: 'Fintech Core',
    clientAvatar: 'https://images.unsplash.com/photo-1557804506-669a67965ba0?auto=format&fit=crop&w=120&q=80',
    projectType: 'Fixed' as ProjectType,
    budgetValue: 1800,
    budgetDisplay: '$1,800',
    duration: '10 days',
    level: 'Expert',
    proposals: 14,
    postedHours: 5,
    postedDisplay: '5 hours ago',
    verified: true,
    clientSpent: 12500,
    clientRating: 4.8,
    description: 'Refactor legacy SQL queries into Exposed ORM DSL with HikariCP pooling, automated migrations, and multi-region read replica fallback.',
    skills: ['PostgreSQL', 'Exposed', 'Ktor', 'SQL', 'HikariCP'],
  },
  {
    id: '3',
    title: 'WebAssembly Component for Real-Time Audio Processing',
    client: 'AudioCraft Labs',
    clientAvatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=120&q=80',
    projectType: 'Fixed' as ProjectType,
    budgetValue: 4500,
    budgetDisplay: '$4,500',
    duration: '1 month',
    level: 'Expert',
    proposals: 4,
    postedHours: 24,
    postedDisplay: '1 day ago',
    verified: true,
    clientSpent: 8200,
    clientRating: 5.0,
    description: 'High-performance Wasm module compiled from Kotlin Native to process web audio streams in real-time with zero latency drops.',
    skills: ['Wasm', 'Kotlin', 'WebAudio', 'Desktop'],
  },
  {
    id: '4',
    title: 'Native Android Material 3 Design Overhaul',
    client: 'HealthSync Mobile',
    clientAvatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=120&q=80',
    projectType: 'Hourly' as ProjectType,
    budgetValue: 65,
    budgetDisplay: '$65 / hr',
    duration: '2–3 weeks',
    level: 'Entry',
    proposals: 19,
    postedHours: 48,
    postedDisplay: '2 days ago',
    verified: false,
    clientSpent: 1500,
    clientRating: 4.2,
    description: 'Modernize Android app views to Material 3, Jetpack Compose adaptive layouts, and dynamic theme switching support.',
    skills: ['Android', 'Jetpack Compose', 'Kotlin', 'Material 3'],
  },
];

export const ProjectListPage: React.FC = () => {
  const [search, setSearch] = useState('');
  const [sort, setSort] = useState<'newest' | 'highest_budget' | 'proposals'>('newest');
  const [preset, setPreset] = useState<SmartPreset>('all');
  
  // Filters State
  const [projectType, setProjectType] = useState<ProjectType>('All');
  const [minBudget, setMinBudget] = useState<string>('');
  const [maxBudget, setMaxBudget] = useState<string>('');
  const [selectedSkills, setSelectedSkills] = useState<string[]>([]);
  const [levels, setLevels] = useState<string[]>([]);
  const [proposalsRange, setProposalsRange] = useState<string>('all');
  const [verifiedOnly, setVerifiedOnly] = useState(false);
  const [showMoreFilters, setShowMoreFilters] = useState(false);
  const [maxHours, setMaxHours] = useState<string>('all');
  const [saved, setSaved] = useState<string[]>([]);

  const toggleBookmark = (id: string, e: React.MouseEvent) => {
    e.preventDefault();
    setSaved((p) => p.includes(id) ? p.filter((x) => x !== id) : [...p, id]);
  };

  const toggleSkill = (skill: string) => {
    setSelectedSkills((prev) =>
      prev.includes(skill) ? prev.filter((s) => s !== skill) : [...prev, skill]
    );
  };

  const toggleLevel = (lvl: string) => {
    setLevels((prev) =>
      prev.includes(lvl) ? prev.filter((l) => l !== lvl) : [...prev, lvl]
    );
  };

  const resetAllFilters = () => {
    setSearch('');
    setProjectType('All');
    setMinBudget('');
    setMaxBudget('');
    setSelectedSkills([]);
    setLevels([]);
    setProposalsRange('all');
    setVerifiedOnly(false);
    setMaxHours('all');
    setPreset('all');
  };

  const filtered = PROJECTS.filter((p) => {
    // Text search
    const q = search.toLowerCase();
    if (q && !(p.title.toLowerCase().includes(q) || p.description.toLowerCase().includes(q) || p.skills.some((s) => s.toLowerCase().includes(q)))) {
      return false;
    }

    // Smart Presets
    if (preset === 'newest' && p.postedHours > 24) return false;
    if (preset === 'high_budget' && p.budgetValue < 3000) return false;
    if (preset === 'low_competition' && p.proposals >= 5) return false;
    if (preset === 'verified_only' && !p.verified) return false;

    // Project Type
    if (projectType !== 'All' && p.projectType !== projectType) return false;

    // Conditional Budget Filtering
    if (minBudget && p.budgetValue < Number(minBudget)) return false;
    if (maxBudget && p.budgetValue > Number(maxBudget)) return false;

    // Skills Matching
    if (selectedSkills.length > 0) {
      const hasAny = selectedSkills.some((sk) => p.skills.includes(sk));
      if (!hasAny) return false;
    }

    // Experience Levels
    if (levels.length > 0 && !levels.includes(p.level)) return false;

    // Proposal Competition
    if (proposalsRange === 'under5' && p.proposals >= 5) return false;
    if (proposalsRange === '5to15' && (p.proposals < 5 || p.proposals > 15)) return false;
    if (proposalsRange === 'over15' && p.proposals <= 15) return false;

    // Verified
    if (verifiedOnly && !p.verified) return false;

    // Posted Hours
    if (maxHours === '24' && p.postedHours > 24) return false;
    if (maxHours === '72' && p.postedHours > 72) return false;

    return true;
  }).sort((a, b) => {
    if (sort === 'highest_budget') return b.budgetValue - a.budgetValue;
    if (sort === 'proposals') return a.proposals - b.proposals;
    return a.postedHours - b.postedHours; // newest default
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

        {/* 2-Column Sidebar Layout */}
        <div className="projects-layout">

          {/* Left Sidebar Filters */}
          <aside className="projects-sidebar">
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <span style={{ fontSize: 14, fontWeight: 700, color: 'var(--color-ink)' }}>Filter Contracts</span>
              <button onClick={resetAllFilters} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 12, color: 'var(--color-steel)', display: 'flex', alignItems: 'center', gap: 4 }}>
                <RotateCcw size={12} /> Reset
              </button>
            </div>

            {/* 1. Project Type */}
            <div className="projects-filter-group">
              <span className="projects-filter-title">Project Type</span>
              {(['All', 'Fixed', 'Hourly'] as ProjectType[]).map((t) => (
                <label key={t} className="projects-filter-option">
                  <input
                    type="radio"
                    name="projectType"
                    checked={projectType === t}
                    onChange={() => setProjectType(t)}
                  />
                  <span>{t === 'All' ? 'All Types' : t === 'Fixed' ? 'Fixed Price' : 'Hourly Rate'}</span>
                </label>
              ))}
            </div>

            {/* 2. Conditional Budget Range */}
            <div className="projects-filter-group">
              <span className="projects-filter-title">
                {projectType === 'Fixed' ? 'Fixed Budget ($ USD)' : projectType === 'Hourly' ? 'Hourly Rate ($/hr)' : 'Budget / Rate Range'}
              </span>
              <div className="projects-input-row">
                <input
                  type="number"
                  placeholder="Min $"
                  value={minBudget}
                  onChange={(e) => setMinBudget(e.target.value)}
                  className="projects-number-input"
                />
                <span style={{ fontSize: 12, color: 'var(--color-steel)' }}>to</span>
                <input
                  type="number"
                  placeholder="Max $"
                  value={maxBudget}
                  onChange={(e) => setMaxBudget(e.target.value)}
                  className="projects-number-input"
                />
              </div>
            </div>

            {/* 3. Required Skills */}
            <div className="projects-filter-group">
              <div className="projects-filter-title">
                <span>Required Skills</span>
                <span style={{ fontSize: 10, color: 'var(--color-steel)' }}>{selectedSkills.length} selected</span>
              </div>
              <div className="projects-skill-box">
                {AVAILABLE_SKILLS.map((sk) => {
                  const isSelected = selectedSkills.includes(sk);
                  return (
                    <button
                      key={sk}
                      type="button"
                      onClick={() => toggleSkill(sk)}
                      className={`projects-skill-chip-btn${isSelected ? ' selected' : ''}`}
                    >
                      {sk}
                    </button>
                  );
                })}
              </div>
            </div>

            {/* 4. Experience Level */}
            <div className="projects-filter-group">
              <span className="projects-filter-title">Experience Level</span>
              {['Entry', 'Intermediate', 'Expert'].map((lvl) => (
                <label key={lvl} className="projects-filter-option">
                  <input
                    type="checkbox"
                    checked={levels.includes(lvl)}
                    onChange={() => toggleLevel(lvl)}
                  />
                  <span>{lvl}</span>
                </label>
              ))}
            </div>

            {/* 5. Proposal Competition */}
            <div className="projects-filter-group">
              <span className="projects-filter-title">Competition Level</span>
              {[
                { id: 'all', label: 'Any proposals' },
                { id: 'under5', label: 'Low (< 5 proposals)' },
                { id: '5to15', label: 'Medium (5–15 proposals)' },
                { id: 'over15', label: 'High (15+ proposals)' },
              ].map((opt) => (
                <label key={opt.id} className="projects-filter-option">
                  <input
                    type="radio"
                    name="proposalsRange"
                    checked={proposalsRange === opt.id}
                    onChange={() => setProposalsRange(opt.id)}
                  />
                  <span>{opt.label}</span>
                </label>
              ))}
            </div>

            {/* 6. Client Quality */}
            <div className="projects-filter-group">
              <span className="projects-filter-title">Client Quality</span>
              <label className="projects-filter-option">
                <input
                  type="checkbox"
                  checked={verifiedOnly}
                  onChange={(e) => setVerifiedOnly(e.target.checked)}
                />
                <span>Verified Payment Only</span>
              </label>
            </div>

            {/* 7. Collapsible More Filters */}
            <div className="projects-filter-group" style={{ borderBottom: 'none', paddingBottom: 0 }}>
              <button
                type="button"
                onClick={() => setShowMoreFilters(!showMoreFilters)}
                style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 12, fontWeight: 600, color: 'var(--color-primary)', display: 'flex', alignItems: 'center', gap: 4, padding: 0 }}
              >
                {showMoreFilters ? <ChevronUp size={14} /> : <ChevronDown size={14} />}
                {showMoreFilters ? 'Hide Advanced Filters' : 'More Advanced Filters'}
              </button>

              {showMoreFilters && (
                <div style={{ display: 'flex', flexDirection: 'column', gap: 10, marginTop: 10 }}>
                  <span className="projects-filter-title">Job Freshness</span>
                  {[
                    { id: 'all', label: 'Any time' },
                    { id: '24', label: 'Last 24 hours' },
                    { id: '72', label: 'Last 3 days' },
                  ].map((f) => (
                    <label key={f.id} className="projects-filter-option">
                      <input
                        type="radio"
                        name="maxHours"
                        checked={maxHours === f.id}
                        onChange={() => setMaxHours(f.id)}
                      />
                      <span>{f.label}</span>
                    </label>
                  ))}
                </div>
              )}
            </div>

          </aside>

          {/* Right Main Feed */}
          <div className="projects-content">

            {/* Smart Preset Pills */}
            <div className="projects-presets-bar">
              {[
                { id: 'all', label: 'All Contracts' },
                { id: 'best_match', label: 'Best Match' },
                { id: 'newest', label: 'Newest' },
                { id: 'high_budget', label: 'High Budget' },
                { id: 'low_competition', label: 'Low Competition' },
                { id: 'verified_only', label: 'Verified Escrow' },
              ].map((p) => (
                <button
                  key={p.id}
                  onClick={() => setPreset(p.id as SmartPreset)}
                  className={`projects-preset-chip${preset === p.id ? ' active' : ''}`}
                >
                  {p.label}
                </button>
              ))}
            </div>

            {/* Search & Sort Toolbar */}
            <div className="projects-toolbar">
              <div className="projects-search-wrap">
                <Search size={15} />
                <input
                  type="text"
                  placeholder="Search by keywords or technology..."
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
                  className="projects-search-input"
                />
              </div>

              <select
                value={sort}
                onChange={(e) => setSort(e.target.value as any)}
                className="projects-sort-select"
              >
                <option value="newest">Sort: Newest First</option>
                <option value="highest_budget">Sort: Highest Budget</option>
                <option value="proposals">Sort: Lowest Competition</option>
              </select>
            </div>

            {/* Results Count */}
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <p className="projects-count">{filtered.length} contract{filtered.length !== 1 ? 's' : ''} found</p>
            </div>

            {/* Listings Grid */}
            {filtered.length === 0 ? (
              <div className="projects-empty">
                No contracts match your selected filters. Try adjusting your skills or budget criteria.
                <div style={{ marginTop: 12 }}>
                  <button onClick={resetAllFilters} className="projects-clear-btn" style={{ width: 'auto', padding: '6px 16px' }}>
                    Reset All Filters
                  </button>
                </div>
              </div>
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
                            {p.verified && (
                              <span className="project-card-verified">
                                <ShieldCheck size={11} /> Verified Escrow
                              </span>
                            )}
                            <span>·</span>
                            <span>{p.postedDisplay}</span>
                          </div>
                          <p className="project-card-title" style={{ marginTop: 6 }}>{p.title}</p>
                        </div>

                        <div style={{ display: 'flex', alignItems: 'flex-start', gap: 10 }}>
                          <div className="project-card-budget-col">
                            <div className="project-card-budget">{p.budgetDisplay}</div>
                            <div className="project-card-budget-type">{p.projectType === 'Fixed' ? 'Fixed Price' : 'Hourly Rate'}</div>
                          </div>
                          <button
                            onClick={(e) => toggleBookmark(p.id, e)}
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

          </div>

        </div>

      </main>

      <Footer />
    </div>
  );
};
