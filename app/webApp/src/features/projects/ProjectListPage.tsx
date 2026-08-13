import React, { useState } from 'react';
import './projects.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Search, ShieldCheck, ArrowRight, Plus, Sparkles, Bookmark, Clock, Award, Layers } from 'lucide-react';
import { Tabs, TabItem } from '../../components/ui/Tabs';

type CategoryId = 'All' | 'KMP' | 'Mobile' | 'Full-Stack' | 'AI';

export const ProjectListPage: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<CategoryId>('All');
  const [experienceLevel, setExperienceLevel] = useState('All');
  const [savedProjects, setSavedProjects] = useState<string[]>([]);

  const categoryTabs: TabItem<CategoryId>[] = [
    { id: 'All', label: 'All Contracts' },
    { id: 'KMP', label: 'Kotlin Multiplatform' },
    { id: 'Mobile', label: 'Mobile (Android/iOS)' },
    { id: 'Full-Stack', label: 'Full-Stack & Ktor' },
    { id: 'AI', label: 'AI & Data Engine' },
  ];

  const projects = [
    {
      id: '1',
      title: 'Compose Multiplatform Desktop App for Ktor Analytics',
      client: 'Acme AI Systems',
      clientAvatar: 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=120&q=80',
      category: 'KMP',
      budget: '$3,200',
      budgetType: 'Fixed Milestone',
      duration: '3 weeks',
      level: 'Intermediate',
      proposals: 8,
      posted: '2 hours ago',
      verified: true,
      description: 'We need a desktop management dashboard built with Compose Multiplatform for Kotlin. Integrates with our existing Ktor backend REST API and WebSockets streaming endpoints.',
      skills: ['Kotlin', 'Compose', 'Ktor', 'Desktop', 'SQLDelight']
    },
    {
      id: '2',
      title: 'High-Concurrency PostgreSQL Exposed ORM Migration',
      client: 'Fintech Core',
      clientAvatar: 'https://images.unsplash.com/photo-1557804506-669a67965ba0?auto=format&fit=crop&w=120&q=80',
      category: 'Full-Stack',
      budget: '$1,800',
      budgetType: 'Fixed Milestone',
      duration: '10 days',
      level: 'Expert',
      proposals: 14,
      posted: '5 hours ago',
      verified: true,
      description: 'Refactor our legacy SQL queries into Exposed ORM DSL with HikariCP connection pooling, automated migrations, and multi-region read replica fallback handling.',
      skills: ['PostgreSQL', 'Exposed', 'Ktor', 'SQL', 'HikariCP']
    },
    {
      id: '3',
      title: 'WebAssembly Wasm Component for Audio Processing',
      client: 'AudioCraft Labs',
      clientAvatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=120&q=80',
      category: 'KMP',
      budget: '$4,500',
      budgetType: 'Fixed Milestone',
      duration: '1 month',
      level: 'Expert',
      proposals: 5,
      posted: '1 day ago',
      verified: true,
      description: 'Build a high-performance Wasm module compiled from Kotlin Native to process web audio streams in real-time with zero latency drops.',
      skills: ['Wasm', 'Kotlin', 'WebAudio', 'C++', 'WebAssembly']
    },
    {
      id: '4',
      title: 'Native Android Material 3 Design Overhaul',
      client: 'HealthSync Mobile',
      clientAvatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=120&q=80',
      category: 'Mobile',
      budget: '$65 / hr',
      budgetType: 'Hourly Rate',
      duration: '2 - 3 weeks',
      level: 'Entry',
      proposals: 19,
      posted: '2 days ago',
      verified: true,
      description: 'Modernize our Android app views to follow latest Material 3 guidelines, Jetpack Compose adaptive layouts, and dynamic theme switching support.',
      skills: ['Android', 'Jetpack Compose', 'Kotlin', 'Material 3']
    }
  ];

  const toggleBookmark = (id: string, e: React.MouseEvent) => {
    e.preventDefault();
    setSavedProjects((prev) =>
      prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id]
    );
  };

  const filteredProjects = projects.filter((p) => {
    const matchesSearch =
      p.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      p.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCat = selectedCategory === 'All' || p.category === selectedCategory;
    const matchesLevel = experienceLevel === 'All' || p.level === experienceLevel;
    return matchesSearch && matchesCat && matchesLevel;
  });

  return (
    <div className="projects-page">
      <Header isLoggedIn={true} />

      <main className="projects-main">
        
        {/* Page Banner Header */}
        <div className="projects-header">
          <div className="space-y-1">
            <div className="projects-badge">
              <Sparkles className="w-3.5 h-3.5" /> Verified Engineering Contracts
            </div>
            <h1 className="projects-title">Find Work & Explore Bounties</h1>
            <p className="projects-subtitle">Browse verified freelance contracts across Kotlin Multiplatform, Mobile, and Web stacks with zero platform fee deductions.</p>
          </div>

          <Link
            to="/projects/new"
            className="projects-btn-primary"
          >
            <Plus className="w-4 h-4" /> Post New Bounty
          </Link>
        </div>

        {/* Search & Filter Section */}
        <div className="projects-filter-card">
          
          <div className="flex flex-col sm:flex-row items-center gap-4">
            <div className="relative flex-1 w-full">
              <Search className="w-5 h-5 text-mute absolute left-4 top-3.5" />
              <input
                type="text"
                placeholder="Search by title, technical skills, or keywords..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="projects-search-input"
              />
            </div>

            <select
              value={experienceLevel}
              onChange={(e) => setExperienceLevel(e.target.value)}
              className="projects-select"
            >
              <option value="All">All Experience Levels</option>
              <option value="Entry">Entry Level</option>
              <option value="Intermediate">Intermediate</option>
              <option value="Expert">Expert</option>
            </select>
          </div>

          <div>
            <Tabs
              tabs={categoryTabs}
              activeTab={selectedCategory}
              onChange={(id) => setSelectedCategory(id)}
            />
          </div>

        </div>

        {/* Project Box Cards List */}
        <div className="space-y-4">
          {filteredProjects.map((project) => {
            const isBookmarked = savedProjects.includes(project.id);
            return (
              <div
                key={project.id}
                className="project-card-box"
              >
                <div className="flex flex-col sm:flex-row sm:items-start justify-between gap-4">
                  
                  <div className="space-y-2 flex-1">
                    <div className="flex flex-wrap items-center gap-2 text-xs">
                      <img
                        src={project.clientAvatar}
                        alt={project.client}
                        className="w-6 h-6 rounded-full object-cover border border-hairline"
                      />
                      <span className="font-bold text-ink">{project.client}</span>
                      <span className="text-mute">&bull;</span>
                      <span className="inline-flex items-center gap-1 text-[11px] font-bold text-brand-green bg-brand-green/10 px-2.5 py-0.5 rounded-full">
                        <ShieldCheck className="w-3.5 h-3.5" /> Verified Escrow Deposit
                      </span>
                      <span className="text-mute">&bull;</span>
                      <span className="text-mute text-[11px] font-medium">{project.posted}</span>
                    </div>

                    <h3 className="project-card-title">
                      <Link to={`/projects/${project.id}`}>{project.title}</Link>
                    </h3>
                  </div>

                  <div className="flex items-center sm:items-end justify-between sm:flex-col gap-2 shrink-0">
                    <div className="text-left sm:text-right">
                      <div className="project-card-budget">{project.budget}</div>
                      <div className="text-xs text-mute font-bold uppercase tracking-wider">{project.budgetType}</div>
                    </div>

                    <button
                      onClick={(e) => toggleBookmark(project.id, e)}
                      className={`p-2 rounded-full border border-hairline transition-all ${
                        isBookmarked
                          ? 'bg-brand-green text-white border-brand-green'
                          : 'bg-surface-elevated text-mute hover:text-ink'
                      }`}
                      title={isBookmarked ? 'Saved to bookmarks' : 'Save project'}
                    >
                      <Bookmark className="w-4 h-4 fill-current" />
                    </button>
                  </div>

                </div>

                <p className="text-sm text-mute leading-relaxed line-clamp-3">
                  {project.description}
                </p>

                <div className="flex flex-wrap items-center gap-4 text-xs text-mute pt-1">
                  <span className="flex items-center gap-1.5 font-semibold">
                    <Clock className="w-3.5 h-3.5 text-brand-green" /> Est. {project.duration}
                  </span>
                  <span>&bull;</span>
                  <span className="flex items-center gap-1.5 font-semibold">
                    <Award className="w-3.5 h-3.5 text-brand-green" /> {project.level} Level
                  </span>
                  <span>&bull;</span>
                  <span className="flex items-center gap-1.5 font-semibold">
                    <Layers className="w-3.5 h-3.5 text-brand-green" /> {project.proposals} Proposals Received
                  </span>
                </div>

                <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pt-4 border-t border-hairline">
                  <div className="flex flex-wrap gap-2">
                    {project.skills.map((s) => (
                      <span key={s} className="project-card-tag">
                        {s}
                      </span>
                    ))}
                  </div>

                  <Link
                    to={`/projects/${project.id}/apply`}
                    className="project-card-cta"
                  >
                    Submit Proposal <ArrowRight className="w-4 h-4" />
                  </Link>

                </div>

              </div>
            );
          })}
        </div>

      </main>

      <Footer />
    </div>
  );
};
