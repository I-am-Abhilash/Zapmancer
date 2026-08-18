import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { ThemeProvider } from './context/ThemeContext';
import { ToastProvider } from './context/ToastContext';
import { AuthProvider } from './context/AuthContext';
import { ErrorBoundary } from './components/common/ErrorBoundary';
import { SEO } from './components/common/SEO';
import { AppLayout } from './components/layout/AppLayout';
import { ProtectedRoute } from './components/common/ProtectedRoute';
import { PublicOnlyRoute } from './components/common/PublicOnlyRoute';
import { NotFoundPage } from './components/common/NotFoundPage';

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
import { KycVerificationPage } from './features/kyc/KycVerificationPage';
import { KycReceiptPage } from './features/kyc/KycReceiptPage';

export const AppRouter: React.FC = () => {
  return (
    <ErrorBoundary>
      <ThemeProvider>
        <AuthProvider>
          <ToastProvider>
            <BrowserRouter>
              <Routes>
                
                {/* Global App Layout with persistent Header & Footer */}
                <Route element={<AppLayout />}>

                  {/* Public Marketplace Discovery Routes */}
                  <Route path="/" element={
                    <>
                      <SEO title="Zapmancer — Transparent Open-Source Freelance Marketplace" />
                      <LandingPage />
                    </>
                  } />

                  <Route path="/projects" element={
                    <>
                      <SEO title="Browse Bounties & Contracts — Zapmancer" />
                      <ProjectListPage />
                    </>
                  } />

                  <Route path="/projects/:id" element={
                    <>
                      <SEO title="Project Bounties — Zapmancer" />
                      <ProjectDetailPage />
                    </>
                  } />

                  <Route path="/search" element={
                    <>
                      <SEO title="Find Verified Talent — Zapmancer" />
                      <SearchPage />
                    </>
                  } />

                  <Route path="/profile/:id" element={
                    <>
                      <SEO title="Engineer Profile — Zapmancer" />
                      <ProfilePage />
                    </>
                  } />

                  <Route path="/kyc/receipt/:id" element={
                    <>
                      <SEO title="KYC Cryptographic Audit Receipt — Zapmancer" />
                      <KycReceiptPage />
                    </>
                  } />

                  {/* Guest Authentication Routes (Public Only) */}
                  <Route element={<PublicOnlyRoute />}>
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
                  </Route>

                  {/* Protected Company / Client Workspace Routes */}
                  <Route element={<ProtectedRoute allowedRoles={['company']} />}>
                    <Route path="/company/dashboard" element={
                      <>
                        <SEO title="Company OS Dashboard — Zapmancer" />
                        <CompanyDashboardPage />
                      </>
                    } />

                    <Route path="/projects/new" element={
                      <>
                        <SEO title="Post Project Bounty — Zapmancer" />
                        <PostProjectPage />
                      </>
                    } />

                    <Route path="/client/proposals" element={
                      <>
                        <SEO title="Client Proposals Hub — Zapmancer" />
                        <ClientProposalsPage />
                      </>
                    } />
                  </Route>

                  {/* Protected Freelancer Workspace Routes */}
                  <Route element={<ProtectedRoute allowedRoles={['freelancer']} />}>
                    <Route path="/home" element={
                      <>
                        <SEO title="Talent Workspace — Zapmancer" />
                        <HomePage />
                      </>
                    } />

                    <Route path="/projects/:id/apply" element={
                      <>
                        <SEO title="Submit Proposal — Zapmancer" />
                        <ProposalSubmitPage />
                      </>
                    } />

                    <Route path="/profile/edit" element={
                      <>
                        <SEO title="Edit Profile — Zapmancer" />
                        <EditProfilePage />
                      </>
                    } />
                  </Route>

                  {/* Protected Shared Authenticated Routes */}
                  <Route element={<ProtectedRoute />}>
                    <Route path="/onboarding" element={
                      <>
                        <SEO title="Profile Setup — Zapmancer" />
                        <OnboardingPage />
                      </>
                    } />

                    <Route path="/messages" element={
                      <>
                        <SEO title="Direct Messages — Zapmancer" />
                        <MessagesPage />
                      </>
                    } />

                    <Route path="/notifications" element={
                      <>
                        <SEO title="Notifications — Zapmancer" />
                        <NotificationPage />
                      </>
                    } />

                    <Route path="/settings" element={
                      <>
                        <SEO title="Settings & Escrow — Zapmancer" />
                        <SettingsPage />
                      </>
                    } />

                    <Route path="/kyc" element={
                      <>
                        <SEO title="Biometric Identity Verification — Zapmancer" />
                        <KycVerificationPage />
                      </>
                    } />
                  </Route>

                  {/* 404 Catch-All Route */}
                  <Route path="*" element={
                    <>
                      <SEO title="404 Not Found — Zapmancer" />
                      <NotFoundPage />
                    </>
                  } />

                </Route>

              </Routes>
            </BrowserRouter>
          </ToastProvider>
        </AuthProvider>
      </ThemeProvider>
    </ErrorBoundary>
  );
};
