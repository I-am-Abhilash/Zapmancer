import React from 'react';
import { Link } from 'react-router-dom';
import { Zap, ShieldCheck, Github, Twitter, Linkedin, Globe } from 'lucide-react';

export const Footer: React.FC = () => {
  return (
    <footer className="bg-slate-950 text-slate-400 border-t border-slate-900 pt-16 pb-12 transition-colors">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Footer Top Grid */}
        <div className="grid grid-cols-1 md:grid-cols-5 gap-10 pb-12 border-b border-slate-900">
          
          {/* Brand Col */}
          <div className="md:col-span-2 space-y-4">
            <Link to="/" className="flex items-center gap-2 font-bold text-xl text-white">
              <Zap className="h-6 w-6 text-indigo-500 fill-current" />
              <span className="font-mono tracking-tight">ZAPMANCER</span>
            </Link>
            <p className="text-sm text-slate-400 max-w-sm leading-relaxed">
              The modern, open-source freelance marketplace built on Kotlin Multiplatform & Compose. Transparent milestone escrow with zero hidden fees.
            </p>
            <div className="flex items-center gap-2 text-xs font-semibold text-emerald-400 bg-emerald-950/40 border border-emerald-800/40 px-3 py-1.5 rounded-lg w-fit">
              <ShieldCheck className="w-4 h-4" /> 100% Escrow Protected Contracts
            </div>
          </div>

          {/* Navigation Links */}
          <div>
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-200 mb-4">Marketplace</h4>
            <ul className="space-y-2.5 text-sm">
              <li><Link to="/projects" className="hover:text-white transition-colors">Browse Projects</Link></li>
              <li><Link to="/search" className="hover:text-white transition-colors">Find Talent</Link></li>
              <li><Link to="/projects/new" className="hover:text-white transition-colors">Post a Project</Link></li>
              <li><Link to="/client/proposals" className="hover:text-white transition-colors">Client Hub</Link></li>
            </ul>
          </div>

          <div>
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-200 mb-4">Platform</h4>
            <ul className="space-y-2.5 text-sm">
              <li><Link to="/onboarding" className="hover:text-white transition-colors">Getting Started</Link></li>
              <li><Link to="/messages" className="hover:text-white transition-colors">Direct Messaging</Link></li>
              <li><Link to="/notifications" className="hover:text-white transition-colors">Notifications</Link></li>
              <li><Link to="/settings" className="hover:text-white transition-colors">Escrow & Billing</Link></li>
            </ul>
          </div>

          <div>
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-200 mb-4">Account</h4>
            <ul className="space-y-2.5 text-sm">
              <li><Link to="/login" className="hover:text-white transition-colors">Sign In</Link></li>
              <li><Link to="/signup" className="hover:text-white transition-colors">Join Marketplace</Link></li>
              <li><Link to="/profile/me" className="hover:text-white transition-colors">My Profile</Link></li>
              <li><Link to="/profile/edit" className="hover:text-white transition-colors">Edit Profile</Link></li>
            </ul>
          </div>

        </div>

        {/* Footer Bottom Bar */}
        <div className="mt-8 flex flex-col md:flex-row items-center justify-between gap-4 text-xs">
          <p>&copy; {new Date().getFullYear()} Zapmancer Ecosystem. Open-source KMP Marketplace.</p>

          <div className="flex items-center gap-6">
            <a href="https://github.com/I-am-Abhilash/Zapmancer" target="_blank" rel="noreferrer" className="hover:text-white flex items-center gap-1 transition-colors">
              <Github className="w-4 h-4" /> GitHub
            </a>
            <a href="#" className="hover:text-white flex items-center gap-1 transition-colors">
              <Twitter className="w-4 h-4" /> Twitter
            </a>
            <a href="#" className="hover:text-white flex items-center gap-1 transition-colors">
              <Linkedin className="w-4 h-4" /> LinkedIn
            </a>
            <a href="#" className="hover:text-white flex items-center gap-1 transition-colors">
              <Globe className="w-4 h-4" /> Network Status
            </a>
          </div>
        </div>

      </div>
    </footer>
  );
};
