import React, { useState } from 'react';
import './landing.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import {
  Code, Smartphone, CheckCircle2,
  ChevronDown, ChevronUp, Palette, Terminal, Database, Bot,
  PenTool, TrendingUp, Sliders
} from 'lucide-react';

export const LandingPage: React.FC = () => {
  const [openFaq, setOpenFaq] = useState<number | null>(null);
  const [calcAmount, setCalcAmount] = useState<number>(5000);

  const zapmancerPayout = calcAmount;
  const upworkPayout = calcAmount * 0.80;
  const freelancerPayout = calcAmount * 0.76;

  const features = [
    { name: 'Full-Stack & Web Dev', count: '342 open contracts', icon: Code, tint: 'landing-feat-card-mint' },
    { name: 'AI Builders & Agents', count: '215 open contracts', icon: Bot, tint: 'landing-feat-card-lavender' },
    { name: 'UI/UX & Product Design', count: '189 open contracts', icon: Palette, tint: 'landing-feat-card-sky' },
    { name: 'Mobile (iOS & Android)', count: '194 open contracts', icon: Smartphone, tint: 'landing-feat-card-peach' },
    { name: 'DevOps & Cloud', count: '126 open contracts', icon: Terminal, tint: 'landing-feat-card-rose' },
    { name: 'Data & Analytics', count: '112 open contracts', icon: Database, tint: 'landing-feat-card-yellow' },
    { name: 'Technical Writing', count: '78 open contracts', icon: PenTool, tint: 'landing-feat-card-mint' },
    { name: 'Growth & Marketing', count: '95 open contracts', icon: TrendingUp, tint: 'landing-feat-card-sky' },
  ];

  const steps = [
    {
      n: '1',
      title: 'Post a project or hire directly',
      desc: 'Create a Company Workspace, post a scoped contract, and set a fixed or hourly budget. Receive proposals from vetted workers within hours.'
    },
    {
      n: '2',
      title: 'Deposit into milestone escrow',
      desc: 'Funds are held in secure escrow (Stripe or USDC stablecoin) before any work begins — protecting both sides of the contract.'
    },
    {
      n: '3',
      title: 'Review deliverables and release payment',
      desc: 'Sign off on completed work and funds transfer instantly. An automated Work-for-Hire IP agreement executes at the same moment.'
    },
  ];

  const faqs = [
    {
      q: 'Who can join Zapmancer?',
      a: 'Anyone. Companies register a Workspace to manage teams and contract freelancers. Developers, designers, AI builders, marketers, and creators join to find paid work.'
    },
    {
      q: 'What are the platform fees?',
      a: 'Workers pay 0% commission. Companies pay a flat 3–5% escrow processing fee at milestone deposit time — no hidden charges, no sliding scale.'
    },
    {
      q: 'How does IP ownership work?',
      a: 'Every completed milestone triggers an automated, legally-binding Work-for-Hire copyright transfer. All deliverables — code, design, content — transfer 100% to the client on payment release.'
    },
    {
      q: 'What payment methods are supported?',
      a: 'Stripe bank transfer (USD, EUR, GBP), credit card, and USDC stablecoin. Payouts reach worker accounts with no holding period.'
    },
    {
      q: 'Can companies manage a full-time team on Zapmancer?',
      a: 'Yes. The Company Workspace includes a sprint board, team roster, time logs, payroll, and an escrow vault — covering both permanent staff and contract workers in one place.'
    },
  ];

  return (
    <div className="landing-page">
      <Header isLoggedIn={false} />

      {/* ---- Hero Band ---- */}
      <section className="landing-hero">
        <div className="max-w-6xl mx-auto px-6 lg:px-8">
          <p className="landing-hero-eyebrow">Company OS & Freelance Marketplace</p>
          <h1 className="landing-hero-title">
            Run your company and hire developers, designers, and AI builders.
          </h1>
          <p className="landing-hero-subtitle">
            Manage your full-time team alongside on-demand freelancers — all in one workspace, with milestone escrow and 0% worker commission.
          </p>

          <div className="landing-hero-actions">
            <Link to="/signup" className="btn-hero-primary">
              Get Zapmancer free
            </Link>
            <Link to="/projects" className="btn-hero-secondary">
              Browse open contracts
            </Link>
          </div>

          {/* Stat chips */}
          <div className="landing-stat-chips">
            <div className="landing-stat-chip">
              <span className="landing-stat-chip-value">$1.2M+</span>
              <span className="landing-stat-chip-label">escrow paid</span>
            </div>
            <span className="landing-stat-chip-divider" />
            <div className="landing-stat-chip">
              <span className="landing-stat-chip-value">0%</span>
              <span className="landing-stat-chip-label">worker fee</span>
            </div>
            <span className="landing-stat-chip-divider" />
            <div className="landing-stat-chip">
              <span className="landing-stat-chip-value">3,400+</span>
              <span className="landing-stat-chip-label">verified members</span>
            </div>
            <span className="landing-stat-chip-divider" />
            <div className="landing-stat-chip">
              <span className="landing-stat-chip-value">100%</span>
              <span className="landing-stat-chip-label">auto IP transfer</span>
            </div>
          </div>
        </div>
      </section>



      {/* ---- Open Disciplines Grid ---- */}
      <section className="landing-section">
        <div className="max-w-6xl mx-auto px-6 lg:px-8">
          <div className="mb-10">
            <p className="landing-section-label">Every discipline, one platform</p>
            <h2 className="landing-section-title">Work across every specialty and industry.</h2>
            <p className="landing-section-subtitle">
              Post a contract in any discipline. Zapmancer surfaces the right talent based on verified work history and skills.
            </p>
          </div>

          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            {features.map((feat) => {
              const Icon = feat.icon;
              return (
                <Link key={feat.name} to="/projects" className={`landing-feat-card ${feat.tint}`} style={{ textDecoration: 'none' }}>
                  <div className="landing-feat-card-icon">
                    <Icon size={18} />
                  </div>
                  <div>
                    <p className="landing-feat-card-title">{feat.name}</p>
                    <p className="landing-feat-card-count">{feat.count}</p>
                  </div>
                </Link>
              );
            })}
          </div>
        </div>
      </section>

      {/* ---- How It Works ---- */}
      <section className="landing-section" style={{ backgroundColor: 'var(--color-surface)' }}>
        <div className="max-w-6xl mx-auto px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-12 items-start">
            <div>
              <p className="landing-section-label">How it works</p>
              <h2 className="landing-section-title">From contract post to payout in three steps.</h2>
              <p className="landing-section-subtitle">
                Zapmancer handles escrow, legal IP transfer, and payment release automatically — so you focus on the work.
              </p>
            </div>

            <div>
              {steps.map((step) => (
                <div key={step.n} className="landing-step">
                  <div className="landing-step-number">{step.n}</div>
                  <div>
                    <p className="landing-step-title">{step.title}</p>
                    <p className="landing-step-desc">{step.desc}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>

      {/* ---- Fee Comparison ---- */}
      <section className="landing-section">
        <div className="max-w-6xl mx-auto px-6 lg:px-8">
          <div className="mb-10">
            <p className="landing-section-label">Transparent pricing</p>
            <h2 className="landing-section-title">Keep what you earn.</h2>
            <p className="landing-section-subtitle">
              Compare what you actually take home on a <strong>${calcAmount.toLocaleString()}</strong> contract.
            </p>

            {/* Slider */}
            <div className="mt-6 flex items-center gap-4 max-w-sm">
              <Sliders size={16} className="text-primary shrink-0" style={{ color: 'var(--color-primary)' }} />
              <input
                type="range"
                min="500"
                max="20000"
                step="500"
                value={calcAmount}
                onChange={(e) => setCalcAmount(Number(e.target.value))}
                style={{ accentColor: 'var(--color-primary)' }}
                className="flex-1 cursor-pointer"
              />
              <span className="text-sm font-semibold text-ink" style={{ color: 'var(--color-ink)', minWidth: 72 }}>
                ${calcAmount.toLocaleString()}
              </span>
            </div>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            {/* Zapmancer */}
            <div className="landing-fee-card landing-fee-card-featured">
              <p className="text-xs font-semibold uppercase tracking-wider mb-3" style={{ color: 'var(--color-primary)' }}>
                Zapmancer
              </p>
              <p className="text-3xl font-bold" style={{ color: 'var(--color-ink)', letterSpacing: '-0.5px' }}>
                ${zapmancerPayout.toLocaleString()}
              </p>
              <p className="text-xs mt-2" style={{ color: 'var(--color-steel)' }}>Worker fee: 0%</p>
              <div className="mt-4 flex items-center gap-2 text-xs font-semibold" style={{ color: 'var(--color-primary)' }}>
                <CheckCircle2 size={14} /> Full amount retained
              </div>
            </div>

            {/* Upwork */}
            <div className="landing-fee-card" style={{ opacity: 0.7 }}>
              <p className="text-xs font-semibold uppercase tracking-wider mb-3" style={{ color: 'var(--color-steel)' }}>
                Upwork
              </p>
              <p className="text-3xl font-bold" style={{ color: 'var(--color-ink)', letterSpacing: '-0.5px' }}>
                ${upworkPayout.toLocaleString()}
              </p>
              <p className="text-xs mt-2" style={{ color: 'var(--color-steel)' }}>Worker fee: 20%</p>
              <p className="text-xs mt-4 font-medium" style={{ color: 'var(--color-warning)' }}>
                You lose ${(calcAmount - upworkPayout).toLocaleString()}
              </p>
            </div>

            {/* Freelancer.com */}
            <div className="landing-fee-card" style={{ opacity: 0.6 }}>
              <p className="text-xs font-semibold uppercase tracking-wider mb-3" style={{ color: 'var(--color-steel)' }}>
                Freelancer.com
              </p>
              <p className="text-3xl font-bold" style={{ color: 'var(--color-ink)', letterSpacing: '-0.5px' }}>
                ${freelancerPayout.toLocaleString()}
              </p>
              <p className="text-xs mt-2" style={{ color: 'var(--color-steel)' }}>Worker fee: 24%</p>
              <p className="text-xs mt-4 font-medium" style={{ color: 'var(--color-warning)' }}>
                You lose ${(calcAmount - freelancerPayout).toLocaleString()}
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* ---- Testimonials ---- */}
      <section className="landing-section" style={{ backgroundColor: 'var(--color-surface)' }}>
        <div className="max-w-6xl mx-auto px-6 lg:px-8">
          <div className="mb-10">
            <p className="landing-section-label">From the community</p>
            <h2 className="landing-section-title">Used by workers and companies worldwide.</h2>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="landing-testimonial">
              <p className="text-base leading-relaxed" style={{ color: 'var(--color-ink)' }}>
                "Zapmancer saved me over $12,000 in platform fees compared to Upwork. Milestone escrow releases are instant and I work directly with global companies."
              </p>
              <div className="flex items-center gap-3 mt-6 pt-4 border-t" style={{ borderColor: 'var(--color-hairline)' }}>
                <img
                  src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=64&q=80"
                  alt="Elena Rostova"
                  className="w-10 h-10 rounded-full object-cover"
                  style={{ border: '1px solid var(--color-hairline)' }}
                />
                <div>
                  <p className="text-sm font-semibold" style={{ color: 'var(--color-ink)' }}>Elena Rostova</p>
                  <p className="text-xs" style={{ color: 'var(--color-steel)' }}>Full-Stack & AI Builder · $68,000+ earned</p>
                </div>
              </div>
            </div>

            <div className="landing-testimonial">
              <p className="text-base leading-relaxed" style={{ color: 'var(--color-ink)' }}>
                "Managing our core team and hiring on-demand engineers and designers from one workspace has completely changed how we operate as a company."
              </p>
              <div className="flex items-center gap-3 mt-6 pt-4 border-t" style={{ borderColor: 'var(--color-hairline)' }}>
                <img
                  src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=64&q=80"
                  alt="Marcus Vance"
                  className="w-10 h-10 rounded-full object-cover"
                  style={{ border: '1px solid var(--color-hairline)' }}
                />
                <div>
                  <p className="text-sm font-semibold" style={{ color: 'var(--color-ink)' }}>Marcus Vance</p>
                  <p className="text-xs" style={{ color: 'var(--color-steel)' }}>CTO, Acme AI Systems · 14 milestones funded</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* ---- FAQ ---- */}
      <section className="landing-section">
        <div className="max-w-3xl mx-auto px-6 lg:px-8">
          <div className="mb-10">
            <p className="landing-section-label">Questions</p>
            <h2 className="landing-section-title">Frequently asked.</h2>
          </div>

          <div>
            {faqs.map((faq, idx) => (
              <div
                key={idx}
                className="landing-faq-item"
                onClick={() => setOpenFaq(openFaq === idx ? null : idx)}
              >
                <div className="landing-faq-question">
                  <span>{faq.q}</span>
                  {openFaq === idx
                    ? <ChevronUp size={18} style={{ color: 'var(--color-steel)', flexShrink: 0 }} />
                    : <ChevronDown size={18} style={{ color: 'var(--color-steel)', flexShrink: 0 }} />
                  }
                </div>
                {openFaq === idx && (
                  <p className="landing-faq-answer">{faq.a}</p>
                )}
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ---- CTA Banner ---- */}
      <section className="py-16 border-b" style={{ borderColor: 'var(--color-hairline)', backgroundColor: 'var(--color-canvas)' }}>
        <div className="max-w-6xl mx-auto px-6 lg:px-8">
          <div className="landing-cta-banner">
            <div style={{ maxWidth: 560 }}>
              <h2 className="text-3xl font-bold" style={{ color: 'var(--color-ink)', letterSpacing: '-0.5px', lineHeight: 1.2 }}>
                Start running your company on Zapmancer.
              </h2>
              <p className="mt-3 text-base" style={{ color: 'var(--color-steel)', lineHeight: 1.6 }}>
                Free to join. No subscription. Pay only when you fund a milestone.
              </p>
            </div>
            <div className="flex items-center gap-3 flex-wrap">
              <Link to="/signup" className="btn-hero-primary">
                Get started free
              </Link>
              <Link to="/company/dashboard" className="btn-hero-secondary">
                View workspace demo
              </Link>
            </div>
          </div>
        </div>
      </section>

      <Footer />
    </div>
  );
};
