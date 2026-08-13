import React, { useState } from 'react';
import { X, Zap } from 'lucide-react';
import { LoginCard } from './components/LoginCard';
import { SignupCard } from './components/SignupCard';
import { ForgotPasswordCard } from './components/ForgotPasswordCard';
import { VerificationCard } from './components/VerificationCard';

export type AuthMode = 'login' | 'signup' | 'forgot' | 'verify';

interface AuthModalProps {
  isOpen: boolean;
  initialMode?: AuthMode;
  onClose: () => void;
  onAuthSuccess?: () => void;
}

export const AuthModal: React.FC = ({
  isOpen,
  initialMode = 'login',
  onClose,
  onAuthSuccess
}: AuthModalProps) => {
  const [mode, setMode] = useState<AuthMode>(initialMode);
  const [userEmail, setUserEmail] = useState('');

  if (!isOpen) return null;

  const getTitle = () => {
    switch (mode) {
      case 'login': return 'Welcome Back';
      case 'signup': return 'Create Your Account';
      case 'forgot': return 'Reset Password';
      case 'verify': return 'Security Verification';
    }
  };

  const getSubtitle = () => {
    switch (mode) {
      case 'login': return 'Log in to manage your freelance contracts and escrow milestones';
      case 'signup': return 'Join the open-source KMP freelance marketplace with verified escrow';
      case 'forgot': return 'We will send a password recovery link to your registered email';
      case 'verify': return `We sent a 6-digit verification code to ${userEmail || 'your email'}`;
    }
  };

  return (
    <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4" onClick={onClose}>
      <div className="bg-surface border border-hairline rounded-xl p-8 w-full max-w-md relative shadow-2xl space-y-6 text-ink" onClick={(e) => e.stopPropagation()}>
        
        {/* Close Button */}
        <button onClick={onClose} className="absolute top-4 right-4 p-2 rounded-full text-mute hover:text-ink hover:bg-surface-elevated transition-colors">
          <X size={18} />
        </button>

        <div className="space-y-2">
          <div className="inline-flex items-center gap-1.5 font-mono text-xs font-bold tracking-[0.1em] uppercase text-brand-green">
            <Zap className="w-4 h-4 fill-current text-brand-green" /> ZAPMANCER
          </div>
          <h2 className="text-2xl font-extrabold tracking-tight text-ink">{getTitle()}</h2>
          <p className="text-xs text-mute">{getSubtitle()}</p>
        </div>

        <div className="auth-modal-body">
          {mode === 'login' && (
            <LoginCard
              onNavigateToForgot={() => setMode('forgot')}
              onNavigateToSignup={() => setMode('signup')}
              onLoginSuccess={() => {
                onAuthSuccess?.();
                onClose();
              }}
            />
          )}

          {mode === 'signup' && (
            <SignupCard
              onNavigateToLogin={() => setMode('login')}
              onSignupSuccess={(email) => {
                setUserEmail(email);
                setMode('verify');
              }}
            />
          )}

          {mode === 'forgot' && (
            <ForgotPasswordCard
              onBackToLogin={() => setMode('login')}
            />
          )}

          {mode === 'verify' && (
            <VerificationCard
              email={userEmail}
              onVerificationSuccess={() => {
                onAuthSuccess?.();
                onClose();
              }}
              onBackToSignup={() => setMode('signup')}
            />
          )}
        </div>

      </div>
    </div>
  );
};
