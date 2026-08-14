import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { ThemeProvider } from './context/ThemeContext';
import { ToastProvider } from './context/ToastContext';
import { ErrorBoundary } from './components/common/ErrorBoundary';
import { SEO } from './components/common/SEO';

import { LandingPage } from './features/landing/LandingPage';
import { OnboardingPage } from './features/onboarding/OnboardingPage';
import { AuthPage } from './features/auth/AuthPage';
import { HomePage } from './features/home/HomePage';

import { ProjectListPage } from './features/projects/ProjectListPage';
import { ProjectDetailPage } from './features/projects/ProjectDetailPage';
import { PostProjectPage } from './features/projects/PostProjectPage';
import { ProposalSubmitPage } from './features/proposals/ProposalSubmitPage';
import { ClientProposalsPage } from './features/proposals/ClientProposalsPage';

import { CompanyDashboardPage } from './features/company/CompanyDashboardPage';

import { SearchPage } from './features/search/SearchPage';
import { ProfilePage } from './features/profile/ProfilePage';
import { EditProfilePage } from './features/profile/EditProfilePage';

import { MessagesPage } from './features/messages/MessagesPage';
import { NotificationPage } from './features/notifications/NotificationPage';
import { SettingsPage } from './features/settings/SettingsPage';

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
                  <SEO title="Zapmancer — Transparent Open-Source Freelance Marketplace" />
                  <LandingPage />
                </>
              } />

              {/* Onboarding Route */}
              <Route path="/onboarding" element={
                <>
                  <SEO title="Profile Setup — Zapmancer" />
                  <OnboardingPage />
                </>
              } />

              {/* Company OS Dashboard Route */}
              <Route path="/company/dashboard" element={
                <>
                  <SEO title="Company OS Dashboard — Zapmancer" />
                  <CompanyDashboardPage />
                </>
              } />

              {/* Dashboard Feed Route */}
              <Route path="/home" element={
                <>
                  <SEO title="Dashboard — Zapmancer" />
                  <HomePage />
                </>
              } />

              {/* Projects Routes */}
              <Route path="/projects" element={
                <>
                  <SEO title="Browse Bounties — Zapmancer" />
                  <ProjectListPage />
                </>
              } />

              <Route path="/projects/new" element={
                <>
                  <SEO title="Post Project Bounty — Zapmancer" />
                  <PostProjectPage />
                </>
              } />

              <Route path="/projects/:id" element={
                <>
                  <SEO title="Project Bounties — Zapmancer" />
                  <ProjectDetailPage />
                </>
              } />

              <Route path="/projects/:id/apply" element={
                <>
                  <SEO title="Submit Proposal — Zapmancer" />
                  <ProposalSubmitPage />
                </>
              } />

              {/* Client Proposals Review */}
              <Route path="/client/proposals" element={
                <>
                  <SEO title="Client Proposals Hub — Zapmancer" />
                  <ClientProposalsPage />
                </>
              } />

              {/* Search Route */}
              <Route path="/search" element={
                <>
                  <SEO title="Search Talent & Projects — Zapmancer" />
                  <SearchPage />
                </>
              } />

              {/* Profile Routes */}
              <Route path="/profile/edit" element={
                <>
                  <SEO title="Edit Profile — Zapmancer" />
                  <EditProfilePage />
                </>
              } />

              <Route path="/profile/:id" element={
                <>
                  <SEO title="Engineer Profile — Zapmancer" />
                  <ProfilePage />
                </>
              } />

              {/* Messaging & Chat */}
              <Route path="/messages" element={
                <>
                  <SEO title="Direct Messages — Zapmancer" />
                  <MessagesPage />
                </>
              } />

              {/* Notifications Feed */}
              <Route path="/notifications" element={
                <>
                  <SEO title="Notifications — Zapmancer" />
                  <NotificationPage />
                </>
              } />

              {/* Settings */}
              <Route path="/settings" element={
                <>
                  <SEO title="Settings & Escrow — Zapmancer" />
                  <SettingsPage />
                </>
              } />

              {/* Auth Routes */}
              <Route path="/login" element={
                <>
                  <SEO title="Log In — Zapmancer" />
                  <AuthPage initialMode="login" />
                </>
              } />

              <Route path="/signup" element={
                <>
                  <SEO title="Sign Up — Zapmancer" />
                  <AuthPage initialMode="signup" />
                </>
              } />

              <Route path="/forgot-password" element={
                <>
                  <SEO title="Reset Password — Zapmancer" />
                  <AuthPage initialMode="forgot" />
                </>
              } />

              <Route path="/verify" element={
                <>
                  <SEO title="Security Verification — Zapmancer" />
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
