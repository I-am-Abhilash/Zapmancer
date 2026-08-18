import React, { useState, useEffect } from 'react';
import './profile.css';
import { Link, useParams } from 'react-router-dom';
import { ShieldCheck, Star, MapPin, MessageSquare, Edit3, Share2, Check, ExternalLink, CheckCircle2, Clock, ChevronDown, ChevronUp, X, Loader2 } from 'lucide-react';
import { userService, TalentProfile } from '../../services/userService';

type Tab = 'overview' | 'history' | 'portfolio' | 'certifications';

const TABS: { id: Tab; label: string }[] = [
  { id: 'overview', label: 'Overview' },
  { id: 'history', label: 'Work History' },
  { id: 'portfolio', label: 'Repositories' },
  { id: 'certifications', label: 'Verifications' },
];

export const ProfilePage: React.FC = () => {
  const { id } = useParams();
  const [profile, setProfile] = useState<TalentProfile | null>(null);
  const [loading, setLoading] = useState(true);
  const [tab, setTab] = useState<Tab>('overview');
  const [copiedLink, setCopiedLink] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [expanded, setExpanded] = useState<number | null>(null);

  useEffect(() => {
    let isMounted = true;
    setLoading(true);
    userService.getUserProfile(id || 'me')
      .then((data) => {
        if (isMounted) {
          setProfile(data);
          setLoading(false);
        }
      })
      .catch(() => {
        if (isMounted) setLoading(false);
      });

    return () => { isMounted = false; };
  }, [id]);

  const copyLink = () => {
    navigator.clipboard.writeText(window.location.href);
    setCopiedLink(true);
    setTimeout(() => setCopiedLink(false), 2000);
  };

  if (loading || !profile) {
    return (
      <div className="min-h-[60vh] flex flex-col items-center justify-center gap-3">
        <Loader2 className="w-8 h-8 text-primary animate-spin" />
        <p className="text-sm font-semibold text-mute">Loading engineer profile & reputation...</p>
      </div>
    );
  }

  const isMe = id === 'me' || id === undefined;

  return (
    <div className="profile-page">
      <main className="profile-main">

        {/* Hero */}
        <div className="profile-hero">
          <div className="profile-hero-top">
            <div className="profile-identity">
              <div className="profile-avatar-wrap">
                <img src={profile.avatar} alt={profile.name} className="profile-avatar" />
                <span className="profile-online-dot" title="Available" />
              </div>
              <div>
                <h1 className="profile-name">
                  {profile.name}
                  <span className="profile-verified"><ShieldCheck size={11} /> KYC Verified</span>
                </h1>
                <p className="profile-title">{profile.title}</p>
                <div className="profile-meta">
                  <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}><MapPin size={12} /> {profile.location}</span>
                  <span>·</span>
                  <span className="profile-rating">
                    <Star size={12} fill="#f59e0b" style={{ color: '#f59e0b' }} />
                    {profile.rating} ({profile.reviews} reviews)
                  </span>
                </div>
              </div>
            </div>

            <div className="profile-actions">
              <button onClick={copyLink} className="profile-btn-ghost">
                {copiedLink ? <><Check size={13} /> Copied</> : <><Share2 size={13} /> Share</>}
              </button>
              {isMe ? (
                <Link to="/profile/edit" className="profile-btn-primary">
                  <Edit3 size={13} /> Edit profile
                </Link>
              ) : (
                <button onClick={() => setShowModal(true)} className="profile-btn-primary">
                  <MessageSquare size={13} /> Contact
                </button>
              )}
            </div>
          </div>

          {/* Stats */}
          <div className="profile-stats">
            <div className="profile-stat">
              <p className="profile-stat-label">Hourly rate</p>
              <p className="profile-stat-value">{profile.rateDisplay}</p>
            </div>
            <div className="profile-stat">
              <p className="profile-stat-label">Total earned</p>
              <p className="profile-stat-value">{profile.earnedDisplay}</p>
            </div>
            <div className="profile-stat">
              <p className="profile-stat-label">Contracts</p>
              <p className="profile-stat-value">{profile.contracts}</p>
            </div>
            <div className="profile-stat">
              <p className="profile-stat-label">On-time</p>
              <p className="profile-stat-value">{profile.onTime}</p>
            </div>
          </div>
        </div>

        {/* Tabs */}
        <div className="profile-tabs">
          {TABS.map((t) => (
            <button key={t.id} onClick={() => setTab(t.id)} className={`profile-tab${tab === t.id ? ' active' : ''}`}>
              {t.label}
            </button>
          ))}
        </div>

        {/* Overview */}
        {tab === 'overview' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
            <div className="profile-section">
              <p className="profile-section-header">About</p>
              <div className="profile-section-body">
                <p className="profile-bio">{profile.bio}</p>
                <span className="profile-avail"><Clock size={13} /> {profile.availability}</span>
              </div>
            </div>
            <div className="profile-section">
              <p className="profile-section-header">Technical stack</p>
              <div className="profile-section-body">
                <div className="profile-skills">
                  {profile.skills.map((s) => <span key={s} className="profile-skill">{s}</span>)}
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Work history */}
        {tab === 'history' && (
          <div className="profile-section">
            <p className="profile-section-header">Work history & milestone reviews</p>
            <div className="profile-section-body">
              {profile.workHistory.length === 0 ? (
                <p className="text-sm text-mute p-4">No public milestone reviews completed yet.</p>
              ) : (
                profile.workHistory.map((w, idx) => (
                  <div key={idx} className="profile-history-item">
                    <div className="profile-history-top" onClick={() => setExpanded(expanded === idx ? null : idx)}>
                      <div style={{ flex: 1 }}>
                        <p className="profile-history-project">{w.project}</p>
                        <div className="profile-history-meta">
                          <span style={{ display: 'flex', alignItems: 'center', gap: 3 }}>
                            <Star size={11} fill="#f59e0b" style={{ color: '#f59e0b' }} /> {w.rating}
                          </span>
                          <span>·</span>
                          <span style={{ fontWeight: 600, color: 'var(--color-ink)' }}>{w.client}</span>
                          <span>·</span>
                          <span>{w.date}</span>
                        </div>
                        <p className="profile-review">"{w.review}"</p>
                      </div>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 10, flexShrink: 0 }}>
                        <p className="profile-history-earned">{w.earned}</p>
                        {expanded === idx ? <ChevronUp size={15} style={{ color: 'var(--color-steel)' }} /> : <ChevronDown size={15} style={{ color: 'var(--color-steel)' }} />}
                      </div>
                    </div>
                    {expanded === idx && (
                      <div className="profile-milestones">
                        {w.milestones.map((m, mi) => (
                          <div key={mi} className="profile-milestone-row">
                            <span className="profile-milestone-name">{m.name}</span>
                            <span className="profile-milestone-amount">{m.amount}</span>
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                ))
              )}
            </div>
          </div>
        )}

        {/* Portfolio */}
        {tab === 'portfolio' && (
          <div className="profile-section">
            <p className="profile-section-header">Code repositories</p>
            <div className="profile-section-body">
              <div className="profile-repo-grid">
                {profile.portfolio.length === 0 ? (
                  <p className="text-sm text-mute p-4">No connected public GitHub repositories.</p>
                ) : (
                  profile.portfolio.map((r, i) => (
                    <div key={i} className="profile-repo">
                      <div className="profile-repo-name">
                        <span>{r.name}</span>
                        <span className="profile-repo-stars"><Star size={12} fill="#f59e0b" style={{ color: '#f59e0b' }} /> {r.stars}</span>
                      </div>
                      <p className="profile-repo-desc">{r.desc}</p>
                      <a href={r.url} target="_blank" rel="noreferrer" className="profile-repo-link">
                        View code <ExternalLink size={12} />
                      </a>
                    </div>
                  ))
                )}
              </div>
            </div>
          </div>
        )}

        {/* Verifications */}
        {tab === 'certifications' && (
          <div className="profile-section">
            <p className="profile-section-header">Verifications & security</p>
            <div className="profile-section-body">
              <div className="profile-verif-grid">
                <div className="profile-verif-item">
                  <p className="profile-verif-title"><CheckCircle2 size={15} style={{ color: '#1aae39' }} /> KYC Identity Verified</p>
                  <p className="profile-verif-desc">Government-issued photo ID and proof of address verified via automated biometric security.</p>
                </div>
                <div className="profile-verif-item">
                  <p className="profile-verif-title"><ShieldCheck size={15} style={{ color: 'var(--color-primary)' }} /> Escrow Milestone Protection</p>
                  <p className="profile-verif-desc">100% of contract funds locked in escrow before work commences on any milestone.</p>
                </div>
              </div>
            </div>
          </div>
        )}

      </main>

      {/* Contact modal */}
      {showModal && (
        <div className="profile-modal-overlay" onClick={() => setShowModal(false)}>
          <div className="profile-modal" onClick={(e) => e.stopPropagation()}>
            <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 12 }}>
              <div>
                <h2 className="profile-modal-title">Contact {profile.name}</h2>
                <p style={{ fontSize: 13, color: 'var(--color-steel)', marginTop: 4 }}>
                  Send a direct message or invite them to apply for your active contract.
                </p>
              </div>
              <button onClick={() => setShowModal(false)} style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--color-steel)', padding: 4 }}>
                <X size={18} />
              </button>
            </div>
            <textarea
              rows={4}
              placeholder={`Hi ${profile.name}, we have a KMP project and would love to discuss...`}
              className="profile-modal-textarea"
            />
            <div className="profile-modal-actions">
              <button onClick={() => setShowModal(false)} className="profile-modal-btn-cancel">Cancel</button>
              <Link to="/messages" onClick={() => setShowModal(false)} className="profile-modal-btn-send">
                <MessageSquare size={13} /> Send message
              </Link>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
