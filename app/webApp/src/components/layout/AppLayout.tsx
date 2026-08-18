import React from 'react';
import { Outlet } from 'react-router-dom';
import { Header } from './Header';
import { Footer } from './Footer';

interface AppLayoutProps {
  hideFooter?: boolean;
}

export const AppLayout: React.FC<AppLayoutProps> = ({ hideFooter = false }) => {
  return (
    <div className="min-h-screen flex flex-col bg-surface text-ink transition-colors duration-200">
      <Header />
      <main className="flex-1 w-full">
        <Outlet />
      </main>
      {!hideFooter && <Footer />}
    </div>
  );
};
