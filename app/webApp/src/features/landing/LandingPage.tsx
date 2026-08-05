import React from 'react';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Zap, ShieldCheck, ArrowRight, Code, Cpu, Smartphone, Layers, CheckCircle2, Users, FileCode, Award, Lock, Sparkles } from 'lucide-react';

export const LandingPage: React.FC = () => {
  const categories = [
    { name: 'Kotlin & KMP', count: '142 Open Projects', icon: Code, color: 'text-indigo-500 bg-indigo-50 dark:bg-indigo-950/40' },
    { name: 'Mobile (Android/iOS)', count: '215 Open Projects', icon: Smartphone, color: 'text-emerald-500 bg-emerald-50 dark:bg-emerald-950/40' },
    { name: 'Full-Stack & Ktor', count: '189 Open Projects', icon: Layers, color: 'text-blue-500 bg-blue-50 dark:bg-blue-950/40' },
    { name: 'AI Systems & Gorse', count: '94 Open Projects', icon: Cpu, color: 'text-purple-500 bg-purple-50 dark:bg-purple-950/40' },
  ];

  const featuredProjects = [
    { title: 'KMP Multiplatform Wallet with WebAssembly Target', budget: '$4,500 - $6,000', client: 'Web3 Global Tech', tags: ['Kotlin', 'Wasm', 'Compose'] },
    { title: 'Real-time Ktor WebSockets Messaging Engine', budget: '$2,800 - $3,500', client: 'Zapmancer Labs', tags: ['Ktor', 'PostgreSQL', 'Docker'] },
    { title: 'Cross-Platform iOS & Android Marketplace Client', budget: '$5,000 - $8,000', client: 'Fintech Mobile Inc.', tags: ['KMP', 'SwiftUI', 'Material 3'] },
  ];

  return (
    <div className="min-h-screen flex flex-col bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 transition-colors">
      <Header isLoggedIn={false} />

      {/* Hero Section */}
      <section className="relative overflow-hidden pt-20 pb-24 border-b border-slate-200 dark:border-slate-800 bg-gradient-to-b from-indigo-50/50 via-transparent to-transparent dark:from-indigo-950/20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center relative z-10 space-y-8">
          
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-indigo-100 dark:bg-indigo-900/40 border border-indigo-200 dark:border-indigo-800 text-indigo-700 dark:text-indigo-300 text-xs font-bold tracking-wide uppercase">
            <Sparkles className="w-4 h-4" /> Open-Source KMP Freelance Platform
          </div>

          <h1 className="text-4xl sm:text-6xl font-extrabold tracking-tight text-slate-900 dark:text-white max-w-4xl mx-auto leading-tight">
            Fair, Transparent Escrow Freelancing Built for <span className="text-transparent bg-clip-text bg-gradient-to-r from-indigo-600 to-purple-600">Engineers & Creators</span>
          </h1>

          <p className="text-lg sm:text-xl text-slate-600 dark:text-slate-300 max-w-2xl mx-auto leading-relaxed">
            Zapmancer eliminates high fee extractions (0% to 5% platform cost recovery). Work directly with client escrow protection powered by Kotlin Multiplatform.
          </p>

          <div className="flex flex-col sm:flex-row items-center justify-center gap-4 pt-4">
            <Link
              to="/signup"
              className="w-full sm:w-auto flex items-center justify-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-base px-8 py-4 rounded-xl shadow-lg shadow-indigo-500/25 transition-all"
            >
              Get Started Free <ArrowRight className="w-5 h-5" />
            </Link>
            <Link
              to="/projects"
              className="w-full sm:w-auto flex items-center justify-center gap-2 bg-white dark:bg-slate-900 hover:bg-slate-100 dark:hover:bg-slate-800 text-slate-800 dark:text-slate-200 border border-slate-300 dark:border-slate-700 font-semibold text-base px-8 py-4 rounded-xl transition-all"
            >
              Browse Open Bounties
            </Link>
          </div>

          {/* Quick Metrics */}
          <div className="pt-12 grid grid-cols-2 md:grid-cols-4 gap-6 max-w-4xl mx-auto border-t border-slate-200 dark:border-slate-800/60 mt-12">
            <div>
              <div className="text-3xl font-extrabold text-indigo-600 dark:text-indigo-400">$1.2M+</div>
              <div className="text-xs font-medium text-slate-500 dark:text-slate-400 mt-1">Escrow Bounties Paid</div>
            </div>
            <div>
              <div className="text-3xl font-extrabold text-emerald-600 dark:text-emerald-400">100%</div>
              <div className="text-xs font-medium text-slate-500 dark:text-slate-400 mt-1">Open Source Codebase</div>
            </div>
            <div>
              <div className="text-3xl font-extrabold text-blue-600 dark:text-blue-400">0% - 5%</div>
              <div className="text-xs font-medium text-slate-500 dark:text-slate-400 mt-1">Low Transparent Overhead</div>
            </div>
            <div>
              <div className="text-3xl font-extrabold text-purple-600 dark:text-purple-400">3,400+</div>
              <div className="text-xs font-medium text-slate-500 dark:text-slate-400 mt-1">Verified Engineers</div>
            </div>
          </div>

        </div>
      </section>

      {/* Dual Path Section: Talent vs Client */}
      <section className="py-20 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full">
        <div className="text-center mb-14 space-y-3">
          <h2 className="text-3xl font-bold tracking-tight">Two Paths, One Transparent Marketplace</h2>
          <p className="text-slate-500 dark:text-slate-400 text-sm max-w-xl mx-auto">Choose your workflow on Zapmancer whether you're building products or taking on engineering bounties.</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
          {/* Path 1 */}
          <div className="bg-white dark:bg-slate-900 rounded-3xl p-8 border border-slate-200 dark:border-slate-800 shadow-xl relative overflow-hidden flex flex-col justify-between">
            <div className="space-y-6">
              <div className="w-14 h-14 rounded-2xl bg-indigo-600 text-white flex items-center justify-center shadow-md">
                <FileCode className="w-7 h-7" />
              </div>
              <h3 className="text-2xl font-bold">For Engineers & Creators</h3>
              <p className="text-slate-600 dark:text-slate-300 text-sm leading-relaxed">
                Submit milestone proposals with transparent delivery terms. Enjoy instant milestone payouts directly to your wallet upon client approval.
              </p>
              <ul className="space-y-3 text-sm">
                <li className="flex items-center gap-2 text-slate-700 dark:text-slate-300">
                  <CheckCircle2 className="w-4 h-4 text-emerald-500" /> Keep 100% of your earnings with zero hidden commissions
                </li>
                <li className="flex items-center gap-2 text-slate-700 dark:text-slate-300">
                  <CheckCircle2 className="w-4 h-4 text-emerald-500" /> Verified client escrow deposits before work begins
                </li>
                <li className="flex items-center gap-2 text-slate-700 dark:text-slate-300">
                  <CheckCircle2 className="w-4 h-4 text-emerald-500" /> Showoff KMP, Android, iOS, & WebAssembly work
                </li>
              </ul>
            </div>
            <div className="pt-8">
              <Link to="/signup" className="inline-flex items-center gap-2 font-bold text-indigo-600 dark:text-indigo-400 hover:gap-3 transition-all">
                Create Freelancer Profile <ArrowRight className="w-4 h-4" />
              </Link>
            </div>
          </div>

          {/* Path 2 */}
          <div className="bg-white dark:bg-slate-900 rounded-3xl p-8 border border-slate-200 dark:border-slate-800 shadow-xl relative overflow-hidden flex flex-col justify-between">
            <div className="space-y-6">
              <div className="w-14 h-14 rounded-2xl bg-emerald-600 text-white flex items-center justify-center shadow-md">
                <Users className="w-7 h-7" />
              </div>
              <h3 className="text-2xl font-bold">For Clients & Startups</h3>
              <p className="text-slate-600 dark:text-slate-300 text-sm leading-relaxed">
                Post technical requirements and receive proposals from vetted KMP, Mobile, and Backend specialists in minutes.
              </p>
              <ul className="space-y-3 text-sm">
                <li className="flex items-center gap-2 text-slate-700 dark:text-slate-300">
                  <CheckCircle2 className="w-4 h-4 text-emerald-500" /> Smart developer matching powered by Gorse AI
                </li>
                <li className="flex items-center gap-2 text-slate-700 dark:text-slate-300">
                  <CheckCircle2 className="w-4 h-4 text-emerald-500" /> Milestone escrow lock with contract protection
                </li>
                <li className="flex items-center gap-2 text-slate-700 dark:text-slate-300">
                  <CheckCircle2 className="w-4 h-4 text-emerald-500" /> Real-time messaging with attachment previews
                </li>
              </ul>
            </div>
            <div className="pt-8">
              <Link to="/projects/new" className="inline-flex items-center gap-2 font-bold text-emerald-600 dark:text-emerald-400 hover:gap-3 transition-all">
                Post a Project Bounty <ArrowRight className="w-4 h-4" />
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Featured Categories */}
      <section className="py-16 bg-white dark:bg-slate-900/50 border-y border-slate-200 dark:border-slate-800">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col sm:flex-row sm:items-end justify-between mb-10 gap-4">
            <div>
              <h2 className="text-2xl font-bold tracking-tight">Popular Skill Categories</h2>
              <p className="text-slate-500 dark:text-slate-400 text-sm mt-1">Explore engineering bounties by technology stack.</p>
            </div>
            <Link to="/projects" className="text-indigo-600 dark:text-indigo-400 text-sm font-bold hover:underline">
              View All Categories &rarr;
            </Link>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
            {categories.map((cat) => {
              const Icon = cat.icon;
              return (
                <Link
                  key={cat.name}
                  to="/projects"
                  className="p-6 rounded-2xl bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-slate-800 hover:border-indigo-500 dark:hover:border-indigo-500 transition-all group"
                >
                  <div className={`w-12 h-12 rounded-xl flex items-center justify-center mb-4 ${cat.color}`}>
                    <Icon className="w-6 h-6" />
                  </div>
                  <h3 className="font-bold text-lg text-slate-900 dark:text-white group-hover:text-indigo-600 transition-colors">{cat.name}</h3>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">{cat.count}</p>
                </Link>
              );
            })}
          </div>
        </div>
      </section>

      {/* Featured Projects Listing */}
      <section className="py-20 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full">
        <div className="flex flex-col sm:flex-row sm:items-end justify-between mb-10 gap-4">
          <div>
            <h2 className="text-2xl font-bold tracking-tight">Active Project Bounties</h2>
            <p className="text-slate-500 dark:text-slate-400 text-sm mt-1">Top verified client projects open for proposals right now.</p>
          </div>
          <Link to="/projects" className="text-indigo-600 dark:text-indigo-400 text-sm font-bold hover:underline">
            Explore All Projects &rarr;
          </Link>
        </div>

        <div className="space-y-4">
          {featuredProjects.map((p, idx) => (
            <div key={idx} className="bg-white dark:bg-slate-900 p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm hover:shadow-md transition-all flex flex-col md:flex-row md:items-center justify-between gap-6">
              <div className="space-y-2">
                <div className="flex items-center gap-2">
                  <span className="px-2.5 py-0.5 rounded-md bg-indigo-50 dark:bg-indigo-950/60 text-indigo-600 dark:text-indigo-400 font-mono text-xs font-semibold">
                    {p.client}
                  </span>
                  <span className="text-xs text-slate-400 flex items-center gap-1">
                    <ShieldCheck className="w-3.5 h-3.5 text-emerald-500" /> Escrow Verified
                  </span>
                </div>
                <h3 className="font-bold text-lg text-slate-900 dark:text-white hover:text-indigo-600 transition-colors">
                  <Link to="/projects/1">{p.title}</Link>
                </h3>
                <div className="flex flex-wrap gap-2 pt-1">
                  {p.tags.map((t) => (
                    <span key={t} className="px-2.5 py-1 rounded-lg bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 text-xs font-medium">
                      {t}
                    </span>
                  ))}
                </div>
              </div>

              <div className="flex items-center justify-between md:justify-end gap-6 border-t md:border-t-0 border-slate-100 dark:border-slate-800 pt-4 md:pt-0">
                <div className="text-right">
                  <div className="text-sm font-extrabold text-emerald-600 dark:text-emerald-400">{p.budget}</div>
                  <div className="text-xs text-slate-400">Fixed Milestone</div>
                </div>
                <Link to="/projects/1" className="bg-indigo-600 hover:bg-indigo-700 text-white font-semibold text-sm px-5 py-2.5 rounded-xl transition-all shadow-sm">
                  Apply Now
                </Link>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Security Banner */}
      <section className="py-16 bg-slate-900 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col md:flex-row items-center justify-between gap-8">
          <div className="space-y-3 max-w-2xl">
            <div className="flex items-center gap-2 text-emerald-400 font-bold text-xs uppercase tracking-wider">
              <Lock className="w-4 h-4" /> Built-in Escrow Security
            </div>
            <h2 className="text-3xl font-extrabold tracking-tight">Zero Risk Milestone Escrow Protection</h2>
            <p className="text-slate-300 text-sm leading-relaxed">
              Client funds are safely locked in escrow prior to milestone commencement. Freelancers deliver clean code, and payments release seamlessly upon milestone approval.
            </p>
          </div>
          <Link to="/signup" className="bg-emerald-500 hover:bg-emerald-600 text-slate-950 font-extrabold px-8 py-4 rounded-xl transition-all shadow-lg shadow-emerald-500/20 whitespace-nowrap">
            Join Marketplace Now
          </Link>
        </div>
      </section>

      <Footer />
    </div>
  );
};
