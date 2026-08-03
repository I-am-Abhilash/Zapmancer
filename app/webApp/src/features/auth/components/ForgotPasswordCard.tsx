import React, { useState } from 'react';
import { Mail, CheckCircle2 } from 'lucide-react';

interface ForgotPasswordCardProps {
  onBackToLogin?: () => void;
}

export const ForgotPasswordCard: React.FC<ForgotPasswordCardProps> = ({
  onBackToLogin
}) => {
  const [email, setEmail] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!email) {
      setError('Please enter your email address');
      return;
    }

    setError(null);
    setIsLoading(true);

    // Simulate recovery link request
    setTimeout(() => {
      setIsLoading(false);
      setIsSuccess(true);
    }, 1000);
  };

  return (
    <form onSubmit={handleSubmit} className="auth-form-body">
      
      {error && (
        <div className="auth-error-banner">
          {error}
        </div>
      )}

      {isSuccess ? (
        <div className="auth-success-box">
          <CheckCircle2 size={32} className="auth-success-icon" />
          <h3 className="auth-success-title">Recovery Email Sent</h3>
          <p className="auth-success-desc">
            We've sent password reset instructions to <strong>{email}</strong>. Check your inbox and follow the link.
          </p>
          <button
            type="button"
            onClick={onBackToLogin}
            className="btn-primary auth-submit-btn"
          >
            Back to Log In
          </button>
        </div>
      ) : (
        <>
          <div className="auth-field-group">
            <label className="auth-label">Registered Email Address</label>
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

          <button
            type="submit"
            disabled={isLoading}
            className="btn-primary auth-submit-btn"
          >
            {isLoading ? 'Sending reset link...' : 'Send Reset Link'}
          </button>

          <div className="auth-footer-note">
            <button
              type="button"
              onClick={onBackToLogin}
              className="auth-switch-link"
            >
              &larr; Back to Log In
            </button>
          </div>
        </>
      )}

    </form>
  );
};
