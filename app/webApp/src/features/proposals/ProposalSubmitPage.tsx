import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Send, ShieldCheck, DollarSign, Calendar, Plus, Trash2, ArrowLeft } from 'lucide-react';

export const ProposalSubmitPage: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const [bidAmount, setBidAmount] = useState('3200');
  const [deliveryDays, setDeliveryDays] = useState('21');
  const [coverLetter, setCoverLetter] = useState(
    'Hi, I have extensive experience delivering Compose Multiplatform desktop and mobile clients connected to Ktor backends. I can implement clean MVI architecture, adaptive layout widgets, and WebSockets sync.'
  );

  const [milestones, setMilestones] = useState([
    { title: 'KMP Architecture & Ktor Integration', amount: '1200' },
    { title: 'Compose UI Desktop Views & Analytics Charts', amount: '1200' },
    { title: 'Testing, Polishing & Final Delivery', amount: '800' },
  ]);

  const addMilestone = () => {
    setMilestones([...milestones, { title: 'New Milestone', amount: '500' }]);
  };

  const removeMilestone = (index: number) => {
    setMilestones(milestones.filter((_, i) => i !== index));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    navigate('/home');
  };

  return (
    <div className="min-h-screen flex flex-col bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 transition-colors">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-4xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        <button
          onClick={() => navigate(-1)}
          className="inline-flex items-center gap-2 text-sm font-semibold text-slate-500 hover:text-indigo-600 transition-colors"
        >
          <ArrowLeft className="w-4 h-4" /> Cancel & Return
        </button>

        <div>
          <h1 className="text-3xl font-extrabold tracking-tight">Submit Proposal</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
            Applying for: <strong className="text-slate-900 dark:text-white">Compose Multiplatform Desktop App for Ktor Analytics</strong>
          </p>
        </div>

        <form onSubmit={handleSubmit} className="bg-white dark:bg-slate-900 p-8 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-xl space-y-8">
          
          {/* Bid terms */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-bold text-slate-900 dark:text-white mb-1">
                Total Bid Amount ($)
              </label>
              <div className="relative">
                <span className="absolute left-3.5 top-3 text-slate-400 font-bold">$</span>
                <input
                  type="number"
                  required
                  value={bidAmount}
                  onChange={(e) => setBidAmount(e.target.value)}
                  className="w-full pl-8 pr-4 py-3 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm font-semibold focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <p className="text-xs text-slate-400 mt-1">Zero platform deduction on Zapmancer ($0 fee).</p>
            </div>

            <div>
              <label className="block text-sm font-bold text-slate-900 dark:text-white mb-1">
                Estimated Delivery (Days)
              </label>
              <div className="relative">
                <input
                  type="number"
                  required
                  value={deliveryDays}
                  onChange={(e) => setDeliveryDays(e.target.value)}
                  className="w-full px-4 py-3 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm font-semibold focus:ring-2 focus:ring-indigo-500"
                />
              </div>
            </div>
          </div>

          {/* Milestone Breakdown */}
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <h3 className="text-lg font-bold">Escrow Milestone Breakdown</h3>
              <button
                type="button"
                onClick={addMilestone}
                className="flex items-center gap-1 text-xs font-bold text-indigo-600 dark:text-indigo-400 hover:underline"
              >
                <Plus className="w-4 h-4" /> Add Milestone
              </button>
            </div>

            <div className="space-y-3">
              {milestones.map((m, idx) => (
                <div key={idx} className="flex items-center gap-3 p-3 rounded-xl bg-slate-50 dark:bg-slate-800/50 border border-slate-200 dark:border-slate-700">
                  <span className="text-xs font-bold text-slate-400">#{idx + 1}</span>
                  <input
                    type="text"
                    value={m.title}
                    onChange={(e) => {
                      const updated = [...milestones];
                      updated[idx].title = e.target.value;
                      setMilestones(updated);
                    }}
                    className="flex-1 px-3 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-sm font-medium"
                  />
                  <div className="relative w-32">
                    <span className="absolute left-2.5 top-2 text-xs text-slate-400 font-bold">$</span>
                    <input
                      type="number"
                      value={m.amount}
                      onChange={(e) => {
                        const updated = [...milestones];
                        updated[idx].amount = e.target.value;
                        setMilestones(updated);
                      }}
                      className="w-full pl-6 pr-2 py-1.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-sm font-bold"
                    />
                  </div>
                  {milestones.length > 1 && (
                    <button
                      type="button"
                      onClick={() => removeMilestone(idx)}
                      className="text-slate-400 hover:text-rose-500 p-1"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  )}
                </div>
              ))}
            </div>
          </div>

          {/* Cover Letter */}
          <div>
            <label className="block text-sm font-bold text-slate-900 dark:text-white mb-1">
              Cover Letter & Proposal Pitch
            </label>
            <textarea
              rows={6}
              required
              value={coverLetter}
              onChange={(e) => setCoverLetter(e.target.value)}
              className="w-full px-4 py-3 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm leading-relaxed focus:ring-2 focus:ring-indigo-500"
            />
          </div>

          {/* Action */}
          <div className="pt-4 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between">
            <div className="flex items-center gap-2 text-xs text-emerald-500 font-semibold">
              <ShieldCheck className="w-4 h-4" /> Milestone Escrow Protection active upon hire
            </div>

            <button
              type="submit"
              className="flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-base px-8 py-3.5 rounded-xl shadow-lg shadow-indigo-500/20 transition-all"
            >
              Submit Proposal <Send className="w-5 h-5" />
            </button>
          </div>

        </form>

      </main>

      <Footer />
    </div>
  );
};
