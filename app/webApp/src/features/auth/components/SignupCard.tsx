import React, { useState } from 'react';
import { Mail, Lock, User } from 'lucide-react';
import { useAuth } from '../../../context/AuthContext';

interface SignupCardProps {
  onNavigateToLogin?: () => void;
  onSignupSuccess?: (email: string) => void;
}

export const SignupCard: React.FC<SignupCardProps> = ({ onNavigateToLogin, onSignupSuccess }) => {
  const { signup } = useAuth();
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!email) { setError('Please enter your email address'); return; }
    if (!password || password.length < 6) { setError('Password must be at least 6 characters'); return; }
    setError(null);
    setLoading(true);
    try {
      await signup(username || email.split('@')[0], email, password);
      onSignupSuccess?.(email);
    } catch (err: any) {
      setError(err?.message || 'Account registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="auth-form">
      {error && <div className="auth-error">{error}</div>}

      <div className="auth-field">
        <label className="auth-label">Username <span style={{ fontWeight: 400, color: 'var(--color-steel)' }}>(optional)</span></label>
        <div className="auth-input-wrap">
          <span className="auth-input-icon"><User size={15} /></span>
          <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} placeholder="johndoe" className="auth-input" />
        </div>
      </div>

      <div className="auth-field">
        <label className="auth-label">Email address</label>
        <div className="auth-input-wrap">
          <span className="auth-input-icon"><Mail size={15} /></span>
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="you@example.com" className="auth-input" required />
        </div>
      </div>

      <div className="auth-field">
        <label className="auth-label">Password</label>
        <div className="auth-input-wrap">
          <span className="auth-input-icon"><Lock size={15} /></span>
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="At least 6 characters" className="auth-input" required />
        </div>
      </div>

      <button type="submit" disabled={loading} className="auth-submit">
        {loading ? 'Creating account…' : 'Create account'}
      </button>

      <div className="auth-divider"><span>or join with</span></div>

      <div className="auth-social-grid">
        <button type="button" className="auth-social-btn">Google</button>
        <button type="button" className="auth-social-btn">GitHub</button>
      </div>

      <p className="auth-footer">
        Already have an account?{' '}
        <button type="button" onClick={onNavigateToLogin} className="auth-link-btn" style={{ fontWeight: 600, color: 'var(--color-ink)' }}>Log in</button>
      </p>
    </form>
  );
};
