import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Send, ShieldCheck, Plus, Trash2, ArrowLeft, Sparkles } from 'lucide-react';

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
    <div className="min-h-screen flex flex-col bg-canvas text-ink transition-colors duration-200 antialiased font-sans">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-4xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        <button
          onClick={() => navigate(-1)}
          className="inline-flex items-center gap-2 text-xs uppercase font-bold tracking-[0.05em] text-mute hover:text-ink transition-colors"
        >
          <ArrowLeft className="w-4 h-4 text-brand-green" /> Cancel & Return
        </button>

        <div className="space-y-1">
          <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
            <Sparkles className="w-3.5 h-3.5" /> Proposal Submission
          </div>
          <h1 className="text-3xl font-extrabold tracking-tight text-ink">Submit Proposal</h1>
          <p className="text-sm text-mute">
            Applying for: <strong className="text-ink">Compose Multiplatform Desktop App for Ktor Analytics</strong>
          </p>
        </div>

        <form onSubmit={handleSubmit} className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-8">
          
          {/* Bid terms */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
                Total Bid Amount ($ USD)
              </label>
              <div className="relative">
                <span className="absolute left-4 top-3 text-mute font-bold text-sm">$</span>
                <input
                  type="number"
                  required
                  value={bidAmount}
                  onChange={(e) => setBidAmount(e.target.value)}
                  className="w-full pl-8 pr-4 py-3 rounded-full border border-hairline bg-surface-elevated text-sm font-bold text-ink focus:outline-none focus:border-brand-green transition-colors"
                />
              </div>
              <p className="text-xs text-mute mt-1.5">Zero platform deduction on Zapmancer ($0 fee).</p>
            </div>

            <div>
              <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
                Estimated Delivery (Days)
              </label>
              <input
                type="number"
                required
                value={deliveryDays}
                onChange={(e) => setDeliveryDays(e.target.value)}
                className="w-full px-4 py-3 rounded-full border border-hairline bg-surface-elevated text-sm font-bold text-ink focus:outline-none focus:border-brand-green transition-colors"
              />
            </div>
          </div>

          {/* Milestone Breakdown */}
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <h3 className="text-lg font-bold text-ink">Escrow Milestone Breakdown</h3>
              <button
                type="button"
                onClick={addMilestone}
                className="flex items-center gap-1 text-xs font-bold text-brand-green uppercase tracking-[0.05em] hover:text-brand-green-hover transition-colors"
              >
                <Plus className="w-4 h-4" /> Add Milestone
              </button>
            </div>

            <div className="space-y-3">
              {milestones.map((m, idx) => (
                <div key={idx} className="flex items-center gap-3 p-3 rounded-xl bg-surface-elevated border border-hairline">
                  <span className="text-xs font-bold text-brand-green">#{idx + 1}</span>
                  <input
                    type="text"
                    value={m.title}
                    onChange={(e) => {
                      const updated = [...milestones];
                      updated[idx].title = e.target.value;
                      setMilestones(updated);
                    }}
                    className="flex-1 px-4 py-2 rounded-full border border-hairline bg-surface text-sm font-medium text-ink focus:outline-none focus:border-brand-green"
                  />
                  <div className="relative w-32">
                    <span className="absolute left-3 top-2.5 text-xs text-mute font-bold">$</span>
                    <input
                      type="number"
                      value={m.amount}
                      onChange={(e) => {
                        const updated = [...milestones];
                        updated[idx].amount = e.target.value;
                        setMilestones(updated);
                      }}
                      className="w-full pl-6 pr-3 py-2 rounded-full border border-hairline bg-surface text-sm font-bold text-ink focus:outline-none focus:border-brand-green"
                    />
                  </div>
                  {milestones.length > 1 && (
                    <button
                      type="button"
                      onClick={() => removeMilestone(idx)}
                      className="text-mute hover:text-red-500 p-1 transition-colors"
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
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
              Cover Letter & Proposal Pitch
            </label>
            <textarea
              rows={6}
              required
              value={coverLetter}
              onChange={(e) => setCoverLetter(e.target.value)}
              className="w-full px-4 py-3 rounded-xl border border-hairline bg-surface-elevated text-sm text-ink leading-relaxed focus:outline-none focus:border-brand-green transition-colors"
            />
          </div>

          {/* Action Button & Escrow Note */}
          <div className="pt-4 border-t border-hairline flex flex-col sm:flex-row sm:items-center justify-between gap-4">
            <div className="flex items-center gap-2 text-xs text-brand-green font-bold">
              <ShieldCheck className="w-4 h-4" /> Milestone Escrow Protection active upon hire
            </div>

            <button
              type="submit"
              className="flex items-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white font-bold text-xs uppercase tracking-[0.05em] px-8 py-3.5 rounded-full hover:scale-[1.04] transition-all shadow-md shadow-brand-green/20"
            >
              Submit Proposal <Send className="w-4 h-4" />
            </button>
          </div>

        </form>

      </main>

      <Footer />
    </div>
  );
};
