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
    <form onSubmit={handleSubmit} className="space-y-4">
      
      {error && (
        <div className="p-3 rounded-xl bg-red-500/10 border border-red-500 text-red-500 text-xs font-semibold">
          {error}
        </div>
      )}

      {isSuccess ? (
        <div className="text-center p-6 space-y-4 bg-surface-elevated border border-hairline rounded-xl">
          <CheckCircle2 size={36} className="text-brand-green mx-auto" />
          <h3 className="text-lg font-bold text-ink">Recovery Email Sent</h3>
          <p className="text-xs text-mute leading-relaxed">
            We've sent password reset instructions to <strong>{email}</strong>. Check your inbox and follow the link.
          </p>
          <button
            type="button"
            onClick={onBackToLogin}
            className="w-full py-3 rounded-full bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] transition-all hover:scale-[1.02] shadow-md shadow-brand-green/20"
          >
            Back to Log In
          </button>
        </div>
      ) : (
        <>
          <div className="space-y-1.5">
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute">Registered Email Address</label>
            <div className="relative">
              <Mail className="w-4 h-4 text-mute absolute left-4 top-3.5" />
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="you@example.com"
                className="w-full pl-11 pr-4 py-3 rounded-full border border-hairline bg-surface-elevated text-sm text-ink placeholder:text-mute focus:outline-none focus:border-brand-green transition-colors"
                required
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full py-3.5 rounded-full bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] transition-all hover:scale-[1.02] shadow-md shadow-brand-green/20"
          >
            {isLoading ? 'Sending reset link...' : 'Send Reset Link'}
          </button>

          <div className="text-center text-xs text-mute pt-2">
            <button
              type="button"
              onClick={onBackToLogin}
              className="font-bold text-brand-green hover:text-brand-green-hover transition-colors"
            >
              &larr; Back to Log In
            </button>
          </div>
        </>
      )}

    </form>
  );
};
