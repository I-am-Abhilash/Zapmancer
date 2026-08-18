import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Sun, Moon, Search, Plus, MessageSquare, Bell, User, Settings, LogOut, ChevronDown, Zap, Building2, Briefcase, RefreshCw } from 'lucide-react';
import { useTheme } from '../../context/ThemeContext';
import { useAuth } from '../../context/AuthContext';

export type UserAccountContext = 'company' | 'freelancer' | 'guest';

interface HeaderProps {
  isLoggedIn?: boolean;
}

export const Header: React.FC<HeaderProps> = ({ isLoggedIn }) => {
  const { theme, toggleTheme } = useTheme();
  const { user, isAuthenticated, role, switchRole, logout } = useAuth();
  const navigate = useNavigate();
  const [showProfileMenu, setShowProfileMenu] = useState(false);

  const effectiveLoggedIn = isLoggedIn !== undefined ? isLoggedIn : isAuthenticated;
  const activeAccountType: UserAccountContext = effectiveLoggedIn ? role : 'guest';

  const toggleAccountContext = () => {
    const nextType: UserAccountContext = activeAccountType === 'company' ? 'freelancer' : 'company';
    switchRole(nextType);
    setShowProfileMenu(false);
    if (nextType === 'company') {
      navigate('/company/dashboard');
    } else {
      navigate('/home');
    }
  };

  const handleSignOut = () => {
    setShowProfileMenu(false);
    logout();
    navigate('/login');
  };

  return (
    <header className="site-header border-b border-hairline bg-surface/90 backdrop-blur-md sticky top-0 z-50 transition-colors duration-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        
        {/* Brand Logo & Context Links */}
        <div className="flex items-center gap-8">
          <Link to="/" className="flex items-center gap-2 font-extrabold text-xl tracking-tight text-ink">
            <div className="w-8 h-8 rounded-lg bg-primary text-white flex items-center justify-center font-bold text-sm shadow-sm">
              Z
            </div>
            <span className="font-mono text-ink tracking-tight">ZAPMANCER</span>
          </Link>

          {/* Role-Aware Nav Links */}
          {isLoggedIn && activeAccountType === 'company' && (
            <nav className="hidden md:flex items-center gap-6 text-sm font-medium">
              <Link to="/company/dashboard" className="text-primary font-bold flex items-center gap-1.5">
                <Building2 className="w-4 h-4 text-primary" /> Company OS
              </Link>
              <Link to="/search" className="text-mute hover:text-ink transition-colors flex items-center gap-1.5">
                <Search className="w-4 h-4 text-primary" /> Find Talent
              </Link>
              <Link to="/projects/new" className="text-mute hover:text-ink transition-colors flex items-center gap-1.5">
                <Plus className="w-4 h-4 text-primary" /> Post Project
              </Link>
              <Link to="/client/proposals" className="text-mute hover:text-ink transition-colors">
                Contract Bids
              </Link>
            </nav>
          )}

          {isLoggedIn && activeAccountType === 'freelancer' && (
            <nav className="hidden md:flex items-center gap-6 text-sm font-medium">
              <Link to="/home" className="text-primary font-bold flex items-center gap-1.5">
                <Briefcase className="w-4 h-4 text-primary" /> My Workspace
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
                <Search className="w-4 h-4 text-primary" /> Find Talent
              </Link>
              <Link to="/projects/new" className="text-mute hover:text-ink transition-colors flex items-center gap-1.5">
                <Plus className="w-4 h-4 text-primary" /> Post Project
              </Link>
            </nav>
          )}
        </div>

        {/* Right Actions & Account Controls */}
        <div className="flex items-center gap-3 sm:gap-4">
          
          {/* Light / Dark Toggle */}
          <button
            onClick={toggleTheme}
            className="p-2 rounded-lg text-mute hover:text-ink hover:bg-surface-elevated transition-colors"
            aria-label="Toggle Theme"
            title={`Switch to ${theme === 'light' ? 'Dark' : 'Light'} Mode`}
          >
            {theme === 'light' ? <Moon className="w-4 h-4 text-primary" /> : <Sun className="w-4 h-4 text-primary" />}
          </button>

          {effectiveLoggedIn ? (
            <>
              {/* Messages shortcut */}
              <Link
                to="/messages"
                className="p-2 rounded-lg text-mute hover:text-ink hover:bg-surface-elevated transition-colors relative"
                title="Messages"
              >
                <MessageSquare className="w-4 h-4 text-primary" />
                <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-primary rounded-full"></span>
              </Link>

              {/* Notifications shortcut */}
              <Link
                to="/notifications"
                className="p-2 rounded-lg text-mute hover:text-ink hover:bg-surface-elevated transition-colors relative"
                title="Notifications"
              >
                <Bell className="w-4 h-4 text-primary" />
                <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-primary rounded-full"></span>
              </Link>

              {/* Profile & Context Switcher Dropdown */}
              <div className="relative">
                <button
                  onClick={() => setShowProfileMenu(!showProfileMenu)}
                  className="flex items-center gap-2 p-1 rounded-full hover:ring-2 hover:ring-primary/50 transition-all focus:outline-none"
                >
                  <img
                    src={
                      activeAccountType === 'company'
                        ? 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=120&q=80'
                        : 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80'
                    }
                    alt="User Avatar"
                    className="w-8 h-8 rounded-full object-cover border border-primary/30"
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
                        {user?.username || (activeAccountType === 'company' ? 'Acme AI Systems' : 'Alex Morgan')}
                      </p>
                      <p className="text-xs text-primary font-semibold">
                        {user?.email || (activeAccountType === 'company' ? 'Company Workspace Admin' : 'Verified Talent')}
                      </p>
                    </div>

                    {/* Role Context Switcher Button */}
                    <button
                      onClick={toggleAccountContext}
                      className="w-full flex items-center justify-between px-4 py-2.5 text-xs font-bold text-primary bg-primary/10 hover:bg-primary/20 transition-colors border-y border-hairline text-left"
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
                        <Building2 className="w-4 h-4 text-primary" />
                      ) : (
                        <Zap className="w-4 h-4 text-primary" />)}
                      {activeAccountType === 'company' ? 'Company Dashboard' : 'Talent Dashboard'}
                    </Link>

                    <Link
                      to="/profile/me"
                      onClick={() => setShowProfileMenu(false)}
                      className="flex items-center gap-2 px-4 py-2 text-sm text-ink hover:bg-surface-elevated transition-colors"
                    >
                      <User className="w-4 h-4 text-primary" /> Profile Settings
                    </Link>

                    <Link
                      to="/settings"
                      onClick={() => setShowProfileMenu(false)}
                      className="flex items-center gap-2 px-4 py-2 text-sm text-ink hover:bg-surface-elevated transition-colors"
                    >
                      <Settings className="w-4 h-4 text-primary" /> Account Settings
                    </Link>

                    <div className="border-t border-hairline my-1"></div>

                    <button
                      onClick={handleSignOut}
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
              <Link to="/login" className="text-xs font-semibold text-ink hover:text-primary transition-colors px-3 py-2">
                Log In
              </Link>
              <Link to="/signup" className="text-xs font-semibold text-white bg-primary hover:bg-primary-hover px-4 py-2 rounded-md transition-all shadow-sm">
                Get Zapmancer Free
              </Link>
            </div>
          )}

        </div>
      </div>
    </header>
  );
};
