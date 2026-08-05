import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { PlusCircle, ShieldCheck, CheckCircle2, ArrowRight, DollarSign, UploadCloud, Tag } from 'lucide-react';

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
    <div className="min-h-screen flex flex-col bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 transition-colors">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-4xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        <div>
          <h1 className="text-3xl font-extrabold tracking-tight">Post a New Engineering Bounty</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Publish project requirements to receive proposals from top KMP & mobile developers.</p>
        </div>

        <form onSubmit={handleSubmit} className="bg-white dark:bg-slate-900 p-8 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-xl space-y-6">
          
          {/* Title */}
          <div>
            <label className="block text-sm font-bold text-slate-900 dark:text-white mb-1">
              Project Bounty Title
            </label>
            <input
              type="text"
              required
              placeholder="e.g. Build a Compose Multiplatform Desktop App for Ktor Analytics"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full px-4 py-3 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm focus:ring-2 focus:ring-indigo-500 font-semibold"
            />
          </div>

          {/* Category */}
          <div>
            <label className="block text-sm font-bold text-slate-900 dark:text-white mb-1">
              Primary Stack Category
            </label>
            <select
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              className="w-full px-4 py-3 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm font-semibold text-slate-800 dark:text-slate-200"
            >
              <option value="Kotlin Multiplatform">Kotlin Multiplatform</option>
              <option value="Mobile (Android/iOS)">Mobile (Android/iOS)</option>
              <option value="Full-Stack & Ktor">Full-Stack & Ktor</option>
              <option value="AI Systems & Gorse">AI Systems & Gorse</option>
            </select>
          </div>

          {/* Description */}
          <div>
            <label className="block text-sm font-bold text-slate-900 dark:text-white mb-1">
              Scope & Specifications
            </label>
            <textarea
              rows={6}
              required
              placeholder="Describe the deliverables, timeline, technical expectations, and API integrations needed..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="w-full px-4 py-3 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm leading-relaxed focus:ring-2 focus:ring-indigo-500"
            />
          </div>

          {/* Budget Type & Amount */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-bold text-slate-900 dark:text-white mb-1">
                Budget Model
              </label>
              <div className="flex gap-4">
                <button
                  type="button"
                  onClick={() => setBudgetType('Fixed')}
                  className={`flex-1 py-3 rounded-xl border text-sm font-bold transition-all ${
                    budgetType === 'Fixed'
                      ? 'border-indigo-600 bg-indigo-50 dark:bg-indigo-950/40 text-indigo-600 dark:text-indigo-400'
                      : 'border-slate-300 dark:border-slate-700 text-slate-600 dark:text-slate-400'
                  }`}
                >
                  Fixed Milestone
                </button>
                <button
                  type="button"
                  onClick={() => setBudgetType('Hourly')}
                  className={`flex-1 py-3 rounded-xl border text-sm font-bold transition-all ${
                    budgetType === 'Hourly'
                      ? 'border-indigo-600 bg-indigo-50 dark:bg-indigo-950/40 text-indigo-600 dark:text-indigo-400'
                      : 'border-slate-300 dark:border-slate-700 text-slate-600 dark:text-slate-400'
                  }`}
                >
                  Hourly Rate
                </button>
              </div>
            </div>

            <div>
              <label className="block text-sm font-bold text-slate-900 dark:text-white mb-1">
                Budget Amount ($)
              </label>
              <div className="relative">
                <span className="absolute left-3.5 top-3 text-slate-400 font-bold">$</span>
                <input
                  type="number"
                  required
                  value={budgetAmount}
                  onChange={(e) => setBudgetAmount(e.target.value)}
                  className="w-full pl-8 pr-4 py-3 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm font-semibold focus:ring-2 focus:ring-indigo-500"
                />
              </div>
            </div>
          </div>

          {/* Required Skills */}
          <div>
            <label className="block text-sm font-bold text-slate-900 dark:text-white mb-1">
              Required Technical Skills
            </label>
            <div className="flex gap-2 mb-2">
              <input
                type="text"
                placeholder="Add skill tag (e.g. Compose, PostgreSQL)..."
                value={skillInput}
                onChange={(e) => setSkillInput(e.target.value)}
                className="flex-1 px-4 py-2 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm"
              />
              <button
                type="button"
                onClick={addSkill}
                className="px-4 py-2 bg-slate-200 dark:bg-slate-800 text-slate-800 dark:text-slate-200 rounded-xl text-sm font-bold hover:bg-slate-300"
              >
                Add Tag
              </button>
            </div>

            <div className="flex flex-wrap gap-2">
              {skills.map((s) => (
                <span
                  key={s}
                  onClick={() => removeSkill(s)}
                  className="px-3 py-1 rounded-lg bg-indigo-50 dark:bg-indigo-950/60 text-indigo-700 dark:text-indigo-300 text-xs font-semibold cursor-pointer hover:line-through"
                >
                  {s} &times;
                </span>
              ))}
            </div>
          </div>

          {/* Submit Action */}
          <div className="pt-4 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between">
            <div className="flex items-center gap-2 text-xs text-emerald-500 font-semibold">
              <ShieldCheck className="w-4 h-4" /> Escrow milestone deposit optional until hire
            </div>

            <button
              type="submit"
              className="flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-base px-8 py-3.5 rounded-xl shadow-lg shadow-indigo-500/20 transition-all"
            >
              Publish Project Bounty <ArrowRight className="w-5 h-5" />
            </button>
          </div>

        </form>

      </main>

      <Footer />
    </div>
  );
};
