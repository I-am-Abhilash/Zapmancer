import React from 'react';
import { Link } from 'react-router-dom';
import { Sun, Moon, Edit3, Search } from 'lucide-react';
import { useTheme } from '../../context/ThemeContext';

interface HeaderProps {
  onNavigateLogin?: () => void;
  onNavigateSignUp?: () => void;
}

export const Header: React.FC<HeaderProps> = ({ onNavigateLogin, onNavigateSignUp }) => {
  const { theme, toggleTheme } = useTheme();

  return (
    <header className="site-header">
      <div className="container header-container">
        
        {/* Brand Logo */}
        <Link to="/" className="header-logo">
          SCRIPTSIDE
        </Link>

        {/* Center Navigation Links */}
        <nav className="header-nav hidden md:flex">
          <Link to="/feed">Explore Feed</Link>
          <Link to="/search" className="flex items-center gap-1">
            <Search size={14} /> Search
          </Link>
          <Link to="/editor" className="flex items-center gap-1">
            <Edit3 size={14} /> Write
          </Link>
        </nav>

        {/* Right Action Items */}
        <div className="header-actions">
          {/* Light / Dark Mode Switcher */}
          <button
              onClick={toggleTheme}
              className="theme-toggle-btn"
              aria-label="Toggle Theme"
              title={`Switch to ${theme === 'light' ? 'Dark' : 'Light'} Mode`}
          >
            {theme === 'light' ? (
                <Moon size={18} fill="currentColor" />
            ) : (
                <Sun size={18} fill="currentColor" />
            )}
          </button>

          {/* Auth Actions */}
          <Link to="/login" className="btn-signin">
            Sign in
          </Link>

          <Link to="/signup" className="btn-primary" style={{ padding: '0.65rem 1.65rem', fontSize: '0.9rem' }}>
            Get started
          </Link>
        </div>

      </div>
    </header>
  );
};
