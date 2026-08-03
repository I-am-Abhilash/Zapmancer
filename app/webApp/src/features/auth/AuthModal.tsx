import React, { useState } from 'react';
import { X } from 'lucide-react';
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

export const AuthModal: React.FC<AuthModalProps> = ({
  isOpen,
  initialMode = 'login',
  onClose,
  onAuthSuccess
}) => {
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
      case 'login': return 'Log in to continue your reading & writing journey';
      case 'signup': return 'Join our global community of technical authors & readers';
      case 'forgot': return 'We will send a password recovery link to your inbox';
      case 'verify': return `We sent a 6-digit code to ${userEmail || 'your email'}`;
    }
  };

  return (
    <div className="auth-modal-overlay" onClick={onClose}>
      <div className="auth-modal-container" onClick={(e) => e.stopPropagation()}>
        
        {/* Close Button */}
        <button onClick={onClose} className="auth-modal-close">
          <X size={20} />
        </button>

        <div className="auth-modal-header">
          <span className="auth-brand-badge font-mono">SCRIPTSIDE</span>
          <h2 className="auth-title">{getTitle()}</h2>
          <p className="auth-subtitle">{getSubtitle()}</p>
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
