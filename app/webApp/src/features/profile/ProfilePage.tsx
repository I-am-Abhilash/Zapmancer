import React, { useState } from 'react';
import './profile.css';
import { Link, useParams } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, Star, MapPin, Edit3, MessageSquare, Briefcase, GitBranch, ExternalLink, Award, CheckCircle2, Clock, Sparkles, Share2, Check, X, ChevronDown, ChevronUp } from 'lucide-react';
import { Tabs, TabItem } from '../../components/ui/Tabs';

type ProfileTabId = 'overview' | 'history' | 'portfolio' | 'certifications';

export const ProfilePage: React.FC = () => {
  const { id } = useParams();
  const [activeTab, setActiveTab] = useState<ProfileTabId>('overview');
  const [copiedLink, setCopiedLink] = useState(false);
  const [showHireModal, setShowHireModal] = useState(false);
  const [expandedHistory, setExpandedHistory] = useState<number | null>(null);

  const profileTabs: TabItem<ProfileTabId>[] = [
    { id: 'overview', label: 'Overview', icon: Sparkles },
    { id: 'history', label: 'Work History & Reviews', icon: Briefcase },
    { id: 'portfolio', label: 'Repositories & Code', icon: GitBranch },
    { id: 'certifications', label: 'Verifications', icon: Award },
  ];

  const profile = {
    name: 'Alex Morgan',
    title: 'Senior KMP & Full-Stack Architect',
    avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
    rating: 4.98,
    reviewsCount: 34,
    rate: '$85 / hr',
    totalEarned: '$68,000+',
    jobsCompleted: 18,
    onTimeRate: '100%',
    availability: 'Available (30 hrs/wk)',
    location: 'San Francisco, USA',
    bio: 'Passionate Kotlin Multiplatform engineer with 6+ years of experience architecting cross-platform desktop & mobile apps connected to high-throughput Ktor backend microservices.',
    skills: ['Kotlin Multiplatform', 'Compose UI', 'Ktor Server', 'PostgreSQL', 'WebAssembly', 'Docker'],
    workHistory: [
      {
        project: 'Compose Multiplatform Mobile Wallet Core',
        client: 'Fintech Core Ltd',
        rating: 5.0,
        earned: '$12,500',
        date: 'Jan 2026 - Mar 2026',
        review: 'Alex delivered top tier KMP code with 100% test coverage ahead of schedule.',
        milestones: [
          { name: 'Shared KMP Wallet Logic', amount: '$4,500' },
          { name: 'Compose Mobile Views', amount: '$4,500' },
          { name: 'CI Pipeline & Security Audit', amount: '$3,500' },
        ]
      },
      {
        project: 'Real-Time Ktor WebSockets Chat Engine',
        client: 'Zapmancer Labs',
        rating: 4.9,
        earned: '$8,200',
        date: 'Nov 2025 - Dec 2025',
        review: 'Outstanding microservice architecture and clear milestone documentation.',
        milestones: [
          { name: 'Ktor Channels & Session Store', amount: '$4,200' },
          { name: 'Redis Pub/Sub Integration', amount: '$4,000' },
        ]
      }
    ],
    portfolio: [
      { name: 'KMP Multiplatform Wallet', stars: '1.2k', desc: 'Compose Mobile & Web Wasm wallet app with SQLDelight persistence', url: 'https://github.com' },
      { name: 'Ktor Exposed Microservice Template', stars: '840', desc: 'High concurrency starter template with HikariCP & WebSockets', url: 'https://github.com' },
      { name: 'Compose Desktop Metrics Engine', stars: '490', desc: 'Desktop analytics app with native charts and CSV exports', url: 'https://github.com' },
    ]
  };

  const handleCopyLink = () => {
    navigator.clipboard.writeText(window.location.href);
    setCopiedLink(true);
    setTimeout(() => setCopiedLink(false), 2000);
  };

  return (
    <div className="profile-page">
      <Header isLoggedIn={true} />

      <main className="profile-main">
        
        {/* Above The Fold: Clean Hero Profile Summary Card */}
        <div className="profile-hero-card">
          
          <div className="flex flex-col md:flex-row md:items-center justify-between gap-8">
            
            {/* Left: Avatar & High-Level Identity */}
            <div className="flex items-center gap-6">
              <div className="profile-avatar-wrapper">
                <img
                  src={profile.avatar}
                  alt={profile.name}
                  className="profile-avatar-img"
                />
                <span className="profile-status-dot" title="Available for hire"></span>
              </div>

              <div className="space-y-1.5">
                <div className="profile-name-row">
                  <h1 className="profile-name">
                    {profile.name}
                  </h1>
                  <span className="profile-verified-badge">
                    <ShieldCheck className="w-3.5 h-3.5" /> Verified
                  </span>
                </div>

                <p className="text-sm font-bold text-brand-green">{profile.title}</p>

                <div className="profile-meta-row">
                  <span className="flex items-center gap-1"><MapPin className="w-3.5 h-3.5 text-brand-green" /> {profile.location}</span>
                  <span>&bull;</span>
                  <span className="flex items-center gap-1 text-brand-green font-bold"><Star className="w-3.5 h-3.5 fill-current" /> {profile.rating} ({profile.reviewsCount})</span>
                </div>
              </div>
            </div>

            {/* Right: Actions */}
            <div className="profile-cta-group">
              <button
                onClick={handleCopyLink}
                className="profile-btn-share"
              >
                {copiedLink ? <Check className="w-3.5 h-3.5 text-brand-green" /> : <Share2 className="w-3.5 h-3.5" />}
                {copiedLink ? 'Copied' : 'Share'}
              </button>

              <Link
                to="/profile/edit"
                className="profile-btn-edit"
              >
                Edit
              </Link>

              <button
                onClick={() => setShowHireModal(true)}
                className="profile-btn-primary"
              >
                Contact Engineer
              </button>
            </div>

          </div>

          {/* 80/20 Metrics Bar */}
          <div className="profile-stats-grid">
            <div className="profile-stat-box">
              <p className="profile-stat-label">Hourly Rate</p>
              <p className="profile-stat-val-green">{profile.rate}</p>
            </div>
            <div className="profile-stat-box">
              <p className="profile-stat-label">Earned</p>
              <p className="profile-stat-val-ink">{profile.totalEarned}</p>
            </div>
            <div className="profile-stat-box">
              <p className="profile-stat-label">Contracts</p>
              <p className="profile-stat-val-ink">{profile.jobsCompleted}</p>
            </div>
            <div className="profile-stat-box">
              <p className="profile-stat-label">On-Time</p>
              <p className="profile-stat-val-green">{profile.onTimeRate}</p>
            </div>
          </div>

        </div>

        {/* Tabbed Navigation */}
        <div>
          <Tabs
            tabs={profileTabs}
            activeTab={activeTab}
            onChange={(id) => setActiveTab(id)}
          />
        </div>

        {/* Tab Content */}
        {activeTab === 'overview' && (
          <div className="space-y-6">
            <div className="profile-card">
              <h2 className="profile-card-title">About & Background</h2>
              <p className="text-sm text-mute leading-relaxed">
                {profile.bio}
              </p>
              <div className="inline-flex items-center gap-1.5 px-3.5 py-1 rounded-full bg-brand-green/10 text-brand-green text-xs font-bold">
                <Clock className="w-3.5 h-3.5" /> {profile.availability}
              </div>
            </div>

            <div className="profile-card">
              <h2 className="profile-card-title">Technical Stack</h2>
              <div className="flex flex-wrap gap-2">
                {profile.skills.map((skill) => (
                  <span key={skill} className="profile-skill-chip">
                    {skill}
                  </span>
                ))}
              </div>
            </div>
          </div>
        )}

        {activeTab === 'history' && (
          <div className="profile-card">
            <h2 className="profile-card-title">Work History & Milestone Reviews</h2>
            <div className="space-y-4">
              {profile.workHistory.map((w, idx) => {
                const isExpanded = expandedHistory === idx;
                return (
                  <div key={idx} className="p-6 rounded-xl bg-surface-elevated border border-hairline space-y-3">
                    <div className="flex items-center justify-between">
                      <h3 className="font-bold text-ink text-base">{w.project}</h3>
                      <span className="text-sm font-extrabold text-brand-green">{w.earned}</span>
                    </div>

                    <div className="flex items-center gap-3 text-xs text-mute">
                      <span className="flex items-center gap-1 text-brand-green font-bold">
                        <Star className="w-3.5 h-3.5 fill-current" /> {w.rating}
                      </span>
                      <span>&bull;</span>
                      <span className="font-semibold text-ink">{w.client}</span>
                      <span>&bull;</span>
                      <span>{w.date}</span>
                    </div>

                    <p className="text-xs text-mute italic bg-surface p-4 rounded-lg border border-hairline leading-relaxed">
                      "{w.review}"
                    </p>

                    <div>
                      <button
                        onClick={() => setExpandedHistory(isExpanded ? null : idx)}
                        className="inline-flex items-center gap-1 text-xs font-bold text-brand-green pt-1"
                      >
                        {isExpanded ? <ChevronUp className="w-3.5 h-3.5" /> : <ChevronDown className="w-3.5 h-3.5" />}
                        {isExpanded ? 'Hide Milestones' : 'View Milestones'}
                      </button>

                      {isExpanded && (
                        <div className="mt-3 space-y-2 border-t border-hairline pt-3">
                          {w.milestones.map((m, mIdx) => (
                            <div key={mIdx} className="flex items-center justify-between text-xs p-2.5 rounded-lg bg-surface border border-hairline">
                              <span className="font-medium text-ink">{m.name}</span>
                              <span className="font-bold text-brand-green">{m.amount}</span>
                            </div>
                          ))}
                        </div>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {activeTab === 'portfolio' && (
          <div className="profile-card">
            <h2 className="profile-card-title">Code Repositories</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              {profile.portfolio.map((repo, idx) => (
                <div key={idx} className="p-6 rounded-xl bg-surface-elevated border border-hairline space-y-3 flex flex-col justify-between">
                  <div className="space-y-2">
                    <div className="flex items-center justify-between">
                      <h3 className="font-bold text-ink text-sm truncate">{repo.name}</h3>
                      <span className="flex items-center gap-1 text-xs font-bold text-brand-green">
                        <Star className="w-3.5 h-3.5 fill-current" /> {repo.stars}
                      </span>
                    </div>
                    <p className="text-xs text-mute leading-relaxed">{repo.desc}</p>
                  </div>

                  <a
                    href={repo.url}
                    target="_blank"
                    rel="noreferrer"
                    className="inline-flex items-center gap-1.5 text-xs font-bold text-brand-green uppercase tracking-wider pt-2"
                  >
                    View Code <ExternalLink className="w-3.5 h-3.5" />
                  </a>
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === 'certifications' && (
          <div className="profile-card">
            <h2 className="profile-card-title">Verifications & Security</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="p-6 rounded-xl bg-surface-elevated border border-hairline space-y-2">
                <div className="flex items-center gap-2 text-brand-green font-bold text-sm">
                  <CheckCircle2 className="w-5 h-5" /> KYC Identity Verified
                </div>
                <p className="text-xs text-mute leading-relaxed">
                  Government-issued photo ID and proof of address verified via automated biometric security.
                </p>
              </div>

              <div className="p-6 rounded-xl bg-surface-elevated border border-hairline space-y-2">
                <div className="flex items-center gap-2 text-brand-green font-bold text-sm">
                  <ShieldCheck className="w-5 h-5" /> Escrow Milestone Protection
                </div>
                <p className="text-xs text-mute leading-relaxed">
                  100% of contract funds locked in smart escrow before work commences on any milestone.
                </p>
              </div>
            </div>
          </div>
        )}

      </main>

      {/* Inquiry Modal */}
      {showHireModal && (
        <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4" onClick={() => setShowHireModal(false)}>
          <div className="bg-surface border border-hairline rounded-xl p-8 w-full max-w-md relative space-y-6 text-ink shadow-2xl" onClick={(e) => e.stopPropagation()}>
            <button onClick={() => setShowHireModal(false)} className="absolute top-4 right-4 p-2 rounded-full text-mute hover:text-ink">
              <X size={18} />
            </button>

            <div className="space-y-1">
              <h2 className="text-2xl font-extrabold text-ink">Contact {profile.name}</h2>
              <p className="text-xs text-mute">Send a direct message or invite {profile.name} to apply for your active bounty.</p>
            </div>

            <div className="space-y-4">
              <textarea
                rows={4}
                placeholder={`Hi ${profile.name}, we have a KMP project bounty and would love to discuss...`}
                className="w-full px-4 py-3 rounded-xl border border-hairline bg-surface-elevated text-sm text-ink focus:outline-none focus:border-brand-green"
              />

              <div className="flex items-center justify-end gap-3 pt-2">
                <button onClick={() => setShowHireModal(false)} className="px-5 py-2.5 rounded-full border border-hairline text-xs font-bold text-mute hover:text-ink">
                  Cancel
                </button>
                <Link
                  to="/messages"
                  onClick={() => setShowHireModal(false)}
                  className="bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] px-6 py-2.5 rounded-full shadow-md shadow-brand-green/20"
                >
                  Send Inquiry
                </Link>
              </div>
            </div>

          </div>
        </div>
      )}

      <Footer />
    </div>
  );
};
