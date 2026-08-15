import React, { useState } from 'react';
import './auth.css';
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

const TITLE: Record<AuthMode, string> = {
  login: 'Welcome back',
  signup: 'Create your account',
  forgot: 'Reset password',
  verify: 'Security verification',
};

const SUBTITLE: Record<AuthMode, string> = {
  login: 'Log in to manage your contracts and escrow milestones.',
  signup: 'Join the open-source KMP marketplace with verified escrow.',
  forgot: 'We will send a password recovery link to your registered email.',
  verify: 'Enter the 6-digit code sent to your email.',
};

export const AuthModal: React.FC<AuthModalProps> = ({ isOpen, initialMode = 'login', onClose, onAuthSuccess }) => {
  const [mode, setMode] = useState<AuthMode>(initialMode);
  const [userEmail, setUserEmail] = useState('');

  if (!isOpen) return null;

  return (
    <div className="auth-modal-overlay" onClick={onClose}>
      <div className="auth-modal" style={{ position: 'relative' }} onClick={(e) => e.stopPropagation()}>

        <button onClick={onClose} className="auth-modal-close">
          <X size={18} />
        </button>

        <div>
          <p style={{ fontSize: 12, fontWeight: 700, color: 'var(--color-steel)', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: 6 }}>
            Zapmancer
          </p>
          <h2 style={{ fontSize: 22, fontWeight: 700, color: 'var(--color-ink)', letterSpacing: '-0.3px', marginBottom: 4 }}>{TITLE[mode]}</h2>
          <p style={{ fontSize: 14, color: 'var(--color-steel)' }}>{SUBTITLE[mode]}</p>
        </div>

        {mode === 'login' && (
          <LoginCard
            onNavigateToForgot={() => setMode('forgot')}
            onNavigateToSignup={() => setMode('signup')}
            onLoginSuccess={() => { onAuthSuccess?.(); onClose(); }}
          />
        )}
        {mode === 'signup' && (
          <SignupCard
            onNavigateToLogin={() => setMode('login')}
            onSignupSuccess={(email) => { setUserEmail(email); setMode('verify'); }}
          />
        )}
        {mode === 'forgot' && (
          <ForgotPasswordCard onBackToLogin={() => setMode('login')} />
        )}
        {mode === 'verify' && (
          <VerificationCard
            email={userEmail}
            onVerificationSuccess={() => { onAuthSuccess?.(); onClose(); }}
            onBackToSignup={() => setMode('signup')}
          />
        )}

      </div>
    </div>
  );
};
