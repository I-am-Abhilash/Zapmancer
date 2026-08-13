import React, { useState } from 'react';
import { Eye, Download, AlertTriangle, ShieldCheck, FileText, CheckCircle2 } from 'lucide-react';

export const PrivacyTab: React.FC = () => {
  const [visibility, setVisibility] = useState<'public' | 'clients_only' | 'private'>('public');
  const [isExporting, setIsExporting] = useState(false);
  const [exportComplete, setExportComplete] = useState(false);

  const handleExportData = () => {
    setIsExporting(true);
    setTimeout(() => {
      setIsExporting(false);
      setExportComplete(true);
    }, 1500);
  };

  return (
    <div className="space-y-8">
      {/* Profile Privacy & Search Visibility */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div>
          <h2 className="text-xl font-bold text-ink flex items-center gap-2">
            <Eye className="w-5 h-5 text-brand-green" /> Profile Privacy & Discovery
          </h2>
          <p className="text-xs text-mute mt-1">Control who can discover your freelancer profile and view project proposals.</p>
        </div>

        <div className="space-y-3">
          <label
            onClick={() => setVisibility('public')}
            className={`flex items-center justify-between p-4 rounded-xl border cursor-pointer transition-all ${
              visibility === 'public'
                ? 'bg-surface-elevated border-brand-green'
                : 'bg-surface border-hairline hover:bg-surface-elevated'
            }`}
          >
            <div>
              <div className="text-sm font-bold text-ink">🌐 Public (Recommended)</div>
              <div className="text-xs text-mute">Visible to all marketplace clients and indexed by search engines.</div>
            </div>
            <input type="radio" name="visibility" checked={visibility === 'public'} onChange={() => {}} className="accent-brand-green" />
          </label>

          <label
            onClick={() => setVisibility('clients_only')}
            className={`flex items-center justify-between p-4 rounded-xl border cursor-pointer transition-all ${
              visibility === 'clients_only'
                ? 'bg-surface-elevated border-brand-green'
                : 'bg-surface border-hairline hover:bg-surface-elevated'
            }`}
          >
            <div>
              <div className="text-sm font-bold text-ink">🔒 Logged-in Zapmancer Clients Only</div>
              <div className="text-xs text-mute">Hidden from search engines; only verified clients on Zapmancer can view your profile.</div>
            </div>
            <input type="radio" name="visibility" checked={visibility === 'clients_only'} onChange={() => {}} className="accent-brand-green" />
          </label>

          <label
            onClick={() => setVisibility('private')}
            className={`flex items-center justify-between p-4 rounded-xl border cursor-pointer transition-all ${
              visibility === 'private'
                ? 'bg-surface-elevated border-brand-green'
                : 'bg-surface border-hairline hover:bg-surface-elevated'
            }`}
          >
            <div>
              <div className="text-sm font-bold text-ink">🕵️ Private & Stealth Mode</div>
              <div className="text-xs text-mute">Hidden from all searches. Only clients you submit direct proposals to can view your profile.</div>
            </div>
            <input type="radio" name="visibility" checked={visibility === 'private'} onChange={() => {}} className="accent-brand-green" />
          </label>
        </div>
      </div>

      {/* GDPR Data Export */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div>
          <h2 className="text-xl font-bold text-ink flex items-center gap-2">
            <Download className="w-5 h-5 text-brand-green" /> GDPR Data Export
          </h2>
          <p className="text-xs text-mute mt-1">Download a complete JSON archive of your account profile, proposal history, and invoices.</p>
        </div>

        <div className="p-4 rounded-xl bg-surface-elevated border border-hairline flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <div className="text-sm font-bold text-ink flex items-center gap-2">
              <FileText className="w-4 h-4 text-brand-green" /> Complete Profile & Escrow Transaction Record
            </div>
            <div className="text-xs text-mute">Package includes user metadata, proposal logs, and payout ledger receipts.</div>
          </div>

          <button
            onClick={handleExportData}
            disabled={isExporting}
            className="inline-flex items-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white font-bold px-5 py-2.5 rounded-full text-xs uppercase tracking-[0.05em] transition-all hover:scale-[1.04] shrink-0"
          >
            {isExporting ? (
              'Generating Archive...'
            ) : exportComplete ? (
              <>
                <CheckCircle2 className="w-4 h-4" /> Download Ready (.ZIP)
              </>
            ) : (
              <>
                <Download className="w-4 h-4" /> Export My Data
              </>
            )}
          </button>
        </div>
      </div>

      {/* Danger Zone: Account Closure */}
      <div className="bg-surface p-8 rounded-xl border border-red-500/30 space-y-6">
        <div>
          <h2 className="text-xl font-bold text-red-500 flex items-center gap-2">
            <AlertTriangle className="w-5 h-5" /> Danger Zone: Account Deletion
          </h2>
          <p className="text-xs text-mute mt-1">Permanently close your Zapmancer freelancer account and remove your active bounties.</p>
        </div>

        <div className="p-4 rounded-xl bg-red-500/10 border border-red-500/20 text-xs text-red-400 space-y-2">
          <p className="font-bold">⚠️ Account deletion guard checks:</p>
          <ul className="list-disc list-inside space-y-1">
            <li>You must have $0.00 pending escrow wallet balances.</li>
            <li>No active milestone contracts or pending disputes can be in progress.</li>
          </ul>
        </div>

        <button className="bg-red-600 hover:bg-red-700 text-white font-bold px-6 py-3 rounded-full text-xs uppercase tracking-[0.05em] transition-all hover:scale-[1.04]">
          Request Account Deletion
        </button>
      </div>
    </div>
  );
};
