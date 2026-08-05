import React from 'react';
import { Link, useParams } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, Star, MapPin, DollarSign, Edit3, MessageSquare, Award, ExternalLink, CheckCircle2, Briefcase } from 'lucide-react';

export const ProfilePage: React.FC = () => {
  const { id } = useParams();

  const profile = {
    name: 'Alex Morgan',
    title: 'Senior KMP & Full-Stack Architect',
    avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
    rating: 4.98,
    reviewsCount: 34,
    rate: '$85 / hr',
    totalEarned: '$68,000+',
    location: 'San Francisco, USA',
    bio: 'Passionate Kotlin Multiplatform engineer with 6+ years of experience architecting cross-platform mobile apps and Ktor backend microservices. Active open-source contributor.',
    skills: ['Kotlin Multiplatform', 'Compose Multiplatform', 'Ktor Server', 'PostgreSQL', 'WebAssembly', 'Docker', 'MVI Architecture'],
    workHistory: [
      { project: 'Cross-Platform Mobile Wallet Core', client: 'Fintech Corp', rating: 5.0, earned: '$12,500', review: 'Alex delivered top tier KMP code with full test coverage ahead of schedule.' },
      { project: 'Ktor WebSockets Live Chat Microservice', client: 'Zapmancer Labs', rating: 4.9, earned: '$8,200', review: 'Outstanding architecture and clear milestone communication.' },
    ],
    portfolio: [
      { name: 'KMP Multiplatform Wallet', url: 'https://github.com', desc: 'Compose Mobile & Web Wasm wallet app' },
      { name: 'Ktor Exposed Microservice template', url: 'https://github.com', desc: 'High concurrency starter for Ktor' },
    ]
  };

  return (
    <div className="min-h-screen flex flex-col bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 transition-colors">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-5xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        {/* Profile Card Header */}
        <div className="bg-white dark:bg-slate-900 p-8 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-xl space-y-6">
          <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
            <div className="flex items-center gap-6">
              <img
                src={profile.avatar}
                alt={profile.name}
                className="w-24 h-24 rounded-3xl object-cover border-2 border-indigo-500/40 shadow-lg"
              />
              <div className="space-y-1">
                <div className="flex items-center gap-2">
                  <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900 dark:text-white">
                    {profile.name}
                  </h1>
                  <span className="flex items-center gap-1 text-xs font-bold text-emerald-600 bg-emerald-50 dark:bg-emerald-950/60 px-2.5 py-1 rounded-full border border-emerald-500/20">
                    <ShieldCheck className="w-4 h-4" /> Verified Talent
                  </span>
                </div>
                <p className="text-sm font-semibold text-slate-500 dark:text-slate-400">{profile.title}</p>
                <div className="flex items-center gap-4 text-xs text-slate-400 pt-1">
                  <span className="flex items-center gap-1"><MapPin className="w-3.5 h-3.5" /> {profile.location}</span>
                  <span className="flex items-center gap-1 text-amber-500 font-bold"><Star className="w-3.5 h-3.5 fill-current" /> {profile.rating} ({profile.reviewsCount} reviews)</span>
                </div>
              </div>
            </div>

            <div className="flex items-center gap-3">
              <Link
                to="/profile/edit"
                className="flex items-center gap-2 px-5 py-3 rounded-xl border border-slate-300 dark:border-slate-700 hover:bg-slate-100 dark:hover:bg-slate-800 font-bold text-sm text-slate-700 dark:text-slate-200 transition-all"
              >
                <Edit3 className="w-4 h-4" /> Edit Profile
              </Link>
              <Link
                to="/messages"
                className="flex items-center gap-2 px-5 py-3 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-sm transition-all shadow-md shadow-indigo-500/20"
              >
                <MessageSquare className="w-4 h-4" /> Contact / Hire
              </Link>
            </div>
          </div>

          <p className="text-sm text-slate-600 dark:text-slate-300 leading-relaxed border-t border-slate-100 dark:border-slate-800 pt-6">
            {profile.bio}
          </p>

          {/* Stats Bar */}
          <div className="grid grid-cols-2 sm:grid-cols-3 gap-4 pt-4 border-t border-slate-100 dark:border-slate-800 text-center">
            <div>
              <p className="text-xs text-slate-400 font-medium">Hourly Rate</p>
              <p className="text-xl font-extrabold text-emerald-600 dark:text-emerald-400 mt-0.5">{profile.rate}</p>
            </div>
            <div>
              <p className="text-xs text-slate-400 font-medium">Total Bounties Earned</p>
              <p className="text-xl font-extrabold text-slate-900 dark:text-white mt-0.5">{profile.totalEarned}</p>
            </div>
            <div className="col-span-2 sm:col-span-1">
              <p className="text-xs text-slate-400 font-medium">Job Success Rate</p>
              <p className="text-xl font-extrabold text-indigo-600 dark:text-indigo-400 mt-0.5">100%</p>
            </div>
          </div>
        </div>

        {/* Skills & Expertise */}
        <div className="bg-white dark:bg-slate-900 p-8 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-sm space-y-4">
          <h2 className="text-lg font-bold">Skills & Technologies</h2>
          <div className="flex flex-wrap gap-2">
            {profile.skills.map((s) => (
              <span key={s} className="px-3 py-1.5 rounded-xl bg-indigo-50 dark:bg-indigo-950/60 text-indigo-700 dark:text-indigo-300 text-xs font-bold">
                {s}
              </span>
            ))}
          </div>
        </div>

        {/* Work History & Reviews */}
        <div className="bg-white dark:bg-slate-900 p-8 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-sm space-y-6">
          <h2 className="text-lg font-bold flex items-center gap-2">
            <Briefcase className="w-5 h-5 text-indigo-500" /> Work History & Client Feedback
          </h2>

          <div className="space-y-4">
            {profile.workHistory.map((w, idx) => (
              <div key={idx} className="p-5 rounded-2xl bg-slate-50 dark:bg-slate-800/50 border border-slate-100 dark:border-slate-800 space-y-2">
                <div className="flex items-center justify-between">
                  <h4 className="font-bold text-slate-900 dark:text-white text-base">{w.project}</h4>
                  <span className="text-sm font-extrabold text-emerald-600">{w.earned}</span>
                </div>
                <div className="flex items-center gap-2 text-xs text-amber-500 font-bold">
                  <Star className="w-3.5 h-3.5 fill-current" /> {w.rating} rating &bull; <span className="text-slate-400 font-normal">{w.client}</span>
                </div>
                <p className="text-xs text-slate-600 dark:text-slate-300 italic pt-1">"{w.review}"</p>
              </div>
            ))}
          </div>
        </div>

      </main>

      <Footer />
    </div>
  );
};
