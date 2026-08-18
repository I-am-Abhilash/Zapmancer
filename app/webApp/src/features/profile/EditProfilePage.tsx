import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Save, ArrowLeft, Check, Loader2 } from 'lucide-react';
import { userService } from '../../services/userService';

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

export const EditProfilePage: React.FC = () => {
  const navigate = useNavigate();

  const [name, setName] = useState('');
  const [title, setTitle] = useState('');
  const [rate, setRate] = useState('85');
  const [location, setLocation] = useState('');
  const [bio, setBio] = useState('');
  const [loading, setLoading] = useState(true);
  const [saved, setSaved] = useState(false);

  useEffect(() => {
    let isMounted = true;
    userService.getUserProfile('me').then((data) => {
      if (isMounted) {
        setName(data.name);
        setTitle(data.title);
        setRate(String(data.rateNum || 85));
        setLocation(data.location);
        setBio(data.bio);
        setLoading(false);
      }
    });

    return () => { isMounted = false; };
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await userService.updateUserProfile({
      name,
      title,
      rateNum: Number(rate) || 85,
      location,
      bio,
    });
    setSaved(true);
    setTimeout(() => {
      setSaved(false);
      navigate('/profile/me');
    }, 1000);
  };

  if (loading) {
    return (
      <div className="min-h-[50vh] flex flex-col items-center justify-center gap-3">
        <Loader2 className="w-6 h-6 text-primary animate-spin" />
        <p className="text-xs font-semibold text-mute">Loading profile settings...</p>
      </div>
    );
  }

  return (
    <div style={{ maxWidth: 720, width: '100%', margin: '0 auto', padding: '40px 24px 64px', display: 'flex', flexDirection: 'column', gap: 24 }}>
      {/* Back */}
      <button onClick={() => navigate(-1)} style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontSize: 13, color: 'var(--color-steel)', background: 'none', border: 'none', cursor: 'pointer', fontFamily: 'var(--font-sans)', padding: 0 }}>
        <ArrowLeft size={15} /> Cancel & return
      </button>

      {/* Heading */}
      <div style={{ paddingBottom: 24, borderBottom: '1px solid var(--color-hairline)' }}>
        <h1 style={{ fontSize: 26, fontWeight: 700, letterSpacing: '-0.4px', color: 'var(--color-ink)', lineHeight: 1.2 }}>Edit profile</h1>
        <p style={{ fontSize: 14, color: 'var(--color-steel)', marginTop: 4 }}>Keep your public engineering resume and billing rates updated.</p>
      </div>

      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>

        {/* Identity */}
        <div style={{ border: '1px solid var(--color-hairline)', borderRadius: 12, overflow: 'hidden' }}>
          <div style={{ padding: '14px 20px', borderBottom: '1px solid var(--color-hairline)', fontWeight: 600, fontSize: 14, color: 'var(--color-ink)', backgroundColor: 'var(--color-canvas)' }}>
            Identity
          </div>
          <div style={{ padding: 20, display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
            <div>
              <label style={labelStyle}>Full name</label>
              <input type="text" value={name} onChange={(e) => setName(e.target.value)} style={inputStyle} required />
            </div>
            <div>
              <label style={labelStyle}>Professional title</label>
              <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} style={inputStyle} required />
            </div>
            <div>
              <label style={labelStyle}>Hourly rate ($ USD / hr)</label>
              <input type="number" value={rate} onChange={(e) => setRate(e.target.value)} style={inputStyle} required />
            </div>
            <div>
              <label style={labelStyle}>Location / timezone</label>
              <input type="text" value={location} onChange={(e) => setLocation(e.target.value)} style={inputStyle} required />
            </div>
          </div>
        </div>

        {/* Bio */}
        <div style={{ border: '1px solid var(--color-hairline)', borderRadius: 12, overflow: 'hidden' }}>
          <div style={{ padding: '14px 20px', borderBottom: '1px solid var(--color-hairline)', fontWeight: 600, fontSize: 14, color: 'var(--color-ink)', backgroundColor: 'var(--color-canvas)' }}>
            Bio & engineering summary
          </div>
          <div style={{ padding: 20 }}>
            <textarea
              rows={5}
              value={bio}
              onChange={(e) => setBio(e.target.value)}
              style={{ width: '100%', padding: '10px 12px', border: '1px solid var(--color-hairline-strong)', borderRadius: 8, backgroundColor: 'var(--color-canvas)', color: 'var(--color-ink)', fontSize: 14, fontFamily: 'var(--font-sans)', lineHeight: 1.65, outline: 'none', resize: 'vertical', boxSizing: 'border-box' }}
              required
            />
          </div>
        </div>

        {/* Save row */}
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end', gap: 12, paddingTop: 4 }}>
          {saved && (
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontSize: 13, fontWeight: 600, color: '#1aae39' }}>
              <Check size={14} /> Saved!
            </span>
          )}
          <button
            type="submit"
            style={{ display: 'inline-flex', alignItems: 'center', gap: 8, backgroundColor: 'var(--color-primary)', color: '#ffffff', fontSize: 14, fontWeight: 500, padding: '10px 20px', borderRadius: 8, border: 'none', cursor: 'pointer', fontFamily: 'var(--font-sans)' }}
          >
            <Save size={14} /> Save changes
          </button>
        </div>

      </form>
    </div>
  );
};
