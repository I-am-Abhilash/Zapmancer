import React, { useState } from 'react';
import { Mail, CheckCircle2 } from 'lucide-react';

interface ForgotPasswordCardProps {
  onBackToLogin?: () => void;
}

export const ForgotPasswordCard: React.FC<ForgotPasswordCardProps> = ({ onBackToLogin }) => {
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!email) { setError('Please enter your email address'); return; }
    setError(null);
    setLoading(true);
    setTimeout(() => { setLoading(false); setSuccess(true); }, 1000);
  };

  if (success) {
    return (
      <div className="auth-success">
        <div className="auth-success-icon"><CheckCircle2 size={24} /></div>
        <p style={{ fontSize: 16, fontWeight: 700, color: 'var(--color-ink)' }}>Reset link sent</p>
        <p style={{ fontSize: 14, color: 'var(--color-steel)', lineHeight: 1.55 }}>
          We've sent instructions to <strong style={{ color: 'var(--color-ink)' }}>{email}</strong>. Check your inbox.
        </p>
        <button onClick={onBackToLogin} className="auth-submit" style={{ marginTop: 8 }}>
          Back to log in
        </button>
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit} className="auth-form">
      {error && <div className="auth-error">{error}</div>}

      <div className="auth-field">
        <label className="auth-label">Registered email address</label>
        <div className="auth-input-wrap">
          <span className="auth-input-icon"><Mail size={15} /></span>
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="you@example.com" className="auth-input" required />
        </div>
      </div>

      <button type="submit" disabled={loading} className="auth-submit">
        {loading ? 'Sending…' : 'Send reset link'}
      </button>

      <p className="auth-footer">
        <button type="button" onClick={onBackToLogin} className="auth-link-btn" style={{ fontWeight: 600, color: 'var(--color-ink)' }}>
          ← Back to log in
        </button>
      </p>
    </form>
  );
};
