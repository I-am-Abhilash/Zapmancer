import React, { useState } from 'react';
import './settings.css';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Lock, User, CreditCard, Bell, Shield } from 'lucide-react';
import { AccountTab } from './components/AccountTab';
import { ProfileTab } from './components/ProfileTab';
import { PayoutTab } from './components/PayoutTab';
import { NotificationsTab } from './components/NotificationsTab';
import { PrivacyTab } from './components/PrivacyTab';

type TabId = 'account' | 'profile' | 'payout' | 'notifications' | 'privacy';

const NAV: { id: TabId; label: string; icon: React.ElementType }[] = [
  { id: 'account', label: 'Account & Security', icon: Lock },
  { id: 'profile', label: 'Public Profile', icon: User },
  { id: 'payout', label: 'Payout & Escrow', icon: CreditCard },
  { id: 'notifications', label: 'Notifications', icon: Bell },
  { id: 'privacy', label: 'Privacy & Data', icon: Shield },
];

export const SettingsPage: React.FC = () => {
  const [tab, setTab] = useState<TabId>('account');

  return (
    <div className="settings-page">
      <Header isLoggedIn={true} />

      <main className="settings-main">
        <div className="settings-page-header">
          <h1 className="settings-page-title">Settings</h1>
          <p className="settings-page-sub">Manage your account, profile, payout methods, and privacy preferences.</p>
        </div>

        <div className="settings-layout">
          {/* Sidebar */}
          <aside className="settings-sidebar">
            <p className="settings-sidebar-label">Navigation</p>
            {NAV.map((n) => {
              const Icon = n.icon;
              return (
                <button
                  key={n.id}
                  onClick={() => setTab(n.id)}
                  className={`settings-nav-item${tab === n.id ? ' active' : ''}`}
                >
                  <Icon size={15} /> {n.label}
                </button>
              );
            })}
          </aside>

          {/* Content */}
          <section className="settings-content">
            {tab === 'account' && <AccountTab />}
            {tab === 'profile' && <ProfileTab />}
            {tab === 'payout' && <PayoutTab />}
            {tab === 'notifications' && <NotificationsTab />}
            {tab === 'privacy' && <PrivacyTab />}
          </section>
        </div>
      </main>

      <Footer />
    </div>
  );
};
