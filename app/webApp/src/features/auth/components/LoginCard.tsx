import React, { useState } from 'react';
import { Eye, EyeOff, Lock, Mail } from 'lucide-react';

interface LoginCardProps {
  onNavigateToForgot?: () => void;
  onNavigateToSignup?: () => void;
  onLoginSuccess?: () => void;
}

export const LoginCard: React.FC<LoginCardProps> = ({ onNavigateToForgot, onNavigateToSignup, onLoginSuccess }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPwd, setShowPwd] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!email) { setError('Please enter your email address'); return; }
    if (!password) { setError('Please enter your password'); return; }
    setError(null);
    setLoading(true);
    setTimeout(() => { setLoading(false); onLoginSuccess?.(); }, 1000);
  };

  return (
    <form onSubmit={handleSubmit} className="auth-form">
      {error && <div className="auth-error">{error}</div>}

      <div className="auth-field">
        <label className="auth-label">Email address</label>
        <div className="auth-input-wrap">
          <span className="auth-input-icon"><Mail size={15} /></span>
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="you@example.com" className="auth-input" required />
        </div>
      </div>

      <div className="auth-field">
        <div className="auth-label-row">
          <label className="auth-label">Password</label>
          {onNavigateToForgot && (
            <button type="button" onClick={onNavigateToForgot} className="auth-link-btn">Forgot password?</button>
          )}
        </div>
        <div className="auth-input-wrap">
          <span className="auth-input-icon"><Lock size={15} /></span>
          <input type={showPwd ? 'text' : 'password'} value={password} onChange={(e) => setPassword(e.target.value)} placeholder="••••••••" className="auth-input" style={{ paddingRight: 40 }} required />
          <button type="button" onClick={() => setShowPwd(!showPwd)} className="auth-pwd-toggle" aria-label="Toggle password">
            {showPwd ? <EyeOff size={15} /> : <Eye size={15} />}
          </button>
        </div>
      </div>

      <button type="submit" disabled={loading} className="auth-submit">
        {loading ? 'Signing in…' : 'Log in'}
      </button>

      <div className="auth-divider"><span>or continue with</span></div>

      <div className="auth-social-grid">
        <button type="button" className="auth-social-btn">Google</button>
        <button type="button" className="auth-social-btn">GitHub</button>
      </div>

      <p className="auth-footer">
        Don't have an account?{' '}
        <button type="button" onClick={onNavigateToSignup} className="auth-link-btn" style={{ fontWeight: 600, color: 'var(--color-ink)' }}>Sign up</button>
      </p>
    </form>
  );
};
