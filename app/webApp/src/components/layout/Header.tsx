import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Sun, Moon, Search, PlusCircle, MessageSquare, Bell, User, Settings, LogOut, ChevronDown, Zap } from 'lucide-react';
import { useTheme } from '../../context/ThemeContext';

interface HeaderProps {
  isLoggedIn?: boolean;
}

export const Header: React.FC<HeaderProps> = ({ isLoggedIn = true }) => {
  const { theme, toggleTheme } = useTheme();
  const navigate = useNavigate();
  const [showProfileMenu, setShowProfileMenu] = useState(false);

  return (
    <header className="site-header border-b border-[var(--color-border,#e2e8f0)] bg-[var(--color-bg,#ffffff)]/90 backdrop-blur-md sticky top-0 z-50 transition-colors">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        
        {/* Brand Logo */}
        <div className="flex items-center gap-8">
          <Link to="/" className="flex items-center gap-2 font-bold text-xl tracking-tight text-indigo-600 dark:text-indigo-400">
            <Zap className="h-6 w-6 fill-current text-indigo-600 dark:text-indigo-400" />
            <span className="font-mono text-slate-900 dark:text-white">ZAPMANCER</span>
          </Link>

          {/* Nav links */}
          <nav className="hidden md:flex items-center gap-6 text-sm font-medium">
            <Link to="/projects" className="text-slate-600 hover:text-indigo-600 dark:text-slate-300 dark:hover:text-white transition-colors">
              Find Work
            </Link>
            <Link to="/search" className="text-slate-600 hover:text-indigo-600 dark:text-slate-300 dark:hover:text-white transition-colors flex items-center gap-1.5">
              <Search className="w-4 h-4" /> Find Talent
            </Link>
            <Link to="/projects/new" className="text-slate-600 hover:text-indigo-600 dark:text-slate-300 dark:hover:text-white transition-colors flex items-center gap-1.5">
              <PlusCircle className="w-4 h-4" /> Post Project
            </Link>
            <Link to="/client/proposals" className="text-slate-600 hover:text-indigo-600 dark:text-slate-300 dark:hover:text-white transition-colors">
              Client Hub
            </Link>
          </nav>
        </div>

        {/* Right Actions */}
        <div className="flex items-center gap-3 sm:gap-4">
          
          {/* Light / Dark Toggle */}
          <button
            onClick={toggleTheme}
            className="p-2 rounded-lg text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
            aria-label="Toggle Theme"
            title={`Switch to ${theme === 'light' ? 'Dark' : 'Light'} Mode`}
          >
            {theme === 'light' ? <Moon className="w-5 h-5" /> : <Sun className="w-5 h-5" />}
          </button>

          {isLoggedIn ? (
            <>
              {/* Messages shortcut */}
              <Link
                to="/messages"
                className="p-2 rounded-lg text-slate-500 hover:text-indigo-600 dark:text-slate-400 dark:hover:text-indigo-400 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors relative"
                title="Messages"
              >
                <MessageSquare className="w-5 h-5" />
                <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-indigo-600 rounded-full"></span>
              </Link>

              {/* Notifications shortcut */}
              <Link
                to="/notifications"
                className="p-2 rounded-lg text-slate-500 hover:text-indigo-600 dark:text-slate-400 dark:hover:text-indigo-400 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors relative"
                title="Notifications"
              >
                <Bell className="w-5 h-5" />
                <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-rose-500 rounded-full"></span>
              </Link>

              {/* User Profile Dropdown */}
              <div className="relative">
                <button
                  onClick={() => setShowProfileMenu(!showProfileMenu)}
                  className="flex items-center gap-2 p-1 rounded-full hover:ring-2 hover:ring-indigo-500 transition-all focus:outline-none"
                >
                  <img
                    src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80"
                    alt="User Avatar"
                    className="w-8 h-8 rounded-full object-cover border border-indigo-500/30"
                  />
                  <ChevronDown className="w-4 h-4 text-slate-500 dark:text-slate-400 hidden sm:block" />
                </button>

                {showProfileMenu && (
                  <div 
                    className="absolute right-0 mt-2 w-56 bg-white dark:bg-slate-900 rounded-xl shadow-xl border border-slate-200 dark:border-slate-800 py-1 z-50"
                    onMouseLeave={() => setShowProfileMenu(false)}
                  >
                    <div className="px-4 py-3 border-b border-slate-100 dark:border-slate-800">
                      <p className="text-sm font-semibold text-slate-900 dark:text-white">Alex Morgan</p>
                      <p className="text-xs text-slate-500 dark:text-slate-400 truncate">alex.m@zapmancer.io</p>
                    </div>

                    <Link
                      to="/home"
                      onClick={() => setShowProfileMenu(false)}
                      className="flex items-center gap-2 px-4 py-2 text-sm text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-800"
                    >
                      <Zap className="w-4 h-4 text-indigo-500" /> Dashboard Feed
                    </Link>

                    <Link
                      to="/profile/me"
                      onClick={() => setShowProfileMenu(false)}
                      className="flex items-center gap-2 px-4 py-2 text-sm text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-800"
                    >
                      <User className="w-4 h-4 text-slate-400" /> View Profile
                    </Link>

                    <Link
                      to="/settings"
                      onClick={() => setShowProfileMenu(false)}
                      className="flex items-center gap-2 px-4 py-2 text-sm text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-800"
                    >
                      <Settings className="w-4 h-4 text-slate-400" /> Account Settings
                    </Link>

                    <div className="border-t border-slate-100 dark:border-slate-800 my-1"></div>

                    <button
                      onClick={() => {
                        setShowProfileMenu(false);
                        navigate('/login');
                      }}
                      className="w-full flex items-center gap-2 px-4 py-2 text-sm text-rose-600 hover:bg-rose-50 dark:hover:bg-rose-950/30 text-left"
                    >
                      <LogOut className="w-4 h-4" /> Sign Out
                    </button>
                  </div>
                )}
              </div>
            </>
          ) : (
            <div className="flex items-center gap-3">
              <Link to="/login" className="text-sm font-semibold text-slate-700 dark:text-slate-200 hover:text-indigo-600 transition-colors px-3 py-2">
                Log In
              </Link>
              <Link to="/signup" className="text-sm font-semibold text-white bg-indigo-600 hover:bg-indigo-700 px-4 py-2 rounded-xl transition-all shadow-md shadow-indigo-500/20">
                Sign Up
              </Link>
            </div>
          )}

        </div>
      </div>
    </header>
  );
};
