import React from 'react';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Zap, Briefcase, FileText, MessageSquare, DollarSign, ArrowUpRight, TrendingUp, Search, PlusCircle, Clock, ShieldCheck } from 'lucide-react';

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
    <div className="min-h-screen flex flex-col bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 transition-colors">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
        
        {/* Welcome Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-white dark:bg-slate-900 p-8 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-sm">
          <div className="space-y-1">
            <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight flex items-center gap-2">
              Welcome Back, Alex! <Zap className="w-6 h-6 text-indigo-500 fill-current" />
            </h1>
            <p className="text-sm text-slate-500 dark:text-slate-400">
              Here is what's happening across your active proposals and recommended bounties today.
            </p>
          </div>

          <div className="flex items-center gap-3">
            <Link
              to="/projects"
              className="flex items-center gap-2 bg-slate-100 dark:bg-slate-800 hover:bg-slate-200 dark:hover:bg-slate-700 text-slate-800 dark:text-slate-200 text-sm font-semibold px-4 py-2.5 rounded-xl transition-all"
            >
              <Search className="w-4 h-4" /> Find Projects
            </Link>
            <Link
              to="/projects/new"
              className="flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-semibold px-4 py-2.5 rounded-xl transition-all shadow-md shadow-indigo-500/20"
            >
              <PlusCircle className="w-4 h-4" /> Post Bounty
            </Link>
          </div>
        </div>

        {/* Quick Metrics */}
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
          <div className="bg-white dark:bg-slate-900 p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm flex items-center justify-between">
            <div>
              <p className="text-xs font-semibold uppercase text-slate-400">Active Proposals</p>
              <h3 className="text-2xl font-extrabold text-slate-900 dark:text-white mt-1">4</h3>
            </div>
            <div className="w-12 h-12 rounded-xl bg-indigo-50 dark:bg-indigo-950/50 text-indigo-600 flex items-center justify-center">
              <FileText className="w-6 h-6" />
            </div>
          </div>

          <div className="bg-white dark:bg-slate-900 p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm flex items-center justify-between">
            <div>
              <p className="text-xs font-semibold uppercase text-slate-400">Active Contracts</p>
              <h3 className="text-2xl font-extrabold text-emerald-600 dark:text-emerald-400 mt-1">2</h3>
            </div>
            <div className="w-12 h-12 rounded-xl bg-emerald-50 dark:bg-emerald-950/50 text-emerald-600 flex items-center justify-center">
              <Briefcase className="w-6 h-6" />
            </div>
          </div>

          <div className="bg-white dark:bg-slate-900 p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm flex items-center justify-between">
            <div>
              <p className="text-xs font-semibold uppercase text-slate-400">Escrow Locked</p>
              <h3 className="text-2xl font-extrabold text-slate-900 dark:text-white mt-1">$6,200</h3>
            </div>
            <div className="w-12 h-12 rounded-xl bg-purple-50 dark:bg-purple-950/50 text-purple-600 flex items-center justify-center">
              <DollarSign className="w-6 h-6" />
            </div>
          </div>

          <div className="bg-white dark:bg-slate-900 p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm flex items-center justify-between">
            <div>
              <p className="text-xs font-semibold uppercase text-slate-400">Unread Messages</p>
              <h3 className="text-2xl font-extrabold text-blue-600 dark:text-blue-400 mt-1">3</h3>
            </div>
            <div className="w-12 h-12 rounded-xl bg-blue-50 dark:bg-blue-950/50 text-blue-600 flex items-center justify-center">
              <MessageSquare className="w-6 h-6" />
            </div>
          </div>
        </div>

        {/* Dashboard Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          {/* Left 2 Cols: Recommended Bounties */}
          <div className="lg:col-span-2 space-y-6">
            <div className="flex items-center justify-between">
              <h2 className="text-xl font-bold tracking-tight flex items-center gap-2">
                <TrendingUp className="w-5 h-5 text-indigo-500" /> Recommended For Your Stack
              </h2>
              <Link to="/projects" className="text-xs font-bold text-indigo-600 dark:text-indigo-400 hover:underline">
                View All Bounties &rarr;
              </Link>
            </div>

            <div className="space-y-4">
              {recommendedProjects.map((project) => (
                <div key={project.id} className="bg-white dark:bg-slate-900 p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm hover:border-indigo-500 transition-all space-y-3">
                  <div className="flex items-start justify-between gap-4">
                    <div>
                      <div className="flex items-center gap-2 mb-1">
                        <span className="text-xs font-semibold text-slate-500">{project.client}</span>
                        <span className="text-xs text-emerald-500 flex items-center gap-1"><ShieldCheck className="w-3 h-3" /> Verified</span>
                      </div>
                      <h3 className="font-bold text-lg text-slate-900 dark:text-white hover:text-indigo-600 transition-colors">
                        <Link to={`/projects/${project.id}`}>{project.title}</Link>
                      </h3>
                    </div>
                    <span className="text-lg font-extrabold text-emerald-600 dark:text-emerald-400 whitespace-nowrap">
                      {project.budget}
                    </span>
                  </div>

                  <div className="flex flex-wrap items-center justify-between gap-4 pt-2 border-t border-slate-100 dark:border-slate-800 text-xs">
                    <div className="flex gap-2">
                      {project.tags.map((t) => (
                        <span key={t} className="px-2.5 py-1 rounded-md bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 font-medium">
                          {t}
                        </span>
                      ))}
                    </div>
                    <div className="flex items-center gap-4 text-slate-400">
                      <span className="flex items-center gap-1"><Clock className="w-3.5 h-3.5" /> Est: {project.deadline}</span>
                      <Link to={`/projects/${project.id}/apply`} className="text-indigo-600 dark:text-indigo-400 font-bold flex items-center gap-0.5 hover:gap-1.5 transition-all">
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
            <h2 className="text-xl font-bold tracking-tight flex items-center gap-2">
              <FileText className="w-5 h-5 text-indigo-500" /> Submitted Proposals
            </h2>

            <div className="bg-white dark:bg-slate-900 p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm space-y-4">
              {activeProposals.map((prop) => (
                <div key={prop.id} className="pb-4 border-b border-slate-100 dark:border-slate-800 last:border-0 last:pb-0 space-y-2">
                  <div className="flex items-start justify-between gap-2">
                    <h4 className="font-semibold text-sm text-slate-900 dark:text-white line-clamp-1">{prop.project}</h4>
                    <span className="text-xs font-bold text-amber-600 dark:text-amber-400 bg-amber-50 dark:bg-amber-950/50 px-2 py-0.5 rounded">
                      {prop.status}
                    </span>
                  </div>
                  <div className="flex items-center justify-between text-xs text-slate-500 dark:text-slate-400">
                    <span>Bid: <strong className="text-slate-900 dark:text-white">{prop.bid}</strong></span>
                    <span>Submitted {prop.date}</span>
                  </div>
                </div>
              ))}

              <Link to="/client/proposals" className="block text-center text-xs font-bold text-indigo-600 dark:text-indigo-400 hover:underline pt-2">
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
