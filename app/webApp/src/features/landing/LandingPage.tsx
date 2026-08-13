import React, { useState } from 'react';
import './landing.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, ArrowRight, Code, Cpu, Smartphone, Layers, CheckCircle2, Users, FileCode, Lock, Sparkles, Scale, GitBranch, ChevronDown, ChevronUp } from 'lucide-react';

export const LandingPage: React.FC = () => {
  const [openFaq, setOpenFaq] = useState<number | null>(null);

  const categories = [
    { name: 'Kotlin & KMP', count: '142 Open Projects', icon: Code },
    { name: 'Mobile (Android/iOS)', count: '215 Open Projects', icon: Smartphone },
    { name: 'Full-Stack & Ktor', count: '189 Open Projects', icon: Layers },
    { name: 'AI Systems & Gorse', count: '94 Open Projects', icon: Cpu },
  ];

  const featuredProjects = [
    {
      id: '1',
      title: 'Compose Multiplatform Desktop App for Ktor Analytics',
      budget: '$3,200',
      budgetType: 'Fixed Milestone',
      client: 'Acme AI Systems',
      posted: '2 hours ago',
      tags: ['Compose', 'Ktor', 'Desktop', 'SQLDelight']
    },
    {
      id: '2',
      title: 'High-Concurrency PostgreSQL Exposed ORM Migration',
      budget: '$1,800',
      budgetType: 'Fixed Milestone',
      client: 'Fintech Core',
      posted: '5 hours ago',
      tags: ['PostgreSQL', 'Exposed', 'Ktor', 'HikariCP']
    },
    {
      id: '3',
      title: 'WebAssembly Wasm Component for Audio Processing',
      budget: '$4,500',
      budgetType: 'Fixed Milestone',
      client: 'AudioCraft Labs',
      posted: '1 day ago',
      tags: ['Wasm', 'Kotlin', 'WebAudio', 'C++']
    },
  ];

  const paymentSteps = [
    {
      step: '01',
      title: 'Client Milestone Escrow Lock',
      desc: 'Before engineering work starts, the client deposits the milestone budget into a secure escrow account (Stripe / USDC).'
    },
    {
      step: '02',
      title: 'Code Delivery & Review',
      desc: 'The engineer builds the feature, submits PR deliverables, and provides live demonstration build artifacts.'
    },
    {
      step: '03',
      title: 'Instant Release & IP Transfer',
      desc: 'Upon client sign-off, funds release immediately to the developer with automated legal IP copyright transfer.'
    }
  ];

  const legalFaqs = [
    {
      q: 'How does Zapmancer handle Intellectual Property (IP) ownership?',
      a: 'Every completed milestone includes an automated, legally binding Work-for-Hire copyright transfer agreement. Upon escrow funds release, 100% of code IP, patents, and assets transfer exclusively to the client under standard software license terms (or custom NDA).'
    },
    {
      q: 'Why is Zapmancer Open Source and how does it prevent legal lock-in?',
      a: 'The core Zapmancer platform is 100% open-source under the Apache 2.0 license. This guarantees total auditability of escrow logic, eliminates vendor lock-in, and allows enterprise teams to self-host private marketplace nodes.'
    },
    {
      q: 'What are the exact platform pricing fees?',
      a: 'Unlike traditional platforms taking a 20% cut from engineers, Zapmancer charges 0% commission to developers. Clients pay a flat 3% to 5% operational cost recovery fee at escrow deposit time.'
    },
    {
      q: 'How are dispute resolution and escrow refunds managed?',
      a: 'If a milestone deliverable does not match agreed contract specs, either party can initiate automated dispute resolution backed by code repository audit logs and milestone verification windows.'
    }
  ];

  return (
    <div className="landing-page">
      <Header isLoggedIn={false} />

      {/* Hero Section */}
      <section className="landing-hero-section">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center relative z-10 space-y-8">
          
          {/* Overline Badge */}
          <div className="landing-hero-badge">
            <Sparkles className="w-3.5 h-3.5" /> Open-Source KMP Freelance Platform
          </div>

          {/* Hero Title */}
          <h1 className="landing-hero-title">
            Fair, Transparent Escrow Freelancing Built for <span className="text-brand-green">Engineers & Creators</span>
          </h1>

          {/* Subtitle */}
          <p className="landing-hero-subtitle">
            Zapmancer eliminates high fee extractions (0% developer commission). Work directly with client escrow protection powered by Kotlin Multiplatform.
          </p>

          {/* Action Buttons */}
          <div className="flex flex-col sm:flex-row items-center justify-center gap-4 pt-4">
            <Link
              to="/signup"
              className="landing-btn-primary"
            >
              Get Started Free <ArrowRight className="w-4 h-4" />
            </Link>
            <Link
              to="/projects"
              className="landing-btn-secondary"
            >
              Browse Open Bounties
            </Link>
          </div>

          {/* Quick Metrics */}
          <div className="pt-12 grid grid-cols-2 md:grid-cols-4 gap-4 max-w-4xl mx-auto mt-12">
            <div className="landing-metric-card">
              <div className="text-3xl font-extrabold text-brand-green">$1.2M+</div>
              <div className="text-xs font-bold text-mute uppercase tracking-[0.05em] mt-1.5">Escrow Bounties Paid</div>
            </div>
            <div className="landing-metric-card">
              <div className="text-3xl font-extrabold text-ink">100%</div>
              <div className="text-xs font-bold text-mute uppercase tracking-[0.05em] mt-1.5">Open Source Codebase</div>
            </div>
            <div className="landing-metric-card">
              <div className="text-3xl font-extrabold text-brand-green">0%</div>
              <div className="text-xs font-bold text-mute uppercase tracking-[0.05em] mt-1.5">Developer Fees</div>
            </div>
            <div className="landing-metric-card">
              <div className="text-3xl font-extrabold text-ink">3,400+</div>
              <div className="text-xs font-bold text-mute uppercase tracking-[0.05em] mt-1.5">Verified Engineers</div>
            </div>
          </div>

        </div>
      </section>

      {/* How Payment & Escrow Works Section */}
      <section className="py-20 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full space-y-12">
        <div className="text-center space-y-2 max-w-2xl mx-auto">
          <div className="text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">Transparent Payments</div>
          <h2 className="text-3xl font-extrabold tracking-[-0.01em] text-ink">How Escrow & Payouts Work</h2>
          <p className="text-mute text-sm leading-relaxed">
            Zero surprise fees. Funds are protected in milestone escrow before coding begins, and released instantly upon sign-off.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {paymentSteps.map((step) => (
            <div key={step.step} className="bg-surface border border-hairline p-8 rounded-xl space-y-4 shadow-sm dark:shadow-none hover:bg-surface-elevated transition-colors">
              <div className="text-2xl font-extrabold text-brand-green font-mono">{step.step}</div>
              <h3 className="text-lg font-bold text-ink">{step.title}</h3>
              <p className="text-xs text-mute leading-relaxed">{step.desc}</p>
            </div>
          ))}
        </div>

        {/* Pricing Comparison Bar */}
        <div className="bg-surface-elevated border border-hairline p-8 rounded-xl grid grid-cols-1 md:grid-cols-2 gap-8 items-center">
          <div className="space-y-3">
            <span className="text-[11px] font-bold text-brand-green uppercase tracking-wider">Pricing Transparency</span>
            <h3 className="text-2xl font-extrabold text-ink">Why Choose Zapmancer Over Legacy Platforms?</h3>
            <p className="text-xs text-mute leading-relaxed">
              Traditional platforms extract 20% of developer income while holding payouts hostage for weeks. Zapmancer offers 0% developer fees and automated Stripe Direct Deposit or Web3 USDC payouts.
            </p>
          </div>

          <div className="space-y-3">
            <div className="p-4 rounded-xl bg-surface border border-hairline flex items-center justify-between">
              <div>
                <p className="text-xs font-bold text-ink">Zapmancer Marketplace</p>
                <p className="text-[11px] text-brand-green font-semibold">0% Developer Fee &bull; Instant Release</p>
              </div>
              <span className="text-lg font-extrabold text-brand-green">0% Cut</span>
            </div>

            <div className="p-4 rounded-xl bg-surface border border-hairline flex items-center justify-between opacity-60">
              <div>
                <p className="text-xs font-bold text-ink">Upwork / Freelancer</p>
                <p className="text-[11px] text-mute">10% - 20% Cut &bull; 14-day hold</p>
              </div>
              <span className="text-lg font-extrabold text-mute">20% Cut</span>
            </div>
          </div>
        </div>
      </section>

      {/* Legal & Open Source Compliance Section */}
      <section className="py-20 bg-surface border-y border-hairline">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-12">
          
          <div className="text-center space-y-2 max-w-2xl mx-auto">
            <div className="text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">Open Source & Legal Safety</div>
            <h2 className="text-3xl font-extrabold tracking-[-0.01em] text-ink">Auditable Code, Guaranteed IP Transfer</h2>
            <p className="text-mute text-sm leading-relaxed">
              Addressing legal challenges, IP rights, and open-source transparency for enterprise teams and developers.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div className="p-8 rounded-xl bg-surface-elevated border border-hairline space-y-4">
              <div className="w-10 h-10 rounded-full bg-brand-green/10 text-brand-green flex items-center justify-center border border-brand-green/20">
                <Scale className="w-5 h-5" />
              </div>
              <h3 className="text-xl font-bold text-ink">Work-for-Hire IP Transfer</h3>
              <p className="text-xs text-mute leading-relaxed">
                All contract submissions include automated legal IP copyright transfer. Clients retain 100% exclusive ownership of custom code deliverables, patents, and design assets upon milestone funds release.
              </p>
              <div className="flex items-center gap-2 text-xs font-bold text-brand-green pt-1">
                <ShieldCheck className="w-4 h-4" /> Standardized Legal Contracts Attached
              </div>
            </div>

            <div className="p-8 rounded-xl bg-surface-elevated border border-hairline space-y-4">
              <div className="w-10 h-10 rounded-full bg-brand-green/10 text-brand-green flex items-center justify-center border border-brand-green/20">
                <GitBranch className="w-5 h-5" />
              </div>
              <h3 className="text-xl font-bold text-ink">Why 100% Open Source?</h3>
              <p className="text-xs text-mute leading-relaxed">
                By keeping the platform core open-source, escrow logic and matching algorithms are completely auditable. No hidden black-box algorithms or proprietary vendor lock-in.
              </p>
              <div className="flex items-center gap-2 text-xs font-bold text-brand-green pt-1">
                <CheckCircle2 className="w-4 h-4" /> Apache 2.0 Audited Marketplace Core
              </div>
            </div>
          </div>

          <div className="space-y-4 max-w-3xl mx-auto pt-6">
            <h3 className="text-xl font-extrabold text-ink text-center mb-6">Frequently Asked Questions</h3>

            {legalFaqs.map((faq, idx) => {
              const isOpen = openFaq === idx;
              return (
                <div key={idx} className="bg-surface-elevated border border-hairline rounded-xl overflow-hidden transition-colors">
                  <button
                    onClick={() => setOpenFaq(isOpen ? null : idx)}
                    className="w-full p-5 text-left font-bold text-sm text-ink flex items-center justify-between gap-4"
                  >
                    <span>{faq.q}</span>
                    {isOpen ? <ChevronUp className="w-4 h-4 text-brand-green shrink-0" /> : <ChevronDown className="w-4 h-4 text-mute shrink-0" />}
                  </button>

                  {isOpen && (
                    <div className="px-5 pb-5 text-xs text-mute leading-relaxed border-t border-hairline pt-3 bg-surface">
                      {faq.a}
                    </div>
                  )}
                </div>
              );
            })}
          </div>

        </div>
      </section>

      {/* Featured Categories */}
      <section className="py-16 bg-canvas border-b border-hairline">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col sm:flex-row sm:items-end justify-between mb-10 gap-4">
            <div>
              <div className="text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">Stack Categories</div>
              <h2 className="text-2xl font-bold tracking-tight text-ink mt-1">Popular Skill Ecosystems</h2>
            </div>
            <Link to="/projects" className="text-brand-green hover:text-brand-green-hover text-xs font-bold uppercase tracking-[0.05em] transition-colors">
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

      {/* Live Marketplace — Active Project Bounties Section */}
      <section className="py-20 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full space-y-8">
        <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 border-b border-hairline pb-6">
          <div className="space-y-1">
            <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
              <Sparkles className="w-3.5 h-3.5" /> Live Marketplace
            </div>
            <h2 className="text-3xl font-extrabold tracking-tight text-ink">Active Project Bounties</h2>
            <p className="text-sm text-mute">Explore verified contract bounties with funded milestone escrows.</p>
          </div>
          <Link to="/projects" className="text-brand-green hover:text-brand-green-hover text-xs font-bold uppercase tracking-[0.05em] transition-colors shrink-0">
            Explore All Projects &rarr;
          </Link>
        </div>

        {/* Clean Project Cards List */}
        <div className="space-y-4">
          {featuredProjects.map((p) => (
            <div
              key={p.id}
              className="bg-surface p-6 rounded-xl border border-hairline hover:border-brand-green/40 hover:bg-surface-elevated hover:scale-[1.005] transition-all duration-200 flex flex-col md:flex-row md:items-center justify-between gap-6 shadow-sm dark:shadow-none"
            >
              <div className="space-y-2 flex-1">
                <div className="flex items-center gap-2 text-xs">
                  <span className="font-bold text-brand-green uppercase tracking-wider">{p.client}</span>
                  <span className="text-mute">&bull;</span>
                  <span className="text-mute flex items-center gap-1"><ShieldCheck className="w-3.5 h-3.5 text-brand-green" /> Verified Escrow</span>
                  <span className="text-mute">&bull;</span>
                  <span className="text-mute">{p.posted}</span>
                </div>

                <h3 className="font-extrabold text-lg text-ink hover:text-brand-green transition-colors">
                  <Link to={`/projects/${p.id}`}>{p.title}</Link>
                </h3>

                <div className="flex flex-wrap gap-2 pt-1">
                  {p.tags.map((t) => (
                    <span key={t} className="px-3.5 py-1 rounded-full bg-surface-elevated text-brand-green text-xs font-bold border border-hairline">
                      {t}
                    </span>
                  ))}
                </div>
              </div>

              <div className="flex items-center justify-between md:justify-end gap-6 border-t md:border-t-0 border-hairline pt-4 md:pt-0 shrink-0">
                <div className="text-left md:text-right">
                  <div className="text-xl font-extrabold text-brand-green tracking-tight">{p.budget}</div>
                  <div className="text-xs text-mute font-bold uppercase tracking-wider">{p.budgetType}</div>
                </div>

                <Link
                  to={`/projects/${p.id}/apply`}
                  className="bg-brand-green hover:bg-brand-green-hover text-white font-bold text-xs uppercase tracking-[0.05em] px-6 py-3 rounded-full hover:scale-[1.04] transition-all shadow-md shadow-brand-green/20"
                >
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
          <Link to="/signup" className="bg-brand-green hover:bg-brand-green-hover text-white font-bold text-xs uppercase tracking-[0.05em] px-8 py-4 rounded-full hover:scale-[1.04] transition-all shrink-0 shadow-md shadow-brand-green/20">
            Join Marketplace Now
          </Link>
        </div>
      </section>

      <Footer />
    </div>
  );
};
