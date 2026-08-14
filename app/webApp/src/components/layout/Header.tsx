import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Sun, Moon, Search, Plus, MessageSquare, Bell, User, Settings, LogOut, ChevronDown, Zap, Building2, Briefcase, RefreshCw, Layers } from 'lucide-react';
import { useTheme } from '../../context/ThemeContext';

export type UserAccountContext = 'company' | 'freelancer' | 'guest';

interface HeaderProps {
  isLoggedIn?: boolean;
}

export const Header: React.FC<HeaderProps> = ({ isLoggedIn = true }) => {
  const { theme, toggleTheme } = useTheme();
  const navigate = useNavigate();
  const [showProfileMenu, setShowProfileMenu] = useState(false);
  
  // Persistent or stateful user context (company vs freelancer vs guest)
  const [activeAccountType, setActiveAccountType] = useState<UserAccountContext>(
    isLoggedIn ? 'company' : 'guest'
  );

  const toggleAccountContext = () => {
    const nextType: UserAccountContext = activeAccountType === 'company' ? 'freelancer' : 'company';
    setActiveAccountType(nextType);
    setShowProfileMenu(false);
    if (nextType === 'company') {
      navigate('/company/dashboard');
    } else {
      navigate('/home');
    }
  };

  return (
    <header className="site-header border-b border-hairline bg-surface/90 backdrop-blur-md sticky top-0 z-50 transition-colors duration-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        
        {/* Brand Logo & Context Links */}
        <div className="flex items-center gap-8">
          <Link to="/" className="flex items-center gap-2 font-bold text-xl tracking-tight text-brand-green">
            <Zap className="h-6 w-6 fill-current text-brand-green" />
            <span className="font-mono text-ink">ZAPMANCER</span>
          </Link>

          {/* Role-Aware Nav Links */}
          {isLoggedIn && activeAccountType === 'company' && (
            <nav className="hidden md:flex items-center gap-6 text-sm font-medium">
              <Link to="/company/dashboard" className="text-brand-green font-bold flex items-center gap-1.5">
                <Building2 className="w-4 h-4" /> Company OS
              </Link>
              <Link to="/search" className="text-mute hover:text-ink transition-colors flex items-center gap-1.5">
                <Search className="w-4 h-4 text-brand-green" /> Find Talent
              </Link>
              <Link to="/projects/new" className="text-mute hover:text-ink transition-colors flex items-center gap-1.5">
                <Plus className="w-4 h-4 text-brand-green" /> Post Bounty
              </Link>
              <Link to="/client/proposals" className="text-mute hover:text-ink transition-colors">
                Contract Bids
              </Link>
            </nav>
          )}

          {isLoggedIn && activeAccountType === 'freelancer' && (
            <nav className="hidden md:flex items-center gap-6 text-sm font-medium">
              <Link to="/home" className="text-brand-green font-bold flex items-center gap-1.5">
                <Briefcase className="w-4 h-4" /> My Dashboard
              </Link>
              <Link to="/projects" className="text-mute hover:text-ink transition-colors">
                Find Work
              </Link>
              <Link to="/profile/me" className="text-mute hover:text-ink transition-colors">
                My Profile
              </Link>
              <Link to="/notifications" className="text-mute hover:text-ink transition-colors">
                Activity Feed
              </Link>
            </nav>
          )}

          {(!isLoggedIn || activeAccountType === 'guest') && (
            <nav className="hidden md:flex items-center gap-6 text-sm font-medium">
              <Link to="/projects" className="text-mute hover:text-ink transition-colors">
                Find Work
              </Link>
              <Link to="/search" className="text-mute hover:text-ink transition-colors flex items-center gap-1.5">
                <Search className="w-4 h-4 text-brand-green" /> Find Talent
              </Link>
              <Link to="/projects/new" className="text-mute hover:text-ink transition-colors flex items-center gap-1.5">
                <Plus className="w-4 h-4 text-brand-green" /> Post Project
              </Link>
            </nav>
          )}
        </div>

        {/* Right Actions & Account Controls */}
        <div className="flex items-center gap-3 sm:gap-4">
          
          {/* Light / Dark Toggle */}
          <button
            onClick={toggleTheme}
            className="p-2.5 rounded-full text-mute hover:text-ink hover:bg-surface-elevated transition-colors"
            aria-label="Toggle Theme"
            title={`Switch to ${theme === 'light' ? 'Dark' : 'Light'} Mode`}
          >
            {theme === 'light' ? <Moon className="w-4 h-4 text-brand-green" /> : <Sun className="w-4 h-4 text-brand-green" />}
          </button>

          {isLoggedIn ? (
            <>
              {/* Messages shortcut */}
              <Link
                to="/messages"
                className="p-2.5 rounded-full text-mute hover:text-ink hover:bg-surface-elevated transition-colors relative"
                title="Messages"
              >
                <MessageSquare className="w-4 h-4 text-brand-green" />
                <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-brand-green rounded-full"></span>
              </Link>

              {/* Notifications shortcut */}
              <Link
                to="/notifications"
                className="p-2.5 rounded-full text-mute hover:text-ink hover:bg-surface-elevated transition-colors relative"
                title="Notifications"
              >
                <Bell className="w-4 h-4 text-brand-green" />
                <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-brand-green rounded-full"></span>
              </Link>

              {/* Profile & Context Switcher Dropdown */}
              <div className="relative">
                <button
                  onClick={() => setShowProfileMenu(!showProfileMenu)}
                  className="flex items-center gap-2 p-1 rounded-full hover:ring-2 hover:ring-brand-green/50 transition-all focus:outline-none"
                >
                  <img
                    src={
                      activeAccountType === 'company'
                        ? 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=120&q=80'
                        : 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80'
                    }
                    alt="User Avatar"
                    className="w-8 h-8 rounded-full object-cover border border-brand-green/30"
                  />
                  <ChevronDown className="w-4 h-4 text-mute hidden sm:block" />
                </button>

                {showProfileMenu && (
                  <div 
                    className="absolute right-0 mt-2 w-64 bg-surface rounded-xl shadow-xl border border-hairline py-1 z-50 text-ink"
                    onMouseLeave={() => setShowProfileMenu(false)}
                  >
                    <div className="px-4 py-3 border-b border-hairline space-y-0.5">
                      <p className="text-sm font-bold text-ink">
                        {activeAccountType === 'company' ? 'Acme AI Systems' : 'Alex Morgan'}
                      </p>
                      <p className="text-xs text-brand-green font-semibold">
                        {activeAccountType === 'company' ? 'Company Admin' : 'Verified Talent'}
                      </p>
                    </div>

                    {/* Role Context Switcher Button */}
                    <button
                      onClick={toggleAccountContext}
                      className="w-full flex items-center justify-between px-4 py-2.5 text-xs font-bold text-brand-green bg-brand-green/10 hover:bg-brand-green/20 transition-colors border-y border-hairline text-left"
                    >
                      <span className="flex items-center gap-2">
                        <RefreshCw className="w-3.5 h-3.5" />
                        {activeAccountType === 'company' ? 'Switch to Freelancer Mode' : 'Switch to Company OS Mode'}
                      </span>
                    </button>

                    <Link
                      to={activeAccountType === 'company' ? '/company/dashboard' : '/home'}
                      onClick={() => setShowProfileMenu(false)}
                      className="flex items-center gap-2 px-4 py-2 text-sm text-ink hover:bg-surface-elevated transition-colors"
                    >
                      {activeAccountType === 'company' ? (
                        <Building2 className="w-4 h-4 text-brand-green" />
                      ) : (
                        <Zap className="w-4 h-4 text-brand-green" />)}
                      {activeAccountType === 'company' ? 'Company Dashboard' : 'Talent Dashboard'}
                    </Link>

                    <Link
                      to="/profile/me"
                      onClick={() => setShowProfileMenu(false)}
                      className="flex items-center gap-2 px-4 py-2 text-sm text-ink hover:bg-surface-elevated transition-colors"
                    >
                      <User className="w-4 h-4 text-brand-green" /> Profile Settings
                    </Link>

                    <Link
                      to="/settings"
                      onClick={() => setShowProfileMenu(false)}
                      className="flex items-center gap-2 px-4 py-2 text-sm text-ink hover:bg-surface-elevated transition-colors"
                    >
                      <Settings className="w-4 h-4 text-brand-green" /> Account Settings
                    </Link>

                    <div className="border-t border-hairline my-1"></div>

                    <button
                      onClick={() => {
                        setShowProfileMenu(false);
                        navigate('/login');
                      }}
                      className="w-full flex items-center gap-2 px-4 py-2 text-sm text-red-500 hover:bg-red-500/10 text-left transition-colors font-medium"
                    >
                      <LogOut className="w-4 h-4" /> Sign Out
                    </button>
                  </div>
                )}
              </div>
            </>
          ) : (
            <div className="flex items-center gap-3">
              <Link to="/login" className="text-xs font-bold uppercase tracking-[0.05em] text-ink hover:text-brand-green transition-colors px-3 py-2">
                Log In
              </Link>
              <Link to="/signup" className="text-xs font-bold uppercase tracking-[0.05em] text-white bg-brand-green hover:bg-brand-green-hover px-5 py-2.5 rounded-full transition-all hover:scale-[1.04] shadow-md shadow-brand-green/20">
                Sign Up
              </Link>
            </div>
          )}

        </div>
      </div>
    </header>
  );
};
