import React, { useState } from 'react';
import './proposals.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, Star, GitPullRequest, ExternalLink, Scale, CheckCircle2, X, Check, ArrowRight, GitBranch, CheckSquare } from 'lucide-react';

type TabId = 'milestones' | 'proposals';

interface Milestone {
  id: string;
  projectTitle: string;
  developerName: string;
  developerAvatar: string;
  developerRole: string;
  milestoneBudget: string;
  prUrl: string;
  commitHash: string;
  demoUrl: string;
  testSuiteStatus: string;
  coverage: string;
  submittedDate: string;
  status: 'Pending Client Approval' | 'Escrow Released & IP Transferred';
}

const MILESTONES: Milestone[] = [
  {
    id: 'm-102',
    projectTitle: 'Task #102 · Compose Wasm WebAudio Processing Engine',
    developerName: 'Elena Rostova',
    developerAvatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
    developerRole: 'KMP & Wasm Lead',
    milestoneBudget: '$4,500.00',
    prUrl: 'https://github.com/acme-ai/kmp-core/pull/42',
    commitHash: 'b8f9a2e',
    demoUrl: 'https://demo.acme.ai/wasm-preview',
    testSuiteStatus: '14/14 unit tests passing',
    coverage: '94% coverage',
    submittedDate: '2 hours ago',
    status: 'Pending Client Approval',
  },
  {
    id: 'm-108',
    projectTitle: 'Task #108 · High-Concurrency Ktor WebSockets Clustering',
    developerName: 'Marcus Vance',
    developerAvatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
    developerRole: 'Backend Architect',
    milestoneBudget: '$3,200.00',
    prUrl: 'https://github.com/acme-ai/ktor-server/pull/84',
    commitHash: 'f4a19c3',
    demoUrl: 'https://demo.acme.ai/ktor-ws',
    testSuiteStatus: '28/28 integration tests passing',
    coverage: '98% coverage',
    submittedDate: '1 day ago',
    status: 'Pending Client Approval',
  },
];

const PROPOSALS = [
  {
    id: 'pr1',
    name: 'Elena Rostova',
    title: 'Senior KMP & WebAssembly Lead',
    avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
    bid: '$3,200',
    rating: 5.0,
    reviews: 42,
    pitch: 'I have architected over 15 Compose Multiplatform desktop and mobile clients connected to Ktor backends. Ready to deliver milestone 1 within 5 days with full test coverage.',
    skills: ['Kotlin', 'Wasm', 'Ktor', 'Compose'],
  },
];

export const ClientProposalsPage: React.FC = () => {
  const [tab, setTab] = useState<TabId>('milestones');
  const [modalMilestone, setModalMilestone] = useState<Milestone | null>(null);
  const [success, setSuccess] = useState(false);

  const confirmRelease = () => {
    setSuccess(true);
    setTimeout(() => {
      if (modalMilestone) modalMilestone.status = 'Escrow Released & IP Transferred';
      setSuccess(false);
      setModalMilestone(null);
    }, 2000);
  };

  const TABS = [
    { id: 'milestones' as TabId, label: `Milestone Escrows (${MILESTONES.length})` },
    { id: 'proposals' as TabId, label: `Proposals & Bids (${PROPOSALS.length})` },
  ];

  return (
    <div className="proposals-page">
      <Header isLoggedIn={true} />

      <main className="proposals-main">

        {/* Header */}
        <div className="proposals-header">
          <h1 className="proposals-page-title">Escrow & Proposals Hub</h1>
          <p className="proposals-page-sub">Review submitted deliverables, approve milestones, and release escrow payments.</p>
        </div>

        {/* Tabs */}
        <div className="proposals-tabs">
          {TABS.map((t) => (
            <button key={t.id} onClick={() => setTab(t.id)} className={`proposals-tab${tab === t.id ? ' active' : ''}`}>
              {t.label}
            </button>
          ))}
        </div>

        {/* ---- Milestones tab ---- */}
        {tab === 'milestones' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>

            {/* Info banner */}
            <div className="proposals-info-banner">
              <div>
                <p className="proposals-info-banner-label"><GitBranch size={12} style={{ display: 'inline', marginRight: 4 }} />GitHub CI/CD sync active</p>
                <p className="proposals-info-banner-title">0% developer fee · Milestone escrow protection</p>
                <p className="proposals-info-banner-sub">Developers receive 100% of the budget on sign-off. Clients pay a 3% deposit fee at escrow funding.</p>
              </div>
              <p className="proposals-info-banner-stat">$0 dev cut</p>
            </div>

            {/* List */}
            <div className="proposals-list">
              {MILESTONES.map((m) => (
                <div key={m.id} className="proposals-card">
                  {/* Top */}
                  <div className="proposals-card-top">
                    <div className="proposals-card-identity">
                      <img src={m.developerAvatar} alt={m.developerName} className="proposals-avatar" />
                      <div>
                        <p className="proposals-name">
                          {m.developerName}
                          <span className="proposals-verified"><ShieldCheck size={11} /> KYC</span>
                        </p>
                        <p className="proposals-role">{m.developerRole}</p>
                        <p className="proposals-project-ref">{m.projectTitle}</p>
                      </div>
                    </div>
                    <div className="proposals-card-amount">
                      <p className="proposals-budget">{m.milestoneBudget}</p>
                      <span className={`proposals-status-badge ${m.status.includes('Released') ? 'proposals-status-released' : 'proposals-status-pending'}`}>
                        {m.status}
                      </span>
                    </div>
                  </div>

                  {/* Deliverables */}
                  <div className="proposals-deliverables">
                    <div>
                      <p className="proposals-deliverable-label">GitHub Pull Request</p>
                      <a href={m.prUrl} target="_blank" rel="noopener noreferrer" className="proposals-deliverable-link">
                        <GitPullRequest size={12} /> PR #{m.id.split('-')[1]} · {m.commitHash} <ExternalLink size={11} />
                      </a>
                    </div>
                    <div>
                      <p className="proposals-deliverable-label">Automated Test Suite</p>
                      <p className="proposals-deliverable-value"><CheckSquare size={12} style={{ color: '#1aae39' }} /> {m.testSuiteStatus}</p>
                    </div>
                    <div>
                      <p className="proposals-deliverable-label">Coverage & Preview</p>
                      <a href={m.demoUrl} target="_blank" rel="noopener noreferrer" className="proposals-deliverable-link">
                        {m.coverage} · Build artifact <ExternalLink size={11} />
                      </a>
                    </div>
                  </div>

                  {/* Footer */}
                  <div className="proposals-card-footer">
                    <p className="proposals-footer-meta">Submitted <strong style={{ color: 'var(--color-ink)' }}>{m.submittedDate}</strong></p>
                    {m.status.includes('Released') ? (
                      <span className="proposals-released-badge"><CheckCircle2 size={14} /> Escrow released & IP transferred</span>
                    ) : (
                      <button onClick={() => setModalMilestone(m)} className="proposals-btn-primary">
                        <Scale size={14} /> Approve & release escrow
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* ---- Proposals tab ---- */}
        {tab === 'proposals' && (
          <div className="proposals-list">
            {PROPOSALS.map((p) => (
              <div key={p.id} className="proposals-card">
                <div className="proposals-card-top">
                  <div className="proposals-card-identity">
                    <img src={p.avatar} alt={p.name} className="proposals-avatar" />
                    <div>
                      <p className="proposals-name">
                        {p.name}
                        <span className="proposals-verified"><ShieldCheck size={11} /> KYC</span>
                      </p>
                      <p className="proposals-role">{p.title}</p>
                      <p style={{ fontSize: 12, color: 'var(--color-steel)', marginTop: 4, display: 'flex', alignItems: 'center', gap: 4 }}>
                        <Star size={11} fill="#f59e0b" style={{ color: '#f59e0b' }} /> {p.rating} ({p.reviews} reviews)
                      </p>
                    </div>
                  </div>
                  <div className="proposals-card-amount">
                    <p className="proposals-budget">{p.bid}</p>
                    <p style={{ fontSize: 11, color: 'var(--color-steel)', marginTop: 2 }}>Fixed bid</p>
                  </div>
                </div>

                <blockquote className="proposals-pitch">"{p.pitch}"</blockquote>

                <div className="proposals-card-footer">
                  <div className="proposals-skills">
                    {p.skills.map((s) => <span key={s} className="proposals-skill-tag">{s}</span>)}
                  </div>
                  <Link to="/messages" className="proposals-btn-primary">
                    Hire & award contract <ArrowRight size={13} />
                  </Link>
                </div>
              </div>
            ))}
          </div>
        )}

      </main>

      {/* ---- Modal ---- */}
      {modalMilestone && (
        <div className="proposals-modal-overlay" onClick={() => setModalMilestone(null)}>
          <div className="proposals-modal-content" onClick={(e) => e.stopPropagation()}>
            <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 12 }}>
              <div>
                <p style={{ fontSize: 12, fontWeight: 600, color: 'var(--color-primary)', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: 4 }}>
                  <Scale size={12} style={{ display: 'inline', marginRight: 4 }} />Work-for-Hire Agreement
                </p>
                <h3 className="proposals-modal-title">Automated IP Transfer & Escrow Release</h3>
              </div>
              <button onClick={() => setModalMilestone(null)} style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--color-steel)', padding: 4 }}>
                <X size={18} />
              </button>
            </div>

            {/* Contract doc */}
            <div className="proposals-contract-doc">
              <div style={{ borderBottom: '1px solid var(--color-hairline)', paddingBottom: 8, marginBottom: 12, fontFamily: 'var(--font-sans)', display: 'flex', justifyContent: 'space-between' }}>
                <strong>CONTRACT ID: IP-TRANSFER-2026-88192</strong>
                <span style={{ color: '#1aae39' }}>STATUS: EXECUTABLE</span>
              </div>
              <p><strong>1. PARTIES & ENGAGEMENT</strong></p>
              <p>This Work-for-Hire Agreement is executed between <strong>Acme AI Systems (Client)</strong> and <strong>{modalMilestone.developerName} (Developer)</strong> upon release of escrow funds of <strong>{modalMilestone.milestoneBudget}</strong>.</p>
              <br />
              <p><strong>2. INTELLECTUAL PROPERTY ASSIGNMENT</strong></p>
              <p>Developer irrevocably assigns to Client 100% ownership of all deliverables, source code ({modalMilestone.commitHash}), and derivative works submitted via {modalMilestone.prUrl}.</p>
              <br />
              <p><strong>3. 0% DEVELOPER COMMISSION GUARANTEE</strong></p>
              <p>Zapmancer certifies that 100% of {modalMilestone.milestoneBudget} transfers directly to Developer with $0 platform deductions.</p>
            </div>

            {/* Stamp */}
            <div className="proposals-contract-stamp">
              <p style={{ fontWeight: 700, color: 'var(--color-primary)', fontFamily: 'var(--font-sans)', fontSize: 11, textTransform: 'uppercase', letterSpacing: '0.05em' }}>Digital Transfer Receipt</p>
              <p><strong>Client:</strong> Acme AI Systems (Delaware C-Corp)</p>
              <p><strong>Developer:</strong> {modalMilestone.developerName}</p>
              <p style={{ color: 'var(--color-steel)' }}><strong>Timestamp:</strong> {new Date().toUTCString()}</p>
            </div>

            {success && (
              <div className="proposals-success-banner">
                <Check size={16} /> Legal IP transfer executed & funds released!
              </div>
            )}

            {/* Actions */}
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end', gap: 10, paddingTop: 4, borderTop: '1px solid var(--color-hairline)' }}>
              <button onClick={() => setModalMilestone(null)} className="proposals-btn-secondary">Cancel</button>
              <button onClick={confirmRelease} className="proposals-btn-primary">
                <CheckCircle2 size={14} /> Sign & release escrow
              </button>
            </div>
          </div>
        </div>
      )}

      <Footer />
    </div>
  );
};
