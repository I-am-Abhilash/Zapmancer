import React from 'react';
import { Link } from 'react-router-dom';

export const Footer: React.FC = () => {
  return (
    <footer className="site-footer">
      <div className="container">
        
        {/* Footer Navigation Columns */}
        <div className="footer-grid">
          
          <div>
            <div className="footer-col-title font-mono">SCRIPTSIDE</div>
            <p className="text-sm text-[var(--color-mute)] leading-relaxed">
              The modern digital magazine platform for technical readers, engineers, and creators.
            </p>
          </div>

          <div>
            <div className="footer-col-title">Product</div>
            <ul className="footer-list">
              <li><Link to="/feed">Explore Feed</Link></li>
              <li><Link to="/search">Search Stories</Link></li>
              <li><Link to="/editor">Writing Studio</Link></li>
              <li><a href="#">Membership</a></li>
            </ul>
          </div>

          <div>
            <div className="footer-col-title">Resources</div>
            <ul className="footer-list">
              <li><a href="#">Writing Guidelines</a></li>
              <li><a href="#">Style Guide</a></li>
              <li><a href="#">KMP Architecture</a></li>
              <li><a href="#">API Docs</a></li>
            </ul>
          </div>

          <div>
            <div className="footer-col-title">Account</div>
            <ul className="footer-list">
              <li><Link to="/login">Sign In</Link></li>
              <li><Link to="/signup">Get Started</Link></li>
              <li><Link to="/profile/me">My Profile</Link></li>
            </ul>
          </div>

          <div>
            <div className="footer-col-title">Legal</div>
            <ul className="footer-list">
              <li><a href="#">Privacy Policy</a></li>
              <li><a href="#">Terms of Service</a></li>
              <li><a href="#">Security</a></li>
            </ul>
          </div>

        </div>

        {/* Footer Bottom Bar */}
        <div className="footer-bottom">
          <div>
            &copy; {new Date().getFullYear()} ScriptSide Inc. All rights reserved.
          </div>

          <div className="footer-socials">
            <a href="#">Twitter</a>
            <a href="#">GitHub</a>
            <a href="#">LinkedIn</a>
            <a href="#">RSS Feed</a>
          </div>
        </div>

      </div>
    </footer>
  );
};
