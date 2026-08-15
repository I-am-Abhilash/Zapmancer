import React, { useState } from 'react';
import './company.css';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Building2, Users, Layers, DollarSign, Plus, Search, ShieldCheck, Sparkles, UserPlus, CheckCircle2, Clock, ArrowUpRight, Lock, X, Check, Activity, Zap, MapPin, Globe, CreditCard } from 'lucide-react';
import { Link } from 'react-router-dom';
import { Tabs, TabItem } from '../../components/ui/Tabs';

type CompanyTabId = 'roster' | 'sprints' | 'telemetry' | 'payroll';

interface TeamMember {
  id: string;
  name: string;
  role: string;
  type: 'Full-Time' | 'Contractor';
  salaryOrRate: string;
  activeTask: string;
  hoursLogged: number;
  status: 'Active' | 'In Review' | 'On Leave';
  avatar: string;
}

interface CompanyTask {
  id: string;
  title: string;
  assignee: string;
  deadline: string;
  status: 'In Progress' | 'PR Review' | 'Completed';
  budget: string;
  hoursLogged: number;
  isPublicBounty: boolean;
}

export const CompanyDashboardPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<CompanyTabId>('roster');
  const [showAddDevModal, setShowAddDevModal] = useState<boolean>(false);
  const [selectedTaskForDev, setSelectedTaskForDev] = useState<string>('Task #102: Custom RAG AI Agent Pipeline');
  const [invitedSuccess, setInvitedSuccess] = useState<boolean>(false);

  const companyTabs: TabItem<CompanyTabId>[] = [
    { id: 'roster', label: 'Team Roster (4)', icon: Users },
    { id: 'sprints', label: 'Sprint & Task Board (3)', icon: Layers },
    { id: 'telemetry', label: 'Sprint Velocity & Time Logs', icon: Activity },
    { id: 'payroll', label: 'Payroll & Escrow Payouts', icon: CreditCard },
  ];

  const teamMembers: TeamMember[] = [
    {
      id: '1',
      name: 'Elena Rostova',
      role: 'Full-Stack & Web Architect',
      type: 'Contractor',
      salaryOrRate: '$85 / hr',
      activeTask: 'Task #102: Custom RAG AI Agent Pipeline',
      hoursLogged: 38,
      status: 'Active',
      avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80'
    },
    {
      id: '2',
      name: 'Dr. Lucas Meyer',
      role: 'AI Agent Specialist',
      type: 'Full-Time',
      salaryOrRate: '$14,500 / mo',
      activeTask: 'Task #108: Multi-Modal Model Fine-Tuning',
      hoursLogged: 42,
      status: 'Active',
      avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80'
    },
    {
      id: '3',
      name: 'Sophia Al-Mansoor',
      role: 'Lead UI/UX Designer',
      type: 'Full-Time',
      salaryOrRate: '$11,000 / mo',
      activeTask: 'Task #112: Design System Tokens Redesign',
      hoursLogged: 35,
      status: 'In Review',
      avatar: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=150&q=80'
    },
    {
      id: '4',
      name: 'David Kim',
      role: 'DevOps & Infra Specialist',
      type: 'Contractor',
      salaryOrRate: '$90 / hr',
      activeTask: 'Task #115: Kubernetes Cluster Auto-Scaling',
      hoursLogged: 27,
      status: 'Active',
      avatar: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=150&q=80'
    }
  ];

  const companyTasks: CompanyTask[] = [
    {
      id: '102',
      title: 'Custom RAG AI Agent Pipeline Integration',
      assignee: 'Elena Rostova',
      deadline: 'Aug 20, 2026',
      status: 'In Progress',
      budget: '$4,500 Escrow',
      hoursLogged: 38,
      isPublicBounty: false
    },
    {
      id: '108',
      title: 'Multi-Modal LLM Fine-Tuning & Vector Store',
      assignee: 'Dr. Lucas Meyer',
      deadline: 'Aug 25, 2026',
      status: 'PR Review',
      budget: '$3,800 Salary',
      hoursLogged: 42,
      isPublicBounty: false
    },
    {
      id: '114',
      title: 'Mobile App Design System & Micro-Animations',
      assignee: 'Unassigned (Needs Designer)',
      deadline: 'Sep 01, 2026',
      status: 'In Progress',
      budget: '$2,800 Bounty',
      hoursLogged: 0,
      isPublicBounty: true
    }
  ];

  const gorseAiTalentCandidates = [
    {
      name: 'Dr. Lucas Meyer',
      title: 'AI Agent & RAG Pipeline Specialist',
      gorseMatchScore: '98% AI Match',
      rating: '5.0 ★',
      rate: '$95/hr',
      skills: ['AI Agent', 'Python', 'LangChain', 'OpenAI']
    },
    {
      name: 'Sophia Al-Mansoor',
      title: 'Principal UI/UX & Design Systems Lead',
      gorseMatchScore: '96% AI Match',
      rating: '5.0 ★',
      rate: '$80/hr',
      skills: ['Figma', 'UI/UX Design', 'Design Tokens', 'Tailwind']
    },
    {
      name: 'Alex Rivera',
      title: 'Full-Stack Software Architect',
      gorseMatchScore: '94% AI Match',
      rating: '5.0 ★',
      rate: '$90/hr',
      skills: ['TypeScript', 'React', 'Node.js', 'PostgreSQL']
    }
  ];

  const handleSendInvite = () => {
    setInvitedSuccess(true);
    setTimeout(() => {
      setInvitedSuccess(false);
      setShowAddDevModal(false);
    }, 1800);
  };

  return (
    <div className="company-page">
      <Header isLoggedIn={true} />

      <main className="company-main">
        
        {/* Spacious Hero Banner */}
        <section className="company-hero-banner">
          
          {/* Top Meta Row */}
          <div className="company-hero-top-row">
            <span className="company-badge-verified">
              <ShieldCheck className="w-3.5 h-3.5" /> Verified Business Entity
            </span>

            <div className="flex flex-wrap items-center gap-2.5">
              <span className="company-meta-chip">
                <Globe className="w-3 h-3 text-brand-green" /> acme.ai
              </span>
              <span className="company-meta-chip">
                <MapPin className="w-3 h-3 text-brand-green" /> San Francisco, CA
              </span>
              <span className="company-meta-chip">
                <Users className="w-3 h-3 text-brand-green" /> 12 Active Team Members
              </span>
            </div>
          </div>

          {/* Main Identity Group */}
          <div className="company-identity-group">
            <div className="company-logo-box">
              <Building2 className="w-9 h-9" />
            </div>

            <div className="company-title-group">
              <h1 className="company-title">Acme AI Systems</h1>
              <p className="company-subtitle">
                Central Company Operations Workspace. Manage full-time staff, assign sprint tasks, track payroll, and instant-hire expert developers, designers, and AI builders on-demand.
              </p>
            </div>
          </div>

          {/* Actions Row */}
          <div className="company-actions-wrapper">
            <button
              onClick={() => setShowAddDevModal(true)}
              className="company-btn-primary"
            >
              <UserPlus className="w-4 h-4" /> ＋ Add Fellow Dev/Creator to Task
            </button>

            <Link to="/projects/new" className="company-btn-secondary">
              <Plus className="w-4 h-4 text-brand-green" /> Post Public Bounty
            </Link>
          </div>

        </section>

        {/* Key Metrics Grid */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div className="company-metric-card">
            <div className="flex items-center justify-between text-mute">
              <span className="company-metric-label">Team Members</span>
              <Users className="w-4 h-4 text-brand-green" />
            </div>
            <div className="company-metric-value">12</div>
            <div className="text-[11px] text-mute font-medium">8 Full-Time &bull; 4 Contractors</div>
          </div>

          <div className="company-metric-card">
            <div className="flex items-center justify-between text-mute">
              <span className="company-metric-label">Active Sprints</span>
              <Layers className="w-4 h-4 text-brand-green" />
            </div>
            <div className="company-metric-value">3</div>
            <div className="text-[11px] text-mute font-medium">14 Active Tasks Managed</div>
          </div>

          <div className="company-metric-card">
            <div className="flex items-center justify-between text-mute">
              <span className="company-metric-label">Sprint Hours Logged</span>
              <Activity className="w-4 h-4 text-brand-green" />
            </div>
            <div className="company-metric-value">142 hrs</div>
            <div className="text-[11px] text-brand-green font-semibold">94% Sprint Velocity On Track</div>
          </div>

          <div className="company-metric-card">
            <div className="flex items-center justify-between text-mute">
              <span className="company-metric-label">Public Bounties</span>
              <Sparkles className="w-4 h-4 text-brand-green" />
            </div>
            <div className="company-metric-value">2 Open</div>
            <div className="text-[11px] text-mute font-medium">0% Fee Escrows</div>
          </div>
        </div>

        {/* Reusable Central Pill Tab Navigation */}
        <div>
          <Tabs
            tabs={companyTabs}
            activeTab={activeTab}
            onChange={(id) => setActiveTab(id)}
          />
        </div>

        {/* TAB 1: TEAM ROSTER */}
        {activeTab === 'roster' && (
          <div className="company-section-card">
            <div className="company-card-header">
              <div>
                <h3 className="text-xl font-extrabold text-ink">Employee & Contractor Roster</h3>
                <p className="text-xs text-mute mt-0.5">Manage internal staff, contract rates, and active task assignments across all disciplines.</p>
              </div>
              <button onClick={() => setShowAddDevModal(true)} className="company-btn-primary py-2.5 px-5 text-xs">
                <UserPlus className="w-3.5 h-3.5" /> Add Team Member
              </button>
            </div>

            <div className="company-table-container">
              <table className="company-table">
                <thead>
                  <tr>
                    <th>Member Name</th>
                    <th>Role</th>
                    <th>Employment Type</th>
                    <th>Compensation / Rate</th>
                    <th>Active Task</th>
                    <th>Hours Logged</th>
                    <th>Status</th>
                    <th className="text-right">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {teamMembers.map((m) => (
                    <tr key={m.id}>
                      <td>
                        <div className="flex items-center gap-3">
                          <img src={m.avatar} alt={m.name} className="w-8 h-8 rounded-full object-cover border border-hairline" />
                          <div>
                            <p className="font-bold text-ink text-xs">{m.name}</p>
                          </div>
                        </div>
                      </td>
                      <td className="font-semibold text-ink">{m.role}</td>
                      <td>
                        <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold ${
                          m.type === 'Full-Time' ? 'bg-surface-elevated text-ink border border-hairline' : 'bg-brand-green/10 text-brand-green border border-brand-green/20'
                        }`}>
                          {m.type}
                        </span>
                      </td>
                      <td className="font-mono text-brand-green font-bold">{m.salaryOrRate}</td>
                      <td className="text-mute text-xs truncate max-w-[180px]">{m.activeTask}</td>
                      <td className="font-mono font-bold text-ink">{m.hoursLogged} hrs</td>
                      <td>
                        <span className="inline-flex items-center gap-1 text-[11px] font-bold text-brand-green">
                          <CheckCircle2 className="w-3.5 h-3.5" /> {m.status}
                        </span>
                      </td>
                      <td className="text-right">
                        <button className="text-xs font-bold text-mute hover:text-brand-green transition-colors cursor-pointer">
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

        {/* TAB 2: SPRINTS & TASKS */}
        {activeTab === 'sprints' && (
          <div className="company-section-card">
            <div className="company-card-header">
              <div>
                <h3 className="text-xl font-extrabold text-ink">Active Sprint Tasks</h3>
                <p className="text-xs text-mute mt-0.5">Track internal milestone deliverables or convert a task into a public bounty.</p>
              </div>
              <Link to="/projects/new" className="company-btn-primary py-2.5 px-5 text-xs">
                <Plus className="w-3.5 h-3.5" /> Create Task
              </Link>
            </div>

            <div className="space-y-4">
              {companyTasks.map((t) => (
                <div key={t.id} className="company-task-card">
                  <div className="space-y-1 flex-1">
                    <div className="flex items-center gap-2 text-xs">
                      <span className="font-mono text-brand-green font-bold">#{t.id}</span>
                      <span className="text-mute">&bull;</span>
                      <span className="text-mute flex items-center gap-1"><Clock className="w-3.5 h-3.5" /> Due {t.deadline}</span>
                      {t.isPublicBounty && (
                        <span className="company-task-bounty-badge">
                          Public Bounty
                        </span>
                      )}
                    </div>
                    <h4 className="font-extrabold text-base text-ink">{t.title}</h4>
                    <p className="text-xs text-mute">Assigned to: <strong className="text-ink">{t.assignee}</strong> &bull; {t.hoursLogged} hrs logged</p>
                  </div>

                  <div className="flex items-center gap-4 shrink-0">
                    <div className="text-right">
                      <p className="text-sm font-extrabold text-brand-green font-mono">{t.budget}</p>
                      <p className="text-[10px] text-mute font-bold uppercase tracking-wider">{t.status}</p>
                    </div>

                    <button
                      onClick={() => {
                        setSelectedTaskForDev(`Task #${t.id}: ${t.title}`);
                        setShowAddDevModal(true);
                      }}
                      className="company-btn-secondary py-2.5 px-4 text-xs"
                    >
                      <UserPlus className="w-3.5 h-3.5 text-brand-green" /> Add Fellow Dev/Creator
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* TAB 3: TELEMETRY & TIME LOGS */}
        {activeTab === 'telemetry' && (
          <div className="company-section-card">
            <div>
              <h3 className="text-xl font-extrabold text-ink">Sprint Velocity & Time Telemetry</h3>
              <p className="text-xs text-mute mt-0.5">Real-time breakdown of logged hours across active company tasks.</p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="company-telemetry-box">
                <div className="flex items-center justify-between text-xs font-bold text-mute uppercase tracking-wider">
                  <span>Weekly Logged Hours</span>
                  <span className="text-brand-green font-mono font-extrabold">142 / 160 hrs</span>
                </div>
                <div className="company-progress-track">
                  <div className="company-progress-fill w-[88%]"></div>
                </div>
                <p className="text-xs text-mute">Sprint 24 Velocity: <strong className="text-brand-green font-bold">88% Capacity Used</strong></p>
              </div>

              <div className="company-telemetry-box">
                <div className="flex items-center justify-between text-xs font-bold text-mute uppercase tracking-wider">
                  <span>Milestone Completion Rate</span>
                  <span className="text-brand-green font-mono font-extrabold">94% Success</span>
                </div>
                <div className="company-progress-track">
                  <div className="company-progress-fill w-[94%]"></div>
                </div>
                <p className="text-xs text-mute">Milestones Delivered On-Time: <strong className="text-ink font-bold">15 / 16 Tasks</strong></p>
              </div>
            </div>
          </div>
        )}

        {/* TAB 4: PAYROLL & ESCROW */}
        {activeTab === 'payroll' && (
          <div className="company-section-card">
            <div>
              <h3 className="text-xl font-extrabold text-ink">Payroll & Milestone Escrow Overview</h3>
              <p className="text-xs text-mute mt-0.5">Automated monthly employee salary payouts and funded contractor milestone escrows.</p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="company-payroll-box">
                <div className="flex items-center justify-between">
                  <span className="text-xs font-bold uppercase tracking-wider text-mute">Monthly Employee Payroll</span>
                  <span className="text-xs font-bold text-brand-green bg-brand-green/10 px-2 py-0.5 rounded-full border border-brand-green/20">Auto-Deposit Active</span>
                </div>
                <p className="text-3xl font-extrabold text-ink font-mono">$23,500.00</p>
                <p className="text-xs text-mute">Next Payout Schedule: <strong className="text-ink">August 31, 2026</strong> (Direct Deposit)</p>
              </div>

              <div className="company-payroll-box">
                <div className="flex items-center justify-between">
                  <span className="text-xs font-bold uppercase tracking-wider text-mute">Milestone Escrow Vault</span>
                  <span className="text-xs font-bold text-brand-green bg-brand-green/10 px-2 py-0.5 rounded-full border border-brand-green/20">0% Freelancer Fee</span>
                </div>
                <p className="text-3xl font-extrabold text-brand-green font-mono">$25,000.00</p>
                <p className="text-xs text-mute">Protected in Stripe Escrow with 100% Work-for-Hire IP Transfer</p>
              </div>
            </div>
          </div>
        )}

      </main>

      {/* INSTANT FELLOW DEV HIRE MODAL DRAWER WITH AI MATCHING */}
      {showAddDevModal && (
        <div className="company-modal-overlay">
          <div className="company-modal-content">
            
            {/* Modal Header */}
            <div className="flex items-center justify-between border-b border-hairline pb-4">
              <div className="space-y-0.5">
                <div className="flex items-center gap-1.5 text-xs font-bold text-brand-green uppercase tracking-wider">
                  <Sparkles className="w-4 h-4" /> Zapmancer Universal AI Matcher
                </div>
                <h3 className="text-xl font-extrabold text-ink">＋ Add Fellow Dev/Creator to Task</h3>
              </div>
              <button onClick={() => setShowAddDevModal(false)} className="p-2 rounded-full text-mute hover:text-ink cursor-pointer">
                <X className="w-5 h-5" />
              </button>
            </div>

            {/* Target Task Select */}
            <div className="space-y-2">
              <label className="text-xs font-bold uppercase text-mute">Target Company Task</label>
              <div className="p-3 rounded-xl bg-surface-elevated border border-hairline text-xs font-bold text-ink">
                {selectedTaskForDev}
              </div>
            </div>

            {/* Candidate List with AI Match Scores */}
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <label className="text-xs font-bold uppercase text-mute">Top AI-Matched Candidates Across All Disciplines</label>
                <span className="text-[11px] font-bold text-brand-green flex items-center gap-1">
                  <Zap className="w-3.5 h-3.5" /> Recommender Telemetry Active
                </span>
              </div>

              {gorseAiTalentCandidates.map((c, idx) => (
                <div key={idx} className="p-4 rounded-xl bg-surface-elevated border border-hairline space-y-3">
                  <div className="flex items-center justify-between gap-4">
                    <div className="space-y-0.5">
                      <div className="flex items-center gap-2">
                        <h4 className="font-bold text-sm text-ink">{c.name}</h4>
                        <span className="px-2 py-0.5 rounded-full bg-brand-green/10 text-brand-green border border-brand-green/20 text-[10px] font-extrabold">
                          {c.gorseMatchScore}
                        </span>
                      </div>
                      <p className="text-xs text-mute">{c.title}</p>
                    </div>

                    <div className="text-right shrink-0">
                      <p className="text-sm font-extrabold text-brand-green font-mono">{c.rate}</p>
                      <span className="text-xs font-bold text-brand-green">{c.rating}</span>
                    </div>
                  </div>

                  <div className="flex items-center justify-between gap-4 border-t border-hairline pt-2">
                    <div className="flex flex-wrap gap-1.5">
                      {c.skills.map((s) => (
                        <span key={s} className="company-skill-chip">
                          {s}
                        </span>
                      ))}
                    </div>

                    <button
                      onClick={handleSendInvite}
                      className="company-btn-primary py-2 px-5 text-xs"
                    >
                      Invite Worker
                    </button>
                  </div>
                </div>
              ))}
            </div>

            {invitedSuccess && (
              <div className="p-3 rounded-xl bg-brand-green/10 border border-brand-green/30 text-brand-green text-xs font-bold text-center flex items-center justify-center gap-2">
                <CheckCircle2 className="w-4 h-4" /> Candidate Invite & Escrow Offer Sent!
              </div>
            )}

          </div>
        </div>
      )}

      <Footer />
    </div>
  );
};
