import React from 'react';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Zap, Briefcase, FileText, MessageSquare, DollarSign, ArrowUpRight, TrendingUp, Search, Plus, Clock, ShieldCheck, Sparkles } from 'lucide-react';

export const HomePage: React.FC = () => {
  const recommendedProjects = [
    { id: '1', title: 'Compose Multiplatform Desktop App for Ktor Analytics', budget: '$3,200', client: 'Acme AI Systems', deadline: '3 weeks', tags: ['Compose', 'Ktor', 'Desktop'] },
    { id: '2', title: 'High-Concurrency PostgreSQL Exposed ORM Migration', budget: '$1,800', client: 'Fintech Core', deadline: '10 days', tags: ['PostgreSQL', 'Exposed', 'Ktor'] },
    { id: '3', title: 'WebAssembly Wasm Component for Audio Processing', budget: '$4,500', client: 'AudioCraft Labs', deadline: '1 month', tags: ['Wasm', 'Kotlin', 'C++'] },
  ];

  const activeProposals = [
    { id: '101', project: 'iOS KMP Shared Logic Refactoring', client: 'Mobility Ltd', bid: '$2,400', status: 'In Review', date: 'Yesterday' },
    { id: '102', project: 'Gorse AI Recommendation Service Integration', client: 'DataMesh Inc', bid: '$3,800', status: 'Shortlisted', date: '3 days ago' },
  ];

  return (
    <div className="min-h-screen flex flex-col bg-canvas text-ink transition-colors duration-200 antialiased font-sans">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
        
        {/* Welcome Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none">
          <div className="space-y-1">
            <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
              <Sparkles className="w-3.5 h-3.5" /> Engineer Dashboard
            </div>
            <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight flex items-center gap-2 text-ink">
              Welcome Back, Alex! <Zap className="w-6 h-6 text-brand-green fill-current" />
            </h1>
            <p className="text-sm text-mute">
              Here is what's happening across your active proposals and recommended bounties today.
            </p>
          </div>

          <div className="flex items-center gap-3">
            <Link
              to="/projects"
              className="flex items-center gap-2 bg-surface-elevated hover:bg-surface-modal text-ink text-xs font-bold uppercase tracking-[0.05em] px-5 py-3 rounded-full border border-hairline transition-all hover:scale-[1.04]"
            >
              <Search className="w-4 h-4 text-brand-green" /> Find Projects
            </Link>
            <Link
              to="/projects/new"
              className="flex items-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] px-6 py-3 rounded-full transition-all hover:scale-[1.04] shadow-md shadow-brand-green/20"
            >
              <Plus className="w-4 h-4" /> Post Bounty
            </Link>
          </div>
        </div>

        {/* Quick Metrics */}
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
          <div className="bg-surface p-6 rounded-xl border border-hairline shadow-sm dark:shadow-none flex items-center justify-between">
            <div>
              <p className="text-xs font-bold uppercase tracking-wider text-mute">Active Proposals</p>
              <h3 className="text-2xl font-extrabold text-ink mt-1">4</h3>
            </div>
            <div className="w-12 h-12 rounded-full bg-surface-elevated text-brand-green flex items-center justify-center border border-hairline">
              <FileText className="w-6 h-6" />
            </div>
          </div>

          <div className="bg-surface p-6 rounded-xl border border-hairline shadow-sm dark:shadow-none flex items-center justify-between">
            <div>
              <p className="text-xs font-bold uppercase tracking-wider text-mute">Active Contracts</p>
              <h3 className="text-2xl font-extrabold text-brand-green mt-1">2</h3>
            </div>
            <div className="w-12 h-12 rounded-full bg-surface-elevated text-brand-green flex items-center justify-center border border-hairline">
              <Briefcase className="w-6 h-6" />
            </div>
          </div>

          <div className="bg-surface p-6 rounded-xl border border-hairline shadow-sm dark:shadow-none flex items-center justify-between">
            <div>
              <p className="text-xs font-bold uppercase tracking-wider text-mute">Escrow Locked</p>
              <h3 className="text-2xl font-extrabold text-ink mt-1">$6,200</h3>
            </div>
            <div className="w-12 h-12 rounded-full bg-surface-elevated text-brand-green flex items-center justify-center border border-hairline">
              <DollarSign className="w-6 h-6" />
            </div>
          </div>

          <div className="bg-surface p-6 rounded-xl border border-hairline shadow-sm dark:shadow-none flex items-center justify-between">
            <div>
              <p className="text-xs font-bold uppercase tracking-wider text-mute">Unread Messages</p>
              <h3 className="text-2xl font-extrabold text-brand-green mt-1">3</h3>
            </div>
            <div className="w-12 h-12 rounded-full bg-surface-elevated text-brand-green flex items-center justify-center border border-hairline">
              <MessageSquare className="w-6 h-6" />
            </div>
          </div>
        </div>

        {/* Dashboard Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          {/* Left 2 Cols: Recommended Bounties */}
          <div className="lg:col-span-2 space-y-6">
            <div className="flex items-center justify-between border-b border-hairline pb-4">
              <h2 className="text-xl font-bold tracking-tight text-ink flex items-center gap-2">
                <TrendingUp className="w-5 h-5 text-brand-green" /> Recommended For Your Stack
              </h2>
              <Link to="/projects" className="text-xs font-bold uppercase tracking-[0.05em] text-brand-green hover:text-brand-green-hover transition-colors">
                View All Bounties &rarr;
              </Link>
            </div>

            <div className="space-y-4">
              {recommendedProjects.map((project) => (
                <div key={project.id} className="bg-surface p-6 rounded-xl border border-hairline shadow-sm dark:shadow-none hover:bg-surface-elevated hover:scale-[1.01] transition-all duration-200 space-y-3">
                  <div className="flex items-start justify-between gap-4">
                    <div>
                      <div className="flex items-center gap-2 mb-1">
                        <span className="text-xs font-bold text-brand-green uppercase tracking-wider">{project.client}</span>
                        <span className="text-mute flex items-center gap-1"><ShieldCheck className="w-3.5 h-3.5 text-brand-green" /> Verified Escrow</span>
                      </div>
                      <h3 className="font-bold text-lg text-ink hover:text-brand-green transition-colors">
                        <Link to={`/projects/${project.id}`}>{project.title}</Link>
                      </h3>
                    </div>
                    <span className="text-lg font-extrabold text-brand-green whitespace-nowrap">
                      {project.budget}
                    </span>
                  </div>

                  <div className="flex flex-wrap items-center justify-between gap-4 pt-2 border-t border-hairline text-xs">
                    <div className="flex gap-2">
                      {project.tags.map((t) => (
                        <span key={t} className="px-3 py-1 rounded-full bg-surface-elevated text-brand-green font-bold border border-hairline">
                          {t}
                        </span>
                      ))}
                    </div>
                    <div className="flex items-center gap-4 text-mute">
                      <span className="flex items-center gap-1"><Clock className="w-3.5 h-3.5 text-brand-green" /> Est: {project.deadline}</span>
                      <Link to={`/projects/${project.id}/apply`} className="text-brand-green font-bold uppercase tracking-[0.05em] flex items-center gap-0.5 hover:gap-1.5 transition-all">
                        Apply <ArrowUpRight className="w-4 h-4" />
                      </Link>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Right Col: Active Proposals & Activity */}
          <div className="space-y-6">
            <div className="border-b border-hairline pb-4">
              <h2 className="text-xl font-bold tracking-tight text-ink flex items-center gap-2">
                <FileText className="w-5 h-5 text-brand-green" /> Submitted Proposals
              </h2>
            </div>

            <div className="bg-surface p-6 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-4">
              {activeProposals.map((prop) => (
                <div key={prop.id} className="pb-4 border-b border-hairline last:border-0 last:pb-0 space-y-2">
                  <div className="flex items-start justify-between gap-2">
                    <h4 className="font-bold text-sm text-ink line-clamp-1">{prop.project}</h4>
                    <span className="text-[11px] font-bold text-brand-green bg-brand-green/10 px-2.5 py-0.5 rounded-full uppercase tracking-wider">
                      {prop.status}
                    </span>
                  </div>
                  <div className="flex items-center justify-between text-xs text-mute">
                    <span>Bid: <strong className="text-brand-green font-extrabold">{prop.bid}</strong></span>
                    <span>Submitted {prop.date}</span>
                  </div>
                </div>
              ))}

              <Link to="/client/proposals" className="block text-center text-xs font-bold uppercase tracking-[0.05em] text-brand-green hover:text-brand-green-hover pt-2 transition-colors">
                Manage All Proposals &rarr;
              </Link>
            </div>
          </div>

        </div>

      </main>

      <Footer />
    </div>
  );
};
