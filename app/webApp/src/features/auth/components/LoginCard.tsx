import React, { useState } from 'react';
import { Eye, EyeOff, Lock, Mail } from 'lucide-react';

interface LoginCardProps {
  onNavigateToForgot?: () => void;
  onNavigateToSignup?: () => void;
  onLoginSuccess?: () => void;
}

export const LoginCard: React.FC<LoginCardProps> = ({
  onNavigateToForgot,
  onNavigateToSignup,
  onLoginSuccess
}) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!email) {
      setError('Please enter your email address');
      return;
    }
    if (!password) {
      setError('Please enter your password');
      return;
    }

    setError(null);
    setIsLoading(true);

    // Simulate authentication API call
    setTimeout(() => {
      setIsLoading(false);
      onLoginSuccess?.();
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
        <div className="auth-label-row">
          <label className="auth-label">Password</label>
          {onNavigateToForgot && (
            <button
              type="button"
              onClick={onNavigateToForgot}
              className="auth-forgot-link"
            >
              Forgot Password?
            </button>
          )}
        </div>
        <div className="auth-input-wrapper">
          <Lock className="auth-input-icon" size={18} />
          <input
            type={showPassword ? 'text' : 'password'}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="••••••••"
            className="auth-input"
            required
          />
          <button
            type="button"
            onClick={() => setShowPassword(!showPassword)}
            className="auth-toggle-pwd"
            aria-label="Toggle password visibility"
          >
            {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
          </button>
        </div>
      </div>

      <button
        type="submit"
        disabled={isLoading}
        className="btn-primary auth-submit-btn"
      >
        {isLoading ? 'Signing in...' : 'Log In'}
      </button>

      <div className="auth-divider">
        <span>or continue with</span>
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
        <span>Don't have an account? </span>
        <button
          type="button"
          onClick={onNavigateToSignup}
          className="auth-switch-link"
        >
          Sign Up
        </button>
      </div>

    </form>
  );
};
