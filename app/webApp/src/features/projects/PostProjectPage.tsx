import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, ArrowRight, Sparkles, Plus, X } from 'lucide-react';

export const PostProjectPage: React.FC = () => {
  const navigate = useNavigate();

  const [title, setTitle] = useState('');
  const [category, setCategory] = useState('Kotlin Multiplatform');
  const [description, setDescription] = useState('');
  const [budgetType, setBudgetType] = useState<'Fixed' | 'Hourly'>('Fixed');
  const [budgetAmount, setBudgetAmount] = useState<string>('3500');
  const [skillInput, setSkillInput] = useState('');
  const [skills, setSkills] = useState<string[]>(['Kotlin', 'Compose Multiplatform', 'Ktor']);

  const addSkill = () => {
    if (skillInput.trim() && !skills.includes(skillInput.trim())) {
      setSkills([...skills, skillInput.trim()]);
      setSkillInput('');
    }
  };

  const removeSkill = (s: string) => {
    setSkills(skills.filter((item) => item !== s));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    navigate('/projects');
  };

  return (
    <div className="min-h-screen flex flex-col bg-canvas text-ink transition-colors duration-200 antialiased font-sans">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-4xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        <div className="space-y-1 border-b border-hairline pb-6">
          <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
            <Sparkles className="w-3.5 h-3.5" /> Client Portal
          </div>
          <h1 className="text-3xl font-extrabold tracking-tight text-ink">Post a New Engineering Bounty</h1>
          <p className="text-sm text-mute">Publish project requirements to receive proposals from top KMP & mobile developers.</p>
        </div>

        <form onSubmit={handleSubmit} className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
          
          {/* Title */}
          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
              Project Bounty Title
            </label>
            <input
              type="text"
              required
              placeholder="e.g. Build a Compose Multiplatform Desktop App for Ktor Analytics"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full px-4 py-3 rounded-full border border-hairline bg-surface-elevated text-sm font-semibold text-ink focus:outline-none focus:border-brand-green transition-colors placeholder:text-mute"
            />
          </div>

          {/* Category */}
          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
              Primary Stack Category
            </label>
            <select
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              className="w-full px-4 py-3 rounded-full border border-hairline bg-surface-elevated text-sm font-semibold text-ink focus:outline-none focus:border-brand-green transition-colors"
            >
              <option value="Kotlin Multiplatform">Kotlin Multiplatform</option>
              <option value="Mobile (Android/iOS)">Mobile (Android/iOS)</option>
              <option value="Full-Stack & Ktor">Full-Stack & Ktor</option>
              <option value="AI Systems & Gorse">AI Systems & Gorse</option>
            </select>
          </div>

          {/* Description */}
          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
              Scope & Specifications
            </label>
            <textarea
              rows={6}
              required
              placeholder="Describe the deliverables, timeline, technical expectations, and API integrations needed..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="w-full px-4 py-3 rounded-xl border border-hairline bg-surface-elevated text-sm text-ink leading-relaxed focus:outline-none focus:border-brand-green transition-colors placeholder:text-mute"
            />
          </div>

          {/* Budget Type & Amount */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
                Budget Model
              </label>
              <div className="flex gap-3">
                <button
                  type="button"
                  onClick={() => setBudgetType('Fixed')}
                  className={`flex-1 py-3 rounded-full border text-xs font-bold uppercase tracking-[0.05em] transition-all ${
                    budgetType === 'Fixed'
                      ? 'bg-brand-green text-white border-brand-green shadow-md shadow-brand-green/20'
                      : 'bg-surface-elevated border-hairline text-mute hover:text-ink'
                  }`}
                >
                  Fixed Milestone
                </button>
                <button
                  type="button"
                  onClick={() => setBudgetType('Hourly')}
                  className={`flex-1 py-3 rounded-full border text-xs font-bold uppercase tracking-[0.05em] transition-all ${
                    budgetType === 'Hourly'
                      ? 'bg-brand-green text-white border-brand-green shadow-md shadow-brand-green/20'
                      : 'bg-surface-elevated border-hairline text-mute hover:text-ink'
                  }`}
                >
                  Hourly Rate
                </button>
              </div>
            </div>

            <div>
              <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
                Budget Amount ($ USD)
              </label>
              <div className="relative">
                <span className="absolute left-4 top-3 text-mute font-bold text-sm">$</span>
                <input
                  type="number"
                  required
                  value={budgetAmount}
                  onChange={(e) => setBudgetAmount(e.target.value)}
                  className="w-full pl-8 pr-4 py-3 rounded-full border border-hairline bg-surface-elevated text-sm font-bold text-ink focus:outline-none focus:border-brand-green transition-colors"
                />
              </div>
            </div>
          </div>

          {/* Required Skills */}
          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
              Required Technical Skills
            </label>
            <div className="flex gap-2 mb-3 max-w-md">
              <input
                type="text"
                placeholder="Add skill tag (e.g. Compose, PostgreSQL)..."
                value={skillInput}
                onChange={(e) => setSkillInput(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && (e.preventDefault(), addSkill())}
                className="flex-1 px-4 py-2 rounded-full border border-hairline bg-surface-elevated text-xs text-ink focus:outline-none focus:border-brand-green"
              />
              <button
                type="button"
                onClick={addSkill}
                className="px-5 py-2 bg-surface-elevated hover:bg-surface-modal border border-hairline text-ink rounded-full text-xs font-bold uppercase tracking-[0.05em] transition-all"
              >
                <Plus className="w-3.5 h-3.5 text-brand-green inline mr-1" /> Add
              </button>
            </div>

            <div className="flex flex-wrap gap-2">
              {skills.map((s) => (
                <span
                  key={s}
                  onClick={() => removeSkill(s)}
                  className="px-3.5 py-1.5 rounded-full bg-surface-elevated border border-hairline text-brand-green text-xs font-bold cursor-pointer hover:border-red-500 hover:text-red-500 transition-all"
                >
                  {s} <X className="w-3 h-3 inline ml-1" />
                </span>
              ))}
            </div>
          </div>

          {/* Submit Action */}
          <div className="pt-4 border-t border-hairline flex flex-col sm:flex-row sm:items-center justify-between gap-4">
            <div className="flex items-center gap-2 text-xs text-brand-green font-bold">
              <ShieldCheck className="w-4 h-4" /> Escrow milestone deposit optional until hire
            </div>

            <button
              type="submit"
              className="flex items-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white font-bold text-xs uppercase tracking-[0.05em] px-8 py-3.5 rounded-full hover:scale-[1.04] transition-all shadow-md shadow-brand-green/20"
            >
              Publish Project Bounty <ArrowRight className="w-4 h-4" />
            </button>
          </div>

        </form>

      </main>

      <Footer />
    </div>
  );
};
