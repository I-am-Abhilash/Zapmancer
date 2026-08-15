import React, { useState } from 'react';
import '../settings.css';
import { User, Plus, X, Check } from 'lucide-react';

const INIT_SKILLS = ['Kotlin Multiplatform', 'Ktor Server', 'Compose UI', 'PostgreSQL'];

export const ProfileTab: React.FC = () => {
  const [skills, setSkills] = useState(INIT_SKILLS);
  const [skillInput, setSkillInput] = useState('');
  const [saved, setSaved] = useState(false);

  const addSkill = () => {
    const s = skillInput.trim();
    if (s && !skills.includes(s)) { setSkills([...skills, s]); setSkillInput(''); }
  };

  const save = () => { setSaved(true); setTimeout(() => setSaved(false), 2000); };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>

      <div className="settings-section">
        <div className="settings-section-header">
          <User size={15} style={{ color: 'var(--color-steel)' }} />
          <div>
            <p className="settings-section-title">Public profile</p>
            <p className="settings-section-sub">This information is visible to clients browsing the marketplace.</p>
          </div>
        </div>
        <div className="settings-section-body">
          <div className="settings-grid-2">
            <div className="settings-field">
              <label className="settings-label">Full name</label>
              <input type="text" defaultValue="Alex Morgan" className="settings-input" />
            </div>
            <div className="settings-field">
              <label className="settings-label">Professional title</label>
              <input type="text" defaultValue="Senior KMP & Full-Stack Architect" className="settings-input" />
            </div>
          </div>

          <div className="settings-grid-2">
            <div className="settings-field">
              <label className="settings-label">Location</label>
              <input type="text" defaultValue="San Francisco, USA" className="settings-input" />
            </div>
            <div className="settings-field">
              <label className="settings-label">Hourly rate (USD)</label>
              <input type="number" defaultValue="85" className="settings-input" />
            </div>
          </div>

          <div className="settings-field">
            <label className="settings-label">Bio</label>
            <textarea
              rows={4}
              defaultValue="Kotlin Multiplatform engineer with 6+ years architecting cross-platform apps connected to high-throughput Ktor backend microservices."
              className="settings-textarea"
            />
          </div>

          <div className="settings-field">
            <label className="settings-label">Skills</label>
            <div style={{ display: 'flex', gap: 8, marginBottom: 8 }}>
              <input
                type="text"
                placeholder="Add a skill..."
                value={skillInput}
                onChange={(e) => setSkillInput(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && (e.preventDefault(), addSkill())}
                className="settings-input"
                style={{ flex: 1 }}
              />
              <button className="settings-btn-secondary" onClick={addSkill}><Plus size={14} /> Add</button>
            </div>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
              {skills.map((s) => (
                <span
                  key={s}
                  onClick={() => setSkills(skills.filter((x) => x !== s))}
                  style={{
                    display: 'inline-flex', alignItems: 'center', gap: 5,
                    fontSize: 12, fontWeight: 600, padding: '3px 10px',
                    borderRadius: 6, cursor: 'pointer',
                    backgroundColor: 'var(--color-card-tint-sky)',
                    color: 'var(--color-link-blue)',
                  }}
                  title="Click to remove"
                >
                  {s} <X size={11} />
                </span>
              ))}
            </div>
          </div>

          {saved && <div className="settings-toast"><Check size={14} /> Profile updated.</div>}
          <div className="settings-btn-row">
            <button className="settings-btn-primary" onClick={save}>Save profile</button>
          </div>
        </div>
      </div>

    </div>
  );
};
