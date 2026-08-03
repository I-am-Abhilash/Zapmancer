import React, { useState } from 'react';
import { Mail, Lock, User } from 'lucide-react';

interface SignupCardProps {
  onNavigateToLogin?: () => void;
  onSignupSuccess?: (email: string) => void;
}

export const SignupCard: React.FC<SignupCardProps> = ({
  onNavigateToLogin,
  onSignupSuccess
}) => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!email) {
      setError('Please enter your email address');
      return;
    }
    if (!password || password.length < 6) {
      setError('Password must be at least 6 characters');
      return;
    }

    setError(null);
    setIsLoading(true);

    // Simulate registration API call
    setTimeout(() => {
      setIsLoading(false);
      onSignupSuccess?.(email);
    }, 1000);
  };

  return (
    <form onSubmit={handleSubmit} className="auth-form-body">
      
      {error && (
        <div className="auth-error-banner">
          {error}
        </div>
      )}

      <div className="auth-field-group">
        <label className="auth-label">Username (Optional)</label>
        <div className="auth-input-wrapper">
          <User className="auth-input-icon" size={18} />
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="johndoe"
            className="auth-input"
          />
        </div>
      </div>

      <div className="auth-field-group">
        <label className="auth-label">Email Address</label>
        <div className="auth-input-wrapper">
          <Mail className="auth-input-icon" size={18} />
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="you@example.com"
            className="auth-input"
            required
          />
        </div>
      </div>

      <div className="auth-field-group">
        <label className="auth-label">Password</label>
        <div className="auth-input-wrapper">
          <Lock className="auth-input-icon" size={18} />
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="At least 6 characters"
            className="auth-input"
            required
          />
        </div>
      </div>

      <button
        type="submit"
        disabled={isLoading}
        className="btn-primary auth-submit-btn"
      >
        {isLoading ? 'Creating account...' : 'Create Account'}
      </button>

      <div className="auth-divider">
        <span>or join with</span>
      </div>

      <div className="auth-social-row">
        <button type="button" className="btn-secondary auth-social-btn">
          Google
        </button>
        <button type="button" className="btn-secondary auth-social-btn">
          Apple
        </button>
      </div>

      <div className="auth-footer-note">
        <span>Already have an account? </span>
        <button
          type="button"
          onClick={onNavigateToLogin}
          className="auth-switch-link"
        >
          Log In
        </button>
      </div>

    </form>
  );
};
