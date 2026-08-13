import React, { useState } from 'react';
import { User, Code, DollarSign, GitBranch, Sparkles, Check, Plus, X, Globe } from 'lucide-react';

export const ProfileTab: React.FC = () => {
  const [skills, setSkills] = useState(['Kotlin', 'KMP', 'Compose', 'Ktor', 'iOS', 'WebAssembly']);
  const [newSkill, setNewSkill] = useState('');
  const [availability, setAvailability] = useState<'available' | 'busy' | 'unavailable'>('available');
  const [hourlyRate, setHourlyRate] = useState('85');

  const addSkill = () => {
    if (newSkill.trim() && !skills.includes(newSkill.trim())) {
      setSkills([...skills, newSkill.trim()]);
      setNewSkill('');
    }
  };

  const removeSkill = (skillToRemove: string) => {
    setSkills(skills.filter((s) => s !== skillToRemove));
  };

  return (
    <div className="space-y-8">
      {/* Freelancer Profile Information */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div>
          <h2 className="text-xl font-bold text-ink flex items-center gap-2">
            <User className="w-5 h-5 text-brand-green" /> Freelancer Public Identity
          </h2>
          <p className="text-xs text-mute mt-1">This metadata is displayed on your public profile and bounty proposal submissions.</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">Full Display Name</label>
            <input
              type="text"
              defaultValue="Alex Morgan"
              className="w-full px-4 py-2.5 rounded-full border border-hairline bg-surface-elevated text-sm text-ink focus:outline-none focus:border-brand-green transition-colors"
            />
          </div>

          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">Professional Headline</label>
            <input
              type="text"
              defaultValue="Senior Kotlin Multiplatform & Ktor Systems Architect"
              className="w-full px-4 py-2.5 rounded-full border border-hairline bg-surface-elevated text-sm text-ink focus:outline-none focus:border-brand-green transition-colors"
            />
          </div>
        </div>

        <div>
          <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">Bio & Technical Summary</label>
          <textarea
            rows={4}
            defaultValue="Build high-performance cross-platform mobile apps and backend real-time APIs with Kotlin Multiplatform, Compose, and Ktor. 6+ years shipping Android, iOS, and WebAssembly production software."
            className="w-full px-4 py-3 rounded-xl border border-hairline bg-surface-elevated text-sm text-ink focus:outline-none focus:border-brand-green transition-colors"
          />
        </div>
      </div>

      {/* Hourly Rate & Availability */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div>
          <h2 className="text-xl font-bold text-ink flex items-center gap-2">
            <DollarSign className="w-5 h-5 text-brand-green" /> Hourly Rate & Availability Status
          </h2>
          <p className="text-xs text-mute mt-1">Set your default billing rate and hire availability on the marketplace.</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">Default Hourly Rate ($ USD / hr)</label>
            <div className="relative">
              <span className="absolute left-4 top-2.5 text-mute font-bold text-sm">$</span>
              <input
                type="number"
                value={hourlyRate}
                onChange={(e) => setHourlyRate(e.target.value)}
                className="w-full pl-8 pr-4 py-2.5 rounded-full border border-hairline bg-surface-elevated text-sm font-bold text-ink focus:outline-none focus:border-brand-green"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">Marketplace Availability</label>
            <select
              value={availability}
              onChange={(e) => setAvailability(e.target.value as any)}
              className="w-full px-4 py-2.5 rounded-full border border-hairline bg-surface-elevated text-sm font-bold text-ink focus:outline-none focus:border-brand-green"
            >
              <option value="available">🟢 Available for Hire & Bounties</option>
              <option value="busy">🟡 Busy (In Contract / Limited Capacity)</option>
              <option value="unavailable">🔴 Not Accepting Proposals</option>
            </select>
          </div>
        </div>
      </div>

      {/* Primary Skill Ecosystems */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div>
          <h2 className="text-xl font-bold text-ink flex items-center gap-2">
            <Code className="w-5 h-5 text-brand-green" /> Tech Stack & Verified Skills
          </h2>
          <p className="text-xs text-mute mt-1">Add technology tags used by Gorse AI to match your profile with relevant project bounties.</p>
        </div>

        <div className="flex flex-wrap gap-2">
          {skills.map((skill) => (
            <span
              key={skill}
              className="inline-flex items-center gap-1.5 px-3.5 py-1.5 rounded-full bg-surface-elevated border border-hairline text-xs font-bold text-brand-green"
            >
              {skill}
              <button
                onClick={() => removeSkill(skill)}
                className="hover:text-red-500 transition-colors p-0.5"
              >
                <X className="w-3 h-3" />
              </button>
            </span>
          ))}
        </div>

        <div className="flex gap-3 max-w-md">
          <input
            type="text"
            placeholder="Add new skill tag (e.g. Docker, GraphQL)..."
            value={newSkill}
            onChange={(e) => setNewSkill(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && addSkill()}
            className="flex-1 px-4 py-2 rounded-full border border-hairline bg-surface-elevated text-xs text-ink focus:outline-none focus:border-brand-green"
          />
          <button
            onClick={addSkill}
            className="inline-flex items-center gap-1 bg-surface-elevated hover:bg-surface-modal text-ink border border-hairline font-bold px-4 py-2 rounded-full text-xs uppercase tracking-[0.05em]"
          >
            <Plus className="w-3.5 h-3.5 text-brand-green" /> Add Tag
          </button>
        </div>
      </div>

      {/* Code Repository Showcase */}
      <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
        <div>
          <h2 className="text-xl font-bold text-ink flex items-center gap-2">
            <GitBranch className="w-5 h-5 text-brand-green" /> Connected Code Repositories
          </h2>
          <p className="text-xs text-mute mt-1">Showcase open-source KMP libraries or GitHub repos on your profile.</p>
        </div>

        <div className="p-4 rounded-xl bg-surface-elevated border border-hairline flex items-center justify-between">
          <div className="flex items-center gap-3">
            <GitBranch className="w-6 h-6 text-brand-green" />
            <div>
              <div className="text-sm font-bold text-ink">github.com/alex-morgan-kmp</div>
              <div className="text-xs text-mute">Synced &bull; 14 Public Repositories</div>
            </div>
          </div>

          <span className="px-3 py-1 rounded-full bg-brand-green/10 text-brand-green text-xs font-bold uppercase tracking-wider">
            Connected
          </span>
        </div>
      </div>
    </div>
  );
};

