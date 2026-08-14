import React, { useState } from 'react';
import './home.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, Plus, ArrowRight, Clock, Award, Layers, Building2, CheckCircle2, DollarSign, Upload, GitPullRequest, ExternalLink, X, Check, Sparkles, AlertCircle, Lock, Wallet, Activity, Play, Pause } from 'lucide-react';

interface WorkspaceItem {
  id: string;
  name: string;
  role: string;
  type: 'Full-Time' | 'Contractor' | 'Independent';
  activeTasksCount: number;
}

interface AssignedTask {
  id: string;
  title: string;
  companyName: string;
  deadline: string;
  budget: string;
  status: 'In Progress' | 'PR Submitted' | 'Approved';
  prUrl?: string;
  loggedHours: number;
}

interface PayoutTransaction {
  id: string;
  date: string;
  description: string;
  amount: string;
  method: 'Stripe Direct Deposit' | 'Web3 USDC Wallet';
  status: 'Completed' | 'Escrow Locked';
}

export const HomePage: React.FC = () => {
  // Multi-Workspace state
  const workspaces: WorkspaceItem[] = [
    { id: 'w1', name: 'Acme AI Systems', role: 'KMP & Wasm Lead', type: 'Contractor', activeTasksCount: 2 },
    { id: 'w2', name: 'Fintech Core', role: 'Backend Specialist', type: 'Full-Time', activeTasksCount: 1 },
    { id: 'w3', name: 'Solo Freelance Hub', role: 'Independent Engineer', type: 'Independent', activeTasksCount: 3 },
  ];

  const [activeWorkspace, setActiveWorkspace] = useState<string>('w1');
  const [showSubmitPrModal, setShowSubmitPrModal] = useState<boolean>(false);
  const [selectedTaskForPr, setSelectedTaskForPr] = useState<AssignedTask | null>(null);
  
  // Timer & Log State
  const [isTimerRunning, setIsTimerRunning] = useState<boolean>(false);
  const [timerSeconds, setTimerSeconds] = useState<number>(3600); // 1 hr active
  const [logSuccessMsg, setLogSuccessMsg] = useState<string>('');

  // Submit PR Form State
  const [prUrlInput, setPrUrlInput] = useState<string>('');
  const [commitHashInput, setCommitHashInput] = useState<string>('');
  const [demoUrlInput, setDemoUrlInput] = useState<string>('');
  const [submissionSuccess, setSubmissionSuccess] = useState<boolean>(false);

  const assignedTasks: AssignedTask[] = [
    {
      id: '102',
      title: 'Task #102: Compose Wasm WebAudio Processing Engine',
      companyName: 'Acme AI Systems',
      deadline: 'Aug 20, 2026',
      budget: '$4,500.00 Escrow',
      status: 'In Progress',
      loggedHours: 38
    },
    {
      id: '108',
      title: 'Task #108: High-Concurrency Ktor WebSockets Clustering',
      companyName: 'Acme AI Systems',
      deadline: 'Aug 25, 2026',
      budget: '$3,200.00 Milestone',
      status: 'PR Submitted',
      prUrl: 'https://github.com/acme-ai/kmp-core/pull/84',
      loggedHours: 42
    },
    {
      id: '112',
      title: 'Task #112: SKie Swift Interop & Memory Leak Resolution',
      companyName: 'Fintech Core',
      deadline: 'Aug 28, 2026',
      budget: '$2,800.00 Bounty',
      status: 'In Progress',
      loggedHours: 15
    }
  ];

  const payoutHistory: PayoutTransaction[] = [
    {
      id: 'tx-881',
      date: 'Aug 10, 2026',
      description: 'Milestone Release: Ktor Exposed ORM Migration',
      amount: '+$3,500.00',
      method: 'Stripe Direct Deposit',
      status: 'Completed'
    },
    {
      id: 'tx-882',
      date: 'Aug 02, 2026',
      description: 'Milestone Release: Android Compose WearOS Component',
      amount: '+$2,800.00',
      method: 'Web3 USDC Wallet',
      status: 'Completed'
    },
    {
      id: 'tx-883',
      date: 'Pending',
      description: 'Escrow Vault: Compose Wasm Audio Engine',
      amount: '$4,500.00',
      method: 'Stripe Direct Deposit',
      status: 'Escrow Locked'
    }
  ];

  const handleOpenSubmitModal = (task: AssignedTask) => {
    setSelectedTaskForPr(task);
    setPrUrlInput(task.prUrl || '');
    setShowSubmitPrModal(true);
  };

  const handleSubmitPrForm = (e: React.FormEvent) => {
    e.preventDefault();
    setSubmissionSuccess(true);
    setTimeout(() => {
      setSubmissionSuccess(false);
      setShowSubmitPrModal(false);
    }, 1800);
  };

  const handleToggleTimer = () => {
    setIsTimerRunning(!isTimerRunning);
    if (!isTimerRunning) {
      setLogSuccessMsg('Live Time Tracker Active — Recording Code Commits');
      setTimeout(() => setLogSuccessMsg(''), 2500);
    }
  };

  const currentWs = workspaces.find((w) => w.id === activeWorkspace) || workspaces[0];

  return (
    <div className="home-page">
      <Header isLoggedIn={true} />

      <main className="home-main">
        
        {/* Welcome Header & Multi-Workspace Selector */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 border-b border-hairline pb-6">
          <div className="space-y-1">
            <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
              <ShieldCheck className="w-3.5 h-3.5" /> Talent Workspace Hub
            </div>
            <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight text-ink">Welcome back, Alex</h1>
            <p className="text-sm text-mute">Manage your assigned company tasks, code PR submissions, and 0% fee escrow wallet.</p>
          </div>

          {/* Multi-Workspace Selector Pills */}
          <div className="space-y-2">
            <span className="text-[10px] font-bold text-mute uppercase tracking-wider block">Active Company Workspace</span>
            <div className="flex flex-wrap gap-2">
              {workspaces.map((ws) => {
                const isActive = ws.id === activeWorkspace;
                return (
                  <button
                    key={ws.id}
                    onClick={() => setActiveWorkspace(ws.id)}
                    className={`home-workspace-pill ${isActive ? 'home-workspace-pill-active' : ''}`}
                  >
                    <Building2 className="w-3.5 h-3.5" />
                    <span>{ws.name}</span>
                    <span className="px-1.5 py-0.2 rounded-full bg-surface text-[10px] border border-hairline">
                      {ws.activeTasksCount}
                    </span>
                  </button>
                );
              })}
            </div>
          </div>
        </div>

        {/* Live Interactive Time Tracker Card */}
        <div className="bg-surface p-6 rounded-xl border border-hairline flex flex-col md:flex-row md:items-center justify-between gap-4 shadow-sm dark:shadow-none">
          <div className="space-y-1">
            <div className="flex items-center gap-2">
              <span className="w-2.5 h-2.5 rounded-full bg-brand-green animate-pulse"></span>
              <span className="text-xs font-bold text-brand-green uppercase tracking-wider">Live Time Log Telemetry</span>
            </div>
            <h3 className="font-extrabold text-ink text-lg">Task #102: Compose Wasm WebAudio Processing Engine</h3>
            <p className="text-xs text-mute">Logged this week: <strong className="text-ink font-mono">38 hrs</strong> &bull; Client Target: <strong className="text-brand-green font-mono">40 hrs max</strong></p>
          </div>

          <div className="flex items-center gap-4 shrink-0">
            <div className="text-right font-mono">
              <span className="text-2xl font-extrabold text-brand-green">01:00:00</span>
              <span className="text-[10px] text-mute block uppercase font-bold">Session Timer</span>
            </div>

            <button
              onClick={handleToggleTimer}
              className={`px-5 py-2.5 rounded-full font-bold text-xs uppercase tracking-wider flex items-center gap-2 transition-all ${
                isTimerRunning ? 'bg-red-500 hover:bg-red-600 text-white' : 'bg-brand-green hover:bg-brand-green-hover text-white'
              }`}
            >
              {isTimerRunning ? <Pause className="w-4 h-4" /> : <Play className="w-4 h-4 fill-current" />}
              {isTimerRunning ? 'Pause Tracker' : 'Start Timer'}
            </button>
          </div>
        </div>

        {logSuccessMsg && (
          <div className="p-3 rounded-xl bg-brand-green/10 border border-brand-green/30 text-brand-green text-xs font-bold text-center flex items-center justify-center gap-2">
            <Activity className="w-4 h-4 animate-spin" /> {logSuccessMsg}
          </div>
        )}

        {/* Telemetry Metrics Bar */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="home-metric-card">
            <span className="text-xs font-bold uppercase tracking-wider text-mute flex items-center justify-between">
              Assigned Milestones <Layers className="w-4 h-4 text-brand-green" />
            </span>
            <p className="text-3xl font-extrabold text-ink">3 Active</p>
            <p className="text-xs text-mute font-medium">2 In Progress &bull; 1 Under PR Review</p>
          </div>

          <div className="home-metric-card">
            <span className="text-xs font-bold uppercase tracking-wider text-mute flex items-center justify-between">
              Escrow Locked Vault <Lock className="w-4 h-4 text-brand-green" />
            </span>
            <p className="text-3xl font-extrabold text-brand-green">$4,500.00</p>
            <p className="text-xs text-brand-green font-bold">100% Retained (0% Freelancer Fee)</p>
          </div>

          <div className="home-metric-card">
            <span className="text-xs font-bold uppercase tracking-wider text-mute flex items-center justify-between">
              Total Lifetime Payouts <Wallet className="w-4 h-4 text-brand-green" />
            </span>
            <p className="text-3xl font-extrabold text-ink">$32,400.00</p>
            <p className="text-xs text-mute font-medium">Direct Deposit & Web3 USDC</p>
          </div>
        </div>

        {/* Assigned Tasks & PR Submission Section */}
        <div className="home-card space-y-6">
          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-hairline pb-4">
            <div>
              <h2 className="text-xl font-bold text-ink">My Assigned Workspace Tasks</h2>
              <p className="text-xs text-mute">Submit code PR deliverables and request milestone escrow releases.</p>
            </div>

            <Link to="/projects" className="text-brand-green hover:text-brand-green-hover text-xs font-bold uppercase tracking-[0.05em]">
              Browse More Public Bounties &rarr;
            </Link>
          </div>

          <div className="space-y-4">
            {assignedTasks.map((task) => (
              <div key={task.id} className="p-6 rounded-xl bg-surface-elevated border border-hairline flex flex-col md:flex-row md:items-center justify-between gap-6 hover:border-brand-green/30 transition-all">
                <div className="space-y-1 flex-1">
                  <div className="flex items-center gap-2 text-xs">
                    <span className="font-bold text-brand-green uppercase tracking-wider">{task.companyName}</span>
                    <span className="text-mute">&bull;</span>
                    <span className="text-mute flex items-center gap-1"><Clock className="w-3.5 h-3.5" /> Due {task.deadline}</span>
                    <span className="text-mute">&bull;</span>
                    <span className="text-ink font-mono font-bold">{task.loggedHours} hrs logged</span>
                  </div>

                  <h3 className="font-extrabold text-ink text-base">{task.title}</h3>

                  {task.prUrl && (
                    <a
                      href={task.prUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="inline-flex items-center gap-1.5 text-xs font-mono text-brand-green hover:underline pt-1"
                    >
                      <GitPullRequest className="w-3.5 h-3.5" /> {task.prUrl} <ExternalLink className="w-3 h-3" />
                    </a>
                  )}
                </div>

                <div className="flex items-center justify-between md:justify-end gap-6 shrink-0 border-t md:border-t-0 border-hairline pt-4 md:pt-0">
                  <div className="text-left md:text-right">
                    <p className="text-lg font-extrabold text-brand-green font-mono">{task.budget}</p>
                    <span className={`text-[10px] font-bold uppercase px-2.5 py-0.5 rounded-full ${
                      task.status === 'PR Submitted' ? 'bg-yellow-500/10 text-yellow-500 border border-yellow-500/20' : 'bg-brand-green/10 text-brand-green border border-brand-green/20'
                    }`}>
                      {task.status}
                    </span>
                  </div>

                  <button
                    onClick={() => handleOpenSubmitModal(task)}
                    className="inline-flex items-center gap-1.5 bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] px-5 py-2.5 rounded-full hover:scale-[1.04] transition-all shadow-md shadow-brand-green/20"
                  >
                    <Upload className="w-3.5 h-3.5" /> {task.status === 'PR Submitted' ? 'Update PR' : 'Submit PR'}
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Wallet & Escrow Payout History */}
        <div className="home-card space-y-6">
          <div className="flex items-center justify-between border-b border-hairline pb-4">
            <div>
              <h2 className="text-xl font-bold text-ink">Recent Wallet Payout History</h2>
              <p className="text-xs text-mute">100% retained milestone earnings paid directly to your account.</p>
            </div>
            <Link to="/settings" className="text-brand-green hover:text-brand-green-hover text-xs font-bold uppercase tracking-[0.05em]">
              Manage Payout Accounts &rarr;
            </Link>
          </div>

          <div className="w-full overflow-x-auto rounded-xl border border-hairline">
            <table className="w-full text-left text-xs text-ink border-collapse">
              <thead>
                <tr className="bg-surface-elevated border-b border-hairline text-mute uppercase tracking-wider font-bold">
                  <th className="p-4">Transaction ID</th>
                  <th className="p-4">Date</th>
                  <th className="p-4">Description</th>
                  <th className="p-4">Payout Method</th>
                  <th className="p-4">Amount</th>
                  <th className="p-4 text-right">Status</th>
                </tr>
              </thead>
              <tbody>
                {payoutHistory.map((tx) => (
                  <tr key={tx.id} className="border-b border-hairline hover:bg-surface-elevated transition-colors">
                    <td className="p-4 font-mono font-bold text-ink">{tx.id}</td>
                    <td className="p-4 text-mute">{tx.date}</td>
                    <td className="p-4 font-bold text-ink">{tx.description}</td>
                    <td className="p-4 text-mute font-medium">{tx.method}</td>
                    <td className="p-4 font-mono font-extrabold text-brand-green">{tx.amount}</td>
                    <td className="p-4 text-right">
                      <span className={`px-2.5 py-1 rounded-full text-[10px] font-bold ${
                        tx.status === 'Completed' ? 'bg-brand-green/10 text-brand-green border border-brand-green/20' : 'bg-yellow-500/10 text-yellow-500 border border-yellow-500/20'
                      }`}>
                        {tx.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

      </main>

      {/* SUBMIT PR DELIVERABLE MODAL DRAWER */}
      {showSubmitPrModal && selectedTaskForPr && (
        <div className="home-modal-overlay">
          <div className="home-modal-content">
            
            {/* Modal Header */}
            <div className="flex items-center justify-between border-b border-hairline pb-4">
              <div className="space-y-0.5">
                <div className="flex items-center gap-1.5 text-xs font-bold text-brand-green uppercase tracking-wider">
                  <GitPullRequest className="w-4 h-4" /> Code PR Deliverable & Escrow Release
                </div>
                <h3 className="text-xl font-extrabold text-ink">Submit Milestone Completion</h3>
              </div>
              <button onClick={() => setShowSubmitPrModal(false)} className="p-2 rounded-full text-mute hover:text-ink">
                <X className="w-5 h-5" />
              </button>
            </div>

            {/* Form */}
            <form onSubmit={handleSubmitPrForm} className="space-y-4">
              
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-mute">Task Title</label>
                <div className="p-3 rounded-xl bg-surface-elevated border border-hairline text-xs font-bold text-ink">
                  {selectedTaskForPr.title} ({selectedTaskForPr.companyName})
                </div>
              </div>

              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-mute">GitHub Pull Request URL *</label>
                <input
                  type="url"
                  required
                  placeholder="https://github.com/company/repo/pull/42"
                  value={prUrlInput}
                  onChange={(e) => setPrUrlInput(e.target.value)}
                  className="w-full px-4 py-3 rounded-xl border border-hairline bg-surface-elevated text-xs text-ink placeholder:text-mute focus:outline-none focus:border-brand-green font-mono"
                />
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div className="space-y-1">
                  <label className="text-xs font-bold uppercase text-mute">Git Commit Hash (Optional)</label>
                  <input
                    type="text"
                    placeholder="e.g. b8f9a2e"
                    value={commitHashInput}
                    onChange={(e) => setCommitHashInput(e.target.value)}
                    className="w-full px-4 py-3 rounded-xl border border-hairline bg-surface-elevated text-xs text-ink placeholder:text-mute focus:outline-none focus:border-brand-green font-mono"
                  />
                </div>

                <div className="space-y-1">
                  <label className="text-xs font-bold uppercase text-mute">Live Preview Build URL (Optional)</label>
                  <input
                    type="url"
                    placeholder="https://demo.acme.ai/preview"
                    value={demoUrlInput}
                    onChange={(e) => setDemoUrlInput(e.target.value)}
                    className="w-full px-4 py-3 rounded-xl border border-hairline bg-surface-elevated text-xs text-ink placeholder:text-mute focus:outline-none focus:border-brand-green font-mono"
                  />
                </div>
              </div>

              <div className="p-4 rounded-xl bg-brand-green/10 border border-brand-green/30 text-xs text-brand-green space-y-1">
                <p className="font-bold flex items-center gap-1.5">
                  <ShieldCheck className="w-4 h-4" /> Automatic IP Transfer Attached
                </p>
                <p className="text-[11px] leading-relaxed text-brand-green/80">
                  Submitting this PR triggers a client milestone verification window. Upon approval, 100% of escrow funds release instantly with Work-for-Hire copyright transfer.
                </p>
              </div>

              <div className="flex items-center justify-end gap-3 pt-2 border-t border-hairline">
                <button
                  type="button"
                  onClick={() => setShowSubmitPrModal(false)}
                  className="px-5 py-2.5 rounded-full bg-surface-elevated hover:bg-surface-modal text-ink font-bold text-xs uppercase"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="inline-flex items-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] px-6 py-2.5 rounded-full hover:scale-[1.03] transition-all shadow-md shadow-brand-green/20"
                >
                  <CheckCircle2 className="w-4 h-4" /> Submit PR & Request Escrow
                </button>
              </div>

            </form>

            {submissionSuccess && (
              <div className="p-3 rounded-xl bg-brand-green/10 border border-brand-green/30 text-brand-green text-xs font-bold text-center flex items-center justify-center gap-2">
                <Check className="w-4 h-4" /> PR Deliverable & Escrow Release Request Submitted!
              </div>
            )}

          </div>
        </div>
      )}

      <Footer />
    </div>
  );
};
