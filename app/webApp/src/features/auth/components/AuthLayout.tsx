import React from 'react';
import { ArrowLeft, ShieldCheck, Zap } from 'lucide-react';

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
    <div className="min-h-screen w-full bg-canvas text-ink transition-colors duration-200 antialiased font-sans flex items-center justify-center">
      <div className="grid grid-cols-1 lg:grid-cols-2 w-full min-h-screen">
        
        {/* Left Side: Editorial Branding Banner */}
        <div className="hidden lg:flex bg-surface border-r border-hairline p-16 flex-col justify-between">
          <div className="space-y-8 max-w-lg my-auto">
            <a href="#" onClick={onBackToLanding} className="inline-flex items-center gap-2 font-mono font-extrabold text-2xl tracking-tight text-brand-green">
              <Zap className="h-7 w-7 text-brand-green fill-current" />
              <span className="text-ink">ZAPMANCER</span>
            </a>
            
            <h1 className="text-4xl sm:text-5xl font-extrabold tracking-tight text-ink leading-tight">
              The premier platform for <br />
              <span className="text-brand-green">KMP & Mobile Engineers.</span>
            </h1>

            <p className="text-base text-mute leading-relaxed">
              Connect directly with verified clients, post project bounties, and get paid with 100% milestone escrow protection and zero platform deductions.
            </p>

            <div className="space-y-4 pt-4 text-sm font-bold text-ink">
              <div className="flex items-center gap-3">
                <ShieldCheck className="w-5 h-5 text-brand-green shrink-0" />
                <span>Transparent milestone escrow protection</span>
              </div>
              <div className="flex items-center gap-3">
                <ShieldCheck className="w-5 h-5 text-brand-green shrink-0" />
                <span>Zero platform fee deductions on earnings</span>
              </div>
              <div className="flex items-center gap-3">
                <ShieldCheck className="w-5 h-5 text-brand-green shrink-0" />
                <span>Verified client budget deposits</span>
              </div>
            </div>
          </div>

          <div className="text-xs text-mute font-medium">
            &copy; {new Date().getFullYear()} Zapmancer Ecosystem. Open-source KMP Marketplace.
          </div>
        </div>

        {/* Right Side: Form Card */}
        <div className="p-8 sm:p-12 md:p-16 flex flex-col justify-center items-center relative bg-canvas">
          {onBackToLanding && (
            <button onClick={onBackToLanding} className="absolute top-8 left-8 inline-flex items-center gap-2 text-xs font-bold uppercase tracking-[0.05em] text-mute hover:text-ink transition-colors">
              <ArrowLeft size={16} className="text-brand-green" /> Back to home
            </button>
          )}

          <div className="w-full max-w-md space-y-6">
            <div className="space-y-2">
              <h2 className="text-3xl font-extrabold tracking-tight text-ink">{title}</h2>
              <p className="text-sm text-mute">{subtitle}</p>
            </div>

            {children}
          </div>
        </div>

      </div>
    </div>
  );
};
