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
      case 'login': return 'Welcome Back';
      case 'signup': return 'Create Your Account';
      case 'forgot': return 'Reset Password';
      case 'verify': return 'Verify Your Email';
    }
  };

  const getSubtitle = () => {
    switch (mode) {
      case 'login': return 'Log in to continue your reading & writing journey';
      case 'signup': return 'Join our community of passionate technical writers & readers';
      case 'forgot': return 'Enter your email address to receive a recovery link';
      case 'verify': return `Enter the 6-digit security code sent to ${userEmail || 'your email'}`;
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
