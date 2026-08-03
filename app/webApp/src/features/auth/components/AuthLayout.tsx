import React from 'react';
import { ArrowLeft } from 'lucide-react';

interface AuthLayoutProps {
  title: string;
  subtitle: string;
  onBackToLanding?: () => void;
  children: React.ReactNode;
}

export const AuthLayout: React.FC<AuthLayoutProps> = ({
  title,
  subtitle,
  onBackToLanding,
  children
}) => {
  return (
    <div className="auth-layout-container">
      <div className="auth-split-grid">
        
        {/* Left Side: Editorial Branding Banner */}
        <div className="auth-brand-side">
          <div className="auth-brand-content">
            <a href="#" onClick={onBackToLanding} className="auth-brand-logo">
              SCRIPTSIDE
            </a>
            
            <h1 className="auth-brand-headline font-serif">
              The modern platform for <br />
              <span className="italic font-normal">thoughtful readers & creators.</span>
            </h1>

            <p className="auth-brand-subtext">
              Join thousands of developers, architects, and designers sharing deep perspectives daily.
            </p>

            <div className="auth-brand-features">
              <div className="auth-feature-item">
                <span className="auth-feature-bullet">•</span>
                <span>Distraction-free reading experience</span>
              </div>
              <div className="auth-feature-item">
                <span className="auth-feature-bullet">•</span>
                <span>High-impact markdown publishing studio</span>
              </div>
              <div className="auth-feature-item">
                <span className="auth-feature-bullet">•</span>
                <span>Direct community support for independent authors</span>
              </div>
            </div>
          </div>
        </div>

        {/* Right Side: Form Card */}
        <div className="auth-form-side">
          {onBackToLanding && (
            <button onClick={onBackToLanding} className="auth-back-btn">
              <ArrowLeft size={16} /> Back to home
            </button>
          )}

          <div className="auth-form-card">
            <div className="auth-form-header">
              <h2 className="auth-title">{title}</h2>
              <p className="auth-subtitle">{subtitle}</p>
            </div>

            {children}
          </div>
        </div>

      </div>
    </div>
  );
};
