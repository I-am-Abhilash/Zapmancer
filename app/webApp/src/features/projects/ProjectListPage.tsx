import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Search, ShieldCheck, ArrowUpRight, Plus, Sparkles } from 'lucide-react';

export const ProjectListPage: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [experienceLevel, setExperienceLevel] = useState('All');

  const categories = ['All', 'Kotlin Multiplatform', 'Mobile (Android/iOS)', 'Full-Stack', 'AI & Data Engine'];

  const projects = [
    {
      id: '1',
      title: 'Compose Multiplatform Desktop App for Ktor Analytics',
      client: 'Acme AI Systems',
      category: 'Kotlin Multiplatform',
      budget: '$3,200',
      budgetType: 'Fixed-price',
      level: 'Intermediate',
      proposals: 8,
      posted: '2 hours ago',
      description: 'We need a desktop management dashboard built with Compose Multiplatform for Kotlin. Integrates with our existing Ktor backend REST API.',
      skills: ['Kotlin', 'Compose', 'Ktor', 'Desktop']
    },
    {
      id: '2',
      title: 'High-Concurrency PostgreSQL Exposed ORM Migration',
      client: 'Fintech Core',
      category: 'Full-Stack',
      budget: '$1,800',
      budgetType: 'Fixed-price',
      level: 'Expert',
      proposals: 14,
      posted: '5 hours ago',
      description: 'Refactor our legacy SQL queries into Exposed ORM DSL with HikariCP connection pooling and multi-region read replicas.',
      skills: ['PostgreSQL', 'Exposed', 'Ktor', 'SQL']
    },
    {
      id: '3',
      title: 'WebAssembly Wasm Component for Audio Processing',
      client: 'AudioCraft Labs',
      category: 'Kotlin Multiplatform',
      budget: '$4,500',
      budgetType: 'Fixed-price',
      level: 'Expert',
      proposals: 5,
      posted: '1 day ago',
      description: 'Build a high-performance Wasm module compiled from Kotlin Native to process web audio streams in real-time.',
      skills: ['Wasm', 'Kotlin', 'WebAudio', 'C++']
    },
    {
      id: '4',
      title: 'Native Android Material 3 Design Overhaul',
      client: 'HealthSync Mobile',
      category: 'Mobile (Android/iOS)',
      budget: '$65 / hr',
      budgetType: 'Hourly',
      level: 'Entry',
      proposals: 19,
      posted: '2 days ago',
      description: 'Modernize our Android app views to follow latest Material 3 guidelines and Jetpack Compose adaptive layouts.',
      skills: ['Android', 'Jetpack Compose', 'Kotlin']
    }
  ];

  const filteredProjects = projects.filter((p) => {
    const matchesSearch = p.title.toLowerCase().includes(searchQuery.toLowerCase()) || p.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCat = selectedCategory === 'All' || p.category === selectedCategory;
    const matchesLevel = experienceLevel === 'All' || p.level === experienceLevel;
    return matchesSearch && matchesCat && matchesLevel;
  });

  return (
    <div className="min-h-screen flex flex-col bg-canvas text-ink transition-colors duration-200 antialiased font-sans">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        {/* Top Title & Search */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 border-b border-hairline pb-6">
          <div className="space-y-1">
            <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
              <Sparkles className="w-3.5 h-3.5" /> Live Marketplace
            </div>
            <h1 className="text-3xl font-extrabold tracking-tight text-ink">Explore Project Bounties</h1>
            <p className="text-sm text-mute">Browse verified freelance contracts across Kotlin, Mobile, and Web stacks.</p>
          </div>

          <Link
            to="/projects/new"
            className="flex items-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white font-bold text-xs uppercase tracking-[0.05em] px-6 py-3.5 rounded-full transition-all hover:scale-[1.04] shadow-md shadow-brand-green/20 w-fit"
          >
            <Plus className="w-4 h-4" /> Post New Bounty
          </Link>
        </div>

        {/* Search & Filter Bar */}
        <div className="bg-surface p-4 rounded-xl border border-hairline shadow-sm flex flex-col md:flex-row items-center gap-4">
          <div className="relative flex-1 w-full">
            <Search className="w-5 h-5 text-mute absolute left-4 top-3.5" />
            <input
              type="text"
              placeholder="Search by keywords, skills, or tech stack..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-12 pr-4 py-3 rounded-full border border-hairline bg-surface-elevated focus:outline-none focus:border-brand-green text-sm text-ink font-medium transition-colors"
            />
          </div>

          <div className="flex items-center gap-3 w-full md:w-auto">
            <select
              value={experienceLevel}
              onChange={(e) => setExperienceLevel(e.target.value)}
              className="px-5 py-3 rounded-full border border-hairline bg-surface-elevated text-xs font-bold uppercase tracking-[0.05em] text-ink focus:outline-none focus:border-brand-green"
            >
              <option value="All">All Levels</option>
              <option value="Entry">Entry Level</option>
              <option value="Intermediate">Intermediate</option>
              <option value="Expert">Expert</option>
            </select>
          </div>
        </div>

        {/* Category Pills */}
        <div className="flex items-center gap-2 overflow-x-auto pb-2 no-scrollbar">
          {categories.map((cat) => (
            <button
              key={cat}
              onClick={() => setSelectedCategory(cat)}
              className={`px-5 py-2.5 rounded-full text-xs font-bold uppercase tracking-[0.05em] whitespace-nowrap transition-all ${
                selectedCategory === cat
                  ? 'bg-brand-green text-white shadow-md shadow-brand-green/20'
                  : 'bg-surface-elevated border border-hairline text-mute hover:text-ink'
              }`}
            >
              {cat}
            </button>
          ))}
        </div>

        {/* Project Cards List */}
        <div className="space-y-4">
          {filteredProjects.map((project) => (
            <div
              key={project.id}
              className="bg-surface p-6 rounded-xl border border-hairline shadow-sm hover:bg-surface-elevated hover:scale-[1.01] transition-all duration-200 space-y-4"
            >
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div className="space-y-1">
                  <div className="flex items-center gap-2 text-xs">
                    <span className="font-bold text-brand-green uppercase tracking-wider">{project.client}</span>
                    <span className="text-mute">&bull;</span>
                    <span className="text-mute flex items-center gap-1"><ShieldCheck className="w-3.5 h-3.5 text-brand-green" /> Verified Client</span>
                    <span className="text-mute">&bull;</span>
                    <span className="text-mute">{project.posted}</span>
                  </div>
                  <h3 className="text-xl font-bold text-ink hover:text-brand-green transition-colors">
                    <Link to={`/projects/${project.id}`}>{project.title}</Link>
                  </h3>
                </div>

                <div className="text-left md:text-right">
                  <div className="text-xl font-extrabold text-brand-green">{project.budget}</div>
                  <div className="text-xs text-mute font-medium">{project.budgetType} &bull; {project.level}</div>
                </div>
              </div>

              <p className="text-sm text-mute leading-relaxed">
                {project.description}
              </p>

              <div className="flex flex-wrap items-center justify-between gap-4 pt-3 border-t border-hairline">
                <div className="flex flex-wrap gap-2">
                  {project.skills.map((s) => (
                    <span key={s} className="px-3 py-1 rounded-full bg-surface-elevated border border-hairline text-brand-green text-xs font-bold">
                      {s}
                    </span>
                  ))}
                </div>

                <div className="flex items-center gap-4 text-xs">
                  <span className="text-mute">{project.proposals} Proposals Submitted</span>
                  <Link
                    to={`/projects/${project.id}/apply`}
                    className="flex items-center gap-1 text-brand-green font-bold uppercase tracking-[0.05em] hover:text-brand-green-hover transition-colors"
                  >
                    Submit Proposal <ArrowUpRight className="w-4 h-4" />
                  </Link>
                </div>
              </div>
            </div>
          ))}
        </div>

      </main>

      <Footer />
    </div>
  );
};
