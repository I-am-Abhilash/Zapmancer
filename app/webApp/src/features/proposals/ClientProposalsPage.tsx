import React, { useState, useEffect } from 'react';
import './proposals.css';
import {
  ShieldCheck,
  Star,
  GitPullRequest,
  ExternalLink,
  Scale,
  CheckCircle2,
  X,
  Check,
  ArrowRight,
  GitBranch,
  CheckSquare,
  Loader2,
  ThumbsDown,
} from 'lucide-react';
import {
  proposalService,
  MilestoneDeliverable,
  ProposalItem,
} from '../../services/proposalService';

type TabId = 'milestones' | 'proposals';

export const ClientProposalsPage: React.FC = () => {
  const [tab, setTab] = useState<TabId>('milestones');
  const [milestones, setMilestones] = useState<MilestoneDeliverable[]>([]);
  const [proposals, setProposals] = useState<ProposalItem[]>([]);
  const [loading, setLoading] = useState(true);

  const [modalMilestone, setModalMilestone] = useState<MilestoneDeliverable | null>(null);
  const [success, setSuccess] = useState(false);
  const [actionToast, setActionToast] = useState<string | null>(null);

  useEffect(() => {
    let isMounted = true;
    setLoading(true);
    Promise.all([
      proposalService.getPendingMilestones(),
      proposalService.getClientProposals(),
    ])
      .then(([mils, props]) => {
        if (isMounted) {
          setMilestones(mils);
          setProposals(props);
          setLoading(false);
        }
      })
      .catch(() => {
        if (isMounted) setLoading(false);
      });

    return () => {
      isMounted = false;
    };
  }, []);

  const confirmRelease = async () => {
    if (!modalMilestone) return;
    setSuccess(true);
    await proposalService.approveMilestone(modalMilestone.id);
    setTimeout(() => {
      setMilestones((prev) =>
        prev.map((m) =>
          m.id === modalMilestone.id
            ? { ...m, status: 'Escrow Released & IP Transferred' as const }
            : m
        )
      );
      setSuccess(false);
      setModalMilestone(null);
      showToast('Milestone approved! Escrow payment released and IP transferred.');
    }, 1500);
  };

  const handleAcceptProposal = async (id: string) => {
    await proposalService.acceptProposal(id);
    setProposals((prev) =>
      prev.map((p) => (p.id === id ? { ...p, status: 'ACCEPTED' as const } : p))
    );
    showToast('Proposal accepted! Contract generated and escrow funding initialized.');
  };

  const handleRejectProposal = async (id: string) => {
    await proposalService.rejectProposal(id);
    setProposals((prev) =>
      prev.map((p) => (p.id === id ? { ...p, status: 'REJECTED' as const } : p))
    );
    showToast('Proposal declined.');
  };

  const showToast = (msg: string) => {
    setActionToast(msg);
    setTimeout(() => setActionToast(null), 3000);
  };

  const TABS = [
    { id: 'milestones' as TabId, label: `Milestone Escrows (${milestones.length})` },
    { id: 'proposals' as TabId, label: `Proposals & Bids (${proposals.length})` },
  ];

  return (
    <div className="proposals-page">
      <main className="proposals-main">
        {/* Header */}
        <div className="proposals-header">
          <h1 className="proposals-page-title">Escrow & Proposals Hub</h1>
          <p className="proposals-page-sub">
            Review submitted deliverables, approve milestones, and release escrow payments.
          </p>
        </div>

        {/* Action Toast */}
        {actionToast && (
          <div className="p-4 rounded-xl bg-primary/10 border border-primary/20 text-primary text-xs font-semibold flex items-center gap-2">
            <CheckCircle2 size={16} /> {actionToast}
          </div>
        )}

        {/* Tabs */}
        <div className="proposals-tabs">
          {TABS.map((t) => (
            <button
              key={t.id}
              onClick={() => setTab(t.id)}
              className={`proposals-tab${tab === t.id ? ' active' : ''}`}
            >
              {t.label}
            </button>
          ))}
        </div>

        {loading ? (
          <div className="min-h-[300px] flex flex-col items-center justify-center gap-3 bg-surface-elevated rounded-xl border border-hairline p-12">
            <Loader2 className="w-6 h-6 text-primary animate-spin" />
            <p className="text-xs font-semibold text-mute">Loading active contracts & proposals...</p>
          </div>
        ) : tab === 'milestones' ? (
          /* ---- Milestones tab ---- */
          <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
            {/* Info banner */}
            <div className="proposals-info-banner">
              <div>
                <p className="proposals-info-banner-label">
                  <GitBranch size={12} style={{ display: 'inline', marginRight: 4 }} />
                  GitHub CI/CD sync active
                </p>
                <p className="proposals-info-banner-title">
                  0% developer fee · Milestone escrow protection
                </p>
                <p className="proposals-info-banner-sub">
                  Developers receive 100% of the budget on sign-off. Clients pay a 3% deposit fee at escrow funding.
                </p>
              </div>
              <p className="proposals-info-banner-stat">$0 dev cut</p>
            </div>

            {/* List */}
            <div className="proposals-list">
              {milestones.length === 0 ? (
                <div className="p-8 text-center text-sm text-mute bg-surface-elevated rounded-xl border border-hairline">
                  No active milestone deliverables pending review.
                </div>
              ) : (
                milestones.map((m) => (
                  <div key={m.id} className="proposals-card">
                    {/* Top */}
                    <div className="proposals-card-top">
                      <div className="proposals-card-identity">
                        <img
                          src={m.developerAvatar}
                          alt={m.developerName}
                          className="proposals-avatar"
                        />
                        <div>
                          <p className="proposals-name">
                            {m.developerName}
                            <span className="proposals-verified">
                              <ShieldCheck size={11} /> KYC
                            </span>
                          </p>
                          <p className="proposals-role">{m.developerRole}</p>
                          <p className="proposals-project-title">{m.projectTitle}</p>
                        </div>
                      </div>
                      <div className="proposals-card-amount">
                        <p className="proposals-budget">{m.milestoneBudget}</p>
                        <span
                          className={`proposals-status-badge ${
                            m.status === 'Escrow Released & IP Transferred'
                              ? 'released'
                              : 'pending'
                          }`}
                        >
                          {m.status}
                        </span>
                      </div>
                    </div>

                    {/* CI telemetry */}
                    <div className="proposals-telemetry">
                      <a
                        href={m.prUrl}
                        target="_blank"
                        rel="noreferrer"
                        className="proposals-pr-link"
                      >
                        <GitPullRequest size={13} /> {m.commitHash} <ExternalLink size={11} />
                      </a>
                      <span className="proposals-coverage">
                        <CheckSquare size={12} /> {m.testSuiteStatus} ({m.coverage})
                      </span>
                      <a
                        href={m.demoUrl}
                        target="_blank"
                        rel="noreferrer"
                        className="proposals-demo-link"
                      >
                        Live preview <ExternalLink size={11} />
                      </a>
                    </div>

                    {/* Footer */}
                    <div className="proposals-card-footer">
                      <p className="proposals-time">Submitted {m.submittedDate}</p>
                      {m.status === 'Pending Client Approval' ? (
                        <button
                          onClick={() => setModalMilestone(m)}
                          className="proposals-btn-primary"
                        >
                          <Scale size={13} /> Review & release escrow
                        </button>
                      ) : (
                        <span
                          style={{
                            fontSize: 13,
                            fontWeight: 600,
                            color: '#1aae39',
                            display: 'inline-flex',
                            alignItems: 'center',
                            gap: 6,
                          }}
                        >
                          <CheckCircle2 size={15} /> IP Transferred · Escrow Settled
                        </span>
                      )}
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>
        ) : (
          /* ---- Proposals tab ---- */
          <div className="proposals-list">
            {proposals.length === 0 ? (
              <div className="p-8 text-center text-sm text-mute bg-surface-elevated rounded-xl border border-hairline">
                No incoming candidate proposals for your open projects.
              </div>
            ) : (
              proposals.map((p) => (
                <div key={p.id} className="proposals-card">
                  <div className="proposals-card-top">
                    <div className="proposals-card-identity">
                      <img src={p.avatar} alt={p.name} className="proposals-avatar" />
                      <div>
                        <p className="proposals-name">
                          {p.name}
                          <span className="proposals-verified">
                            <ShieldCheck size={11} /> KYC
                          </span>
                        </p>
                        <p className="proposals-role">{p.title}</p>
                        <p
                          style={{
                            fontSize: 12,
                            color: 'var(--color-steel)',
                            marginTop: 4,
                            display: 'flex',
                            alignItems: 'center',
                            gap: 4,
                          }}
                        >
                          <Star size={11} fill="#f59e0b" style={{ color: '#f59e0b' }} /> {p.rating}{' '}
                          ({p.reviews} reviews)
                        </p>
                      </div>
                    </div>
                    <div className="proposals-card-amount">
                      <p className="proposals-budget">{p.bid}</p>
                      <p style={{ fontSize: 11, color: 'var(--color-steel)', marginTop: 2 }}>
                        Fixed bid
                      </p>
                    </div>
                  </div>

                  <blockquote className="proposals-pitch">"{p.pitch}"</blockquote>

                  <div className="proposals-card-footer">
                    <div className="proposals-skills">
                      {p.skills.map((s) => (
                        <span key={s} className="proposals-skill-tag">
                          {s}
                        </span>
                      ))}
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                      {p.status === 'ACCEPTED' ? (
                        <span className="text-xs font-bold text-emerald-500 flex items-center gap-1">
                          <CheckCircle2 size={14} /> Accepted & Contract Initialized
                        </span>
                      ) : p.status === 'REJECTED' ? (
                        <span className="text-xs font-bold text-mute">Declined</span>
                      ) : (
                        <>
                          <button
                            type="button"
                            onClick={() => handleRejectProposal(p.id)}
                            className="proposals-btn-secondary py-1.5 px-3 text-xs"
                          >
                            <ThumbsDown size={12} /> Decline
                          </button>
                          <button
                            type="button"
                            onClick={() => handleAcceptProposal(p.id)}
                            className="proposals-btn-primary"
                          >
                            Accept & Hire <ArrowRight size={13} />
                          </button>
                        </>
                      )}
                    </div>
                  </div>
                </div>
              ))
            )}
          </div>
        )}
      </main>

      {/* ---- Modal ---- */}
      {modalMilestone && (
        <div className="proposals-modal-overlay" onClick={() => setModalMilestone(null)}>
          <div className="proposals-modal-content" onClick={(e) => e.stopPropagation()}>
            <div
              style={{
                display: 'flex',
                alignItems: 'flex-start',
                justifyContent: 'space-between',
                gap: 12,
              }}
            >
              <div>
                <p
                  style={{
                    fontSize: 12,
                    fontWeight: 600,
                    color: 'var(--color-primary)',
                    textTransform: 'uppercase',
                    letterSpacing: '0.05em',
                    marginBottom: 4,
                  }}
                >
                  <Scale size={12} style={{ display: 'inline', marginRight: 4 }} />
                  Work-for-Hire Agreement
                </p>
                <h3 className="proposals-modal-title">Automated IP Transfer & Escrow Release</h3>
              </div>
              <button
                onClick={() => setModalMilestone(null)}
                style={{
                  background: 'none',
                  border: 'none',
                  cursor: 'pointer',
                  color: 'var(--color-steel)',
                  padding: 4,
                }}
              >
                <X size={18} />
              </button>
            </div>

            {/* Contract doc */}
            <div className="proposals-contract-doc">
              <div
                style={{
                  borderBottom: '1px solid var(--color-hairline)',
                  paddingBottom: 8,
                  marginBottom: 12,
                  fontFamily: 'var(--font-sans)',
                  display: 'flex',
                  justifyContent: 'space-between',
                }}
              >
                <strong>CONTRACT ID: IP-TRANSFER-2026-88192</strong>
                <span style={{ color: '#1aae39' }}>STATUS: EXECUTABLE</span>
              </div>
              <p>
                <strong>1. PARTIES & ENGAGEMENT</strong>
              </p>
              <p>
                This Work-for-Hire Agreement is executed between{' '}
                <strong>Acme AI Systems (Client)</strong> and{' '}
                <strong>{modalMilestone.developerName} (Developer)</strong> upon release of escrow
                funds of <strong>{modalMilestone.milestoneBudget}</strong>.
              </p>
              <br />
              <p>
                <strong>2. INTELLECTUAL PROPERTY ASSIGNMENT</strong>
              </p>
              <p>
                Developer irrevocably assigns to Client 100% ownership of all deliverables, source
                code ({modalMilestone.commitHash}), and derivative works submitted via{' '}
                {modalMilestone.prUrl}.
              </p>
              <br />
              <p>
                <strong>3. 0% DEVELOPER COMMISSION GUARANTEE</strong>
              </p>
              <p>
                Zapmancer certifies that 100% of {modalMilestone.milestoneBudget} transfers directly
                to Developer with $0 platform deductions.
              </p>
            </div>

            {/* Stamp */}
            <div className="proposals-contract-stamp">
              <p
                style={{
                  fontWeight: 700,
                  color: 'var(--color-primary)',
                  fontFamily: 'var(--font-sans)',
                  fontSize: 11,
                  textTransform: 'uppercase',
                  letterSpacing: '0.05em',
                }}
              >
                Digital Transfer Receipt
              </p>
              <p>
                <strong>Client:</strong> Acme AI Systems (Delaware C-Corp)
              </p>
              <p>
                <strong>Developer:</strong> {modalMilestone.developerName}
              </p>
              <p style={{ color: 'var(--color-steel)' }}>
                <strong>Timestamp:</strong> {new Date().toUTCString()}
              </p>
            </div>

            {success && (
              <div className="proposals-success-banner">
                <Check size={16} /> Legal IP transfer executed & funds released!
              </div>
            )}

            {/* Actions */}
            <div
              style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'flex-end',
                gap: 10,
                paddingTop: 4,
                borderTop: '1px solid var(--color-hairline)',
              }}
            >
              <button
                onClick={() => setModalMilestone(null)}
                className="proposals-btn-secondary"
              >
                Cancel
              </button>
              <button onClick={confirmRelease} className="proposals-btn-primary">
                <CheckCircle2 size={14} /> Sign & release escrow
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
