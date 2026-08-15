import React, { useState, useRef } from 'react';

interface VerificationCardProps {
  email: string;
  onVerificationSuccess?: () => void;
  onBackToSignup?: () => void;
}

export const VerificationCard: React.FC<VerificationCardProps> = ({ email, onVerificationSuccess, onBackToSignup }) => {
  const CODE_LEN = 6;
  const [digits, setDigits] = useState<string[]>(Array(CODE_LEN).fill(''));
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const refs = useRef<(HTMLInputElement | null)[]>([]);

  const handleChange = (idx: number, val: string) => {
    if (!/^\d*$/.test(val)) return;
    const next = [...digits];
    next[idx] = val.slice(-1);
    setDigits(next);
    if (val && idx < CODE_LEN - 1) refs.current[idx + 1]?.focus();
  };

  const handleKeyDown = (idx: number, e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Backspace' && !digits[idx] && idx > 0) refs.current[idx - 1]?.focus();
  };

  const handlePaste = (e: React.ClipboardEvent<HTMLInputElement>) => {
    e.preventDefault();
    const text = e.clipboardData.getData('text').trim();
    if (!/^\d+$/.test(text)) return;
    const next = [...digits];
    text.slice(0, CODE_LEN).split('').forEach((d, i) => { next[i] = d; });
    setDigits(next);
    refs.current[Math.min(text.length, CODE_LEN - 1)]?.focus();
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (digits.join('').length < CODE_LEN) { setError('Please enter all 6 digits'); return; }
    setError(null);
    setLoading(true);
    setTimeout(() => { setLoading(false); onVerificationSuccess?.(); }, 1000);
  };

  return (
    <form onSubmit={handleSubmit} className="auth-form">
      {error && <div className="auth-error">{error}</div>}

      <p style={{ fontSize: 14, color: 'var(--color-steel)', textAlign: 'center', lineHeight: 1.55 }}>
        Enter the 6-digit code sent to <strong style={{ color: 'var(--color-ink)' }}>{email || 'your email'}</strong>
      </p>

      <div className="auth-otp-grid">
        {digits.map((d, i) => (
          <input
            key={i}
            ref={(el) => (refs.current[i] = el)}
            type="text"
            inputMode="numeric"
            maxLength={1}
            value={d}
            onChange={(e) => handleChange(i, e.target.value)}
            onKeyDown={(e) => handleKeyDown(i, e)}
            onPaste={handlePaste}
            className={`auth-otp-box${d ? ' filled' : ''}`}
            required
          />
        ))}
      </div>

      <button type="submit" disabled={loading || digits.join('').length < CODE_LEN} className="auth-submit">
        {loading ? 'Verifying…' : 'Verify code'}
      </button>

      <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 8 }}>
        <button type="button" className="auth-link-btn" style={{ color: 'var(--color-steel)' }}>
          Didn't receive a code? Resend
        </button>
        {onBackToSignup && (
          <button type="button" onClick={onBackToSignup} className="auth-link-btn" style={{ fontWeight: 600, color: 'var(--color-ink)' }}>
            Change email address
          </button>
        )}
      </div>
    </form>
  );
};
