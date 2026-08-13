import React, { useState } from 'react';
import { Bell, Webhook, Send, Check, Plus, Trash2, Mail, ShieldAlert } from 'lucide-react';

export const NotificationsTab: React.FC = () => {
  const [webhookUrl, setWebhookUrl] = useState('');
  const [webhooks, setWebhooks] = useState([
    { id: '1', name: 'DevOps Slack Channel', url: 'https://hooks.slack.com/services/T00/B00/X00', platform: 'Slack' },
    { id: '2', name: 'Zapmancer Bounties Discord', url: 'https://discord.com/api/webhooks/123/xyz', platform: 'Discord' },
  ]);

  const addWebhook = () => {
    if (webhookUrl.trim()) {
      const platform = webhookUrl.includes('slack.com') ? 'Slack' : webhookUrl.includes('discord.com') ? 'Discord' : 'Custom Webhook';
      setWebhooks([...webhooks, { id: Date.now().toString(), name: `${platform} Integration`, url: webhookUrl.trim(), platform }]);
      setWebhookUrl('');
    }
  };

  const removeWebhook = (id: string) => {
    setWebhooks(webhooks.filter((w) => w.id !== id));
  };

  return (
    <div className="space-y-8">
      {/* Email & Marketplace Notification Alerts */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div>
          <h2 className="text-xl font-bold text-ink flex items-center gap-2">
            <Bell className="w-5 h-5 text-brand-green" /> Email & Push Alert Preferences
          </h2>
          <p className="text-xs text-mute mt-1">Choose which events send immediate email and browser push notifications.</p>
        </div>

        <div className="space-y-3">
          <label className="flex items-center justify-between p-4 rounded-xl bg-surface-elevated border border-hairline cursor-pointer hover:bg-surface-modal transition-colors">
            <div>
              <div className="text-sm font-bold text-ink">Bounty Proposal Submissions</div>
              <div className="text-xs text-mute">Alert me when a developer submits a proposal to my active project.</div>
            </div>
            <input type="checkbox" defaultChecked className="w-4 h-4 rounded accent-brand-green cursor-pointer" />
          </label>

          <label className="flex items-center justify-between p-4 rounded-xl bg-surface-elevated border border-hairline cursor-pointer hover:bg-surface-modal transition-colors">
            <div>
              <div className="text-sm font-bold text-ink">Milestone Escrow Funding & Payouts</div>
              <div className="text-xs text-mute">Alert me when milestone funds are deposited, locked, or released.</div>
            </div>
            <input type="checkbox" defaultChecked className="w-4 h-4 rounded accent-brand-green cursor-pointer" />
          </label>

          <label className="flex items-center justify-between p-4 rounded-xl bg-surface-elevated border border-hairline cursor-pointer hover:bg-surface-modal transition-colors">
            <div>
              <div className="text-sm font-bold text-ink">Direct Messages & Attachment Alerts</div>
              <div className="text-xs text-mute">Alert me when a client or freelancer sends a message or file attachment.</div>
            </div>
            <input type="checkbox" defaultChecked className="w-4 h-4 rounded accent-brand-green cursor-pointer" />
          </label>

          <label className="flex items-center justify-between p-4 rounded-xl bg-surface-elevated border border-hairline cursor-pointer hover:bg-surface-modal transition-colors">
            <div>
              <div className="text-sm font-bold text-ink">Dispute & Legal Audit Warnings</div>
              <div className="text-xs text-mute">High-priority alerts if a milestone enters dispute review.</div>
            </div>
            <input type="checkbox" defaultChecked className="w-4 h-4 rounded accent-brand-green cursor-pointer" />
          </label>
        </div>
      </div>

      {/* Real-time Webhook Integration */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div>
          <h2 className="text-xl font-bold text-ink flex items-center gap-2">
            <Webhook className="w-5 h-5 text-brand-green" /> Developer Webhooks (Slack / Discord / Telegram)
          </h2>
          <p className="text-xs text-mute mt-1">Receive real-time JSON event webhooks for your team's chat tools or backend servers.</p>
        </div>

        <div className="flex flex-col sm:flex-row gap-3">
          <input
            type="url"
            placeholder="Paste Webhook URL (e.g. https://hooks.slack.com/services/...)..."
            value={webhookUrl}
            onChange={(e) => setWebhookUrl(e.target.value)}
            className="flex-1 px-4 py-2.5 rounded-full border border-hairline bg-surface-elevated text-xs text-ink focus:outline-none focus:border-brand-green"
          />
          <button
            onClick={addWebhook}
            className="inline-flex items-center justify-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white font-bold px-6 py-2.5 rounded-full text-xs uppercase tracking-[0.05em] transition-all hover:scale-[1.04]"
          >
            <Plus className="w-4 h-4" /> Add Webhook
          </button>
        </div>

        <div className="space-y-3">
          {webhooks.map((w) => (
            <div key={w.id} className="p-4 rounded-xl bg-surface-elevated border border-hairline flex items-center justify-between gap-4">
              <div className="flex items-center gap-3">
                <Send className="w-4 h-4 text-brand-green" />
                <div>
                  <div className="text-sm font-bold text-ink">{w.name}</div>
                  <div className="text-xs font-mono text-mute truncate max-w-xs sm:max-w-md">{w.url}</div>
                </div>
              </div>

              <button
                onClick={() => removeWebhook(w.id)}
                className="text-mute hover:text-red-500 transition-colors p-1"
                title="Remove Webhook"
              >
                <Trash2 className="w-4 h-4" />
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
