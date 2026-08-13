import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Save, ArrowLeft, Sparkles } from 'lucide-react';

export const EditProfilePage: React.FC = () => {
  const navigate = useNavigate();

  const [name, setName] = useState('Alex Morgan');
  const [title, setTitle] = useState('Senior KMP & Full-Stack Architect');
  const [rate, setRate] = useState('85');
  const [location, setLocation] = useState('San Francisco, USA');
  const [bio, setBio] = useState(
    'Passionate Kotlin Multiplatform engineer with 6+ years of experience architecting cross-platform mobile apps and Ktor backend microservices.'
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    navigate('/profile/me');
  };

  return (
    <div className="min-h-screen flex flex-col bg-canvas text-ink transition-colors duration-200 antialiased font-sans">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-4xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        <button
          onClick={() => navigate(-1)}
          className="inline-flex items-center gap-2 text-xs uppercase font-bold tracking-[0.05em] text-mute hover:text-ink transition-colors"
        >
          <ArrowLeft className="w-4 h-4 text-brand-green" /> Cancel & Return
        </button>

        <div className="space-y-1 border-b border-hairline pb-6">
          <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
            <Sparkles className="w-3.5 h-3.5" /> Engineer Profile
          </div>
          <h1 className="text-3xl font-extrabold tracking-tight text-ink">Edit Profile Attributes</h1>
          <p className="text-sm text-mute">Keep your public engineering resume and billing rates updated.</p>
        </div>

        <form onSubmit={handleSubmit} className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
                Full Display Name
              </label>
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="w-full px-4 py-3 rounded-full border border-hairline bg-surface-elevated text-sm font-semibold text-ink focus:outline-none focus:border-brand-green transition-colors"
              />
            </div>

            <div>
              <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
                Professional Title
              </label>
              <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                className="w-full px-4 py-3 rounded-full border border-hairline bg-surface-elevated text-sm font-semibold text-ink focus:outline-none focus:border-brand-green transition-colors"
              />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
                Hourly Billing Rate ($ USD / hr)
              </label>
              <input
                type="number"
                value={rate}
                onChange={(e) => setRate(e.target.value)}
                className="w-full px-4 py-3 rounded-full border border-hairline bg-surface-elevated text-sm font-bold text-ink focus:outline-none focus:border-brand-green transition-colors"
              />
            </div>

            <div>
              <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
                Location / Timezone
              </label>
              <input
                type="text"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
                className="w-full px-4 py-3 rounded-full border border-hairline bg-surface-elevated text-sm font-semibold text-ink focus:outline-none focus:border-brand-green transition-colors"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs uppercase font-bold tracking-[0.05em] text-mute mb-1.5">
              Bio & Engineering Summary
            </label>
            <textarea
              rows={5}
              value={bio}
              onChange={(e) => setBio(e.target.value)}
              className="w-full px-4 py-3 rounded-xl border border-hairline bg-surface-elevated text-sm text-ink leading-relaxed focus:outline-none focus:border-brand-green transition-colors"
            />
          </div>

          <div className="pt-4 border-t border-hairline flex justify-end">
            <button
              type="submit"
              className="flex items-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white font-bold text-xs uppercase tracking-[0.05em] px-8 py-3 rounded-full hover:scale-[1.04] transition-all shadow-md shadow-brand-green/20"
            >
              Save Profile Changes <Save className="w-4 h-4" />
            </button>
          </div>

        </form>

      </main>

      <Footer />
    </div>
  );
};
