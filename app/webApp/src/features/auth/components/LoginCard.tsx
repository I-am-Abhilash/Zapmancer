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
    <form onSubmit={handleSubmit} className="space-y-4">
      
      {error && (
        <div className="p-3 rounded-xl bg-red-500/10 border border-red-500 text-red-500 text-xs font-semibold">
          {error}
        </div>
      )}

      <div className="space-y-1.5">
        <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute">Email Address</label>
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

      <div className="space-y-1.5">
        <div className="flex items-center justify-between">
          <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute">Password</label>
          {onNavigateToForgot && (
            <button
              type="button"
              onClick={onNavigateToForgot}
              className="text-xs font-bold text-brand-green hover:text-brand-green-hover transition-colors"
            >
              Forgot Password?
            </button>
          )}
        </div>
        <div className="relative">
          <Lock className="w-4 h-4 text-mute absolute left-4 top-3.5" />
          <input
            type={showPassword ? 'text' : 'password'}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="••••••••"
            className="w-full pl-11 pr-11 py-3 rounded-full border border-hairline bg-surface-elevated text-sm text-ink placeholder:text-mute focus:outline-none focus:border-brand-green transition-colors"
            required
          />
          <button
            type="button"
            onClick={() => setShowPassword(!showPassword)}
            className="absolute right-4 top-3.5 text-mute hover:text-ink transition-colors"
            aria-label="Toggle password visibility"
          >
            {showPassword ? <EyeOff size={16} /> : <Eye size={16} />}
          </button>
        </div>
      </div>

      <button
        type="submit"
        disabled={isLoading}
        className="w-full py-3.5 rounded-full bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] transition-all hover:scale-[1.02] shadow-md shadow-brand-green/20"
      >
        {isLoading ? 'Signing in...' : 'Log In to Zapmancer'}
      </button>

      <div className="relative flex py-2 items-center">
        <div className="flex-grow border-t border-hairline"></div>
        <span className="flex-shrink mx-4 text-xs font-bold uppercase tracking-wider text-mute">or continue with</span>
        <div className="flex-grow border-t border-hairline"></div>
      </div>

      <div className="grid grid-cols-2 gap-3">
        <button type="button" className="py-2.5 rounded-full border border-hairline bg-surface-elevated text-ink text-xs font-bold hover:bg-surface-modal transition-colors">
          Google
        </button>
        <button type="button" className="py-2.5 rounded-full border border-hairline bg-surface-elevated text-ink text-xs font-bold hover:bg-surface-modal transition-colors">
          GitHub
        </button>
      </div>

      <div className="text-center text-xs text-mute pt-2">
        <span>Don't have an account? </span>
        <button
          type="button"
          onClick={onNavigateToSignup}
          className="font-bold text-brand-green hover:text-brand-green-hover transition-colors"
        >
          Sign Up
        </button>
      </div>

    </form>
  );
};
