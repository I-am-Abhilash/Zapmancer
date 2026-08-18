import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Zap, CheckCircle2, UserCheck, Briefcase, ArrowRight, DollarSign, Sparkles } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

export const OnboardingPage: React.FC = () => {
  const navigate = useNavigate();
  const { switchRole } = useAuth();
  const [step, setStep] = useState<1 | 2 | 3>(1);
  const [role, setRole] = useState<'freelancer' | 'client'>('freelancer');
  const [selectedSkills, setSelectedSkills] = useState<string[]>(['Kotlin Multiplatform', 'React & Vite']);
  const [hourlyRate, setHourlyRate] = useState<number>(65);
  const [bio, setBio] = useState<string>('Full-stack developer specialized in modern KMP, WebAssembly, and responsive React applications.');

  const availableSkills = [
    'Kotlin Multiplatform', 'Compose Multiplatform', 'React & Vite', 'TypeScript', 
    'Ktor Server', 'PostgreSQL', 'Android Native', 'iOS Swift', 'TailwindCSS', 
    'WebAssembly (Wasm)', 'Docker & K8s', 'UI/UX Design'
  ];

  const toggleSkill = (skill: string) => {
    if (selectedSkills.includes(skill)) {
      setSelectedSkills(selectedSkills.filter((s) => s !== skill));
    } else {
      setSelectedSkills([...selectedSkills, skill]);
    }
  };

  const handleFinish = () => {
    if (role === 'client') {
      switchRole('company');
      navigate('/company/dashboard');
    } else {
      switchRole('freelancer');
      navigate('/home');
    }
  };

  return (
    <div className="flex-1 max-w-4xl w-full mx-auto px-4 py-12">
        {/* Progress Bar */}
        <div className="mb-10 text-center">
          <div className="flex items-center justify-center gap-2 mb-3">
            <span className={`w-8 h-8 rounded-full flex items-center justify-center font-bold text-xs ${step >= 1 ? 'bg-indigo-600 text-white' : 'bg-slate-200 dark:bg-slate-800 text-slate-500'}`}>1</span>
            <div className={`w-16 h-1 rounded ${step >= 2 ? 'bg-indigo-600' : 'bg-slate-200 dark:bg-slate-800'}`}></div>
            <span className={`w-8 h-8 rounded-full flex items-center justify-center font-bold text-xs ${step >= 2 ? 'bg-indigo-600 text-white' : 'bg-slate-200 dark:bg-slate-800 text-slate-500'}`}>2</span>
            <div className={`w-16 h-1 rounded ${step >= 3 ? 'bg-indigo-600' : 'bg-slate-200 dark:bg-slate-800'}`}></div>
            <span className={`w-8 h-8 rounded-full flex items-center justify-center font-bold text-xs ${step >= 3 ? 'bg-indigo-600 text-white' : 'bg-slate-200 dark:bg-slate-800 text-slate-500'}`}>3</span>
          </div>
          <h1 className="text-3xl font-extrabold tracking-tight">Set Up Your Zapmancer Profile</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Customize how clients and talent interact with you on the marketplace.</p>
        </div>

        {/* Step 1: Role Selection */}
        {step === 1 && (
          <div className="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-8 shadow-xl space-y-6">
            <h2 className="text-xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
              <UserCheck className="w-5 h-5 text-indigo-500" /> How do you plan to use Zapmancer?
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div
                onClick={() => setRole('freelancer')}
                className={`p-6 rounded-2xl border-2 cursor-pointer transition-all ${
                  role === 'freelancer'
                    ? 'border-indigo-600 bg-indigo-50/50 dark:bg-indigo-950/20 shadow-md'
                    : 'border-slate-200 dark:border-slate-800 hover:border-indigo-300'
                }`}
              >
                <div className="w-12 h-12 rounded-xl bg-indigo-600 text-white flex items-center justify-center mb-4">
                  <Briefcase className="w-6 h-6" />
                </div>
                <h3 className="text-lg font-bold">Work as a Freelancer</h3>
                <p className="text-sm text-slate-500 dark:text-slate-400 mt-2 leading-relaxed">
                  Browse engineering & design contracts, submit milestone proposals, and get paid securely with zero platform markup.
                </p>
              </div>

              <div
                onClick={() => setRole('client')}
                className={`p-6 rounded-2xl border-2 cursor-pointer transition-all ${
                  role === 'client'
                    ? 'border-indigo-600 bg-indigo-50/50 dark:bg-indigo-950/20 shadow-md'
                    : 'border-slate-200 dark:border-slate-800 hover:border-indigo-300'
                }`}
              >
                <div className="w-12 h-12 rounded-xl bg-emerald-600 text-white flex items-center justify-center mb-4">
                  <Zap className="w-6 h-6" />
                </div>
                <h3 className="text-lg font-bold">Hire Top Talent</h3>
                <p className="text-sm text-slate-500 dark:text-slate-400 mt-2 leading-relaxed">
                  Post project bounties, review proposals from verified developers, and manage deliverables with escrow protection.
                </p>
              </div>
            </div>

            <div className="flex justify-end pt-4">
              <button
                onClick={() => setStep(2)}
                className="flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-3 rounded-xl font-semibold transition-all shadow-md shadow-indigo-500/20"
              >
                Continue <ArrowRight className="w-4 h-4" />
              </button>
            </div>
          </div>
        )}

        {/* Step 2: Skills & Expertise */}
        {step === 2 && (
          <div className="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-8 shadow-xl space-y-6">
            <h2 className="text-xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
              <Sparkles className="w-5 h-5 text-indigo-500" /> Select Your Core Tech Stack
            </h2>

            <p className="text-sm text-slate-500 dark:text-slate-400">
              Pick the technologies you specialize in or project tags you want highlighted on your feed.
            </p>

            <div className="flex flex-wrap gap-3">
              {availableSkills.map((skill) => {
                const isSelected = selectedSkills.includes(skill);
                return (
                  <button
                    key={skill}
                    onClick={() => toggleSkill(skill)}
                    className={`flex items-center gap-2 px-4 py-2 rounded-xl text-sm font-medium transition-all ${
                      isSelected
                        ? 'bg-indigo-600 text-white shadow-sm'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
                    }`}
                  >
                    {isSelected && <CheckCircle2 className="w-4 h-4" />} {skill}
                  </button>
                );
              })}
            </div>

            <div className="flex justify-between pt-4">
              <button
                onClick={() => setStep(1)}
                className="px-6 py-3 rounded-xl font-semibold text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800 transition-all"
              >
                Back
              </button>
              <button
                onClick={() => setStep(3)}
                className="flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-3 rounded-xl font-semibold transition-all shadow-md shadow-indigo-500/20"
              >
                Continue <ArrowRight className="w-4 h-4" />
              </button>
            </div>
          </div>
        )}

        {/* Step 3: Rates & Bio */}
        {step === 3 && (
          <div className="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-8 shadow-xl space-y-6">
            <h2 className="text-xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
              <DollarSign className="w-5 h-5 text-indigo-500" /> Set Rates & Bio Summary
            </h2>

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-1">
                  Default Hourly Rate ($/hr)
                </label>
                <div className="relative max-w-xs">
                  <span className="absolute left-3.5 top-3 text-slate-400 font-bold">$</span>
                  <input
                    type="number"
                    value={hourlyRate}
                    onChange={(e) => setHourlyRate(Number(e.target.value))}
                    className="w-full pl-8 pr-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 focus:ring-2 focus:ring-indigo-500 font-semibold"
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-1">
                  Short Professional Bio
                </label>
                <textarea
                  rows={4}
                  value={bio}
                  onChange={(e) => setBio(e.target.value)}
                  className="w-full px-4 py-3 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 focus:ring-2 focus:ring-indigo-500 text-sm leading-relaxed"
                />
              </div>
            </div>

            <div className="flex justify-between pt-4">
              <button
                onClick={() => setStep(2)}
                className="px-6 py-3 rounded-xl font-semibold text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800 transition-all"
              >
                Back
              </button>
              <button
                onClick={handleFinish}
                className="flex items-center gap-2 bg-emerald-600 hover:bg-emerald-700 text-white px-8 py-3 rounded-xl font-bold transition-all shadow-md shadow-emerald-500/20"
              >
                Complete Profile Setup <CheckCircle2 className="w-5 h-5" />
              </button>
            </div>
          </div>
        )}
    </div>
  );
};
