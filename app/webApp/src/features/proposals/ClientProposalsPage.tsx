import React, { useState } from 'react';
import './proposals.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, Star, Sparkles } from 'lucide-react';
import { Tabs, TabItem } from '../../components/ui/Tabs';

type ProposalFilterId = 'all' | 'interviewing' | 'shortlisted';

export const ClientProposalsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<ProposalFilterId>('all');

  const filterTabs: TabItem<ProposalFilterId>[] = [
    { id: 'all', label: 'All Proposals (8)' },
    { id: 'interviewing', label: 'Interviewing (2)' },
    { id: 'shortlisted', label: 'Shortlisted (3)' },
  ];

  const proposals = [
    {
      id: 'pr1',
      name: 'Elena Rostova',
      title: 'Senior KMP & WebAssembly Lead',
      avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
      bid: '$3,200',
      rating: 5.0,
      reviews: 42,
      pitch: 'I have architected over 15 Compose Multiplatform desktop and mobile clients connected to Ktor backends. Ready to deliver milestone 1 within 5 days with full test coverage.',
      skills: ['Kotlin', 'Wasm', 'Ktor', 'Compose']
    }
  ];

  return (
    <div className="proposals-page">
      <Header isLoggedIn={true} />

      <main className="proposals-main">
        <div className="space-y-1 border-b border-hairline pb-6">
          <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
            <Sparkles className="w-3.5 h-3.5" /> Client Hub
          </div>
          <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight text-ink">Contract Proposals & Bids</h1>
          <p className="text-sm text-mute">Review applicant proposals, compare milestone breakdowns, and award escrow contracts.</p>
        </div>

        <div>
          <Tabs
            tabs={filterTabs}
            activeTab={activeTab}
            onChange={(id) => setActiveTab(id)}
          />
        </div>

        <div className="space-y-4">
          {proposals.map((p) => (
            <div key={p.id} className="proposals-card">
              <div className="flex flex-col sm:flex-row sm:items-start justify-between gap-4">
                <div className="flex items-center gap-4">
                  <img src={p.avatar} alt={p.name} className="w-12 h-12 rounded-full object-cover border border-hairline shrink-0" />
                  <div>
                    <h3 className="font-extrabold text-lg text-ink flex items-center gap-1.5">
                      {p.name} <ShieldCheck className="w-4 h-4 text-brand-green" />
                    </h3>
                    <p className="text-xs text-mute font-medium">{p.title}</p>
                    <div className="flex items-center gap-2 text-xs text-brand-green font-bold pt-0.5">
                      <Star className="w-3.5 h-3.5 fill-current" /> {p.rating} ({p.reviews} reviews)
                    </div>
                  </div>
                </div>

                <div className="text-left sm:text-right shrink-0">
                  <div className="text-xl font-extrabold text-brand-green">{p.bid}</div>
                  <div className="text-xs text-mute font-bold uppercase tracking-wider">Fixed Bid</div>
                </div>
              </div>

              <p className="text-xs text-mute leading-relaxed bg-surface-elevated p-4 rounded-xl border border-hairline">
                "{p.pitch}"
              </p>

              <div className="flex items-center justify-between gap-4 pt-2 border-t border-hairline">
                <div className="flex flex-wrap gap-2">
                  {p.skills.map((s) => (
                    <span key={s} className="px-3 py-1 rounded-full bg-surface-elevated text-brand-green text-xs font-bold border border-hairline">
                      {s}
                    </span>
                  ))}
                </div>

                <Link
                  to="/messages"
                  className="bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] px-6 py-2.5 rounded-full shadow-md shadow-brand-green/20 hover:scale-[1.03] transition-all"
                >
                  Hire & Award Contract
                </Link>
              </div>
            </div>
          ))}
        </div>
      </main>

      <Footer />
    </div>
  );
};
