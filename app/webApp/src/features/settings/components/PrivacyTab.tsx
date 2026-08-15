import React, { useState } from 'react';
import '../settings.css';
import { Eye, Download, AlertTriangle, CheckCircle2, Check } from 'lucide-react';

export const PrivacyTab: React.FC = () => {
  const [visibility, setVisibility] = useState<'public' | 'clients_only' | 'private'>('public');
  const [exporting, setExporting] = useState(false);
  const [exportDone, setExportDone] = useState(false);
  const [saved, setSaved] = useState(false);

  const handleExport = () => {
    setExporting(true);
    setTimeout(() => { setExporting(false); setExportDone(true); }, 1500);
  };

  const save = () => { setSaved(true); setTimeout(() => setSaved(false), 2000); };

  const VISIBILITY_OPTIONS = [
    { id: 'public' as const, label: 'Public', desc: 'Visible to all marketplace clients and indexed by search engines.' },
    { id: 'clients_only' as const, label: 'Logged-in clients only', desc: 'Hidden from search engines. Only verified Zapmancer clients can view your profile.' },
    { id: 'private' as const, label: 'Private', desc: 'Hidden from all searches. Only clients you apply to directly can view your profile.' },
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>

      {/* Visibility */}
      <div className="settings-section">
        <div className="settings-section-header">
          <Eye size={15} style={{ color: 'var(--color-steel)' }} />
          <div>
            <p className="settings-section-title">Profile visibility</p>
            <p className="settings-section-sub">Control who can discover your freelancer profile.</p>
          </div>
        </div>
        <div className="settings-section-body">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {VISIBILITY_OPTIONS.map((o) => (
              <div
                key={o.id}
                className={`settings-radio-row${visibility === o.id ? ' active' : ''}`}
                onClick={() => setVisibility(o.id)}
              >
                <div>
                  <p className="settings-radio-title">{o.label}</p>
                  <p className="settings-radio-desc">{o.desc}</p>
                </div>
                <input type="radio" name="visibility" readOnly checked={visibility === o.id} style={{ accentColor: 'var(--color-primary)' }} />
              </div>
            ))}
          </div>
          {saved && <div className="settings-toast"><Check size={14} /> Visibility preference saved.</div>}
          <div className="settings-btn-row">
            <button className="settings-btn-primary" onClick={save}>Save visibility</button>
          </div>
        </div>
      </div>

      {/* Data export */}
      <div className="settings-section">
        <div className="settings-section-header">
          <Download size={15} style={{ color: 'var(--color-steel)' }} />
          <div>
            <p className="settings-section-title">Data export (GDPR)</p>
            <p className="settings-section-sub">Download a complete JSON archive of your account, proposals, and invoices.</p>
          </div>
        </div>
        <div className="settings-section-body">
          <div className="settings-list-row">
            <div>
              <p style={{ fontSize: 13, fontWeight: 600, color: 'var(--color-ink)' }}>Complete profile & escrow transaction record</p>
              <p style={{ fontSize: 12, color: 'var(--color-steel)', marginTop: 2 }}>Includes user metadata, proposal logs, and payout ledger receipts.</p>
            </div>
            <button className="settings-btn-secondary" onClick={handleExport} disabled={exporting}>
              {exporting ? 'Generating...' : exportDone ? <><CheckCircle2 size={14} /> Download ready</> : <><Download size={14} /> Export</>}
            </button>
          </div>
        </div>
      </div>

      {/* Danger zone */}
      <div className="settings-section-danger">
        <div className="settings-danger-header">
          <p className="settings-danger-title"><AlertTriangle size={14} /> Danger zone</p>
        </div>
        <div className="settings-danger-body">
          <div className="settings-note-warn">
            <strong>Account deletion guard:</strong> You must have $0.00 pending escrow and no active milestone contracts before requesting deletion.
          </div>
          <div>
            <button className="settings-btn-danger">Request account deletion</button>
          </div>
        </div>
      </div>

    </div>
  );
};
