import React, { useState, useEffect } from 'react';
import './company.css';
import {
  Building2,
  Users,
  Layers,
  Plus,
  ShieldCheck,
  UserPlus,
  CheckCircle2,
  X,
  Activity,
  Zap,
  MapPin,
  Globe,
  Loader2,
  Check,
} from 'lucide-react';
import { Link } from 'react-router-dom';
import {
  companyService,
  CompanyDashboardData,
  TaskStatus,
} from '../../services/companyService';

type TabId = 'roster' | 'sprints' | 'telemetry' | 'payroll';

function statusBadgeClass(s: string) {
  if (s === 'Active') return 'company-badge company-badge-active';
  if (s === 'In Review') return 'company-badge company-badge-review';
  if (s === 'Onboarding') return 'company-badge company-badge-bounty';
  return 'company-badge';
}

function taskStatusClass(s: string) {
  if (s === 'In Progress') return 'company-badge company-badge-contractor';
  if (s === 'PR Review') return 'company-badge company-badge-review';
  if (s === 'Completed') return 'company-badge company-badge-active';
  return 'company-badge company-badge-bounty';
}

export const CompanyDashboardPage: React.FC = () => {
  const [tab, setTab] = useState<TabId>('roster');
  const [dashboard, setDashboard] = useState<CompanyDashboardData | null>(null);
  const [loading, setLoading] = useState(true);

  const [modalOpen, setModalOpen] = useState(false);
  const [selectedTask, setSelectedTask] = useState('Task #102: Custom RAG AI Agent Pipeline');
  const [inviteSuccess, setInviteSuccess] = useState(false);
  const [statusToast, setStatusToast] = useState<string | null>(null);

  useEffect(() => {
    let isMounted = true;
    setLoading(true);
    companyService.getDashboard().then((data) => {
      if (isMounted) {
        setDashboard(data);
        setLoading(false);
      }
    });
    return () => {
      isMounted = false;
    };
  }, []);

  const openModal = (taskLabel: string) => {
    setSelectedTask(taskLabel);
    setModalOpen(true);
  };

  const sendInvite = async (candidateName: string) => {
    await companyService.inviteContractor(candidateName, selectedTask);
    const updated = await companyService.getDashboard();
    setDashboard(updated);
    setInviteSuccess(true);
    setTimeout(() => {
      setInviteSuccess(false);
      setModalOpen(false);
      showToast(`Invited ${candidateName} to ${selectedTask}!`);
    }, 1200);
  };

  const handleTaskStatusChange = async (taskId: string, newStatus: TaskStatus) => {
    await companyService.updateTaskStatus(taskId, newStatus);
    const updated = await companyService.getDashboard();
    setDashboard(updated);
    showToast(`Task updated to ${newStatus}`);
  };

  const showToast = (msg: string) => {
    setStatusToast(msg);
    setTimeout(() => setStatusToast(null), 3000);
  };

  if (loading || !dashboard) {
    return (
      <div className="company-page">
        <main className="company-main flex flex-col items-center justify-center min-h-[50vh] gap-3">
          <Loader2 className="w-8 h-8 text-primary animate-spin" />
          <p className="text-xs font-semibold text-mute">Loading Company OS Workspace...</p>
        </main>
      </div>
    );
  }

  const TABS: { id: TabId; label: string }[] = [
    { id: 'roster', label: `Team roster (${dashboard.team.length})` },
    { id: 'sprints', label: `Sprint board (${dashboard.tasks.length})` },
    { id: 'telemetry', label: 'Velocity & time logs' },
    { id: 'payroll', label: 'Payroll & escrow' },
  ];

  return (
    <div className="company-page">
      <main className="company-main">
        {/* Header */}
        <div className="company-header">
          <div className="company-identity">
            <div className="company-logo">
              <Building2 size={22} />
            </div>
            <div>
              <h1 className="company-name">
                {dashboard.companyName}
                {dashboard.isVerified && (
                  <span className="company-verified">
                    <ShieldCheck size={11} /> Verified
                  </span>
                )}
              </h1>
              <div className="company-meta">
                <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                  <Globe size={12} /> {dashboard.website}
                </span>
                <span>·</span>
                <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                  <MapPin size={12} /> {dashboard.location}
                </span>
                <span>·</span>
                <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                  <Users size={12} /> {dashboard.team.length} team members
                </span>
              </div>
            </div>
          </div>
          <div className="company-actions">
            <button
              onClick={() => openModal(dashboard.tasks[0]?.title || 'Sprint Task')}
              className="company-btn-secondary"
            >
              <UserPlus size={14} /> Add developer
            </button>
            <Link to="/projects/new" className="company-btn-primary">
              <Plus size={14} /> Post bounty
            </Link>
          </div>
        </div>

        {statusToast && (
          <div className="p-4 rounded-xl bg-primary/10 border border-primary/20 text-primary text-xs font-semibold flex items-center gap-2">
            <Check size={16} /> {statusToast}
          </div>
        )}

        {/* Metrics */}
        <div className="company-metrics">
          <div className="company-metric">
            <p className="company-metric-label">
              Team members <Users size={13} />
            </p>
            <p className="company-metric-value">{dashboard.metrics.totalTeam}</p>
            <p className="company-metric-sub">
              {dashboard.metrics.fullTimeCount} full-time · {dashboard.metrics.contractorCount} contractors
            </p>
          </div>
          <div className="company-metric">
            <p className="company-metric-label">
              Active sprints <Layers size={13} />
            </p>
            <p className="company-metric-value">{dashboard.metrics.activeSprints}</p>
            <p className="company-metric-sub">{dashboard.metrics.totalTasks} tasks managed</p>
          </div>
          <div className="company-metric">
            <p className="company-metric-label">
              Hours logged <Activity size={13} />
            </p>
            <p className="company-metric-value">{dashboard.metrics.hoursLogged} hrs</p>
            <p className="company-metric-sub">94% velocity on track</p>
          </div>
          <div className="company-metric">
            <p className="company-metric-label">
              Open bounties <Zap size={13} />
            </p>
            <p className="company-metric-value">{dashboard.metrics.openBounties} open</p>
            <p className="company-metric-sub">0% fee escrows</p>
          </div>
        </div>

        {/* Tabs */}
        <div className="company-tabs">
          {TABS.map((t) => (
            <button
              key={t.id}
              onClick={() => setTab(t.id)}
              className={`company-tab${tab === t.id ? ' active' : ''}`}
            >
              {t.label}
            </button>
          ))}
        </div>

        {/* Roster */}
        {tab === 'roster' && (
          <div className="company-section">
            <div className="company-section-header">
              <div>
                <p className="company-section-title">Employee & contractor roster</p>
                <p className="company-section-sub">Manage staff, rates, and active task assignments.</p>
              </div>
              <button
                onClick={() => openModal(dashboard.tasks[0]?.title || 'Sprint Task')}
                className="company-btn-primary"
              >
                <UserPlus size={14} /> Add member
              </button>
            </div>
            <div className="company-table-wrap">
              <table className="company-table">
                <thead>
                  <tr>
                    <th>Member</th>
                    <th>Type</th>
                    <th>Compensation</th>
                    <th>Assigned task</th>
                    <th>Hours (this sprint)</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {dashboard.team.map((m) => (
                    <tr key={m.id}>
                      <td>
                        <div className="company-member-cell">
                          <img src={m.avatar} alt={m.name} className="company-member-avatar" />
                          <div>
                            <p className="company-member-name">{m.name}</p>
                            <p className="company-member-role">{m.role}</p>
                          </div>
                        </div>
                      </td>
                      <td>
                        <span
                          className={`company-badge ${
                            m.type === 'Contractor'
                              ? 'company-badge-contractor'
                              : 'company-badge-fulltime'
                          }`}
                        >
                          {m.type}
                        </span>
                      </td>
                      <td>
                        <span style={{ fontFamily: 'var(--font-mono)', fontWeight: 600, fontSize: 13 }}>
                          {m.comp}
                        </span>
                      </td>
                      <td>
                        <span style={{ fontSize: 12, color: 'var(--color-ink)', fontWeight: 500 }}>
                          {m.task}
                        </span>
                      </td>
                      <td>
                        <span style={{ fontFamily: 'var(--font-mono)', fontSize: 13 }}>
                          {m.hrs > 0 ? `${m.hrs} hrs` : '—'}
                        </span>
                      </td>
                      <td>
                        <span className={statusBadgeClass(m.status)}>{m.status}</span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Sprint board */}
        {tab === 'sprints' && (
          <div className="company-section">
            <div className="company-section-header">
              <div>
                <p className="company-section-title">Sprint backlog & tasks</p>
                <p className="company-section-sub">Sprint 24 · Ending August 31, 2026</p>
              </div>
              <Link to="/projects/new" className="company-btn-primary">
                <Plus size={14} /> New task
              </Link>
            </div>
            <div className="company-tasks-list">
              {dashboard.tasks.map((task) => (
                <div key={task.id} className="company-task-item">
                  <div style={{ flex: 1 }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 4 }}>
                      <p className="company-task-title">{task.title}</p>
                      <span className={taskStatusClass(task.status)}>{task.status}</span>
                      {task.bounty && (
                        <span className="company-badge company-badge-bounty">Bounty</span>
                      )}
                    </div>
                    <div className="company-task-meta">
                      <span>Assignee: <strong style={{ color: 'var(--color-ink)' }}>{task.assignee}</strong></span>
                      <span>·</span>
                      <span>Due: <strong style={{ color: 'var(--color-ink)' }}>{task.deadline}</strong></span>
                      <span>·</span>
                      <span>Logged: <strong style={{ color: 'var(--color-ink)' }}>{task.hrs} hrs</strong></span>
                    </div>
                  </div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                    <div style={{ textAlign: 'right' }}>
                      <p className="company-task-budget">{task.budget}</p>
                      <p style={{ fontSize: 11, color: 'var(--color-steel)', marginTop: 2 }}>{task.budgetType}</p>
                    </div>
                    {task.assignee === 'Unassigned' ? (
                      <button onClick={() => openModal(task.title)} className="company-btn-primary">
                        Assign
                      </button>
                    ) : task.status === 'In Progress' ? (
                      <button
                        onClick={() => handleTaskStatusChange(task.id, 'PR Review')}
                        className="company-btn-secondary"
                      >
                        Submit PR
                      </button>
                    ) : task.status === 'PR Review' ? (
                      <Link to="/client/proposals" className="company-btn-primary">
                        Review Deliverable
                      </Link>
                    ) : null}
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Telemetry */}
        {tab === 'telemetry' && (
          <div className="company-section">
            <div className="company-section-header">
              <div>
                <p className="company-section-title">Sprint velocity & engineer telemetry</p>
                <p className="company-section-sub">Real-time hours, commit activity, and sprint burndown rate.</p>
              </div>
            </div>
            <div className="company-telemetry-grid">
              {[
                { label: 'Sprint 24 completion', val: '78%', pct: 78, sub: '11 of 14 tasks finished' },
                { label: 'Total team velocity', val: '142 hrs', pct: 89, sub: 'Target: 160 hrs' },
                { label: 'Pull requests merged', val: '9 PRs', pct: 64, sub: '3 currently in review' },
                { label: 'Test suite coverage', val: '94.2%', pct: 94, sub: 'All 84 tests passing' },
              ].map((item, i) => (
                <div key={i} className="company-telemetry-card">
                  <p className="company-telemetry-card-label">{item.label}</p>
                  <p className="company-telemetry-card-val">{item.val}</p>
                  <div className="company-progress-track">
                    <div className="company-progress-fill" style={{ width: `${item.pct}%` }} />
                  </div>
                  <p style={{ fontSize: 12, color: 'var(--color-steel)', marginTop: 2 }}>{item.sub}</p>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Payroll */}
        {tab === 'payroll' && (
          <div className="company-section">
            <div className="company-section-header">
              <div>
                <p className="company-section-title">Payroll & milestone escrow</p>
                <p className="company-section-sub">Automated employee payouts and funded contractor milestone escrows.</p>
              </div>
            </div>
            <div className="company-payroll-grid">
              <div className="company-payroll-card">
                <div className="company-payroll-label">
                  Monthly payroll
                  <span className="company-badge company-badge-active">Auto-deposit</span>
                </div>
                <p className="company-payroll-amount">$23,500.00</p>
                <p className="company-payroll-sub">Next payout: <strong style={{ color: 'var(--color-ink)' }}>August 31, 2026</strong></p>
              </div>
              <div className="company-payroll-card">
                <div className="company-payroll-label">
                  Escrow vault
                  <span className="company-badge company-badge-contractor">0% fee</span>
                </div>
                <p className="company-payroll-amount">$25,000.00</p>
                <p className="company-payroll-sub">Protected in Stripe Escrow · 100% IP transfer on release</p>
              </div>
            </div>
          </div>
        )}
      </main>

      {/* Modal */}
      {modalOpen && (
        <div className="company-modal-overlay" onClick={() => setModalOpen(false)}>
          <div className="company-modal" onClick={(e) => e.stopPropagation()}>
            <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 12 }}>
              <div>
                <p style={{ fontSize: 12, fontWeight: 600, color: 'var(--color-primary)', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: 4 }}>
                  <Zap size={12} style={{ display: 'inline', marginRight: 4 }} /> AI Talent Matcher
                </p>
                <h3 className="company-modal-title">Add developer to task</h3>
              </div>
              <button
                onClick={() => setModalOpen(false)}
                style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--color-steel)', padding: 4 }}
              >
                <X size={18} />
              </button>
            </div>

            <div>
              <p style={{ fontSize: 12, fontWeight: 600, color: 'var(--color-steel)', marginBottom: 6 }}>Target task</p>
              <div style={{ padding: '10px 12px', border: '1px solid var(--color-hairline)', borderRadius: 8, fontSize: 13, fontWeight: 500, color: 'var(--color-ink)', backgroundColor: 'var(--color-surface)' }}>
                {selectedTask}
              </div>
            </div>

            <div>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 10 }}>
                <p style={{ fontSize: 12, fontWeight: 600, color: 'var(--color-steel)' }}>Top AI-matched candidates</p>
                <span style={{ fontSize: 11, color: '#1aae39', fontWeight: 600, display: 'flex', alignItems: 'center', gap: 4 }}>
                  <Zap size={11} /> Recommender active
                </span>
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
                {dashboard.candidates.map((c, i) => (
                  <div key={i} className="company-candidate">
                    <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 12 }}>
                      <div>
                        <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 3 }}>
                          <p style={{ fontSize: 14, fontWeight: 600, color: 'var(--color-ink)' }}>{c.name}</p>
                          <span className="company-match-badge">{c.match}</span>
                        </div>
                        <p style={{ fontSize: 12, color: 'var(--color-steel)' }}>{c.title}</p>
                      </div>
                      <div style={{ textAlign: 'right', flexShrink: 0 }}>
                        <p style={{ fontSize: 15, fontWeight: 700, fontFamily: 'var(--font-mono)', color: 'var(--color-ink)' }}>{c.rate}</p>
                        <p style={{ fontSize: 12, color: 'var(--color-steel)' }}>★ {c.rating}</p>
                      </div>
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', paddingTop: 10, borderTop: '1px solid var(--color-hairline)' }}>
                      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 5 }}>
                        {c.skills.map((s) => (
                          <span key={s} className="company-skill-tag">
                            {s}
                          </span>
                        ))}
                      </div>
                      <button onClick={() => sendInvite(c.name)} className="company-btn-primary">
                        Invite
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {inviteSuccess && (
              <div className="company-toast">
                <CheckCircle2 size={14} /> Invite & escrow offer sent!
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
};
