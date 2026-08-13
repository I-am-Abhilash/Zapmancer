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
    <form onSubmit={handleSubmit} className="space-y-4">
      
      {error && (
        <div className="p-3 rounded-xl bg-red-500/10 border border-red-500 text-red-500 text-xs font-semibold">
          {error}
        </div>
      )}

      <div className="space-y-1.5">
        <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute">Username (Optional)</label>
        <div className="relative">
          <User className="w-4 h-4 text-mute absolute left-4 top-3.5" />
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="johndoe"
            className="w-full pl-11 pr-4 py-3 rounded-full border border-hairline bg-surface-elevated text-sm text-ink placeholder:text-mute focus:outline-none focus:border-brand-green transition-colors"
          />
        </div>
      </div>

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
        <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute">Password</label>
        <div className="relative">
          <Lock className="w-4 h-4 text-mute absolute left-4 top-3.5" />
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="At least 6 characters"
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
        {isLoading ? 'Creating account...' : 'Create Account'}
      </button>

      <div className="relative flex py-2 items-center">
        <div className="flex-grow border-t border-hairline"></div>
        <span className="flex-shrink mx-4 text-xs font-bold uppercase tracking-wider text-mute">or join with</span>
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
        <span>Already have an account? </span>
        <button
          type="button"
          onClick={onNavigateToLogin}
          className="font-bold text-brand-green hover:text-brand-green-hover transition-colors"
        >
          Log In
        </button>
      </div>

    </form>
  );
};
