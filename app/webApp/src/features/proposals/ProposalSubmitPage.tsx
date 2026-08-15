import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Send, ShieldCheck, Plus, Trash2, ArrowLeft } from 'lucide-react';

const inputStyle: React.CSSProperties = {
  width: '100%', height: 40, padding: '0 12px',
  border: '1px solid var(--color-hairline-strong)', borderRadius: 8,
  backgroundColor: 'var(--color-canvas)', color: 'var(--color-ink)',
  fontSize: 14, fontFamily: 'var(--font-sans)', outline: 'none',
  boxSizing: 'border-box',
};

const labelStyle: React.CSSProperties = {
  fontSize: 13, fontWeight: 600, color: 'var(--color-ink)', display: 'block', marginBottom: 6,
};

export const ProposalSubmitPage: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const [bidAmount, setBidAmount] = useState('3200');
  const [deliveryDays, setDeliveryDays] = useState('21');
  const [coverLetter, setCoverLetter] = useState(
    'Hi, I have extensive experience delivering Compose Multiplatform desktop and mobile clients connected to Ktor backends. I can implement clean MVI architecture, adaptive layout widgets, and WebSockets sync.'
  );
  const [milestones, setMilestones] = useState([
    { title: 'KMP Architecture & Ktor Integration', amount: '1200' },
    { title: 'Compose UI Desktop Views & Analytics Charts', amount: '1200' },
    { title: 'Testing, Polishing & Final Delivery', amount: '800' },
  ]);

  const addMilestone = () => setMilestones([...milestones, { title: 'New milestone', amount: '500' }]);
  const removeMilestone = (i: number) => setMilestones(milestones.filter((_, idx) => idx !== i));

  const handleSubmit = (e: React.FormEvent) => { e.preventDefault(); navigate('/home'); };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column', backgroundColor: 'var(--color-canvas)', color: 'var(--color-ink)', fontFamily: 'var(--font-sans)', WebkitFontSmoothing: 'antialiased' }}>
      <Header isLoggedIn={true} />

      <main style={{ flex: 1, maxWidth: 720, width: '100%', margin: '0 auto', padding: '40px 24px 64px', display: 'flex', flexDirection: 'column', gap: 24 }}>

        {/* Back */}
        <button onClick={() => navigate(-1)} style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontSize: 13, color: 'var(--color-steel)', background: 'none', border: 'none', cursor: 'pointer', fontFamily: 'var(--font-sans)', padding: 0 }}>
          <ArrowLeft size={15} /> Back
        </button>

        {/* Page heading */}
        <div style={{ paddingBottom: 24, borderBottom: '1px solid var(--color-hairline)' }}>
          <h1 style={{ fontSize: 26, fontWeight: 700, letterSpacing: '-0.4px', color: 'var(--color-ink)', lineHeight: 1.2 }}>Submit proposal</h1>
          <p style={{ fontSize: 14, color: 'var(--color-steel)', marginTop: 4 }}>
            Applying for: <strong style={{ color: 'var(--color-ink)' }}>Compose Multiplatform Desktop App for Ktor Analytics</strong>
          </p>
        </div>

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>

          {/* Bid terms */}
          <div style={{ border: '1px solid var(--color-hairline)', borderRadius: 12, overflow: 'hidden' }}>
            <div style={{ padding: '14px 20px', borderBottom: '1px solid var(--color-hairline)', fontWeight: 600, fontSize: 14, color: 'var(--color-ink)', backgroundColor: 'var(--color-canvas)' }}>
              Bid terms
            </div>
            <div style={{ padding: '20px', display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
              <div>
                <label style={labelStyle}>Total bid ($ USD)</label>
                <div style={{ position: 'relative' }}>
                  <span style={{ position: 'absolute', left: 12, top: 11, fontSize: 13, color: 'var(--color-steel)', fontWeight: 600 }}>$</span>
                  <input type="number" required value={bidAmount} onChange={(e) => setBidAmount(e.target.value)} style={{ ...inputStyle, paddingLeft: 26 }} />
                </div>
                <p style={{ fontSize: 12, color: 'var(--color-steel)', marginTop: 5 }}>0% platform fee — you receive the full amount.</p>
              </div>
              <div>
                <label style={labelStyle}>Delivery estimate (days)</label>
                <input type="number" required value={deliveryDays} onChange={(e) => setDeliveryDays(e.target.value)} style={inputStyle} />
              </div>
            </div>
          </div>

          {/* Milestone breakdown */}
          <div style={{ border: '1px solid var(--color-hairline)', borderRadius: 12, overflow: 'hidden' }}>
            <div style={{ padding: '14px 20px', borderBottom: '1px solid var(--color-hairline)', display: 'flex', alignItems: 'center', justifyContent: 'space-between', backgroundColor: 'var(--color-canvas)' }}>
              <p style={{ fontWeight: 600, fontSize: 14, color: 'var(--color-ink)' }}>Escrow milestone breakdown</p>
              <button type="button" onClick={addMilestone} style={{ display: 'flex', alignItems: 'center', gap: 5, fontSize: 13, fontWeight: 500, color: 'var(--color-primary)', background: 'none', border: 'none', cursor: 'pointer', fontFamily: 'var(--font-sans)' }}>
                <Plus size={14} /> Add milestone
              </button>
            </div>
            <div style={{ padding: 20, display: 'flex', flexDirection: 'column', gap: 10 }}>
              {milestones.map((m, idx) => (
                <div key={idx} style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                  <span style={{ fontSize: 12, fontWeight: 700, color: 'var(--color-steel)', minWidth: 24, textAlign: 'center' }}>#{idx + 1}</span>
                  <input
                    type="text"
                    value={m.title}
                    onChange={(e) => { const u = [...milestones]; u[idx].title = e.target.value; setMilestones(u); }}
                    style={{ ...inputStyle, flex: 1 }}
                    placeholder="Milestone title"
                  />
                  <div style={{ position: 'relative', width: 110, flexShrink: 0 }}>
                    <span style={{ position: 'absolute', left: 10, top: 11, fontSize: 13, color: 'var(--color-steel)', fontWeight: 600 }}>$</span>
                    <input
                      type="number"
                      value={m.amount}
                      onChange={(e) => { const u = [...milestones]; u[idx].amount = e.target.value; setMilestones(u); }}
                      style={{ ...inputStyle, paddingLeft: 22, width: '100%' }}
                    />
                  </div>
                  {milestones.length > 1 && (
                    <button type="button" onClick={() => removeMilestone(idx)} style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--color-steel)', padding: 4 }}>
                      <Trash2 size={15} />
                    </button>
                  )}
                </div>
              ))}
              <div style={{ paddingTop: 10, borderTop: '1px solid var(--color-hairline)', fontSize: 12, color: 'var(--color-steel)' }}>
                Total: <strong style={{ color: 'var(--color-ink)', fontFamily: 'var(--font-mono)' }}>${milestones.reduce((s, m) => s + (Number(m.amount) || 0), 0).toLocaleString()}</strong>
              </div>
            </div>
          </div>

          {/* Cover letter */}
          <div style={{ border: '1px solid var(--color-hairline)', borderRadius: 12, overflow: 'hidden' }}>
            <div style={{ padding: '14px 20px', borderBottom: '1px solid var(--color-hairline)', fontWeight: 600, fontSize: 14, color: 'var(--color-ink)', backgroundColor: 'var(--color-canvas)' }}>
              Cover letter & pitch
            </div>
            <div style={{ padding: 20 }}>
              <textarea
                rows={6}
                required
                value={coverLetter}
                onChange={(e) => setCoverLetter(e.target.value)}
                style={{ width: '100%', padding: '10px 12px', border: '1px solid var(--color-hairline-strong)', borderRadius: 8, backgroundColor: 'var(--color-canvas)', color: 'var(--color-ink)', fontSize: 14, fontFamily: 'var(--font-sans)', lineHeight: 1.65, outline: 'none', resize: 'vertical', boxSizing: 'border-box' }}
              />
            </div>
          </div>

          {/* Submit row */}
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 12, paddingTop: 4 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 13, color: '#1aae39', fontWeight: 600 }}>
              <ShieldCheck size={14} /> Milestone escrow protection active upon hire
            </div>
            <button
              type="submit"
              style={{ display: 'inline-flex', alignItems: 'center', gap: 8, backgroundColor: 'var(--color-primary)', color: '#ffffff', fontSize: 14, fontWeight: 500, padding: '10px 20px', borderRadius: 8, border: 'none', cursor: 'pointer', fontFamily: 'var(--font-sans)' }}
            >
              Submit proposal <Send size={14} />
            </button>
          </div>

        </form>
      </main>

      <Footer />
    </div>
  );
};
