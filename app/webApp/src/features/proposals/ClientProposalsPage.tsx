import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Star, MessageSquare, Check, Sparkles } from 'lucide-react';
import { Tabs, TabItem } from '../../components/ui/Tabs';

type FilterStatus = 'All' | 'Shortlisted' | 'Accepted';

export const ClientProposalsPage: React.FC = () => {
  const [filterStatus, setFilterStatus] = useState<FilterStatus>('All');

  const filterTabs: TabItem<FilterStatus>[] = [
    { id: 'All', label: 'All Bids' },
    { id: 'Shortlisted', label: 'Shortlisted' },
    { id: 'Accepted', label: 'Accepted Contracts' },
  ];

  const [proposals, setProposals] = useState([
    {
      id: 'p1',
      freelancer: 'Sarah Jenkins',
      title: 'Senior KMP & Compose Specialist',
      avatar: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80',
      rating: 4.9,
      bid: '$3,200',
      days: '18 Days',
      status: 'Shortlisted',
      cover: 'I have built 4 enterprise Compose Desktop apps with Ktor. I will deliver full unit tests and milestone docs.',
      skills: ['Kotlin', 'Compose', 'Ktor', 'PostgreSQL']
    },
    {
      id: 'p2',
      freelancer: 'Marcus Vance',
      title: 'Android & KMP Architect',
      avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
      rating: 4.8,
      bid: '$3,000',
      days: '21 Days',
      status: 'Pending',
      cover: 'Experienced in modular architecture and UI performance optimization across desktop and mobile.',
      skills: ['Kotlin', 'Java', 'Coroutines', 'SQLDelight']
    }
  ]);

  const handleAction = (id: string, newStatus: string) => {
    setProposals(proposals.map((p) => (p.id === id ? { ...p, status: newStatus } : p)));
  };

  const filteredProposals = proposals.filter((p) => {
    if (filterStatus === 'All') return true;
    return p.status === filterStatus;
  });

  return (
    <div className="min-h-screen flex flex-col bg-canvas text-ink transition-colors duration-200 antialiased font-sans">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-6xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-hairline pb-6">
          <div className="space-y-1">
            <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
              <Sparkles className="w-3.5 h-3.5" /> Proposal Review
            </div>
            <h1 className="text-3xl font-extrabold tracking-tight text-ink">Client Proposal Hub</h1>
            <p className="text-sm text-mute">Review applicant bids for your project: Compose Multiplatform Desktop App.</p>
          </div>

          <div>
            <Tabs
              tabs={filterTabs}
              activeTab={filterStatus}
              onChange={(id) => setFilterStatus(id)}
            />
          </div>
        </div>

        {/* Proposals List */}
        <div className="space-y-6">
          {filteredProposals.map((prop) => (
            <div
              key={prop.id}
              className="bg-surface p-6 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6 hover:bg-surface-elevated transition-all duration-200"
            >
              {/* Header Info */}
              <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                <div className="flex items-center gap-4">
                  <img
                    src={prop.avatar}
                    alt={prop.freelancer}
                    className="w-14 h-14 rounded-full object-cover border border-brand-green/30"
                  />
                  <div>
                    <h3 className="text-lg font-bold text-ink flex items-center gap-2">
                      <Link to="/profile/1" className="hover:text-brand-green transition-colors">{prop.freelancer}</Link>
                      <span className="flex items-center gap-1 text-xs font-bold text-brand-green bg-brand-green/10 px-2.5 py-0.5 rounded-full">
                        <Star className="w-3 h-3 fill-current" /> {prop.rating}
                      </span>
                    </h3>
                    <p className="text-xs text-mute font-medium">{prop.title}</p>
                  </div>
                </div>

                <div className="text-left sm:text-right">
                  <div className="text-xl font-extrabold text-brand-green">{prop.bid}</div>
                  <div className="text-xs text-mute font-medium">{prop.days} Delivery</div>
                </div>
              </div>

              {/* Pitch Cover Letter */}
              <p className="text-sm text-ink bg-surface-elevated p-4 rounded-xl border border-hairline leading-relaxed italic">
                "{prop.cover}"
              </p>

              {/* Actions & Skill Tags */}
              <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pt-4 border-t border-hairline">
                <div className="flex flex-wrap gap-2">
                  {prop.skills.map((s) => (
                    <span key={s} className="px-3 py-1 rounded-full bg-surface-elevated text-brand-green text-xs font-bold border border-hairline">
                      {s}
                    </span>
                  ))}
                </div>

                <div className="flex items-center gap-3">
                  <Link
                    to="/messages"
                    className="flex items-center gap-1.5 px-5 py-2.5 rounded-full border border-hairline bg-surface-elevated text-xs font-bold text-ink hover:bg-surface-modal transition-all"
                  >
                    <MessageSquare className="w-4 h-4 text-brand-green" /> Message
                  </Link>

                  {prop.status !== 'Accepted' ? (
                    <button
                      onClick={() => handleAction(prop.id, 'Accepted')}
                      className="flex items-center gap-1.5 px-6 py-2.5 rounded-full bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] transition-all hover:scale-[1.04] shadow-md shadow-brand-green/20"
                    >
                      <Check className="w-4 h-4" /> Accept & Award Contract
                    </button>
                  ) : (
                    <span className="px-4 py-2 rounded-full bg-brand-green/10 text-brand-green text-xs font-bold uppercase tracking-wider">
                      Contract Awarded
                    </span>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>

      </main>

      <Footer />
    </div>
  );
};
