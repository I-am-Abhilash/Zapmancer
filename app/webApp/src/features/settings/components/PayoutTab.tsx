import React, { useState } from 'react';
import { CreditCard, Wallet, ShieldCheck, CheckCircle2, Clock, Landmark, Plus, FileText, Check, AlertCircle } from 'lucide-react';

export const PayoutTab: React.FC = () => {
  const [autoReleaseDays, setAutoReleaseDays] = useState('7');
  const [defaultCurrency, setDefaultCurrency] = useState('USD');
  const [payoutRail, setPayoutRail] = useState<'stripe' | 'usdc'>('stripe');
  const [savedSuccessMsg, setSavedSuccessMsg] = useState('');

  const payoutMethods = [
    { type: 'Bank Direct Deposit (Stripe Connect)', identifier: 'Chase Bank ****8492 (USD)', rail: 'stripe', isDefault: true, icon: Landmark },
    { type: 'Web3 Non-Custodial USDC Wallet', identifier: '0x71C...39A2 (Polygon USDC)', rail: 'usdc', isDefault: false, icon: Wallet },
  ];

  const handleSavePreferences = () => {
    setSavedSuccessMsg('Payout Rails & Tax Compliance Preferences Saved!');
    setTimeout(() => setSavedSuccessMsg(''), 2500);
  };

  return (
    <div className="space-y-8">
      
      {/* Biometric KYC Identity & Tax Compliance Panel */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-hairline pb-4">
          <div>
            <h2 className="text-xl font-bold text-ink flex items-center gap-2">
              <ShieldCheck className="w-5 h-5 text-brand-green" /> Biometric Identity & Tax Compliance
            </h2>
            <p className="text-xs text-mute mt-1">Stripe Identity KYC verification and annual W-8BEN / W-9 tax documentation.</p>
          </div>

          <span className="px-3.5 py-1 rounded-full bg-brand-green/10 text-brand-green border border-brand-green/20 text-xs font-bold uppercase tracking-wider flex items-center gap-1.5 shrink-0">
            <CheckCircle2 className="w-4 h-4" /> KYC Level 2 Verified
          </span>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-xs">
          <div className="p-4 rounded-xl bg-surface-elevated border border-hairline space-y-1">
            <span className="text-mute font-bold uppercase text-[10px]">Biometric Identity Verification</span>
            <p className="font-bold text-ink">Stripe Identity &bull; Passport / Government ID</p>
            <p className="text-[11px] text-brand-green font-semibold">Verified on Jan 14, 2026</p>
          </div>

          <div className="p-4 rounded-xl bg-surface-elevated border border-hairline space-y-1">
            <span className="text-mute font-bold uppercase text-[10px]">Tax Certificate Status</span>
            <p className="font-bold text-ink">W-8BEN Foreign Status Form</p>
            <p className="text-[11px] text-brand-green font-semibold">Active &bull; Valid for 2026 Tax Year</p>
          </div>
        </div>
      </div>

      {/* Payout Methods & Rails Selection */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h2 className="text-xl font-bold text-ink flex items-center gap-2">
              <CreditCard className="w-5 h-5 text-brand-green" /> Payout & Escrow Transfer Rails
            </h2>
            <p className="text-xs text-mute mt-1">Select your primary payout rail for 100% retained milestone funds (0% developer fee).</p>
          </div>

          <button className="inline-flex items-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white font-bold px-5 py-2.5 rounded-full text-xs uppercase tracking-[0.05em] transition-all hover:scale-[1.04] shadow-md shadow-brand-green/20">
            <Plus className="w-4 h-4" /> Add Payout Account
          </button>
        </div>

        <div className="space-y-3">
          {payoutMethods.map((m, idx) => {
            const Icon = m.icon;
            const isCurrentRail = payoutRail === m.rail;
            return (
              <div
                key={idx}
                onClick={() => setPayoutRail(m.rail as 'stripe' | 'usdc')}
                className={`p-5 rounded-xl border transition-all cursor-pointer flex items-center justify-between gap-4 ${
                  isCurrentRail
                    ? 'bg-surface-elevated border-brand-green/40 shadow-sm'
                    : 'bg-surface border-hairline hover:bg-surface-elevated'
                }`}
              >
                <div className="flex items-center gap-4">
                  <div className="w-10 h-10 rounded-full bg-surface text-brand-green flex items-center justify-center border border-hairline">
                    <Icon className="w-5 h-5" />
                  </div>
                  <div>
                    <div className="text-sm font-bold text-ink">{m.type}</div>
                    <div className="text-xs text-mute">{m.identifier}</div>
                  </div>
                </div>

                <div className="flex items-center gap-3">
                  {isCurrentRail ? (
                    <span className="px-3 py-1 rounded-full bg-brand-green/10 text-brand-green border border-brand-green/20 text-xs font-bold uppercase tracking-wider flex items-center gap-1">
                      <CheckCircle2 className="w-3.5 h-3.5" /> Primary Payout Rail
                    </span>
                  ) : (
                    <span className="text-xs font-bold text-mute uppercase tracking-wider">
                      Click to Select
                    </span>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* Automated Escrow Release Preferences */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div>
          <h2 className="text-xl font-bold text-ink flex items-center gap-2">
            <Clock className="w-5 h-5 text-brand-green" /> Automated Escrow Release Rules
          </h2>
          <p className="text-xs text-mute mt-1">Specify how long clients have to review completed milestone deliverables before funds auto-release.</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">Auto-Release Review Window</label>
            <select
              value={autoReleaseDays}
              onChange={(e) => setAutoReleaseDays(e.target.value)}
              className="w-full px-4 py-2.5 rounded-full border border-hairline bg-surface-elevated text-sm font-bold text-ink focus:outline-none focus:border-brand-green"
            >
              <option value="3">3 Days Review Window (Express Release)</option>
              <option value="7">7 Days Review Window (Standard Recommended)</option>
              <option value="14">14 Days Review Window (Extended Review)</option>
            </select>
          </div>

          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">Preferred Escrow Display Currency</label>
            <select
              value={defaultCurrency}
              onChange={(e) => setDefaultCurrency(e.target.value)}
              className="w-full px-4 py-2.5 rounded-full border border-hairline bg-surface-elevated text-sm font-bold text-ink focus:outline-none focus:border-brand-green"
            >
              <option value="USD">USD ($) - US Dollar</option>
              <option value="EUR">EUR (€) - Euro</option>
              <option value="USDC">USDC ($) - USD Coin (Stablecoin)</option>
            </select>
          </div>
        </div>

        <div className="p-4 rounded-xl bg-surface-elevated border border-hairline flex items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <ShieldCheck className="w-5 h-5 text-brand-green shrink-0" />
            <p className="text-xs text-mute leading-relaxed">
              All client escrow deposits are held in audited smart contract vaults. Funds are released instantly upon client milestone approval.
            </p>
          </div>

          <button
            onClick={handleSavePreferences}
            className="inline-flex items-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white font-bold px-6 py-2.5 rounded-full text-xs uppercase tracking-[0.05em] transition-all hover:scale-[1.04] shrink-0"
          >
            Save Payout Settings
          </button>
        </div>

        {savedSuccessMsg && (
          <div className="p-3 rounded-xl bg-brand-green/10 border border-brand-green/30 text-brand-green text-xs font-bold text-center flex items-center justify-center gap-2">
            <Check className="w-4 h-4" /> {savedSuccessMsg}
          </div>
        )}
      </div>

    </div>
  );
};
