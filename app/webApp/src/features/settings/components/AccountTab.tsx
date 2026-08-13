import React, { useState } from 'react';
import { Lock, ShieldCheck, KeyRound, Smartphone, Monitor, Trash2, CheckCircle2, QrCode } from 'lucide-react';

export const AccountTab: React.FC = () => {
  const [show2FAModal, setShow2FAModal] = useState(false);
  const [is2FAEnabled, setIs2FAEnabled] = useState(true);

  const activeSessions = [
    { device: 'Chrome on macOS (Current Session)', location: 'San Francisco, US', ip: '192.168.1.45', lastActive: 'Active Now', icon: Monitor },
    { device: 'Zapmancer iOS App', location: 'San Francisco, US', ip: '172.56.21.90', lastActive: '2 hours ago', icon: Smartphone },
  ];

  return (
    <div className="space-y-8">
      {/* Security & Password Card */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div>
          <h2 className="text-xl font-bold text-ink flex items-center gap-2">
            <Lock className="w-5 h-5 text-brand-green" /> Security & Password
          </h2>
          <p className="text-xs text-mute mt-1">Manage credentials and authentication preferences for your account.</p>
        </div>

        <div className="space-y-4 max-w-lg">
          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">Email Address</label>
            <input
              type="email"
              disabled
              value="alex.m@zapmancer.io"
              className="w-full px-4 py-2.5 rounded-full border border-hairline bg-surface-elevated text-sm font-medium text-mute cursor-not-allowed"
            />
          </div>

          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">Current Password</label>
            <input
              type="password"
              placeholder="••••••••"
              className="w-full px-4 py-2.5 rounded-full border border-hairline bg-surface-elevated text-sm text-ink focus:outline-none focus:border-brand-green transition-colors"
            />
          </div>

          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">New Password</label>
            <input
              type="password"
              placeholder="••••••••"
              className="w-full px-4 py-2.5 rounded-full border border-hairline bg-surface-elevated text-sm text-ink focus:outline-none focus:border-brand-green transition-colors"
            />
          </div>
        </div>

        <div className="pt-4 border-t border-hairline flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div className="flex items-center gap-2 text-xs font-bold text-brand-green">
            <ShieldCheck className="w-4 h-4" /> Password last changed 30 days ago
          </div>

          <button className="flex items-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white font-bold px-6 py-3 rounded-full text-xs uppercase tracking-[0.05em] transition-all hover:scale-[1.04] shadow-md shadow-brand-green/20">
            Save Password Changes <KeyRound className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* Two-Factor Authentication (2FA) */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h2 className="text-xl font-bold text-ink flex items-center gap-2">
              <ShieldCheck className="w-5 h-5 text-brand-green" /> Two-Factor Authentication (2FA)
            </h2>
            <p className="text-xs text-mute mt-1">Secure milestone payout approvals and logins with TOTP Authenticator apps.</p>
          </div>

          <button
            onClick={() => setShow2FAModal(true)}
            className="inline-flex items-center gap-2 bg-surface-elevated hover:bg-surface-modal text-ink border border-hairline font-bold px-5 py-2.5 rounded-full text-xs uppercase tracking-[0.05em] transition-all hover:scale-[1.04]"
          >
            <QrCode className="w-4 h-4 text-brand-green" /> {is2FAEnabled ? 'Manage 2FA App' : 'Enable 2FA App'}
          </button>
        </div>

        <div className="p-4 rounded-xl bg-surface-elevated border border-hairline flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-brand-green/10 text-brand-green flex items-center justify-center font-bold">
              TOTP
            </div>
            <div>
              <div className="text-sm font-bold text-ink">Google Authenticator / Authy</div>
              <div className="text-xs text-mute">Status: {is2FAEnabled ? 'Active & Enforced' : 'Disabled'}</div>
            </div>
          </div>

          <span className={`px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider ${is2FAEnabled ? 'bg-brand-green/10 text-brand-green' : 'bg-surface-modal text-mute'}`}>
            {is2FAEnabled ? 'Active' : 'Off'}
          </span>
        </div>
      </div>

      {/* Active Logged-in Sessions */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h2 className="text-xl font-bold text-ink">Active Sessions & Authorized Devices</h2>
            <p className="text-xs text-mute mt-1">Devices currently authenticated to your Zapmancer account.</p>
          </div>

          <button className="inline-flex items-center gap-2 bg-surface-elevated hover:bg-surface-modal text-red-500 border border-hairline font-bold px-4 py-2 rounded-full text-xs uppercase tracking-[0.05em] transition-all">
            <Trash2 className="w-3.5 h-3.5" /> Revoke Other Sessions
          </button>
        </div>

        <div className="space-y-3">
          {activeSessions.map((s, idx) => {
            const Icon = s.icon;
            return (
              <div key={idx} className="p-4 rounded-xl bg-surface-elevated border border-hairline flex items-center justify-between gap-4">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-surface text-brand-green flex items-center justify-center">
                    <Icon className="w-5 h-5" />
                  </div>
                  <div>
                    <div className="text-sm font-bold text-ink">{s.device}</div>
                    <div className="text-xs text-mute">{s.location} &bull; IP {s.ip}</div>
                  </div>
                </div>

                <div className="text-xs font-semibold text-brand-green flex items-center gap-1">
                  <CheckCircle2 className="w-3.5 h-3.5" /> {s.lastActive}
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* 2FA Modal */}
      {show2FAModal && (
        <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-surface border border-hairline rounded-xl p-8 max-w-md w-full space-y-6 shadow-2xl">
            <h3 className="text-xl font-bold text-ink">Configure 2-Factor Authenticator</h3>
            <p className="text-xs text-mute">Scan this QR code with your authenticator app (Google Authenticator, Authy, or 1Password) to protect your escrow payouts.</p>
            
            <div className="bg-white p-4 rounded-xl w-48 h-48 mx-auto flex items-center justify-center border border-hairline">
              <QrCode className="w-36 h-36 text-black" />
            </div>

            <div>
              <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">Verification Code</label>
              <input
                type="text"
                placeholder="123 456"
                className="w-full px-4 py-2.5 text-center font-mono tracking-widest text-lg rounded-full border border-hairline bg-surface-elevated text-ink focus:outline-none focus:border-brand-green"
              />
            </div>

            <div className="flex gap-3">
              <button
                onClick={() => setShow2FAModal(false)}
                className="flex-1 bg-surface-elevated hover:bg-surface-modal text-ink font-bold py-3 rounded-full text-xs uppercase tracking-[0.05em]"
              >
                Cancel
              </button>
              <button
                onClick={() => {
                  setIs2FAEnabled(true);
                  setShow2FAModal(false);
                }}
                className="flex-1 bg-brand-green hover:bg-brand-green-hover text-white font-bold py-3 rounded-full text-xs uppercase tracking-[0.05em]"
              >
                Verify & Activate
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
