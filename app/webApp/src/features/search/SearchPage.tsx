import React, { useState, useEffect } from 'react';
import './search.css';
import { Link } from 'react-router-dom';
import { Search, ShieldCheck, Star, MapPin, MessageSquare, ChevronDown, ChevronUp, RotateCcw, Loader2 } from 'lucide-react';
import { userService, TalentProfile } from '../../services/userService';

type TalentCategory = 'All' | 'Dev' | 'AI' | 'Design' | 'DevOps' | 'Growth';
type TalentSmartPreset = 'all' | 'top_rated' | 'available_now' | 'high_earners' | 'verified_kyc' | 'agencies';

const CATEGORIES: { id: TalentCategory; label: string }[] = [
  { id: 'All', label: 'All Specialties' },
  { id: 'Dev', label: 'Software & Web Dev' },
  { id: 'AI', label: 'AI & Agents' },
  { id: 'Design', label: 'UI/UX Design' },
  { id: 'DevOps', label: 'DevOps & Cloud' },
  { id: 'Growth', label: 'Growth & Marketing' },
];

const AVAILABLE_SKILLS = ['TypeScript', 'React', 'Node.js', 'Wasm', 'PostgreSQL', 'Docker', 'Python', 'OpenAI', 'LangChain', 'PGVector', 'Gorse AI', 'PyTorch', 'Figma', 'UI/UX Design', 'Design Tokens', 'Tailwind', 'DevOps', 'Kubernetes', 'Ktor', 'AWS'];

export const SearchPage: React.FC = () => {
  const [talents, setTalents] = useState<TalentProfile[]>([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState('');
  const [sort, setSort] = useState<'highest_rating' | 'highest_rate' | 'lowest_rate' | 'reviews'>('highest_rating');
  const [preset, setPreset] = useState<TalentSmartPreset>('all');

  // Filter States
  const [category, setCategory] = useState<TalentCategory>('All');
  const [minRate, setMinRate] = useState('');
  const [maxRate, setMaxRate] = useState('');
  const [selectedSkills, setSelectedSkills] = useState<string[]>([]);
  const [minRating, setMinRating] = useState<string>('all');
  const [availableOnly, setAvailableOnly] = useState(false);
  const [talentType, setTalentType] = useState<string>('all');
  const [region, setRegion] = useState<string>('all');
  const [showMoreFilters, setShowMoreFilters] = useState(false);
  const [minEarnings, setMinEarnings] = useState<string>('all');

  useEffect(() => {
    let isMounted = true;
    setLoading(true);
    userService.searchTalent({ query, category })
      .then((data) => {
        if (isMounted) {
          setTalents(data);
          setLoading(false);
        }
      })
      .catch(() => {
        if (isMounted) setLoading(false);
      });

    return () => { isMounted = false; };
  }, [query, category]);

  const toggleSkill = (skill: string) => {
    setSelectedSkills((prev) =>
      prev.includes(skill) ? prev.filter((s) => s !== skill) : [...prev, skill]
    );
  };

  const resetAllFilters = () => {
    setQuery('');
    setCategory('All');
    setMinRate('');
    setMaxRate('');
    setSelectedSkills([]);
    setMinRating('all');
    setAvailableOnly(false);
    setTalentType('all');
    setRegion('all');
    setMinEarnings('all');
    setPreset('all');
  };

  const filtered = talents.filter((t) => {
    // Keyword search
    const q = query.toLowerCase();
    if (q && !(t.name.toLowerCase().includes(q) || t.title.toLowerCase().includes(q) || t.bio.toLowerCase().includes(q) || t.skills.some((s) => s.toLowerCase().includes(q)))) {
      return false;
    }

    // Smart Presets
    if (preset === 'top_rated' && t.rating < 4.9) return false;
    if (preset === 'available_now' && !t.availableNow) return false;
    if (preset === 'high_earners' && t.totalEarned < 50000) return false;
    if (preset === 'agencies' && t.talentType !== 'Agency') return false;

    // Specialty Category
    if (category !== 'All' && t.specialty !== category) return false;

    // Rate filtering
    if (minRate && t.rateNum < Number(minRate)) return false;
    if (maxRate && t.rateNum > Number(maxRate)) return false;

    // Skills Matching
    if (selectedSkills.length > 0) {
      const hasAny = selectedSkills.some((sk) => t.skills.includes(sk));
      if (!hasAny) return false;
    }

    // Rating
    if (minRating === '4.5' && t.rating < 4.5) return false;
    if (minRating === '4.8' && t.rating < 4.8) return false;

    // Availability
    if (availableOnly && !t.availableNow) return false;

    // Talent Type
    if (talentType !== 'all' && t.talentType.toLowerCase() !== talentType.toLowerCase()) return false;

    // Region
    if (region !== 'all' && t.region !== region) return false;

    // Earnings
    if (minEarnings === '10k' && t.totalEarned < 10000) return false;
    if (minEarnings === '50k' && t.totalEarned < 50000) return false;

    return true;
  }).sort((a, b) => {
    if (sort === 'highest_rate') return b.rateNum - a.rateNum;
    if (sort === 'lowest_rate') return a.rateNum - b.rateNum;
    if (sort === 'reviews') return b.reviews - a.reviews;
    return b.rating - a.rating; // highest rating default
  });

  return (
    <div className="search-page">
      <main className="search-main">

        {/* Header */}
        <div className="search-header">
          <h1 className="search-title">Find Talent</h1>
          <p className="search-subtitle">Browse vetted developers, AI builders, designers, and growth experts available for contract work.</p>
        </div>

        {/* 2-Column Sidebar Layout */}
        <div className="search-layout">

          {/* Left Sidebar Filters */}
          <aside className="search-sidebar">
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <span style={{ fontSize: 14, fontWeight: 700, color: 'var(--color-ink)' }}>Filter Talent</span>
              <button onClick={resetAllFilters} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 12, color: 'var(--color-steel)', display: 'flex', alignItems: 'center', gap: 4 }}>
                <RotateCcw size={12} /> Reset
              </button>
            </div>

            {/* 1. Specialty Category */}
            <div className="search-filter-group">
              <span className="search-filter-title">Specialty</span>
              <select
                value={category}
                onChange={(e) => setCategory(e.target.value as TalentCategory)}
                className="search-select"
              >
                {CATEGORIES.map((c) => (
                  <option key={c.id} value={c.id}>{c.label}</option>
                ))}
              </select>
            </div>

            {/* 2. Hourly Rate Range */}
            <div className="search-filter-group">
              <span className="search-filter-title">Hourly Rate ($ USD / hr)</span>
              <div className="search-input-row">
                <input
                  type="number"
                  placeholder="Min $"
                  value={minRate}
                  onChange={(e) => setMinRate(e.target.value)}
                  className="search-number-input"
                />
                <span style={{ fontSize: 12, color: 'var(--color-steel)' }}>to</span>
                <input
                  type="number"
                  placeholder="Max $"
                  value={maxRate}
                  onChange={(e) => setMaxRate(e.target.value)}
                  className="search-number-input"
                />
              </div>
            </div>

            {/* 3. Required Skills */}
            <div className="search-filter-group">
              <div className="search-filter-title">
                <span>Required Skills</span>
                <span style={{ fontSize: 10, color: 'var(--color-steel)' }}>{selectedSkills.length} selected</span>
              </div>
              <div className="search-skill-box">
                {AVAILABLE_SKILLS.slice(0, 12).map((sk) => {
                  const isSelected = selectedSkills.includes(sk);
                  return (
                    <button
                      key={sk}
                      type="button"
                      onClick={() => toggleSkill(sk)}
                      className={`search-skill-chip-btn${isSelected ? ' selected' : ''}`}
                    >
                      {sk}
                    </button>
                  );
                })}
              </div>
            </div>

            {/* 4. Minimum Rating */}
            <div className="search-filter-group">
              <span className="search-filter-title">Rating</span>
              {[
                { id: 'all', label: 'Any rating' },
                { id: '4.5', label: '★ 4.5 & up' },
                { id: '4.8', label: '★ 4.8 & up' },
              ].map((r) => (
                <label key={r.id} className="search-filter-option">
                  <input
                    type="radio"
                    name="minRating"
                    checked={minRating === r.id}
                    onChange={() => setMinRating(r.id)}
                  />
                  <span>{r.label}</span>
                </label>
              ))}
            </div>

            {/* 5. Availability */}
            <div className="search-filter-group">
              <span className="search-filter-title">Availability</span>
              <label className="search-filter-option">
                <input
                  type="checkbox"
                  checked={availableOnly}
                  onChange={(e) => setAvailableOnly(e.target.checked)}
                />
                <span>Available Now for Hire</span>
              </label>
            </div>

            {/* 6. Talent Type */}
            <div className="search-filter-group">
              <span className="search-filter-title">Talent Type</span>
              {[
                { id: 'all', label: 'All Talent' },
                { id: 'individual', label: 'Individual Freelancers' },
                { id: 'agency', label: 'Agencies & Teams' },
              ].map((t) => (
                <label key={t.id} className="search-filter-option">
                  <input
                    type="radio"
                    name="talentType"
                    checked={talentType === t.id}
                    onChange={() => setTalentType(t.id)}
                  />
                  <span>{t.label}</span>
                </label>
              ))}
            </div>

            {/* 7. Collapsible More Filters */}
            <div className="search-filter-group" style={{ borderBottom: 'none', paddingBottom: 0 }}>
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
                  <span className="search-filter-title">Region</span>
                  <select value={region} onChange={(e) => setRegion(e.target.value)} className="search-select">
                    <option value="all">Worldwide</option>
                    <option value="Europe">Europe</option>
                    <option value="North America">North America</option>
                  </select>

                  <span className="search-filter-title" style={{ marginTop: 6 }}>Total Earnings</span>
                  {[
                    { id: 'all', label: 'Any earnings' },
                    { id: '10k', label: '$10k+ earned' },
                    { id: '50k', label: '$50k+ earned' },
                  ].map((e) => (
                    <label key={e.id} className="search-filter-option">
                      <input
                        type="radio"
                        name="minEarnings"
                        checked={minEarnings === e.id}
                        onChange={() => setMinEarnings(e.id)}
                      />
                      <span>{e.label}</span>
                    </label>
                  ))}
                </div>
              )}
            </div>

          </aside>

          {/* Right Main Content */}
          <div className="search-content">

            {/* Smart Preset Pills */}
            <div className="search-presets-bar">
              {[
                { id: 'all', label: 'All Talent' },
                { id: 'top_rated', label: 'Top Rated (4.9+)' },
                { id: 'available_now', label: 'Available Now' },
                { id: 'high_earners', label: 'High Earners ($50k+)' },
                { id: 'agencies', label: 'Agencies & Teams' },
              ].map((p) => (
                <button
                  key={p.id}
                  onClick={() => setPreset(p.id as TalentSmartPreset)}
                  className={`search-preset-chip${preset === p.id ? ' active' : ''}`}
                >
                  {p.label}
                </button>
              ))}
            </div>

            {/* Toolbar */}
            <div className="search-toolbar">
              <div className="search-input-wrap">
                <Search size={15} />
                <input
                  type="text"
                  placeholder="Search by name, title, or skills..."
                  value={query}
                  onChange={(e) => setQuery(e.target.value)}
                  className="search-input"
                />
              </div>

              <select
                value={sort}
                onChange={(e) => setSort(e.target.value as any)}
                className="search-sort-select"
              >
                <option value="highest_rating">Sort: Highest Rating</option>
                <option value="highest_rate">Sort: Highest Rate ($/hr)</option>
                <option value="lowest_rate">Sort: Lowest Rate ($/hr)</option>
                <option value="reviews">Sort: Most Reviews</option>
              </select>
            </div>

            {/* Count */}
            <p className="search-count">{filtered.length} talent{filtered.length !== 1 ? 's' : ''} found</p>

            {/* List */}
            {loading ? (
              <div className="min-h-[300px] flex flex-col items-center justify-center gap-3 bg-surface-elevated rounded-xl border border-hairline p-12">
                <Loader2 className="w-6 h-6 text-primary animate-spin" />
                <p className="text-xs font-semibold text-mute">Searching vetted talent & AI builders...</p>
              </div>
            ) : filtered.length === 0 ? (
              <div className="search-empty">
                No talent matches your selected criteria. Try adjusting your rate or skill filters.
                <div style={{ marginTop: 12 }}>
                  <button onClick={resetAllFilters} className="search-clear-btn" style={{ width: 'auto', padding: '6px 16px' }}>
                    Reset All Filters
                  </button>
                </div>
              </div>
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
                            <Link to={`/profile/${t.id}`} className="talent-name hover:text-primary transition-colors">{t.name}</Link>
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
                              {t.rating} ({t.reviews} reviews)
                            </span>
                            <span>·</span>
                            <span style={{ fontWeight: 600, color: t.availableNow ? '#1aae39' : 'var(--color-steel)' }}>
                              {t.availableNow ? 'Available' : 'Busy'}
                            </span>
                          </div>
                        </div>
                      </div>

                      <div className="talent-rate-col">
                        <div className="talent-rate">{t.rateDisplay}</div>
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
                        <Link to={`/profile/${t.id}`} className="talent-link-secondary">View profile</Link>
                        <Link to="/messages" className="talent-btn-primary">
                          <MessageSquare size={13} /> Contact
                        </Link>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}

          </div>

        </div>

      </main>
    </div>
  );
};
