import React, { useState } from 'react';
import { CreditCard, Wallet, ShieldCheck, CheckCircle2, Clock, Landmark, Plus } from 'lucide-react';

export const PayoutTab: React.FC = () => {
  const [autoReleaseDays, setAutoReleaseDays] = useState('7');
  const [defaultCurrency, setDefaultCurrency] = useState('USD');

  const payoutMethods = [
    { type: 'Bank Direct Deposit (Stripe Connect)', identifier: 'Chase Bank ****8492', isDefault: true, icon: Landmark },
    { type: 'Web3 Escrow Wallet', identifier: '0x71C...39A2 (USDC - Polygon)', isDefault: false, icon: Wallet },
  ];

  return (
    <div className="space-y-8">
      {/* Payout Methods */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h2 className="text-xl font-bold text-ink flex items-center gap-2">
              <CreditCard className="w-5 h-5 text-brand-green" /> Payout & Direct Deposit Wallets
            </h2>
            <p className="text-xs text-mute mt-1">Manage external bank accounts and crypto wallets where milestone funds are transferred.</p>
          </div>

          <button className="inline-flex items-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white font-bold px-5 py-2.5 rounded-full text-xs uppercase tracking-[0.05em] transition-all hover:scale-[1.04] shadow-md shadow-brand-green/20">
            <Plus className="w-4 h-4" /> Add Payout Method
          </button>
        </div>

        <div className="space-y-3">
          {payoutMethods.map((m, idx) => {
            const Icon = m.icon;
            return (
              <div key={idx} className="p-5 rounded-xl bg-surface-elevated border border-hairline flex items-center justify-between gap-4">
                <div className="flex items-center gap-4">
                  <div className="w-10 h-10 rounded-full bg-surface text-brand-green flex items-center justify-center">
                    <Icon className="w-5 h-5" />
                  </div>
                  <div>
                    <div className="text-sm font-bold text-ink">{m.type}</div>
                    <div className="text-xs text-mute">{m.identifier}</div>
                  </div>
                </div>

                <div className="flex items-center gap-3">
                  {m.isDefault ? (
                    <span className="px-3 py-1 rounded-full bg-brand-green/10 text-brand-green text-xs font-bold uppercase tracking-wider flex items-center gap-1">
                      <CheckCircle2 className="w-3.5 h-3.5" /> Primary Payout
                    </span>
                  ) : (
                    <button className="text-xs font-bold text-mute hover:text-ink transition-colors uppercase tracking-wider">
                      Make Primary
                    </button>
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

        <div className="p-4 rounded-xl bg-surface-elevated border border-hairline flex items-center gap-3">
          <ShieldCheck className="w-5 h-5 text-brand-green shrink-0" />
          <p className="text-xs text-mute leading-relaxed">
            All client escrow deposits are held in audited smart contract vaults. Funds are released instantly upon client milestone approval or automatically after the chosen review window expires without dispute.
          </p>
        </div>
      </div>
    </div>
  );
};
