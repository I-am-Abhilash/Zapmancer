import React from 'react';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, ArrowRight, Code, Cpu, Smartphone, Layers, CheckCircle2, Users, FileCode, Lock, Sparkles } from 'lucide-react';

export const LandingPage: React.FC = () => {
  const categories = [
    { name: 'Kotlin & KMP', count: '142 Open Projects', icon: Code },
    { name: 'Mobile (Android/iOS)', count: '215 Open Projects', icon: Smartphone },
    { name: 'Full-Stack & Ktor', count: '189 Open Projects', icon: Layers },
    { name: 'AI Systems & Gorse', count: '94 Open Projects', icon: Cpu },
  ];

  const featuredProjects = [
    { title: 'KMP Multiplatform Wallet with WebAssembly Target', budget: '$4,500 - $6,000', client: 'Web3 Global Tech', tags: ['Kotlin', 'Wasm', 'Compose'] },
    { title: 'Real-time Ktor WebSockets Messaging Engine', budget: '$2,800 - $3,500', client: 'Zapmancer Labs', tags: ['Ktor', 'PostgreSQL', 'Docker'] },
    { title: 'Cross-Platform iOS & Android Marketplace Client', budget: '$5,000 - $8,000', client: 'Fintech Mobile Inc.', tags: ['KMP', 'SwiftUI', 'Material 3'] },
  ];

  return (
    <div className="min-h-screen flex flex-col bg-canvas text-ink transition-colors duration-200 antialiased font-sans">
      <Header isLoggedIn={false} />

      {/* Hero Section */}
      <section className="relative overflow-hidden pt-24 pb-28 bg-gradient-to-b from-brand-green/10 via-canvas to-canvas border-b border-hairline">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center relative z-10 space-y-8">
          
          {/* Overline Badge */}
          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-surface-elevated text-brand-green border border-hairline text-[11px] font-bold tracking-[0.1em] uppercase">
            <Sparkles className="w-3.5 h-3.5" /> Open-Source KMP Freelance Platform
          </div>

          {/* Hero Title */}
          <h1 className="text-4xl sm:text-6xl md:text-7xl font-extrabold tracking-[-0.03em] text-ink max-w-5xl mx-auto leading-[1.1]">
            Fair, Transparent Escrow Freelancing Built for <span className="text-brand-green">Engineers & Creators</span>
          </h1>

          {/* Subtitle */}
          <p className="text-base sm:text-lg text-mute max-w-2xl mx-auto leading-relaxed">
            Zapmancer eliminates high fee extractions (0% to 5% platform cost recovery). Work directly with client escrow protection powered by Kotlin Multiplatform.
          </p>

          {/* Action Buttons */}
          <div className="flex flex-col sm:flex-row items-center justify-center gap-4 pt-4">
            <Link
              to="/signup"
              className="w-full sm:w-auto inline-flex items-center justify-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white font-bold text-sm px-8 py-3.5 rounded-full tracking-[0.05em] uppercase hover:scale-[1.04] transition-all duration-200 shadow-md shadow-brand-green/20"
            >
              Get Started Free <ArrowRight className="w-4 h-4" />
            </Link>
            <Link
              to="/projects"
              className="w-full sm:w-auto inline-flex items-center justify-center gap-2 bg-surface hover:bg-surface-elevated text-ink border border-hairline font-bold text-sm px-8 py-3.5 rounded-full tracking-[0.05em] uppercase hover:scale-[1.04] transition-all duration-200"
            >
              Browse Open Bounties
            </Link>
          </div>

          {/* Quick Metrics */}
          <div className="pt-12 grid grid-cols-2 md:grid-cols-4 gap-4 max-w-4xl mx-auto mt-12">
            <div className="bg-surface border border-hairline p-6 rounded-xl text-center shadow-sm dark:shadow-none transition-all duration-200 hover:bg-surface-elevated hover:scale-[1.03]">
              <div className="text-3xl font-extrabold text-brand-green">$1.2M+</div>
              <div className="text-xs font-medium text-mute uppercase tracking-[0.05em] mt-1.5">Escrow Bounties Paid</div>
            </div>
            <div className="bg-surface border border-hairline p-6 rounded-xl text-center shadow-sm dark:shadow-none transition-all duration-200 hover:bg-surface-elevated hover:scale-[1.03]">
              <div className="text-3xl font-extrabold text-ink">100%</div>
              <div className="text-xs font-medium text-mute uppercase tracking-[0.05em] mt-1.5">Open Source Codebase</div>
            </div>
            <div className="bg-surface border border-hairline p-6 rounded-xl text-center shadow-sm dark:shadow-none transition-all duration-200 hover:bg-surface-elevated hover:scale-[1.03]">
              <div className="text-3xl font-extrabold text-brand-green">0% - 5%</div>
              <div className="text-xs font-medium text-mute uppercase tracking-[0.05em] mt-1.5">Transparent Overhead</div>
            </div>
            <div className="bg-surface border border-hairline p-6 rounded-xl text-center shadow-sm dark:shadow-none transition-all duration-200 hover:bg-surface-elevated hover:scale-[1.03]">
              <div className="text-3xl font-extrabold text-ink">3,400+</div>
              <div className="text-xs font-medium text-mute uppercase tracking-[0.05em] mt-1.5">Verified Engineers</div>
            </div>
          </div>

        </div>
      </section>

      {/* Dual Path Section: Talent vs Client */}
      <section className="py-20 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full">
        <div className="text-center mb-14 space-y-2">
          <div className="text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">Tailored Workflows</div>
          <h2 className="text-3xl font-extrabold tracking-[-0.01em] text-ink">Two Paths, One Transparent Marketplace</h2>
          <p className="text-mute text-sm max-w-xl mx-auto">Choose your workflow on Zapmancer whether you're building products or taking on engineering bounties.</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
          {/* Path 1: Engineers */}
          <div className="bg-surface border border-hairline shadow-sm dark:shadow-none rounded-xl p-8 transition-all duration-200 hover:bg-surface-elevated hover:scale-[1.03] flex flex-col justify-between space-y-6">
            <div className="space-y-5">
              <div className="w-12 h-12 rounded-full bg-brand-green text-white flex items-center justify-center shadow-lg">
                <FileCode className="w-6 h-6" />
              </div>
              <h3 className="text-2xl font-bold text-ink">For Engineers & Creators</h3>
              <p className="text-mute text-sm leading-relaxed">
                Submit milestone proposals with transparent delivery terms. Enjoy instant milestone payouts directly to your wallet upon client approval.
              </p>
              <ul className="space-y-3 text-sm">
                <li className="flex items-center gap-2.5 text-mute">
                  <CheckCircle2 className="w-4 h-4 text-brand-green shrink-0" /> Keep 100% of your earnings with zero hidden commissions
                </li>
                <li className="flex items-center gap-2.5 text-mute">
                  <CheckCircle2 className="w-4 h-4 text-brand-green shrink-0" /> Verified client escrow deposits before work begins
                </li>
                <li className="flex items-center gap-2.5 text-mute">
                  <CheckCircle2 className="w-4 h-4 text-brand-green shrink-0" /> Showcase KMP, Android, iOS, & WebAssembly work
                </li>
              </ul>
            </div>
            <div className="pt-6">
              <Link to="/signup" className="inline-flex items-center gap-2 font-bold text-xs uppercase tracking-[0.05em] text-brand-green hover:text-brand-green-hover transition-colors">
                Create Freelancer Profile <ArrowRight className="w-4 h-4" />
              </Link>
            </div>
          </div>

          {/* Path 2: Clients */}
          <div className="bg-surface border border-hairline shadow-sm dark:shadow-none rounded-xl p-8 transition-all duration-200 hover:bg-surface-elevated hover:scale-[1.03] flex flex-col justify-between space-y-6">
            <div className="space-y-5">
              <div className="w-12 h-12 rounded-full bg-brand-green text-white flex items-center justify-center shadow-lg">
                <Users className="w-6 h-6" />
              </div>
              <h3 className="text-2xl font-bold text-ink">For Clients & Startups</h3>
              <p className="text-mute text-sm leading-relaxed">
                Post technical requirements and receive proposals from vetted KMP, Mobile, and Backend specialists in minutes.
              </p>
              <ul className="space-y-3 text-sm">
                <li className="flex items-center gap-2.5 text-mute">
                  <CheckCircle2 className="w-4 h-4 text-brand-green shrink-0" /> Smart developer matching powered by Gorse AI
                </li>
                <li className="flex items-center gap-2.5 text-mute">
                  <CheckCircle2 className="w-4 h-4 text-brand-green shrink-0" /> Milestone escrow lock with contract protection
                </li>
                <li className="flex items-center gap-2.5 text-mute">
                  <CheckCircle2 className="w-4 h-4 text-brand-green shrink-0" /> Real-time messaging with attachment previews
                </li>
              </ul>
            </div>
            <div className="pt-6">
              <Link to="/projects/new" className="inline-flex items-center gap-2 font-bold text-xs uppercase tracking-[0.05em] text-brand-green hover:text-brand-green-hover transition-colors">
                Post a Project Bounty <ArrowRight className="w-4 h-4" />
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Featured Categories */}
      <section className="py-16 bg-surface-elevated border-y border-hairline">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col sm:flex-row sm:items-end justify-between mb-10 gap-4">
            <div>
              <div className="text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">Stack Categories</div>
              <h2 className="text-2xl font-bold tracking-tight text-ink mt-1">Popular Skill Ecosystems</h2>
            </div>
            <Link to="/projects" className="text-brand-green hover:text-brand-green-hover text-xs font-bold uppercase tracking-[0.05em]">
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
                  className="p-6 rounded-xl bg-surface border border-hairline hover:bg-surface-modal transition-all duration-200 hover:scale-[1.03] shadow-sm dark:shadow-none group"
                >
                  <div className="w-10 h-10 rounded-full bg-surface-elevated text-brand-green flex items-center justify-center mb-4 group-hover:bg-brand-green group-hover:text-white transition-colors">
                    <Icon className="w-5 h-5" />
                  </div>
                  <h3 className="font-bold text-base text-ink group-hover:text-brand-green transition-colors">{cat.name}</h3>
                  <p className="text-xs text-mute mt-1">{cat.count}</p>
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
            <div className="text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">Live Marketplace</div>
            <h2 className="text-2xl font-bold tracking-tight text-ink mt-1">Active Project Bounties</h2>
          </div>
          <Link to="/projects" className="text-brand-green hover:text-brand-green-hover text-xs font-bold uppercase tracking-[0.05em]">
            Explore All Projects &rarr;
          </Link>
        </div>

        <div className="space-y-4">
          {featuredProjects.map((p, idx) => (
            <div key={idx} className="bg-surface border border-hairline hover:bg-surface-elevated p-6 rounded-xl transition-all duration-200 hover:scale-[1.01] flex flex-col md:flex-row md:items-center justify-between gap-6 shadow-sm dark:shadow-none">
              <div className="space-y-2">
                <div className="flex items-center gap-2">
                  <span className="px-2.5 py-0.5 rounded-full bg-surface-elevated text-brand-green font-mono text-xs font-bold uppercase tracking-wider">
                    {p.client}
                  </span>
                  <span className="text-xs text-mute flex items-center gap-1">
                    <ShieldCheck className="w-3.5 h-3.5 text-brand-green" /> Escrow Verified
                  </span>
                </div>
                <h3 className="font-bold text-lg text-ink hover:text-brand-green transition-colors">
                  <Link to="/projects/1">{p.title}</Link>
                </h3>
                <div className="flex flex-wrap gap-2 pt-1">
                  {p.tags.map((t) => (
                    <span key={t} className="px-3 py-1 rounded-full bg-surface-elevated text-mute text-xs font-medium">
                      {t}
                    </span>
                  ))}
                </div>
              </div>

              <div className="flex items-center justify-between md:justify-end gap-6 border-t md:border-t-0 border-hairline pt-4 md:pt-0">
                <div className="text-right">
                  <div className="text-base font-extrabold text-brand-green">{p.budget}</div>
                  <div className="text-xs text-mute">Fixed Milestone</div>
                </div>
                <Link to="/projects/1" className="bg-brand-green hover:bg-brand-green-hover text-white font-bold text-xs uppercase tracking-[0.05em] px-6 py-3 rounded-full hover:scale-[1.04] transition-all">
                  Apply Now
                </Link>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Security Banner */}
      <section className="py-16 bg-surface border-t border-hairline text-ink">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col md:flex-row items-center justify-between gap-8">
          <div className="space-y-3 max-w-2xl">
            <div className="flex items-center gap-2 text-brand-green font-bold text-[11px] uppercase tracking-[0.1em]">
              <Lock className="w-4 h-4" /> Built-in Escrow Security
            </div>
            <h2 className="text-3xl font-extrabold tracking-tight text-ink">Zero Risk Milestone Escrow Protection</h2>
            <p className="text-mute text-sm leading-relaxed">
              Client funds are safely locked in escrow prior to milestone commencement. Freelancers deliver clean code, and payments release seamlessly upon milestone approval.
            </p>
          </div>
          <Link to="/signup" className="bg-brand-green hover:bg-brand-green-hover text-white font-bold text-xs uppercase tracking-[0.05em] px-8 py-4 rounded-full hover:scale-[1.04] transition-all shrink-0">
            Join Marketplace Now
          </Link>
        </div>
      </section>

      <Footer />
    </div>
  );
};



