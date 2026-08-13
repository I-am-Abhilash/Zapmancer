import React, { useState } from 'react';
import './settings.css';
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
    <div className="settings-page">
      <Header isLoggedIn={true} />

      <main className="settings-main">
        
        {/* Page Title Header */}
        <div className="settings-header">
          <div className="settings-badge">
            <Sparkles className="w-3.5 h-3.5" /> Marketplace Settings
          </div>
          <h1 className="settings-title">Account & Escrow Settings</h1>
          <p className="settings-subtitle">Manage security credentials, freelancer profile identity, payout wallets, and developer webhooks.</p>
        </div>

        {/* 2-Column Sidebar Layout */}
        <div className="settings-layout">
          
          {/* Left Sidebar Navigation */}
          <aside className="settings-sidebar">
            <div className="settings-sidebar-label">
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
          <section className="settings-content">
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
