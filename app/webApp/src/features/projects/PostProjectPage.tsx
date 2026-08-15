import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, ArrowRight, Sparkles, Plus, X, Lock, CheckCircle2 } from 'lucide-react';

export const PostProjectPage: React.FC = () => {
  const navigate = useNavigate();

  const [title, setTitle] = useState('');
  const [category, setCategory] = useState('Software & Web Dev');
  const [description, setDescription] = useState('');
  const [budgetType, setBudgetType] = useState<'Fixed' | 'Hourly'>('Fixed');
  const [budgetAmount, setBudgetAmount] = useState<string>('3500');
  const [skillInput, setSkillInput] = useState('');
  const [skills, setSkills] = useState<string[]>(['TypeScript', 'React', 'Node.js']);

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
        
        {/* Title Header */}
        <div className="space-y-1 border-b border-hairline pb-6">
          <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
            <Lock className="w-3.5 h-3.5" /> Enterprise Client Portal
          </div>
          <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight text-ink">Post a Project & Contract Milestone</h1>
          <p className="text-sm text-mute">Publish project scope and requirements to receive proposals from vetted global developers, designers, and AI builders.</p>
        </div>

        {/* High-Trust Enterprise Form */}
        <form onSubmit={handleSubmit} className="bg-surface p-8 sm:p-10 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
          
          {/* Title Input */}
          <div className="space-y-1.5">
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute">
              Project / Contract Title *
            </label>
            <input
              type="text"
              required
              placeholder="e.g. Build Custom RAG AI Agent Workflow & Next.js Dashboard"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full px-4 py-3 rounded-xl border border-hairline bg-surface-elevated text-sm font-semibold text-ink focus:outline-none focus:border-brand-green focus:ring-1 focus:ring-brand-green/30 transition-all placeholder:text-mute"
            />
          </div>

          {/* Primary Category Select */}
          <div className="space-y-1.5">
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute">
              Primary Discipline / Category *
            </label>
            <select
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              className="w-full px-4 py-3 rounded-xl border border-hairline bg-surface-elevated text-sm font-semibold text-ink focus:outline-none focus:border-brand-green focus:ring-1 focus:ring-brand-green/30 transition-all cursor-pointer"
            >
              <option value="Software & Web Dev">Software & Full-Stack Web Development</option>
              <option value="AI Builders & Agents">AI Builders, RAG & Machine Learning</option>
              <option value="UI/UX & Product Design">UI/UX & Product Design Systems</option>
              <option value="Mobile (Android/iOS)">Mobile (Android & iOS)</option>
              <option value="DevOps & Cloud Systems">DevOps, Kubernetes & Cloud Systems</option>
              <option value="Growth & Marketing">Growth & Performance Marketing</option>
            </select>
          </div>

          {/* Description Scope */}
          <div className="space-y-1.5">
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute">
              Project Specifications & Deliverables *
            </label>
            <textarea
              rows={6}
              required
              placeholder="Detail the milestone scope, tech stack expectations, API requirements, and expected timeline..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="w-full px-4 py-3 rounded-xl border border-hairline bg-surface-elevated text-sm text-ink leading-relaxed focus:outline-none focus:border-brand-green focus:ring-1 focus:ring-brand-green/30 transition-all placeholder:text-mute"
            />
          </div>

          {/* Budget Model & Amount */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="space-y-1.5">
              <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute">
                Budget Structure
              </label>
              <div className="flex gap-3">
                <button
                  type="button"
                  onClick={() => setBudgetType('Fixed')}
                  className={`flex-1 py-3 rounded-xl border text-xs font-bold uppercase tracking-[0.05em] transition-all cursor-pointer ${
                    budgetType === 'Fixed'
                      ? 'bg-brand-green text-white border-brand-green shadow-sm'
                      : 'bg-surface-elevated border-hairline text-mute hover:text-ink hover:bg-surface-modal'
                  }`}
                >
                  Fixed Milestone
                </button>
                <button
                  type="button"
                  onClick={() => setBudgetType('Hourly')}
                  className={`flex-1 py-3 rounded-xl border text-xs font-bold uppercase tracking-[0.05em] transition-all cursor-pointer ${
                    budgetType === 'Hourly'
                      ? 'bg-brand-green text-white border-brand-green shadow-sm'
                      : 'bg-surface-elevated border-hairline text-mute hover:text-ink hover:bg-surface-modal'
                  }`}
                >
                  Hourly Rate
                </button>
              </div>
            </div>

            <div className="space-y-1.5">
              <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute">
                Estimated Budget ($ USD) *
              </label>
              <div className="relative">
                <span className="absolute left-4 top-3 text-mute font-bold text-sm">$</span>
                <input
                  type="number"
                  required
                  value={budgetAmount}
                  onChange={(e) => setBudgetAmount(e.target.value)}
                  className="w-full pl-8 pr-4 py-3 rounded-xl border border-hairline bg-surface-elevated text-sm font-bold text-ink focus:outline-none focus:border-brand-green focus:ring-1 focus:ring-brand-green/30 transition-all font-mono"
                />
              </div>
            </div>
          </div>

          {/* Required Skills */}
          <div className="space-y-1.5">
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute">
              Required Skills & Technologies
            </label>
            
            <div className="flex gap-2 mb-3 max-w-md">
              <input
                type="text"
                placeholder="Add skill tag (e.g. React, OpenAI, Figma)..."
                value={skillInput}
                onChange={(e) => setSkillInput(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && (e.preventDefault(), addSkill())}
                className="flex-1 px-4 py-2.5 rounded-xl border border-hairline bg-surface-elevated text-xs text-ink focus:outline-none focus:border-brand-green"
              />
              <button
                type="button"
                onClick={addSkill}
                className="px-5 py-2.5 bg-surface-elevated hover:bg-surface-modal border border-hairline text-ink rounded-xl text-xs font-bold uppercase tracking-[0.05em] transition-all cursor-pointer"
              >
                <Plus className="w-3.5 h-3.5 text-brand-green inline mr-1" /> Add Tag
              </button>
            </div>

            <div className="flex flex-wrap gap-2">
              {skills.map((s) => (
                <span
                  key={s}
                  onClick={() => removeSkill(s)}
                  className="px-3 py-1 rounded-full bg-surface-elevated border border-hairline text-brand-green text-xs font-bold cursor-pointer hover:border-red-500 hover:text-red-500 transition-all inline-flex items-center gap-1"
                >
                  {s} <X className="w-3 h-3" />
                </span>
              ))}
            </div>
          </div>

          {/* Escrow Guarantee Box */}
          <div className="p-4 rounded-xl bg-brand-green/10 border border-brand-green/30 text-xs text-brand-green space-y-1">
            <p className="font-bold flex items-center gap-1.5">
              <ShieldCheck className="w-4 h-4" /> 100% Milestone Escrow Protection & 0% Worker Cut
            </p>
            <p className="text-[11px] leading-relaxed text-brand-green/80">
              Escrow funding is required only after you select and hire your preferred candidate. All deliverables include automated Work-for-Hire legal IP transfers upon milestone release.
            </p>
          </div>

          {/* Form Footer Actions */}
          <div className="pt-4 border-t border-hairline flex flex-col sm:flex-row sm:items-center justify-between gap-4">
            <div className="flex items-center gap-2 text-xs text-mute font-medium">
              <CheckCircle2 className="w-4 h-4 text-brand-green" /> Instant publishing & no posting fee
            </div>

            <button
              type="submit"
              className="inline-flex items-center justify-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white font-bold text-xs uppercase tracking-[0.05em] px-8 py-3.5 rounded-full hover:scale-[1.03] transition-all shadow-md shadow-brand-green/20 cursor-pointer"
            >
              Publish Project Contract <ArrowRight className="w-4 h-4" />
            </button>
          </div>

        </form>

      </main>

      <Footer />
    </div>
  );
};
