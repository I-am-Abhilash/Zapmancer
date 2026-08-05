import React, { useState } from 'react';
import './auth.css';
import { AuthLayout } from './components/AuthLayout';
import { LoginCard } from './components/LoginCard';
import { SignupCard } from './components/SignupCard';
import { ForgotPasswordCard } from './components/ForgotPasswordCard';
import { VerificationCard } from './components/VerificationCard';

export type AuthPageMode = 'login' | 'signup' | 'forgot' | 'verify';

interface AuthPageProps {
  initialMode?: AuthPageMode;
  onBackToHome?: () => void;
  onAuthSuccess?: () => void;
}

export const AuthPage: React.FC<AuthPageProps> = ({
  initialMode = 'login',
  onBackToHome,
  onAuthSuccess
}) => {
  const [mode, setMode] = useState<AuthPageMode>(initialMode);
  const [userEmail, setUserEmail] = useState('');

  const getTitle = () => {
    switch (mode) {
      case 'login': return 'Welcome to Zapmancer';
      case 'signup': return 'Create Your Account';
      case 'forgot': return 'Reset Your Password';
      case 'verify': return 'Security Verification';
    }
  };

  const getSubtitle = () => {
    switch (mode) {
      case 'login': return 'Log in to manage your freelance contracts, proposals, and projects';
      case 'signup': return 'Join the open-source KMP freelance marketplace with transparent escrow';
      case 'forgot': return 'Enter your registered email to receive a password reset link';
      case 'verify': return `Enter the 6-digit verification code sent to ${userEmail || 'your email'}`;
    }
  };

  return (
    <AuthLayout
      title={getTitle()}
      subtitle={getSubtitle()}
      onBackToLanding={onBackToHome}
    >
      {mode === 'login' && (
        <LoginCard
          onNavigateToForgot={() => setMode('forgot')}
          onNavigateToSignup={() => setMode('signup')}
          onLoginSuccess={onAuthSuccess}
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
          onVerificationSuccess={onAuthSuccess}
          onBackToSignup={() => setMode('signup')}
        />
      )}
    </AuthLayout>
  );
};
