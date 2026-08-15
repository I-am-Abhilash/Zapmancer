import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, ArrowRight, Plus, X, CheckCircle2 } from 'lucide-react';

export const PostProjectPage: React.FC = () => {
  const navigate = useNavigate();

  const [title, setTitle] = useState('');
  const [category, setCategory] = useState('Software & Web Dev');
  const [description, setDescription] = useState('');
  const [budgetType, setBudgetType] = useState<'Fixed' | 'Hourly'>('Fixed');
  const [budgetAmount, setBudgetAmount] = useState('3500');
  const [skillInput, setSkillInput] = useState('');
  const [skills, setSkills] = useState(['TypeScript', 'React', 'Node.js']);

  const addSkill = () => {
    const s = skillInput.trim();
    if (s && !skills.includes(s)) { setSkills([...skills, s]); setSkillInput(''); }
  };

  const removeSkill = (s: string) => setSkills(skills.filter((x) => x !== s));

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    navigate('/projects');
  };

  const field: React.CSSProperties = {
    width: '100%',
    padding: '9px 12px',
    border: '1px solid var(--color-hairline-strong)',
    borderRadius: 8,
    backgroundColor: 'var(--color-canvas)',
    color: 'var(--color-ink)',
    fontSize: 14,
    fontFamily: 'var(--font-sans)',
    outline: 'none',
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column', backgroundColor: 'var(--color-canvas)', color: 'var(--color-ink)', fontFamily: 'var(--font-sans)', WebkitFontSmoothing: 'antialiased' }}>
      <Header isLoggedIn={true} />

      <main style={{ flex: 1, maxWidth: 720, width: '100%', margin: '0 auto', padding: '40px 24px 64px' }}>

        {/* Page header */}
        <div style={{ paddingBottom: 24, borderBottom: '1px solid var(--color-hairline)', marginBottom: 32 }}>
          <h1 style={{ fontSize: 26, fontWeight: 700, letterSpacing: '-0.4px', color: 'var(--color-ink)', lineHeight: 1.2 }}>
            Post a contract
          </h1>
          <p style={{ fontSize: 14, color: 'var(--color-steel)', marginTop: 4, lineHeight: 1.5 }}>
            Publish your project scope to receive proposals from vetted developers, designers, and AI builders.
          </p>
        </div>

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>

          {/* Title */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            <label style={{ fontSize: 13, fontWeight: 600, color: 'var(--color-ink)' }}>Contract title *</label>
            <input
              type="text"
              required
              placeholder="e.g. Build a RAG AI Agent and Next.js dashboard"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              style={field}
              onFocus={(e) => { e.target.style.borderColor = 'var(--color-primary)'; e.target.style.borderWidth = '2px'; }}
              onBlur={(e) => { e.target.style.borderColor = 'var(--color-hairline-strong)'; e.target.style.borderWidth = '1px'; }}
            />
          </div>

          {/* Category */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            <label style={{ fontSize: 13, fontWeight: 600, color: 'var(--color-ink)' }}>Discipline *</label>
            <select
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              style={{ ...field, cursor: 'pointer' }}
            >
              <option value="Software & Web Dev">Software & Full-Stack Web Dev</option>
              <option value="AI Builders & Agents">AI Builders, RAG & Machine Learning</option>
              <option value="UI/UX & Product Design">UI/UX & Product Design Systems</option>
              <option value="Mobile (Android/iOS)">Mobile (Android & iOS)</option>
              <option value="DevOps & Cloud Systems">DevOps, Kubernetes & Cloud</option>
              <option value="Growth & Marketing">Growth & Performance Marketing</option>
            </select>
          </div>

          {/* Description */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            <label style={{ fontSize: 13, fontWeight: 600, color: 'var(--color-ink)' }}>Project scope & deliverables *</label>
            <textarea
              rows={6}
              required
              placeholder="Detail the milestone scope, tech stack expectations, API requirements, and expected timeline..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              style={{ ...field, resize: 'vertical', lineHeight: 1.6 }}
              onFocus={(e) => { e.target.style.borderColor = 'var(--color-primary)'; e.target.style.borderWidth = '2px'; }}
              onBlur={(e) => { e.target.style.borderColor = 'var(--color-hairline-strong)'; e.target.style.borderWidth = '1px'; }}
            />
          </div>

          {/* Budget */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
              <label style={{ fontSize: 13, fontWeight: 600, color: 'var(--color-ink)' }}>Budget type</label>
              <div style={{ display: 'flex', gap: 8 }}>
                {(['Fixed', 'Hourly'] as const).map((t) => (
                  <button
                    key={t}
                    type="button"
                    onClick={() => setBudgetType(t)}
                    style={{
                      flex: 1,
                      padding: '8px 0',
                      borderRadius: 8,
                      border: `1px solid ${budgetType === t ? 'var(--color-primary)' : 'var(--color-hairline-strong)'}`,
                      backgroundColor: budgetType === t ? 'var(--color-primary)' : 'var(--color-canvas)',
                      color: budgetType === t ? '#ffffff' : 'var(--color-steel)',
                      fontSize: 13,
                      fontWeight: 500,
                      cursor: 'pointer',
                      fontFamily: 'var(--font-sans)',
                    }}
                  >
                    {t}
                  </button>
                ))}
              </div>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
              <label style={{ fontSize: 13, fontWeight: 600, color: 'var(--color-ink)' }}>
                Budget (USD) *
              </label>
              <div style={{ position: 'relative' }}>
                <span style={{ position: 'absolute', left: 12, top: '50%', transform: 'translateY(-50%)', color: 'var(--color-steel)', fontSize: 14, fontWeight: 500 }}>$</span>
                <input
                  type="number"
                  required
                  value={budgetAmount}
                  onChange={(e) => setBudgetAmount(e.target.value)}
                  style={{ ...field, paddingLeft: 26 }}
                  onFocus={(e) => { e.target.style.borderColor = 'var(--color-primary)'; e.target.style.borderWidth = '2px'; }}
                  onBlur={(e) => { e.target.style.borderColor = 'var(--color-hairline-strong)'; e.target.style.borderWidth = '1px'; }}
                />
              </div>
            </div>
          </div>

          {/* Skills */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            <label style={{ fontSize: 13, fontWeight: 600, color: 'var(--color-ink)' }}>Required skills</label>
            <div style={{ display: 'flex', gap: 8, maxWidth: 400 }}>
              <input
                type="text"
                placeholder="Add a skill (e.g. React, Python, Figma)"
                value={skillInput}
                onChange={(e) => setSkillInput(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && (e.preventDefault(), addSkill())}
                style={{ ...field, flex: 1 }}
              />
              <button
                type="button"
                onClick={addSkill}
                style={{
                  display: 'inline-flex', alignItems: 'center', gap: 4,
                  padding: '8px 14px', borderRadius: 8,
                  border: '1px solid var(--color-hairline-strong)',
                  backgroundColor: 'var(--color-canvas)',
                  color: 'var(--color-ink)', fontSize: 13, fontWeight: 500,
                  cursor: 'pointer', fontFamily: 'var(--font-sans)', whiteSpace: 'nowrap',
                }}
              >
                <Plus size={14} /> Add
              </button>
            </div>
            {skills.length > 0 && (
              <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6, marginTop: 4 }}>
                {skills.map((s) => (
                  <span
                    key={s}
                    style={{
                      display: 'inline-flex', alignItems: 'center', gap: 5,
                      fontSize: 12, fontWeight: 600, padding: '3px 10px',
                      borderRadius: 6, cursor: 'pointer',
                      backgroundColor: 'var(--color-card-tint-sky)',
                      color: 'var(--color-link-blue)',
                      border: '1px solid rgba(0,117,222,0.15)',
                    }}
                    onClick={() => removeSkill(s)}
                    title="Click to remove"
                  >
                    {s} <X size={11} />
                  </span>
                ))}
              </div>
            )}
          </div>

          {/* Escrow note */}
          <div style={{
            padding: '14px 16px',
            borderRadius: 8,
            backgroundColor: 'var(--color-card-tint-mint)',
            border: '1px solid rgba(26,174,57,0.2)',
            display: 'flex', gap: 10, alignItems: 'flex-start',
          }}>
            <ShieldCheck size={16} style={{ color: '#1aae39', flexShrink: 0, marginTop: 1 }} />
            <div>
              <p style={{ fontSize: 13, fontWeight: 600, color: '#1a5c2a', margin: 0 }}>Milestone escrow & 0% worker fee</p>
              <p style={{ fontSize: 12, color: '#256334', margin: '3px 0 0', lineHeight: 1.5 }}>
                Escrow funding is required only after hiring. All deliverables include automated Work-for-Hire IP transfer upon milestone release.
              </p>
            </div>
          </div>

          {/* Footer */}
          <div style={{ paddingTop: 16, borderTop: '1px solid var(--color-hairline)', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 12 }}>
            <span style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 13, color: 'var(--color-steel)' }}>
              <CheckCircle2 size={14} style={{ color: '#1aae39' }} /> Free to post · No subscription required
            </span>
            <button
              type="submit"
              style={{
                display: 'inline-flex', alignItems: 'center', gap: 8,
                backgroundColor: 'var(--color-primary)', color: '#ffffff',
                fontSize: 14, fontWeight: 500, padding: '10px 20px',
                borderRadius: 8, border: 'none', cursor: 'pointer',
                fontFamily: 'var(--font-sans)',
              }}
            >
              Publish contract <ArrowRight size={15} />
            </button>
          </div>

        </form>
      </main>

      <Footer />
    </div>
  );
};
