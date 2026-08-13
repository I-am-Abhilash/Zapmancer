import React, { useState } from 'react';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Lock, User, CreditCard, Bell, Shield, Sparkles } from 'lucide-react';
import { Tabs, TabItem } from '../../components/ui/Tabs';
import { AccountTab } from './components/AccountTab';
import { ProfileTab } from './components/ProfileTab';
import { PayoutTab } from './components/PayoutTab';
import { NotificationsTab } from './components/NotificationsTab';
import { PrivacyTab } from './components/PrivacyTab';

type SettingsTabId = 'account' | 'profile' | 'payout' | 'notifications' | 'privacy';

export const SettingsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<SettingsTabId>('account');

  const settingsTabs: TabItem<SettingsTabId>[] = [
    { id: 'account', label: 'Account & Security', icon: Lock },
    { id: 'profile', label: 'Public Profile', icon: User },
    { id: 'payout', label: 'Payout & Escrow', icon: CreditCard },
    { id: 'notifications', label: 'Notifications & Webhooks', icon: Bell },
    { id: 'privacy', label: 'Privacy & Data', icon: Shield },
  ];

  return (
    <div className="min-h-screen flex flex-col bg-canvas text-ink transition-colors duration-200 antialiased font-sans">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        {/* Page Title Header */}
        <div className="space-y-1 border-b border-hairline pb-6">
          <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
            <Sparkles className="w-3.5 h-3.5" /> Marketplace Settings
          </div>
          <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight text-ink">Account & Escrow Settings</h1>
          <p className="text-sm text-mute">Manage security credentials, freelancer profile identity, payout wallets, and developer webhooks.</p>
        </div>

        {/* Professional 2-Column Sidebar Layout */}
        <div className="flex flex-col md:flex-row gap-8 items-start">
          
          {/* Left Sidebar Navigation */}
          <aside className="w-full md:w-64 shrink-0 sticky top-24">
            <div className="text-[11px] font-bold tracking-[0.1em] uppercase text-mute mb-3 px-1">
              Settings Navigation
            </div>
            <Tabs
              tabs={settingsTabs}
              activeTab={activeTab}
              onChange={(id) => setActiveTab(id)}
              orientation="vertical"
            />
          </aside>

          {/* Right Main Settings Content */}
          <section className="flex-1 min-w-0 w-full">
            {activeTab === 'account' && <AccountTab />}
            {activeTab === 'profile' && <ProfileTab />}
            {activeTab === 'payout' && <PayoutTab />}
            {activeTab === 'notifications' && <NotificationsTab />}
            {activeTab === 'privacy' && <PrivacyTab />}
          </section>

        </div>

      </main>

      <Footer />
    </div>
  );
};
