import React, { useState } from 'react';
import '../settings.css';
import { CreditCard, Wallet, ShieldCheck, Check } from 'lucide-react';

export const PayoutTab: React.FC = () => {
  const [method, setMethod] = useState<'stripe' | 'usdc'>('stripe');
  const [saved, setSaved] = useState(false);
  const save = () => { setSaved(true); setTimeout(() => setSaved(false), 2000); };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>

      <div className="settings-section">
        <div className="settings-section-header">
          <CreditCard size={15} style={{ color: 'var(--color-steel)' }} />
          <div>
            <p className="settings-section-title">Payout method</p>
            <p className="settings-section-sub">Choose how you receive milestone escrow payments. 0% platform fee.</p>
          </div>
        </div>
        <div className="settings-section-body">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            <div
              className={`settings-radio-row${method === 'stripe' ? ' active' : ''}`}
              onClick={() => setMethod('stripe')}
            >
              <div>
                <p className="settings-radio-title"><CreditCard size={13} style={{ display: 'inline', marginRight: 6 }} />Stripe Direct Deposit</p>
                <p className="settings-radio-desc">Bank transfer to your linked Stripe account. 1–2 business days.</p>
              </div>
              <input type="radio" name="payout" readOnly checked={method === 'stripe'} style={{ accentColor: 'var(--color-primary)' }} />
            </div>
            <div
              className={`settings-radio-row${method === 'usdc' ? ' active' : ''}`}
              onClick={() => setMethod('usdc')}
            >
              <div>
                <p className="settings-radio-title"><Wallet size={13} style={{ display: 'inline', marginRight: 6 }} />USDC Web3 Wallet</p>
                <p className="settings-radio-desc">Instant stablecoin transfer to your EVM-compatible wallet address.</p>
              </div>
              <input type="radio" name="payout" readOnly checked={method === 'usdc'} style={{ accentColor: 'var(--color-primary)' }} />
            </div>
          </div>

          {method === 'stripe' && (
            <div className="settings-grid-2">
              <div className="settings-field">
                <label className="settings-label">Stripe account email</label>
                <input type="email" placeholder="your@stripe.com" className="settings-input" />
              </div>
              <div className="settings-field">
                <label className="settings-label">Account holder name</label>
                <input type="text" placeholder="Alex Morgan" className="settings-input" />
              </div>
            </div>
          )}

          {method === 'usdc' && (
            <div className="settings-field">
              <label className="settings-label">EVM wallet address</label>
              <input type="text" placeholder="0x..." className="settings-input" style={{ fontFamily: 'var(--font-mono)' }} />
            </div>
          )}

          <div className="settings-note-mint">
            <ShieldCheck size={14} style={{ display: 'inline', marginRight: 6 }} />
            <strong>0% commission guarantee.</strong> 100% of milestone funds transfer directly to you.
          </div>

          {saved && <div className="settings-toast"><Check size={14} /> Payout method saved.</div>}
          <div className="settings-btn-row">
            <button className="settings-btn-primary" onClick={save}>Save payout method</button>
          </div>
        </div>
      </div>

    </div>
  );
};
