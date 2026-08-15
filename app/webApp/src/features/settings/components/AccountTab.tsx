import React, { useState } from 'react';
import '../settings.css';
import { Lock, ShieldCheck, Check } from 'lucide-react';

export const AccountTab: React.FC = () => {
  const [saved, setSaved] = useState(false);
  const save = () => { setSaved(true); setTimeout(() => setSaved(false), 2000); };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>

      <div className="settings-section">
        <div className="settings-section-header">
          <Lock size={15} style={{ color: 'var(--color-steel)' }} />
          <div>
            <p className="settings-section-title">Account credentials</p>
            <p className="settings-section-sub">Update your email address and password.</p>
          </div>
        </div>
        <div className="settings-section-body">
          <div className="settings-grid-2">
            <div className="settings-field">
              <label className="settings-label">Email address</label>
              <input type="email" defaultValue="alex.morgan@email.com" className="settings-input" />
            </div>
            <div className="settings-field">
              <label className="settings-label">Display name</label>
              <input type="text" defaultValue="Alex Morgan" className="settings-input" />
            </div>
          </div>
          <div className="settings-grid-2">
            <div className="settings-field">
              <label className="settings-label">Current password</label>
              <input type="password" placeholder="••••••••" className="settings-input" />
            </div>
            <div className="settings-field">
              <label className="settings-label">New password</label>
              <input type="password" placeholder="••••••••" className="settings-input" />
            </div>
          </div>
          {saved && <div className="settings-toast"><Check size={14} /> Credentials updated.</div>}
          <div className="settings-btn-row">
            <button className="settings-btn-primary" onClick={save}>Save changes</button>
          </div>
        </div>
      </div>

      <div className="settings-section">
        <div className="settings-section-header">
          <ShieldCheck size={15} style={{ color: 'var(--color-steel)' }} />
          <div>
            <p className="settings-section-title">Two-factor authentication</p>
            <p className="settings-section-sub">Add an extra layer of security to your account.</p>
          </div>
        </div>
        <div className="settings-section-body">
          <div className="settings-note-mint">
            2FA is currently <strong>disabled</strong>. We strongly recommend enabling authenticator-based 2FA to protect your escrow wallet.
          </div>
          <div>
            <button className="settings-btn-secondary"><ShieldCheck size={14} /> Enable 2FA (TOTP)</button>
          </div>
        </div>
      </div>

    </div>
  );
};
