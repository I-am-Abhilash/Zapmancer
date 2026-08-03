import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { ThemeProvider } from './context/ThemeContext';
import { ToastProvider } from './context/ToastContext';
import { ErrorBoundary } from './components/common/ErrorBoundary';
import { SEO } from './components/common/SEO';
import { LandingPage } from './features/landing/LandingPage';
import { FeedPage } from './features/feed/FeedPage';
import { ArticlePage } from './features/article/ArticlePage';
import { ProfilePage } from './features/profile/ProfilePage';
import { SearchPage } from './features/search/SearchPage';
import { EditorPage } from './features/editor/EditorPage';
import { AuthPage } from './features/auth/AuthPage';


export const AppRouter: React.FC = () => {
  return (
    <ErrorBoundary>
      <ThemeProvider>
        <ToastProvider>
          <BrowserRouter>
            <Routes>
              
              {/* Landing Route */}
              <Route path="/" element={
                <>
                  <SEO title="Home — The Modern Platform for Thoughtful Readers" />
                  <LandingPage />
                </>
              } />

              {/* Feed Route */}
              <Route path="/feed" element={
                <>
                  <SEO title="Explore Feed — Tech, Design & Engineering" />
                  <FeedPage />
                </>
              } />

              {/* Article Detail Route */}
              <Route path="/article/:id" element={
                <>
                  <SEO title="Reading Story" />
                  <ArticlePage />
                </>
              } />

              {/* User Profile Route */}
              <Route path="/profile/:id" element={
                <>
                  <SEO title="Author Profile" />
                  <ProfilePage />
                </>
              } />

              {/* Search Route */}
              <Route path="/search" element={
                <>
                  <SEO title="Search Stories & Authors" />
                  <SearchPage />
                </>
              } />

              {/* Writing Studio Route */}
              <Route path="/editor" element={
                <>
                  <SEO title="Writing Studio — Craft Your Story" />
                  <EditorPage />
                </>
              } />

              {/* Auth Routes */}
              <Route path="/login" element={
                <>
                  <SEO title="Log In" />
                  <AuthPage initialMode="login" />
                </>
              } />

              <Route path="/signup" element={
                <>
                  <SEO title="Sign Up — Join ScriptSide" />
                  <AuthPage initialMode="signup" />
                </>
              } />

              <Route path="/forgot-password" element={
                <>
                  <SEO title="Reset Password" />
                  <AuthPage initialMode="forgot" />
                </>
              } />

              <Route path="/verify" element={
                <>
                  <SEO title="Security Verification" />
                  <AuthPage initialMode="verify" />
                </>
              } />

            </Routes>
          </BrowserRouter>
        </ToastProvider>
      </ThemeProvider>
    </ErrorBoundary>
  );
};
