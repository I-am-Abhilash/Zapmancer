import React, { useState } from 'react';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Settings, Lock, CreditCard, Bell, Moon, Save, ShieldCheck } from 'lucide-react';
import { useTheme } from '../../context/ThemeContext';

export const SettingsPage: React.FC = () => {
  const { theme, toggleTheme } = useTheme();
  const [activeTab, setActiveTab] = useState<'account' | 'payment' | 'notifications'>('account');

  return (
    <div className="min-h-screen flex flex-col bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 transition-colors">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-5xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        <div>
          <h1 className="text-3xl font-extrabold tracking-tight">Account & Escrow Settings</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Manage security, payout preferences, and marketplace notifications.</p>
        </div>

        {/* Settings Navigation Tabs */}
        <div className="flex border-b border-slate-200 dark:border-slate-800 gap-8 text-sm font-bold">
          <button
            onClick={() => setActiveTab('account')}
            className={`pb-3 flex items-center gap-2 border-b-2 transition-all ${
              activeTab === 'account'
                ? 'border-indigo-600 text-indigo-600 dark:text-indigo-400'
                : 'border-transparent text-slate-500 hover:text-slate-900 dark:hover:text-white'
            }`}
          >
            <Lock className="w-4 h-4" /> Account & Security
          </button>

          <button
            onClick={() => setActiveTab('payment')}
            className={`pb-3 flex items-center gap-2 border-b-2 transition-all ${
              activeTab === 'payment'
                ? 'border-indigo-600 text-indigo-600 dark:text-indigo-400'
                : 'border-transparent text-slate-500 hover:text-slate-900 dark:hover:text-white'
            }`}
          >
            <CreditCard className="w-4 h-4" /> Payout & Escrow
          </button>

          <button
            onClick={() => setActiveTab('notifications')}
            className={`pb-3 flex items-center gap-2 border-b-2 transition-all ${
              activeTab === 'notifications'
                ? 'border-indigo-600 text-indigo-600 dark:text-indigo-400'
                : 'border-transparent text-slate-500 hover:text-slate-900 dark:hover:text-white'
            }`}
          >
            <Bell className="w-4 h-4" /> Notification Alerts
          </button>
        </div>

        {/* Tab Contents */}
        {activeTab === 'account' && (
          <div className="bg-white dark:bg-slate-900 p-8 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-xl space-y-6">
            <h2 className="text-xl font-bold">Security & Password</h2>
            
            <div className="space-y-4 max-w-md">
              <div>
                <label className="block text-sm font-semibold mb-1">Email Address</label>
                <input
                  type="email"
                  disabled
                  value="alex.m@zapmancer.io"
                  className="w-full px-4 py-2.5 rounded-xl border border-slate-200 dark:border-slate-800 bg-slate-100 dark:bg-slate-800 text-sm font-medium text-slate-500"
                />
              </div>

              <div>
                <label className="block text-sm font-semibold mb-1">Current Password</label>
                <input
                  type="password"
                  placeholder="••••••••"
                  className="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm"
                />
              </div>

              <div>
                <label className="block text-sm font-semibold mb-1">New Password</label>
                <input
                  type="password"
                  placeholder="••••••••"
                  className="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm"
                />
              </div>
            </div>

            <div className="pt-4 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between">
              <div className="flex items-center gap-2 text-xs font-semibold text-emerald-500">
                <ShieldCheck className="w-4 h-4" /> 2-Factor Authentication Enabled
              </div>

              <button className="flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white font-bold px-6 py-2.5 rounded-xl text-sm transition-all shadow-md">
                Update Security Settings <Save className="w-4 h-4" />
              </button>
            </div>
          </div>
        )}

        {activeTab === 'payment' && (
          <div className="bg-white dark:bg-slate-900 p-8 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-xl space-y-6">
            <h2 className="text-xl font-bold">Payout & Escrow Wallet</h2>

            <div className="p-6 rounded-2xl bg-slate-50 dark:bg-slate-800/50 border border-slate-200 dark:border-slate-700 space-y-2">
              <h4 className="font-bold text-slate-900 dark:text-white">Connected Direct Deposit Wallet</h4>
              <p className="text-xs text-slate-500">Bank Account ending in ****8492 &bull; Instant Escrow Release</p>
            </div>

            <button className="flex items-center gap-2 bg-emerald-600 hover:bg-emerald-700 text-white font-bold px-6 py-2.5 rounded-xl text-sm transition-all shadow-md">
              Add New Payout Method
            </button>
          </div>
        )}

        {activeTab === 'notifications' && (
          <div className="bg-white dark:bg-slate-900 p-8 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-xl space-y-6">
            <h2 className="text-xl font-bold">Preferences</h2>

            <div className="space-y-4 text-sm font-semibold">
              <label className="flex items-center justify-between p-4 rounded-2xl bg-slate-50 dark:bg-slate-800/50 cursor-pointer">
                <span>Email me when a proposal is submitted to my project</span>
                <input type="checkbox" defaultChecked className="w-4 h-4 rounded text-indigo-600 focus:ring-indigo-500" />
              </label>

              <label className="flex items-center justify-between p-4 rounded-2xl bg-slate-50 dark:bg-slate-800/50 cursor-pointer">
                <span>Email me when a milestone payment is funded into escrow</span>
                <input type="checkbox" defaultChecked className="w-4 h-4 rounded text-indigo-600 focus:ring-indigo-500" />
              </label>
            </div>
          </div>
        )}

      </main>

      <Footer />
    </div>
  );
};
