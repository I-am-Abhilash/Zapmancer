import React, { useState, useEffect } from 'react';
import './landing.css';
import { Link } from 'react-router-dom';
import {
  Code,
  Smartphone,
  ChevronDown,
  ChevronUp,
  Palette,
  Terminal,
  Database,
  Bot,
  PenTool,
  TrendingUp,
  Sliders,
  Sparkles,
} from 'lucide-react';
import { landingService, LandingData } from '../../services/landingService';

const ICON_MAP: Record<string, any> = {
  Code,
  Bot,
  Palette,
  Smartphone,
  Terminal,
  Database,
  PenTool,
  TrendingUp,
};

export const LandingPage: React.FC = () => {
  const [data, setData] = useState<LandingData | null>(null);
  const [openFaq, setOpenFaq] = useState<number | null>(null);
  const [calcAmount, setCalcAmount] = useState<number>(5000);

  useEffect(() => {
    let isMounted = true;
    landingService.getLandingData().then((res) => {
      if (isMounted) setData(res);
    });
    return () => {
      isMounted = false;
    };
  }, []);

  const zapmancerPayout = calcAmount;
  const upworkPayout = calcAmount * 0.8;
  const freelancerPayout = calcAmount * 0.76;

  if (!data) return null;

  return (
    <div className="landing-page">
      {/* ---- Hero Band ---- */}
      <section className="landing-hero">
        <div className="max-w-6xl mx-auto px-6 lg:px-8">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary/10 border border-primary/20 text-primary text-xs font-semibold mb-4">
            <Sparkles size={14} /> Open-Source Freelance Marketplace
          </div>
          <h1 className="landing-hero-title">{data.headline}</h1>
          <p className="landing-hero-subtitle">{data.subtitle}</p>

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
            {data.stats.map((st, i) => (
              <React.Fragment key={st.label}>
                {i > 0 && <span className="landing-stat-chip-divider" />}
                <div className="landing-stat-chip">
                  <span className="landing-stat-chip-value">{st.value}</span>
                  <span className="landing-stat-chip-label">{st.label}</span>
                </div>
              </React.Fragment>
            ))}
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
            {data.categories.map((feat) => {
              const Icon = ICON_MAP[feat.iconName] || Code;
              return (
                <Link
                  key={feat.name}
                  to="/projects"
                  className={`landing-feat-card ${feat.tint}`}
                  style={{ textDecoration: 'none' }}
                >
                  <div className="landing-feat-card-icon">
                    <Icon size={18} />
                  </div>
                  <h3 className="landing-feat-card-title">{feat.name}</h3>
                  <p className="landing-feat-card-count">{feat.count}</p>
                </Link>
              );
            })}
          </div>
        </div>
      </section>

      {/* ---- Fee Comparison Calculator ---- */}
      <section className="landing-section" style={{ backgroundColor: 'var(--color-surface)' }}>
        <div className="max-w-6xl mx-auto px-6 lg:px-8">
          <div className="mb-10">
            <p className="landing-section-label">0% worker fee guarantee</p>
            <h2 className="landing-section-title">Keep 100% of what you earn.</h2>
            <p className="landing-section-subtitle">
              Traditional platforms charge workers 10–20% commission on every milestone. Zapmancer charges workers $0.
            </p>
          </div>

          <div className="landing-calc-wrap">
            <div className="mb-6">
              <div className="flex items-center justify-between mb-2">
                <span className="text-xs font-semibold" style={{ color: 'var(--color-steel)' }}>
                  <Sliders size={13} style={{ display: 'inline', marginRight: 4 }} />
                  Contract milestone value
                </span>
                <span
                  style={{
                    fontFamily: 'var(--font-mono)',
                    fontWeight: 700,
                    fontSize: 18,
                    color: 'var(--color-ink)',
                  }}
                >
                  ${calcAmount.toLocaleString()}
                </span>
              </div>
              <input
                type="range"
                min="500"
                max="25000"
                step="500"
                value={calcAmount}
                onChange={(e) => setCalcAmount(Number(e.target.value))}
                className="w-full h-2 rounded-lg cursor-pointer"
                style={{ accentColor: 'var(--color-primary)' }}
              />
            </div>

            <div className="landing-calc-cards">
              <div className="landing-calc-card landing-calc-card-zapmancer">
                <p className="landing-calc-card-name">Zapmancer</p>
                <p className="landing-calc-card-fee">0% worker commission</p>
                <p className="landing-calc-card-payout">${zapmancerPayout.toLocaleString()}</p>
                <p className="landing-calc-card-desc">Worker keeps 100% of escrow</p>
              </div>

              <div className="landing-calc-card landing-calc-card-competitor">
                <p className="landing-calc-card-name">Upwork</p>
                <p className="landing-calc-card-fee">10–20% platform fee</p>
                <p className="landing-calc-card-payout">${upworkPayout.toLocaleString()}</p>
                <p className="landing-calc-card-desc">-${(calcAmount - upworkPayout).toLocaleString()} lost in fees</p>
              </div>

              <div className="landing-calc-card landing-calc-card-competitor">
                <p className="landing-calc-card-name">Freelancer.com</p>
                <p className="landing-calc-card-fee">10% + withdrawal fee</p>
                <p className="landing-calc-card-payout">${freelancerPayout.toLocaleString()}</p>
                <p className="landing-calc-card-desc">-${(calcAmount - freelancerPayout).toLocaleString()} lost in fees</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* ---- How It Works ---- */}
      <section className="landing-section">
        <div className="max-w-6xl mx-auto px-6 lg:px-8">
          <div className="mb-10">
            <p className="landing-section-label">How it works</p>
            <h2 className="landing-section-title">Milestone escrow in 3 simple steps.</h2>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {data.steps.map((step) => (
              <div key={step.n} className="landing-step-card">
                <div className="landing-step-number">{step.n}</div>
                <h3 className="landing-step-title">{step.title}</h3>
                <p className="landing-step-desc">{step.desc}</p>
              </div>
            ))}
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
              <div
                className="flex items-center gap-3 mt-6 pt-4 border-t"
                style={{ borderColor: 'var(--color-hairline)' }}
              >
                <img
                  src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=64&q=80"
                  alt="Elena Rostova"
                  className="w-10 h-10 rounded-full object-cover"
                  style={{ border: '1px solid var(--color-hairline)' }}
                />
                <div>
                  <p className="text-sm font-semibold" style={{ color: 'var(--color-ink)' }}>
                    Elena Rostova
                  </p>
                  <p className="text-xs" style={{ color: 'var(--color-steel)' }}>
                    Full-Stack & AI Builder · $68,000+ earned
                  </p>
                </div>
              </div>
            </div>

            <div className="landing-testimonial">
              <p className="text-base leading-relaxed" style={{ color: 'var(--color-ink)' }}>
                "Managing our core team and hiring on-demand engineers and designers from one workspace has completely changed how we operate as a company."
              </p>
              <div
                className="flex items-center gap-3 mt-6 pt-4 border-t"
                style={{ borderColor: 'var(--color-hairline)' }}
              >
                <img
                  src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=64&q=80"
                  alt="Marcus Vance"
                  className="w-10 h-10 rounded-full object-cover"
                  style={{ border: '1px solid var(--color-hairline)' }}
                />
                <div>
                  <p className="text-sm font-semibold" style={{ color: 'var(--color-ink)' }}>
                    Marcus Vance
                  </p>
                  <p className="text-xs" style={{ color: 'var(--color-steel)' }}>
                    CTO, Acme AI Systems · 14 milestones funded
                  </p>
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
            {data.faqs.map((faq, idx) => (
              <div
                key={idx}
                className="landing-faq-item"
                onClick={() => setOpenFaq(openFaq === idx ? null : idx)}
              >
                <div className="landing-faq-question">
                  <span>{faq.q}</span>
                  {openFaq === idx ? (
                    <ChevronUp size={18} style={{ color: 'var(--color-steel)', flexShrink: 0 }} />
                  ) : (
                    <ChevronDown size={18} style={{ color: 'var(--color-steel)', flexShrink: 0 }} />
                  )}
                </div>
                {openFaq === idx && <p className="landing-faq-answer">{faq.a}</p>}
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ---- CTA Banner ---- */}
      <section
        className="py-16 border-b"
        style={{ borderColor: 'var(--color-hairline)', backgroundColor: 'var(--color-canvas)' }}
      >
        <div className="max-w-6xl mx-auto px-6 lg:px-8">
          <div className="landing-cta-banner">
            <div style={{ maxWidth: 560 }}>
              <h2
                className="text-3xl font-bold"
                style={{ color: 'var(--color-ink)', letterSpacing: '-0.5px', lineHeight: 1.2 }}
              >
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
    </div>
  );
};
