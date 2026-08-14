import React, { useState } from 'react';
import './proposals.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, Star, Sparkles, GitPullRequest, ExternalLink, Scale, CheckCircle2, Lock, FileText, X, Check, ArrowRight, DollarSign, GitBranch, CheckSquare, Terminal } from 'lucide-react';
import { Tabs, TabItem } from '../../components/ui/Tabs';

type ClientHubTabId = 'milestones' | 'proposals';

interface SubmittedMilestone {
  id: string;
  projectTitle: string;
  developerName: string;
  developerAvatar: string;
  developerRole: string;
  milestoneBudget: string;
  prUrl: string;
  commitHash: string;
  demoUrl: string;
  repoSync: string;
  testSuiteStatus: string;
  coverage: string;
  submittedDate: string;
  status: 'Pending Client Approval' | 'Escrow Released & IP Transferred';
}

export const ClientProposalsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<ClientHubTabId>('milestones');
  const [showLegalContractModal, setShowLegalContractModal] = useState<boolean>(false);
  const [selectedMilestone, setSelectedMilestone] = useState<SubmittedMilestone | null>(null);
  const [contractSignedSuccess, setContractSignedSuccess] = useState<boolean>(false);

  const hubTabs: TabItem<ClientHubTabId>[] = [
    { id: 'milestones', label: 'Milestone Escrows Under Review (2)' },
    { id: 'proposals', label: 'Contract Proposals & Bids (8)' },
  ];

  const milestonesUnderReview: SubmittedMilestone[] = [
    {
      id: 'm-102',
      projectTitle: 'Task #102: Compose Wasm WebAudio Processing Engine',
      developerName: 'Elena Rostova',
      developerAvatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
      developerRole: 'KMP & Wasm Lead',
      milestoneBudget: '$4,500.00',
      prUrl: 'https://github.com/acme-ai/kmp-core/pull/42',
      commitHash: 'b8f9a2e',
      demoUrl: 'https://demo.acme.ai/wasm-preview',
      repoSync: 'github.com/acme-ai/kmp-core',
      testSuiteStatus: '14/14 Unit Tests Passing',
      coverage: '94% Coverage',
      submittedDate: '2 hours ago',
      status: 'Pending Client Approval'
    },
    {
      id: 'm-108',
      projectTitle: 'Task #108: High-Concurrency Ktor WebSockets Clustering',
      developerName: 'Marcus Vance',
      developerAvatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
      developerRole: 'Backend Architect',
      milestoneBudget: '$3,200.00',
      prUrl: 'https://github.com/acme-ai/ktor-server/pull/84',
      commitHash: 'f4a19c3',
      demoUrl: 'https://demo.acme.ai/ktor-ws',
      repoSync: 'github.com/acme-ai/ktor-server',
      testSuiteStatus: '28/28 Integration Tests Passing',
      coverage: '98% Coverage',
      submittedDate: '1 day ago',
      status: 'Pending Client Approval'
    }
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

  const handleOpenApprovalModal = (m: SubmittedMilestone) => {
    setSelectedMilestone(m);
    setShowLegalContractModal(true);
  };

  const handleConfirmEscrowRelease = () => {
    setContractSignedSuccess(true);
    setTimeout(() => {
      if (selectedMilestone) {
        selectedMilestone.status = 'Escrow Released & IP Transferred';
      }
      setContractSignedSuccess(false);
      setShowLegalContractModal(false);
    }, 2000);
  };

  return (
    <div className="proposals-page">
      <Header isLoggedIn={true} />

      <main className="proposals-main">
        
        {/* Page Header */}
        <div className="space-y-1 border-b border-hairline pb-6">
          <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
            <Sparkles className="w-3.5 h-3.5" /> Company Escrow & Proposal Hub
          </div>
          <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight text-ink">Contract Milestone Escrow Hub</h1>
          <p className="text-sm text-mute">Review code PR deliverables, GitHub automated test passes, and execute legal Work-for-Hire IP transfers.</p>
        </div>

        {/* Tab Navigation */}
        <div>
          <Tabs
            tabs={hubTabs}
            activeTab={activeTab}
            onChange={(id) => setActiveTab(id)}
          />
        </div>

        {/* TAB 1: MILESTONES UNDER REVIEW */}
        {activeTab === 'milestones' && (
          <div className="space-y-6">
            
            {/* Fee Transparency & Repo Sync Banner */}
            <div className="bg-surface-elevated p-6 rounded-xl border border-hairline flex flex-col sm:flex-row sm:items-center justify-between gap-4">
              <div className="space-y-1">
                <div className="flex items-center gap-2">
                  <span className="text-[10px] font-bold text-brand-green uppercase tracking-wider">Fee Transparency Guarantee</span>
                  <span className="text-mute">&bull;</span>
                  <span className="text-[10px] font-bold text-brand-green flex items-center gap-1">
                    <GitBranch className="w-3 h-3 text-brand-green" /> GitHub CI/CD Sync Active
                  </span>
                </div>
                <h3 className="font-extrabold text-ink text-base">0% Developer Fee Escrow Protection</h3>
                <p className="text-xs text-mute leading-relaxed">
                  Developers receive 100% of the milestone budget upon sign-off. Clients pay a 3% deposit operational fee at escrow funding time.
                </p>
              </div>
              <span className="text-2xl font-extrabold text-brand-green font-mono shrink-0">$0 Dev Cut</span>
            </div>

            {/* Milestones Card List */}
            <div className="space-y-4">
              {milestonesUnderReview.map((m) => (
                <div key={m.id} className="proposals-card">
                  <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 border-b border-hairline pb-4">
                    <div className="flex items-center gap-4">
                      <img src={m.developerAvatar} alt={m.developerName} className="w-12 h-12 rounded-full object-cover border border-hairline shrink-0" />
                      <div>
                        <h3 className="font-extrabold text-ink text-lg flex items-center gap-1.5">
                          {m.developerName} <ShieldCheck className="w-4 h-4 text-brand-green" />
                        </h3>
                        <p className="text-xs text-mute font-medium">{m.developerRole}</p>
                        <p className="text-xs font-bold text-brand-green pt-0.5">{m.projectTitle}</p>
                      </div>
                    </div>

                    <div className="text-left md:text-right shrink-0">
                      <p className="text-2xl font-extrabold text-brand-green font-mono">{m.milestoneBudget}</p>
                      <span className={`text-[10px] font-bold uppercase px-2.5 py-0.5 rounded-full ${
                        m.status.includes('Released') ? 'bg-brand-green/10 text-brand-green border border-brand-green/20' : 'bg-yellow-500/10 text-yellow-500 border border-yellow-500/20'
                      }`}>
                        {m.status}
                      </span>
                    </div>
                  </div>

                  {/* PR & Automated Test Verification Details */}
                  <div className="p-4 rounded-xl bg-surface-elevated border border-hairline grid grid-cols-1 sm:grid-cols-3 gap-4 text-xs">
                    <div>
                      <span className="text-mute font-bold uppercase block text-[10px]">GitHub Pull Request & Repo</span>
                      <a href={m.prUrl} target="_blank" rel="noopener noreferrer" className="font-mono text-brand-green hover:underline font-bold flex items-center gap-1 mt-0.5">
                        <GitPullRequest className="w-3.5 h-3.5" /> PR #42 ({m.commitHash}) <ExternalLink className="w-3 h-3" />
                      </a>
                    </div>

                    <div>
                      <span className="text-mute font-bold uppercase block text-[10px]">Automated Test Suite</span>
                      <span className="font-mono font-bold text-brand-green flex items-center gap-1 mt-0.5">
                        <CheckSquare className="w-3.5 h-3.5" /> {m.testSuiteStatus}
                      </span>
                    </div>

                    <div>
                      <span className="text-mute font-bold uppercase block text-[10px]">Coverage & Live Preview</span>
                      <a href={m.demoUrl} target="_blank" rel="noopener noreferrer" className="font-mono text-brand-green hover:underline font-bold flex items-center gap-1 mt-0.5">
                        {m.coverage} &bull; Build Artifact <ExternalLink className="w-3 h-3" />
                      </a>
                    </div>
                  </div>

                  {/* Card Actions */}
                  <div className="flex items-center justify-between gap-4 pt-2">
                    <p className="text-xs text-mute">Submitted <strong className="text-ink">{m.submittedDate}</strong></p>

                    {m.status.includes('Released') ? (
                      <span className="text-xs font-bold text-brand-green flex items-center gap-1.5 bg-brand-green/10 px-4 py-2 rounded-full border border-brand-green/20">
                        <CheckCircle2 className="w-4 h-4" /> Escrow Released & IP Transferred
                      </span>
                    ) : (
                      <button
                        onClick={() => handleOpenApprovalModal(m)}
                        className="bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] px-6 py-2.5 rounded-full shadow-md shadow-brand-green/20 hover:scale-[1.03] transition-all flex items-center gap-2"
                      >
                        <Scale className="w-4 h-4" /> Approve & Release Escrow
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>

          </div>
        )}

        {/* TAB 2: PROPOSALS & BIDS */}
        {activeTab === 'proposals' && (
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
        )}

      </main>

      {/* AUTOMATED WORK-FOR-HIRE LEGAL CONTRACT & ESCROW RELEASE MODAL */}
      {showLegalContractModal && selectedMilestone && (
        <div className="proposals-modal-overlay">
          <div className="proposals-modal-content">
            
            {/* Modal Header */}
            <div className="flex items-center justify-between border-b border-hairline pb-4">
              <div className="space-y-0.5">
                <div className="flex items-center gap-1.5 text-xs font-bold text-brand-green uppercase tracking-wider">
                  <Scale className="w-4 h-4" /> Legal Work-for-Hire Agreement
                </div>
                <h3 className="text-xl font-extrabold text-ink">Automated IP Transfer & Escrow Release</h3>
              </div>
              <button onClick={() => setShowLegalContractModal(false)} className="p-2 rounded-full text-mute hover:text-ink">
                <X className="w-5 h-5" />
              </button>
            </div>

            {/* Legal Contract Document Box */}
            <div className="space-y-4 text-xs text-mute leading-relaxed bg-surface-elevated p-5 rounded-xl border border-hairline max-h-[300px] overflow-y-auto font-mono">
              <div className="flex items-center justify-between border-b border-hairline pb-2 font-sans font-bold text-ink">
                <span>CONTRACT AGREEMENT ID: IP-TRANSFER-2026-88192</span>
                <span className="text-brand-green">STATUS: EXECUTABLE</span>
              </div>

              <p className="text-ink font-bold">1. PARTIES & ENGAGEMENT</p>
              <p>
                This Work-for-Hire Assignment Agreement is executed by and between <strong>Acme AI Systems (Client)</strong> and <strong>{selectedMilestone.developerName} (Developer)</strong> upon release of escrow funds in the amount of <strong>{selectedMilestone.milestoneBudget}</strong>.
              </p>

              <p className="text-ink font-bold">2. INTELLECTUAL PROPERTY & COPYRIGHT ASSIGNMENT</p>
              <p>
                Developer hereby irrevocably assigns, transfers, and conveys to Client 100% exclusive ownership of all right, title, and interest in and to the software deliverables, source code commits ({selectedMilestone.commitHash}), Pull Request deliverables ({selectedMilestone.prUrl}), patents, and derivative works.
              </p>

              <p className="text-ink font-bold">3. 0% DEVELOPER COMMISSION GUARANTEE</p>
              <p>
                Zapmancer certifies that 100% of the milestone funds ({selectedMilestone.milestoneBudget}) will be transferred directly to Developer with $0 platform commission deductions.
              </p>
            </div>

            {/* Stamp Receipt Box */}
            <div className="proposals-contract-stamp">
              <p className="font-bold uppercase tracking-wider text-brand-green">Digital Transfer Signature Receipt</p>
              <p className="text-ink">Client Legal Entity: <strong>Acme AI Systems (Delaware C-Corp)</strong></p>
              <p className="text-ink">Developer Recipient: <strong>{selectedMilestone.developerName}</strong></p>
              <p className="text-mute">Timestamp: <strong>{new Date().toUTCString()}</strong></p>
            </div>

            {/* Modal Actions */}
            <div className="flex items-center justify-end gap-3 pt-2 border-t border-hairline">
              <button
                type="button"
                onClick={() => setShowLegalContractModal(false)}
                className="px-5 py-2.5 rounded-full bg-surface-elevated hover:bg-surface-modal text-ink font-bold text-xs uppercase"
              >
                Cancel
              </button>
              <button
                type="button"
                onClick={handleConfirmEscrowRelease}
                className="inline-flex items-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] px-6 py-2.5 rounded-full hover:scale-[1.03] transition-all shadow-md shadow-brand-green/20"
              >
                <CheckCircle2 className="w-4 h-4" /> Sign Contract & Release Escrow Now
              </button>
            </div>

            {contractSignedSuccess && (
              <div className="p-3 rounded-xl bg-brand-green/10 border border-brand-green/30 text-brand-green text-xs font-bold text-center flex items-center justify-center gap-2">
                <Check className="w-4 h-4" /> Legal IP Transfer Executed & Funds Released!
              </div>
            )}

          </div>
        </div>
      )}

      <Footer />
    </div>
  );
};
