import React from 'react';
import '../auth.css';
import { ArrowLeft, ShieldCheck } from 'lucide-react';

interface AuthLayoutProps {
  title: string;
  subtitle: string;
  onBackToLanding?: () => void;
  children: React.ReactNode;
}

const FEATURES = [
  'Transparent milestone escrow protection',
  '0% platform fee on all earnings',
  'Verified client budget deposits',
  'Automatic IP transfer on payout',
];

export const AuthLayout: React.FC<AuthLayoutProps> = ({ title, subtitle, onBackToLanding, children }) => (
  <div className="auth-page">

    {/* Brand side */}
    <div className="auth-brand">
      <a href="/" className="auth-brand-logo">Zapmancer</a>
      <div>
        <h1 className="auth-brand-headline">The open-source<br />freelance marketplace.</h1>
        <p className="auth-brand-sub" style={{ marginTop: 14 }}>
          Connect with verified clients, post project bounties, and get paid with 100% milestone escrow protection.
        </p>
      </div>
      <div className="auth-brand-features">
        {FEATURES.map((f) => (
          <div key={f} className="auth-brand-feature">
            <ShieldCheck size={15} style={{ color: 'var(--color-primary)', flexShrink: 0 }} />
            {f}
          </div>
        ))}
      </div>
      <p style={{ fontSize: 12, color: 'var(--color-steel)', marginTop: 8 }}>
        © {new Date().getFullYear()} Zapmancer · Open-source KMP marketplace
      </p>
    </div>

    {/* Form side */}
    <div className="auth-form-side">
      {onBackToLanding && (
        <button onClick={onBackToLanding} className="auth-back">
          <ArrowLeft size={14} /> Back to home
        </button>
      )}
      <div className="auth-form-card">
        <div className="auth-heading">
          <h2 className="auth-title">{title}</h2>
          <p className="auth-subtitle">{subtitle}</p>
        </div>
        {children}
      </div>
    </div>

  </div>
);
