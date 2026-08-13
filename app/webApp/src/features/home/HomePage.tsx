import React from 'react';
import './home.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, Plus, ArrowRight, Clock, Award, Layers } from 'lucide-react';

export const HomePage: React.FC = () => {
  const metrics = [
    { label: 'Active Proposals', value: '3 Submitted', detail: '1 Client Interviewing' },
    { label: 'Escrow Locked', value: '$4,500.00', detail: '2 Milestones In Progress' },
    { label: 'Total Earnings', value: '$32,400.00', detail: '100% Payout Success' },
  ];

  return (
    <div className="home-page">
      <Header isLoggedIn={true} />

      <main className="home-main">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-hairline pb-6">
          <div className="space-y-1">
            <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
              <ShieldCheck className="w-3.5 h-3.5" /> Engineer Dashboard
            </div>
            <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight text-ink">Welcome back, Alex</h1>
            <p className="text-sm text-mute">Here is your active contract telemetry and recommended bounty proposals.</p>
          </div>

          <div className="flex items-center gap-3">
            <Link
              to="/projects"
              className="inline-flex items-center gap-2 bg-surface hover:bg-surface-elevated text-ink border border-hairline text-xs font-bold uppercase tracking-[0.05em] px-5 py-3 rounded-full transition-all"
            >
              Browse Contracts
            </Link>
            <Link
              to="/projects/new"
              className="inline-flex items-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] px-6 py-3 rounded-full hover:scale-[1.03] transition-all shadow-md shadow-brand-green/20"
            >
              <Plus className="w-4 h-4" /> Post Bounty
            </Link>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {metrics.map((m, idx) => (
            <div key={idx} className="home-metric-card">
              <span className="text-xs font-bold uppercase tracking-wider text-mute">{m.label}</span>
              <p className="text-2xl font-extrabold text-brand-green">{m.value}</p>
              <p className="text-xs text-mute font-medium">{m.detail}</p>
            </div>
          ))}
        </div>

        <div className="home-card">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-bold text-ink">Recommended Contracts for You</h2>
            <Link to="/projects" className="text-brand-green hover:text-brand-green-hover text-xs font-bold uppercase tracking-[0.05em]">
              View All &rarr;
            </Link>
          </div>

          <div className="p-5 rounded-xl bg-surface-elevated border border-hairline flex flex-col sm:flex-row sm:items-center justify-between gap-4">
            <div className="space-y-1">
              <h3 className="font-extrabold text-ink text-base">Real-Time WebSockets Engine in Ktor</h3>
              <p className="text-xs text-mute">Acme Corp &bull; Fixed Bounty $3,500 &bull; Est. 2 weeks</p>
            </div>
            <Link
              to="/projects/1/apply"
              className="inline-flex items-center gap-1.5 bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] px-5 py-2.5 rounded-full hover:scale-[1.04] transition-all shadow-md shadow-brand-green/20 shrink-0"
            >
              Apply <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
};
