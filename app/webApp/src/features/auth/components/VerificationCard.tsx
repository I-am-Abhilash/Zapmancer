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
    <form onSubmit={handleSubmit} className="space-y-4">
      
      {error && (
        <div className="p-3 rounded-xl bg-red-500/10 border border-red-500 text-red-500 text-xs font-semibold">
          {error}
        </div>
      )}

      <p className="text-xs text-mute text-center">
        Enter the 6-digit security code sent to <strong className="text-ink">{email || 'your email'}</strong>
      </p>

      {/* 6-Digit OTP Box Grid */}
      <div className="grid grid-cols-6 gap-2 my-2">
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
            className="w-full h-12 text-center text-lg font-bold font-mono bg-surface-elevated border border-hairline rounded-xl text-ink focus:outline-none focus:border-brand-green transition-all"
            required
          />
        ))}
      </div>

      <button
        type="submit"
        disabled={isLoading || digits.join('').length < codeLength}
        className="w-full py-3.5 rounded-full bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] transition-all hover:scale-[1.02] shadow-md shadow-brand-green/20"
      >
        {isLoading ? 'Verifying...' : 'Verify Security Code'}
      </button>

      <div className="text-center text-xs text-mute flex flex-col gap-2 pt-2">
        <button type="button" className="hover:text-brand-green transition-colors">
          Didn't receive the code? Resend
        </button>
        {onBackToSignup && (
          <button type="button" onClick={onBackToSignup} className="font-bold text-brand-green hover:text-brand-green-hover transition-colors">
            Change Email Address
          </button>
        )}
      </div>

    </form>
  );
};
