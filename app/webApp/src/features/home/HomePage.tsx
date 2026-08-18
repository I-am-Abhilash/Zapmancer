import React, { useState, useEffect } from 'react';
import './home.css';
import { Link } from 'react-router-dom';
import {
  ShieldCheck,
  ArrowRight,
  Clock,
  Layers,
  Lock,
  Wallet,
  Upload,
  GitPullRequest,
  ExternalLink,
  X,
  Check,
  CheckCircle2,
  Play,
  Pause,
  Building2,
  Loader2,
  Download,
} from 'lucide-react';
import {
  homeService,
  FreelancerDashboardData,
  AssignedTask,
} from '../../services/homeService';

function statusClass(s: string) {
  if (s === 'In Progress') return 'home-status-progress';
  if (s === 'PR Submitted') return 'home-status-submitted';
  return 'home-status-approved';
}

export const HomePage: React.FC = () => {
  const [dashboard, setDashboard] = useState<FreelancerDashboardData | null>(null);
  const [loading, setLoading] = useState(true);
  const [activeWs, setActiveWs] = useState('all');

  // Time Tracker State
  const [timerSeconds, setTimerSeconds] = useState(3600); // starts at 1 hour
  const [timerRunning, setTimerRunning] = useState(false);

  // PR Submission Modal
  const [modalTask, setModalTask] = useState<AssignedTask | null>(null);
  const [prUrl, setPrUrl] = useState('');
  const [commitHash, setCommitHash] = useState('');
  const [demoUrl, setDemoUrl] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const [toastMsg, setToastMsg] = useState<string | null>(null);

  useEffect(() => {
    let isMounted = true;
    setLoading(true);
    homeService.getDashboard().then((data) => {
      if (isMounted) {
        setDashboard(data);
        setLoading(false);
      }
    });
    return () => {
      isMounted = false;
    };
  }, []);

  // Ticking Timer Effect
  useEffect(() => {
    let interval: any = null;
    if (timerRunning) {
      interval = setInterval(() => {
        setTimerSeconds((prev) => prev + 1);
      }, 1000);
    } else if (!timerRunning && interval) {
      clearInterval(interval);
    }
    return () => {
      if (interval) clearInterval(interval);
    };
  }, [timerRunning]);

  const formatTimer = (totalSec: number) => {
    const hrs = Math.floor(totalSec / 3600);
    const mins = Math.floor((totalSec % 3600) / 60);
    const secs = totalSec % 60;
    return `${hrs.toString().padStart(2, '0')}:${mins
      .toString()
      .padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  const openModal = (t: AssignedTask) => {
    setModalTask(t);
    setPrUrl(t.prUrl || '');
    setCommitHash(t.commitHash || '');
    setDemoUrl(t.demoUrl || '');
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!modalTask) return;
    setSubmitted(true);
    await homeService.submitPr(modalTask.id, prUrl, commitHash, demoUrl);
    const updated = await homeService.getDashboard();
    setDashboard(updated);
    setTimeout(() => {
      setSubmitted(false);
      setModalTask(null);
      showToast(`PR submitted for ${modalTask.title}!`);
    }, 1200);
  };

  const handleExportCsv = async () => {
    await homeService.exportTimeLogs();
    showToast('Exported time logs CSV successfully.');
  };

  const showToast = (msg: string) => {
    setToastMsg(msg);
    setTimeout(() => setToastMsg(null), 3000);
  };

  if (loading || !dashboard) {
    return (
      <div className="home-page">
        <main className="home-main flex flex-col items-center justify-center min-h-[50vh] gap-3">
          <Loader2 className="w-8 h-8 text-primary animate-spin" />
          <p className="text-xs font-semibold text-mute">Loading Talent Workspace...</p>
        </main>
      </div>
    );
  }

  const filteredTasks =
    activeWs === 'all'
      ? dashboard.tasks
      : dashboard.tasks.filter((t) => t.workspaceId === activeWs);

  return (
    <div className="home-page">
      <main className="home-main">
        {/* Header */}
        <div className="home-header">
          <div>
            <h1 className="home-page-title">Welcome back, {dashboard.freelancerName}</h1>
            <p className="home-page-sub">
              Manage your assigned tasks, PR submissions, and 0% fee escrow wallet.
            </p>
          </div>
          <div>
            <p className="home-ws-label">Active workspace</p>
            <div className="home-ws-pills">
              {dashboard.workspaces.map((ws) => (
                <button
                  key={ws.id}
                  onClick={() => setActiveWs(ws.id)}
                  className={`home-ws-pill${activeWs === ws.id ? ' active' : ''}`}
                >
                  <Building2 size={13} /> {ws.name}
                  <span className="home-ws-count">{ws.count}</span>
                </button>
              ))}
            </div>
          </div>
        </div>

        {toastMsg && (
          <div className="p-4 rounded-xl bg-primary/10 border border-primary/20 text-primary text-xs font-semibold flex items-center gap-2">
            <Check size={16} /> {toastMsg}
          </div>
        )}

        {/* Time tracker */}
        <div className="home-tracker">
          <div style={{ display: 'flex', alignItems: 'flex-start', gap: 10 }}>
            <div
              className="home-tracker-dot"
              style={{
                marginTop: 4,
                backgroundColor: timerRunning ? 'var(--color-primary)' : 'var(--color-steel)',
              }}
            />
            <div>
              <p className="home-tracker-title">
                {dashboard.tasks[0]?.title || 'Active Engineering Contract'}
              </p>
              <p className="home-tracker-sub">
                Logged this week:{' '}
                <strong style={{ color: 'var(--color-ink)', fontFamily: 'var(--font-mono)' }}>
                  38 hrs
                </strong>{' '}
                · Target: 40 hrs max
              </p>
            </div>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 14, flexShrink: 0 }}>
            <span className="home-tracker-timer">{formatTimer(timerSeconds)}</span>
            <button
              onClick={() => setTimerRunning(!timerRunning)}
              className={`home-tracker-btn ${
                timerRunning ? 'home-tracker-btn-pause' : 'home-tracker-btn-start'
              }`}
            >
              {timerRunning ? (
                <>
                  <Pause size={13} /> Pause
                </>
              ) : (
                <>
                  <Play size={13} /> Start timer
                </>
              )}
            </button>
            <button
              onClick={handleExportCsv}
              className="home-btn-secondary text-xs py-1.5 px-3"
              title="Download CSV timesheet"
            >
              <Download size={13} />
            </button>
          </div>
        </div>

        {/* Metrics */}
        <div className="home-metrics">
          <div className="home-metric">
            <p className="home-metric-label">
              Active milestones <Layers size={14} />
            </p>
            <p className="home-metric-value">{dashboard.metrics.activeMilestones}</p>
            <p className="home-metric-sub">
              {dashboard.metrics.inProgressCount} in progress · {dashboard.metrics.underReviewCount} under review
            </p>
          </div>
          <div className="home-metric">
            <p className="home-metric-label">
              Escrow locked <Lock size={14} />
            </p>
            <p className="home-metric-value">{dashboard.metrics.escrowLocked}</p>
            <p className="home-metric-sub">100% retained · 0% fee</p>
          </div>
          <div className="home-metric">
            <p className="home-metric-label">
              Lifetime payouts <Wallet size={14} />
            </p>
            <p className="home-metric-value">{dashboard.metrics.lifetimePayouts}</p>
            <p className="home-metric-sub">Stripe & USDC</p>
          </div>
        </div>

        {/* Tasks */}
        <div className="home-section">
          <div className="home-section-header">
            <div>
              <p className="home-section-title">My Assigned Tasks</p>
              <p className="home-section-sub">
                Submit PR deliverables and request milestone escrow releases.
              </p>
            </div>
            <Link to="/projects" className="home-section-link">
              Browse open contracts <ArrowRight size={13} style={{ display: 'inline' }} />
            </Link>
          </div>
          <div className="home-task-list">
            {filteredTasks.length === 0 ? (
              <div className="p-8 text-center text-sm text-mute bg-surface-elevated rounded-xl border border-hairline">
                No tasks assigned in this workspace.
              </div>
            ) : (
              filteredTasks.map((t) => (
                <div key={t.id} className="home-task">
                  <div style={{ flex: 1 }}>
                    <div className="home-task-meta">
                      <span className="home-task-company">{t.companyName}</span>
                      <span>·</span>
                      <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                        <Clock size={12} /> Due {t.deadline}
                      </span>
                      <span>·</span>
                      <span
                        style={{
                          fontFamily: 'var(--font-mono)',
                          fontWeight: 600,
                          color: 'var(--color-ink)',
                        }}
                      >
                        {t.loggedHours} hrs
                      </span>
                    </div>
                    <p className="home-task-title">{t.title}</p>
                    {t.prUrl && (
                      <a
                        href={t.prUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="home-task-pr-link"
                      >
                        <GitPullRequest size={12} /> {t.prUrl} <ExternalLink size={11} />
                      </a>
                    )}
                  </div>
                  <div className="home-task-right">
                    <div className="home-task-budget">
                      <p className="home-task-budget-val">{t.budget}</p>
                      <p className="home-task-budget-sub">{t.budgetType}</p>
                      <span
                        className={`home-status-badge ${statusClass(t.status)}`}
                        style={{ marginTop: 4, display: 'inline-block' }}
                      >
                        {t.status}
                      </span>
                    </div>
                    <button onClick={() => openModal(t)} className="home-submit-btn">
                      <Upload size={13} /> {t.status === 'PR Submitted' ? 'Update PR' : 'Submit PR'}
                    </button>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Payout history */}
        <div className="home-section">
          <div className="home-section-header">
            <div>
              <p className="home-section-title">Payout History</p>
              <p className="home-section-sub">
                100% retained milestone earnings paid directly to your account.
              </p>
            </div>
            <Link to="/settings" className="home-section-link">
              Manage payout accounts
            </Link>
          </div>
          <div style={{ overflowX: 'auto' }}>
            <table className="home-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Date</th>
                  <th>Description</th>
                  <th>Method</th>
                  <th>Amount</th>
                  <th style={{ textAlign: 'right' }}>Status</th>
                </tr>
              </thead>
              <tbody>
                {dashboard.payouts.map((tx) => (
                  <tr key={tx.id}>
                    <td className="td-mono">{tx.id}</td>
                    <td className="td-muted">{tx.date}</td>
                    <td>{tx.description}</td>
                    <td className="td-muted">{tx.method}</td>
                    <td
                      className="td-amount"
                      style={{ fontFamily: 'var(--font-mono)', fontWeight: 700 }}
                    >
                      {tx.amount}
                    </td>
                    <td className="td-right" style={{ textAlign: 'right' }}>
                      <span
                        className={`home-status-badge ${
                          tx.status === 'Completed'
                            ? 'home-status-approved'
                            : 'home-status-submitted'
                        }`}
                      >
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

      {/* PR Submit Modal */}
      {modalTask && (
        <div className="home-modal-overlay" onClick={() => setModalTask(null)}>
          <div className="home-modal-content" onClick={(e) => e.stopPropagation()}>
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
                  <GitPullRequest size={12} style={{ display: 'inline', marginRight: 4 }} />
                  PR Deliverable Submission
                </p>
                <h3 className="home-modal-title">Submit milestone completion</h3>
              </div>
              <button
                onClick={() => setModalTask(null)}
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

            <form
              onSubmit={handleSubmit}
              style={{ display: 'flex', flexDirection: 'column', gap: 14 }}
            >
              <div className="home-field">
                <label>Task</label>
                <div className="home-field-static">
                  {modalTask.title} · {modalTask.companyName}
                </div>
              </div>
              <div className="home-field">
                <label>GitHub Pull Request URL *</label>
                <input
                  type="url"
                  required
                  placeholder="https://github.com/company/repo/pull/42"
                  value={prUrl}
                  onChange={(e) => setPrUrl(e.target.value)}
                  className="home-input"
                />
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
                <div className="home-field">
                  <label>Commit hash</label>
                  <input
                    type="text"
                    placeholder="e.g. b8f9a2e"
                    value={commitHash}
                    onChange={(e) => setCommitHash(e.target.value)}
                    className="home-input"
                  />
                </div>
                <div className="home-field">
                  <label>Live preview URL</label>
                  <input
                    type="url"
                    placeholder="https://demo.acme.ai/preview"
                    value={demoUrl}
                    onChange={(e) => setDemoUrl(e.target.value)}
                    className="home-input"
                  />
                </div>
              </div>

              <div className="home-modal-note">
                <ShieldCheck
                  size={16}
                  style={{ color: '#1aae39', flexShrink: 0, marginTop: 1 }}
                />
                <div>
                  <p>Automatic IP Transfer attached</p>
                  <span>
                    Upon client approval, 100% of escrow funds release instantly with Work-for-Hire
                    copyright transfer.
                  </span>
                </div>
              </div>

              {submitted && (
                <div className="home-toast">
                  <Check size={14} /> PR submitted & escrow release requested!
                </div>
              )}

              <div className="home-modal-actions">
                <button
                  type="button"
                  onClick={() => setModalTask(null)}
                  className="home-btn-secondary"
                >
                  Cancel
                </button>
                <button type="submit" className="home-btn-primary">
                  <CheckCircle2 size={14} /> Submit PR & request escrow
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
