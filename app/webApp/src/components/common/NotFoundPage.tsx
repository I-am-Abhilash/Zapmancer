import React from 'react';
import { Link } from 'react-router-dom';
import { Compass, ArrowLeft, Home } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

export const NotFoundPage: React.FC = () => {
  const { isAuthenticated, role } = useAuth();
  const dashboardLink = isAuthenticated
    ? (role === 'company' ? '/company/dashboard' : '/home')
    : '/';

  return (
    <div className="min-h-[70vh] flex items-center justify-center px-4 py-16">
      <div className="max-w-md w-full text-center space-y-6">
        <div className="w-16 h-16 rounded-2xl bg-primary/10 text-primary flex items-center justify-center mx-auto shadow-inner">
          <Compass className="w-8 h-8" />
        </div>

        <div className="space-y-2">
          <p className="text-xs font-mono font-bold tracking-widest text-primary uppercase">Error 404</p>
          <h1 className="text-3xl font-extrabold text-ink tracking-tight">Page Not Found</h1>
          <p className="text-sm text-mute leading-relaxed">
            The screen you requested does not exist or has been moved to a new route in production.
          </p>
        </div>

        <div className="flex items-center justify-center gap-3 pt-2">
          <Link
            to={dashboardLink}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-primary hover:bg-primary-hover text-white text-sm font-semibold transition-all shadow-sm"
          >
            <Home className="w-4 h-4" />
            {isAuthenticated ? 'Go to Dashboard' : 'Back to Home'}
          </Link>
          <Link
            to="/projects"
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg border border-hairline hover:bg-surface-elevated text-ink text-sm font-semibold transition-all"
          >
            <ArrowLeft className="w-4 h-4" /> Browse Contracts
          </Link>
        </div>
      </div>
    </div>
  );
};
