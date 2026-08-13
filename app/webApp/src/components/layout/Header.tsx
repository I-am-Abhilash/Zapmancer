import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Sun, Moon, Search, Plus, MessageSquare, Bell, User, Settings, LogOut, ChevronDown, Zap } from 'lucide-react';
import { useTheme } from '../../context/ThemeContext';

interface HeaderProps {
  isLoggedIn?: boolean;
}

export const Header: React.FC<HeaderProps> = ({ isLoggedIn = true }) => {
  const { theme, toggleTheme } = useTheme();
  const navigate = useNavigate();
  const [showProfileMenu, setShowProfileMenu] = useState(false);

  return (
    <header className="site-header border-b border-hairline bg-surface/90 backdrop-blur-md sticky top-0 z-50 transition-colors duration-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        
        {/* Brand Logo */}
        <div className="flex items-center gap-8">
          <Link to="/" className="flex items-center gap-2 font-bold text-xl tracking-tight text-brand-green">
            <Zap className="h-6 w-6 fill-current text-brand-green" />
            <span className="font-mono text-ink">ZAPMANCER</span>
          </Link>

          {/* Nav links */}
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
            <Link to="/client/proposals" className="text-mute hover:text-ink transition-colors">
              Client Hub
            </Link>
          </nav>
        </div>

        {/* Right Actions */}
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

              {/* User Profile Dropdown */}
              <div className="relative">
                <button
                  onClick={() => setShowProfileMenu(!showProfileMenu)}
                  className="flex items-center gap-2 p-1 rounded-full hover:ring-2 hover:ring-brand-green/50 transition-all focus:outline-none"
                >
                  <img
                    src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80"
                    alt="User Avatar"
                    className="w-8 h-8 rounded-full object-cover border border-brand-green/30"
                  />
                  <ChevronDown className="w-4 h-4 text-mute hidden sm:block" />
                </button>

                {showProfileMenu && (
                  <div 
                    className="absolute right-0 mt-2 w-56 bg-surface rounded-xl shadow-xl border border-hairline py-1 z-50"
                    onMouseLeave={() => setShowProfileMenu(false)}
                  >
                    <div className="px-4 py-3 border-b border-hairline">
                      <p className="text-sm font-semibold text-ink">Alex Morgan</p>
                      <p className="text-xs text-mute truncate">alex.m@zapmancer.io</p>
                    </div>

                    <Link
                      to="/home"
                      onClick={() => setShowProfileMenu(false)}
                      className="flex items-center gap-2 px-4 py-2 text-sm text-ink hover:bg-surface-elevated transition-colors"
                    >
                      <Zap className="w-4 h-4 text-brand-green" /> Dashboard Feed
                    </Link>

                    <Link
                      to="/profile/me"
                      onClick={() => setShowProfileMenu(false)}
                      className="flex items-center gap-2 px-4 py-2 text-sm text-ink hover:bg-surface-elevated transition-colors"
                    >
                      <User className="w-4 h-4 text-brand-green" /> View Profile
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
                      className="w-full flex items-center gap-2 px-4 py-2 text-sm text-red-500 hover:bg-red-500/10 text-left transition-colors"
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
