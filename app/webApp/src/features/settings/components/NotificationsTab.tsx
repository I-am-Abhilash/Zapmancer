import React, { useState } from 'react';
import '../settings.css';
import { Bell, Plus, Trash2, Check } from 'lucide-react';

const INIT_WEBHOOKS = [
  { id: 'wh1', name: 'Milestone Release Hook', url: 'https://api.acme.ai/webhooks/milestone' },
];

export const NotificationsTab: React.FC = () => {
  const [webhooks, setWebhooks] = useState(INIT_WEBHOOKS);
  const [webhookUrl, setWebhookUrl] = useState('');
  const [saved, setSaved] = useState(false);

  const addWebhook = () => {
    const url = webhookUrl.trim();
    if (!url) return;
    setWebhooks([...webhooks, { id: `wh${Date.now()}`, name: 'Custom Webhook', url }]);
    setWebhookUrl('');
  };

  const save = () => { setSaved(true); setTimeout(() => setSaved(false), 2000); };

  const TOGGLES = [
    { id: 'escrow', label: 'Escrow released', desc: 'When a client approves and releases a milestone payment.' },
    { id: 'proposal', label: 'New proposal invite', desc: 'When a company sends you a direct contract invite.' },
    { id: 'message', label: 'New message', desc: 'When you receive a new direct message from a client.' },
    { id: 'kyc', label: 'KYC status updates', desc: 'Verification status changes on your identity documents.' },
    { id: 'digest', label: 'Weekly digest', desc: 'A weekly summary of your earnings and open contracts.' },
  ];

  const [enabled, setEnabled] = useState<Record<string, boolean>>({
    escrow: true, proposal: true, message: true, kyc: false, digest: false,
  });

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>

      <div className="settings-section">
        <div className="settings-section-header">
          <Bell size={15} style={{ color: 'var(--color-steel)' }} />
          <div>
            <p className="settings-section-title">Email notifications</p>
            <p className="settings-section-sub">Control which events trigger an email to your inbox.</p>
          </div>
        </div>
        <div className="settings-section-body">
          <div style={{ display: 'flex', flexDirection: 'column' }}>
            {TOGGLES.map((t) => (
              <div key={t.id} className="settings-toggle-row">
                <div>
                  <p className="settings-toggle-label">{t.label}</p>
                  <p className="settings-toggle-sub">{t.desc}</p>
                </div>
                <input
                  type="checkbox"
                  className="settings-toggle"
                  checked={!!enabled[t.id]}
                  onChange={(e) => setEnabled({ ...enabled, [t.id]: e.target.checked })}
                />
              </div>
            ))}
          </div>
          {saved && <div className="settings-toast"><Check size={14} /> Notification preferences saved.</div>}
          <div className="settings-btn-row">
            <button className="settings-btn-primary" onClick={save}>Save preferences</button>
          </div>
        </div>
      </div>

      <div className="settings-section">
        <div className="settings-section-header">
          <Plus size={15} style={{ color: 'var(--color-steel)' }} />
          <div>
            <p className="settings-section-title">Webhook endpoints</p>
            <p className="settings-section-sub">Receive real-time POST events for milestone and escrow activity.</p>
          </div>
        </div>
        <div className="settings-section-body">
          <div style={{ display: 'flex', gap: 8 }}>
            <input
              type="url"
              placeholder="https://your-server.com/webhooks/..."
              value={webhookUrl}
              onChange={(e) => setWebhookUrl(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && (e.preventDefault(), addWebhook())}
              className="settings-input"
              style={{ flex: 1 }}
            />
            <button className="settings-btn-secondary" onClick={addWebhook}><Plus size={14} /> Add</button>
          </div>

          {webhooks.length > 0 && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
              {webhooks.map((w) => (
                <div key={w.id} className="settings-list-row">
                  <div>
                    <p style={{ fontSize: 13, fontWeight: 600, color: 'var(--color-ink)' }}>{w.name}</p>
                    <p style={{ fontSize: 12, fontFamily: 'var(--font-mono)', color: 'var(--color-steel)', marginTop: 2 }}>{w.url}</p>
                  </div>
                  <button
                    onClick={() => setWebhooks(webhooks.filter((x) => x.id !== w.id))}
                    style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--color-steel)', padding: 4 }}
                    title="Remove"
                  >
                    <Trash2 size={15} />
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

    </div>
  );
};
