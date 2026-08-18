import React, { useState } from 'react';
import './company.css';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Building2, Users, Layers, Plus, ShieldCheck, UserPlus, CheckCircle2, Clock, X, Activity, Zap, MapPin, Globe } from 'lucide-react';
import { Link } from 'react-router-dom';

type TabId = 'roster' | 'sprints' | 'telemetry' | 'payroll';

const TABS: { id: TabId; label: string }[] = [
  { id: 'roster', label: 'Team roster (4)' },
  { id: 'sprints', label: 'Sprint board (3)' },
  { id: 'telemetry', label: 'Velocity & time logs' },
  { id: 'payroll', label: 'Payroll & escrow' },
];

const TEAM = [
  { id: '1', name: 'Elena Rostova', role: 'Full-Stack & Web Architect', type: 'Contractor' as const, comp: '$85 / hr', task: 'Task #102: Custom RAG AI Agent Pipeline', hrs: 38, status: 'Active' as const, avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80' },
  { id: '2', name: 'Dr. Lucas Meyer', role: 'AI Agent Specialist', type: 'Full-Time' as const, comp: '$14,500 / mo', task: 'Task #108: Multi-Modal Model Fine-Tuning', hrs: 42, status: 'Active' as const, avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80' },
  { id: '3', name: 'Sophia Al-Mansoor', role: 'Lead UI/UX Designer', type: 'Full-Time' as const, comp: '$11,000 / mo', task: 'Task #112: Design System Tokens Redesign', hrs: 35, status: 'In Review' as const, avatar: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=150&q=80' },
  { id: '4', name: 'David Kim', role: 'DevOps & Infra Specialist', type: 'Contractor' as const, comp: '$90 / hr', task: 'Task #115: Kubernetes Cluster Auto-Scaling', hrs: 27, status: 'Active' as const, avatar: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=150&q=80' },
];

const TASKS = [
  { id: '102', title: 'Custom RAG AI Agent Pipeline Integration', assignee: 'Elena Rostova', deadline: 'Aug 20, 2026', status: 'In Progress', budget: '$4,500', budgetType: 'Escrow', hrs: 38, bounty: false },
  { id: '108', title: 'Multi-Modal LLM Fine-Tuning & Vector Store', assignee: 'Dr. Lucas Meyer', deadline: 'Aug 25, 2026', status: 'PR Review', budget: '$3,800', budgetType: 'Salary', hrs: 42, bounty: false },
  { id: '114', title: 'Mobile App Design System & Micro-Animations', assignee: 'Unassigned', deadline: 'Sep 01, 2026', status: 'Open', budget: '$2,800', budgetType: 'Bounty', hrs: 0, bounty: true },
];

const CANDIDATES = [
  { name: 'Dr. Lucas Meyer', title: 'AI Agent & RAG Pipeline Specialist', match: '98% match', rating: '5.0', rate: '$95/hr', skills: ['AI Agent', 'Python', 'LangChain', 'OpenAI'] },
  { name: 'Sophia Al-Mansoor', title: 'Principal UI/UX & Design Systems Lead', match: '96% match', rating: '5.0', rate: '$80/hr', skills: ['Figma', 'UI/UX Design', 'Design Tokens'] },
  { name: 'Alex Rivera', title: 'Full-Stack Software Architect', match: '94% match', rating: '5.0', rate: '$90/hr', skills: ['TypeScript', 'React', 'Node.js', 'PostgreSQL'] },
];

function statusBadgeClass(s: string) {
  if (s === 'Active') return 'company-badge company-badge-active';
  if (s === 'In Review') return 'company-badge company-badge-review';
  return 'company-badge';
}

function taskStatusClass(s: string) {
  if (s === 'In Progress') return 'company-badge company-badge-contractor';
  if (s === 'PR Review') return 'company-badge company-badge-review';
  return 'company-badge company-badge-bounty';
}

export const CompanyDashboardPage: React.FC = () => {
  const [tab, setTab] = useState<TabId>('roster');
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedTask, setSelectedTask] = useState('Task #102: Custom RAG AI Agent Pipeline');
  const [inviteSuccess, setInviteSuccess] = useState(false);

  const openModal = (taskLabel: string) => { setSelectedTask(taskLabel); setModalOpen(true); };

  const sendInvite = () => {
    setInviteSuccess(true);
    setTimeout(() => { setInviteSuccess(false); setModalOpen(false); }, 1800);
  };

  return (
    <div className="company-page">
      <Header isLoggedIn={true} />

      <main className="company-main">

        {/* Header */}
        <div className="company-header">
          <div className="company-identity">
            <div className="company-logo"><Building2 size={22} /></div>
            <div>
              <h1 className="company-name">
                Acme AI Systems
                <span className="company-verified"><ShieldCheck size={11} /> Verified</span>
              </h1>
              <div className="company-meta">
                <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}><Globe size={12} /> acme.ai</span>
                <span>·</span>
                <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}><MapPin size={12} /> San Francisco, CA</span>
                <span>·</span>
                <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}><Users size={12} /> 12 team members</span>
              </div>
            </div>
          </div>
          <div className="company-actions">
            <button onClick={() => openModal('Task #102: Custom RAG AI Agent Pipeline')} className="company-btn-secondary">
              <UserPlus size={14} /> Add developer
            </button>
            <Link to="/projects/new" className="company-btn-primary">
              <Plus size={14} /> Post bounty
            </Link>
          </div>
        </div>

        {/* Metrics */}
        <div className="company-metrics">
          <div className="company-metric">
            <p className="company-metric-label">Team members <Users size={13} /></p>
            <p className="company-metric-value">12</p>
            <p className="company-metric-sub">8 full-time · 4 contractors</p>
          </div>
          <div className="company-metric">
            <p className="company-metric-label">Active sprints <Layers size={13} /></p>
            <p className="company-metric-value">3</p>
            <p className="company-metric-sub">14 tasks managed</p>
          </div>
          <div className="company-metric">
            <p className="company-metric-label">Hours logged <Activity size={13} /></p>
            <p className="company-metric-value">142 hrs</p>
            <p className="company-metric-sub">94% velocity on track</p>
          </div>
          <div className="company-metric">
            <p className="company-metric-label">Open bounties <Zap size={13} /></p>
            <p className="company-metric-value">2 open</p>
            <p className="company-metric-sub">0% fee escrows</p>
          </div>
        </div>

        {/* Tabs */}
        <div className="company-tabs">
          {TABS.map((t) => (
            <button key={t.id} onClick={() => setTab(t.id)} className={`company-tab${tab === t.id ? ' active' : ''}`}>
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
              <button onClick={() => openModal('Task #102: Custom RAG AI Agent Pipeline')} className="company-btn-primary">
                <UserPlus size={14} /> Add member
              </button>
            </div>
            <div className="company-table-wrap">
              <table className="company-table">
                <thead>
                  <tr>
                    <th>Member</th>
                    <th>Role</th>
                    <th>Type</th>
                    <th>Compensation</th>
                    <th>Active task</th>
                    <th>Hours</th>
                    <th>Status</th>
                    <th style={{ textAlign: 'right' }}>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {TEAM.map((m) => (
                    <tr key={m.id}>
                      <td>
                        <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                          <img src={m.avatar} alt={m.name} style={{ width: 32, height: 32, borderRadius: 9999, objectFit: 'cover', border: '1px solid var(--color-hairline)' }} />
                          <span style={{ fontWeight: 600 }}>{m.name}</span>
                        </div>
                      </td>
                      <td style={{ color: 'var(--color-steel)' }}>{m.role}</td>
                      <td>
                        <span className={m.type === 'Full-Time' ? 'company-badge company-badge-fulltime' : 'company-badge company-badge-contractor'}>{m.type}</span>
                      </td>
                      <td style={{ fontFamily: 'var(--font-mono)', fontWeight: 700 }}>{m.comp}</td>
                      <td style={{ color: 'var(--color-steel)', fontSize: 12, maxWidth: 180, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{m.task}</td>
                      <td style={{ fontFamily: 'var(--font-mono)', fontWeight: 600 }}>{m.hrs} hrs</td>
                      <td>
                        <span className={statusBadgeClass(m.status)}>{m.status}</span>
                      </td>
                      <td style={{ textAlign: 'right' }}>
                        <button style={{ fontSize: 13, color: 'var(--color-primary)', background: 'none', border: 'none', cursor: 'pointer', fontFamily: 'var(--font-sans)' }}>
                          Manage
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Sprints */}
        {tab === 'sprints' && (
          <div className="company-section">
            <div className="company-section-header">
              <div>
                <p className="company-section-title">Active sprint tasks</p>
                <p className="company-section-sub">Track internal milestone deliverables or convert to a public bounty.</p>
              </div>
              <Link to="/projects/new" className="company-btn-primary"><Plus size={14} /> Create task</Link>
            </div>
            <div className="company-task-list">
              {TASKS.map((t) => (
                <div key={t.id} className="company-task-row">
                  <div style={{ flex: 1 }}>
                    <div className="company-task-meta">
                      <span style={{ fontFamily: 'var(--font-mono)', fontWeight: 700, color: 'var(--color-ink)' }}>#{t.id}</span>
                      <span>·</span>
                      <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}><Clock size={12} /> Due {t.deadline}</span>
                      {t.bounty && <span className="company-badge company-badge-bounty">Public bounty</span>}
                    </div>
                    <p className="company-task-title">{t.title}</p>
                    <p className="company-task-sub">
                      Assigned to: <strong style={{ color: 'var(--color-ink)', fontWeight: 600 }}>{t.assignee}</strong>
                      {t.hrs > 0 && <> · {t.hrs} hrs logged</>}
                    </p>
                  </div>
                  <div className="company-task-right">
                    <div style={{ textAlign: 'right' }}>
                      <p className="company-task-budget">{t.budget}</p>
                      <p style={{ fontSize: 11, color: 'var(--color-steel)', marginTop: 2 }}>{t.budgetType}</p>
                      <span className={taskStatusClass(t.status)} style={{ marginTop: 4, display: 'inline-block' }}>{t.status}</span>
                    </div>
                    <button
                      onClick={() => openModal(`Task #${t.id}: ${t.title}`)}
                      className="company-btn-secondary"
                    >
                      <UserPlus size={13} /> Add dev
                    </button>
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
                <p className="company-section-title">Sprint velocity & time telemetry</p>
                <p className="company-section-sub">Real-time breakdown of logged hours across active tasks.</p>
              </div>
            </div>
            <div className="company-telemetry-body">
              {[
                { label: 'Weekly logged hours', value: '142 / 160 hrs', pct: 88, sub: 'Sprint 24 velocity: 88% capacity used' },
                { label: 'Milestone completion rate', value: '94% success', pct: 94, sub: 'Milestones delivered on-time: 15 / 16 tasks' },
              ].map((item) => (
                <div key={item.label} className="company-progress-wrap">
                  <div className="company-progress-label">
                    <span>{item.label}</span>
                    <span style={{ fontFamily: 'var(--font-mono)', fontWeight: 700 }}>{item.value}</span>
                  </div>
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
                  <Zap size={12} style={{ display: 'inline', marginRight: 4 }} />AI Talent Matcher
                </p>
                <h3 className="company-modal-title">Add developer to task</h3>
              </div>
              <button onClick={() => setModalOpen(false)} style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--color-steel)', padding: 4 }}>
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
                {CANDIDATES.map((c, i) => (
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
                        {c.skills.map((s) => <span key={s} className="company-skill-tag">{s}</span>)}
                      </div>
                      <button onClick={sendInvite} className="company-btn-primary">Invite</button>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {inviteSuccess && (
              <div className="company-toast"><CheckCircle2 size={14} /> Invite & escrow offer sent!</div>
            )}
          </div>
        </div>
      )}

      <Footer />
    </div>
  );
};
