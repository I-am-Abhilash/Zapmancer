import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Award, ShieldCheck, MessageSquare, Check, X, Star, FileText } from 'lucide-react';

export const ClientProposalsPage: React.FC = () => {
  const [filterStatus, setFilterStatus] = useState<'All' | 'Shortlisted' | 'Accepted'>('All');

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
    <div className="min-h-screen flex flex-col bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 transition-colors">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-6xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <h1 className="text-3xl font-extrabold tracking-tight">Client Proposal Hub</h1>
            <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Review applicant bids for your project: Compose Multiplatform Desktop App.</p>
          </div>

          <div className="flex items-center gap-2">
            {['All', 'Shortlisted', 'Accepted'].map((st) => (
              <button
                key={st}
                onClick={() => setFilterStatus(st as any)}
                className={`px-4 py-2 rounded-xl text-xs font-bold transition-all ${
                  filterStatus === st
                    ? 'bg-indigo-600 text-white shadow-sm'
                    : 'bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 text-slate-600 dark:text-slate-300'
                }`}
              >
                {st}
              </button>
            ))}
          </div>
        </div>

        {/* Proposals List */}
        <div className="space-y-6">
          {filteredProposals.map((prop) => (
            <div
              key={prop.id}
              className="bg-white dark:bg-slate-900 p-6 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-sm space-y-6"
            >
              {/* Header Info */}
              <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                <div className="flex items-center gap-4">
                  <img
                    src={prop.avatar}
                    alt={prop.freelancer}
                    className="w-14 h-14 rounded-2xl object-cover border border-indigo-500/30"
                  />
                  <div>
                    <h3 className="text-lg font-bold text-slate-900 dark:text-white flex items-center gap-2">
                      <Link to="/profile/1" className="hover:text-indigo-600">{prop.freelancer}</Link>
                      <span className="flex items-center gap-1 text-xs font-bold text-amber-500 bg-amber-50 dark:bg-amber-950/40 px-2 py-0.5 rounded-md">
                        <Star className="w-3 h-3 fill-current" /> {prop.rating}
                      </span>
                    </h3>
                    <p className="text-xs text-slate-500 dark:text-slate-400">{prop.title}</p>
                  </div>
                </div>

                <div className="text-left sm:text-right">
                  <div className="text-xl font-extrabold text-emerald-600 dark:text-emerald-400">{prop.bid}</div>
                  <div className="text-xs text-slate-400 font-medium">{prop.days} Delivery</div>
                </div>
              </div>

              {/* Pitch */}
              <p className="text-sm text-slate-600 dark:text-slate-300 bg-slate-50 dark:bg-slate-800/50 p-4 rounded-2xl border border-slate-100 dark:border-slate-800 leading-relaxed">
                "{prop.cover}"
              </p>

              {/* Actions & Skills */}
              <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pt-2 border-t border-slate-100 dark:border-slate-800">
                <div className="flex flex-wrap gap-2">
                  {prop.skills.map((s) => (
                    <span key={s} className="px-2.5 py-1 rounded-lg bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 text-xs font-semibold">
                      {s}
                    </span>
                  ))}
                </div>

                <div className="flex items-center gap-3">
                  <Link
                    to="/messages"
                    className="flex items-center gap-1.5 px-4 py-2 rounded-xl border border-slate-300 dark:border-slate-700 text-xs font-bold text-slate-700 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800"
                  >
                    <MessageSquare className="w-4 h-4" /> Message
                  </Link>

                  {prop.status !== 'Accepted' ? (
                    <button
                      onClick={() => handleAction(prop.id, 'Accepted')}
                      className="flex items-center gap-1.5 px-5 py-2 rounded-xl bg-emerald-600 hover:bg-emerald-700 text-white text-xs font-bold transition-all shadow-md shadow-emerald-500/20"
                    >
                      <Check className="w-4 h-4" /> Accept & Award Contract
                    </button>
                  ) : (
                    <span className="px-4 py-2 rounded-xl bg-emerald-100 dark:bg-emerald-950/60 text-emerald-600 dark:text-emerald-400 text-xs font-extrabold">
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
