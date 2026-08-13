import React from 'react';
import { Link } from 'react-router-dom';
import { Zap, ShieldCheck, Globe } from 'lucide-react';

const GithubIcon: React.FC<{ className?: string }> = ({ className = 'w-4 h-4' }) => (
  <svg className={className} fill="currentColor" viewBox="0 0 24 24">
    <path fillRule="evenodd" clipRule="evenodd" d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.53 1.032 1.53 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z" />
  </svg>
);

const TwitterIcon: React.FC<{ className?: string }> = ({ className = 'w-4 h-4' }) => (
  <svg className={className} fill="currentColor" viewBox="0 0 24 24">
    <path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z" />
  </svg>
);

const LinkedinIcon: React.FC<{ className?: string }> = ({ className = 'w-4 h-4' }) => (
  <svg className={className} fill="currentColor" viewBox="0 0 24 24">
    <path d="M19 3a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h14m-.5 15.5v-5.3a3.26 3.26 0 0 0-3.26-3.26c-.85 0-1.84.52-2.28 1.3v-1.11h-2.79v8.37h2.79v-4.93c0-.77.62-1.4 1.39-1.4a1.4 1.4 0 0 1 1.4 1.4v4.93h2.75M6.46 10.9v8.37H9.25V10.9H6.46M7.86 6.74a1.62 1.62 0 1 0 0 3.24 1.62 1.62 0 0 0 0-3.24z" />
  </svg>
);

export const Footer: React.FC = () => {
  return (
    <footer className="bg-surface text-mute border-t border-hairline pt-16 pb-12 transition-colors duration-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Footer Top Grid */}
        <div className="grid grid-cols-1 md:grid-cols-5 gap-10 pb-12 border-b border-hairline">
          
          {/* Brand Col */}
          <div className="md:col-span-2 space-y-4">
            <Link to="/" className="flex items-center gap-2 font-bold text-xl text-brand-green">
              <Zap className="h-6 w-6 text-brand-green fill-current" />
              <span className="font-mono text-ink tracking-tight">ZAPMANCER</span>
            </Link>
            <p className="text-sm text-mute max-w-sm leading-relaxed">
              The modern, open-source freelance marketplace built on Kotlin Multiplatform & Compose. Transparent milestone escrow with zero hidden fees.
            </p>
            <div className="flex items-center gap-2 text-xs font-bold text-brand-green bg-surface-elevated border border-hairline px-3.5 py-1.5 rounded-full w-fit">
              <ShieldCheck className="w-4 h-4 text-brand-green" /> 100% Escrow Protected Contracts
            </div>
          </div>

          {/* Navigation Links */}
          <div>
            <h4 className="text-xs font-bold uppercase tracking-[0.1em] text-ink mb-4">Marketplace</h4>
            <ul className="space-y-2.5 text-sm">
              <li><Link to="/projects" className="hover:text-brand-green transition-colors">Browse Projects</Link></li>
              <li><Link to="/search" className="hover:text-brand-green transition-colors">Find Talent</Link></li>
              <li><Link to="/projects/new" className="hover:text-brand-green transition-colors">Post a Project</Link></li>
              <li><Link to="/client/proposals" className="hover:text-brand-green transition-colors">Client Hub</Link></li>
            </ul>
          </div>

          <div>
            <h4 className="text-xs font-bold uppercase tracking-[0.1em] text-ink mb-4">Platform</h4>
            <ul className="space-y-2.5 text-sm">
              <li><Link to="/onboarding" className="hover:text-brand-green transition-colors">Getting Started</Link></li>
              <li><Link to="/messages" className="hover:text-brand-green transition-colors">Direct Messaging</Link></li>
              <li><Link to="/notifications" className="hover:text-brand-green transition-colors">Notifications</Link></li>
              <li><Link to="/settings" className="hover:text-brand-green transition-colors">Escrow & Billing</Link></li>
            </ul>
          </div>

          <div>
            <h4 className="text-xs font-bold uppercase tracking-[0.1em] text-ink mb-4">Account</h4>
            <ul className="space-y-2.5 text-sm">
              <li><Link to="/login" className="hover:text-brand-green transition-colors">Sign In</Link></li>
              <li><Link to="/signup" className="hover:text-brand-green transition-colors">Join Marketplace</Link></li>
              <li><Link to="/profile/me" className="hover:text-brand-green transition-colors">My Profile</Link></li>
              <li><Link to="/profile/edit" className="hover:text-brand-green transition-colors">Edit Profile</Link></li>
            </ul>
          </div>

        </div>

        {/* Footer Bottom Bar */}
        <div className="mt-8 flex flex-col md:flex-row items-center justify-between gap-4 text-xs">
          <p>&copy; {new Date().getFullYear()} Zapmancer Ecosystem. Open-source KMP Marketplace.</p>

          <div className="flex items-center gap-6">
            <a href="https://github.com/I-am-Abhilash/Zapmancer" target="_blank" rel="noreferrer" className="hover:text-brand-green flex items-center gap-1 transition-colors">
              <GithubIcon className="w-4 h-4" /> GitHub
            </a>
            <a href="#" className="hover:text-brand-green flex items-center gap-1 transition-colors">
              <TwitterIcon className="w-4 h-4" /> Twitter
            </a>
            <a href="#" className="hover:text-brand-green flex items-center gap-1 transition-colors">
              <LinkedinIcon className="w-4 h-4" /> LinkedIn
            </a>
            <a href="#" className="hover:text-brand-green flex items-center gap-1 transition-colors">
              <Globe className="w-4 h-4 text-brand-green" /> Network Status
            </a>
          </div>
        </div>

      </div>
    </footer>
  );
};
