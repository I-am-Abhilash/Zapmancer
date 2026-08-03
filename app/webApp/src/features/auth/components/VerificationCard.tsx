import React, { useState, useRef } from 'react';

interface VerificationCardProps {
  email: string;
  onVerificationSuccess?: () => void;
  onBackToSignup?: () => void;
}

export const VerificationCard: React.FC<VerificationCardProps> = ({
  email,
  onVerificationSuccess,
  onBackToSignup
}) => {
  const codeLength = 6;
  const [digits, setDigits] = useState<string[]>(Array(codeLength).fill(''));
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const inputRefs = useRef<(HTMLInputElement | null)[]>([]);

  const handleDigitChange = (index: number, value: string) => {
    if (!/^\d*$/.test(value)) return;

    const newDigits = [...digits];
    newDigits[index] = value.slice(-1);
    setDigits(newDigits);

    // Auto focus next box
    if (value && index < codeLength - 1) {
      inputRefs.current[index + 1]?.focus();
    }
  };

  const handleKeyDown = (index: number, e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Backspace' && !digits[index] && index > 0) {
      inputRefs.current[index - 1]?.focus();
    }
  };

  const handlePaste = (e: React.ClipboardEvent<HTMLInputElement>) => {
    e.preventDefault();
    const pasteData = e.clipboardData.getData('text').trim();
    if (!/^\d+$/.test(pasteData)) return;

    const pastedDigits = pasteData.slice(0, codeLength).split('');
    const newDigits = [...digits];
    pastedDigits.forEach((digit, i) => {
      newDigits[i] = digit;
    });
    setDigits(newDigits);
    inputRefs.current[Math.min(pastedDigits.length, codeLength - 1)]?.focus();
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const fullCode = digits.join('');
    if (fullCode.length < codeLength) {
      setError('Please enter all 6 digits of the verification code');
      return;
    }

    setError(null);
    setIsLoading(true);

    // Simulate OTP verification API call
    setTimeout(() => {
      setIsLoading(false);
      onVerificationSuccess?.();
    }, 1000);
  };

  return (
    <form onSubmit={handleSubmit} className="auth-form-body">
      
      {error && (
        <div className="auth-error-banner">
          {error}
        </div>
      )}

      <p className="auth-otp-notice">
        Enter the 6-digit security code sent to <strong>{email || 'your email'}</strong>
      </p>

      {/* 6-Digit OTP Box Grid */}
      <div className="auth-otp-grid">
        {digits.map((digit, index) => (
          <input
            key={index}
            ref={(el) => (inputRefs.current[index] = el)}
            type="text"
            inputMode="numeric"
            maxLength={1}
            value={digit}
            onChange={(e) => handleDigitChange(index, e.target.value)}
            onKeyDown={(e) => handleKeyDown(index, e)}
            onPaste={handlePaste}
            className={`auth-otp-box ${digit ? 'filled' : ''}`}
            required
          />
        ))}
      </div>

      <button
        type="submit"
        disabled={isLoading || digits.join('').length < codeLength}
        className="btn-primary auth-submit-btn"
      >
        {isLoading ? 'Verifying...' : 'Verify Security Code'}
      </button>

      <div className="auth-footer-note flex flex-col gap-2">
        <button type="button" className="auth-resend-link">
          Didn't receive the code? Resend
        </button>
        {onBackToSignup && (
          <button type="button" onClick={onBackToSignup} className="auth-switch-link text-xs">
            Change Email Address
          </button>
        )}
      </div>

    </form>
  );
};
