import React, { useState } from 'react';
import './messages.css';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Search, Send, Paperclip, ShieldCheck, Sparkles } from 'lucide-react';

export const MessagesPage: React.FC = () => {
  const [activeThread, setActiveThread] = useState('t1');
  const [messageText, setMessageText] = useState('');

  const threads = [
    {
      id: 't1',
      name: 'Acme Corp Team',
      role: 'Client &bull; KMP Wallet Project',
      avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
      lastMessage: 'The milestone 1 code review looks great! Releasing funds now.',
      time: '10:42 AM',
      unread: true
    },
    {
      id: 't2',
      name: 'David Chen',
      role: 'Lead Architect',
      avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
      lastMessage: 'Can you check the Ktor WebSocket channel handler?',
      time: 'Yesterday',
      unread: false
    }
  ];

  return (
    <div className="messages-page">
      <Header isLoggedIn={true} />

      <main className="messages-main">
        <div className="space-y-1 border-b border-hairline pb-6">
          <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
            <Sparkles className="w-3.5 h-3.5" /> Real-Time Chat
          </div>
          <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight text-ink">Contract Messages & Support</h1>
          <p className="text-sm text-mute">Direct end-to-end encrypted messaging with clients and freelancers.</p>
        </div>

        <div className="messages-card">
          <div className="w-full md:w-80 border-r border-hairline bg-surface-elevated p-4 space-y-4">
            <div className="relative">
              <Search className="w-4 h-4 text-mute absolute left-3 top-3" />
              <input
                type="text"
                placeholder="Search conversations..."
                className="w-full pl-9 pr-3 py-2 rounded-full border border-hairline bg-surface text-xs text-ink placeholder:text-mute focus:outline-none focus:border-brand-green"
              />
            </div>

            <div className="space-y-2">
              {threads.map((t) => (
                <div
                  key={t.id}
                  onClick={() => setActiveThread(t.id)}
                  className={`p-3 rounded-xl cursor-pointer transition-colors flex items-center gap-3 ${
                    activeThread === t.id ? 'bg-brand-green/10 border border-brand-green/30' : 'hover:bg-surface-modal'
                  }`}
                >
                  <img src={t.avatar} alt={t.name} className="w-10 h-10 rounded-full object-cover border border-hairline shrink-0" />
                  <div className="min-w-0 flex-1">
                    <div className="flex items-center justify-between">
                      <h4 className="font-bold text-sm text-ink truncate">{t.name}</h4>
                      <span className="text-[10px] text-mute">{t.time}</span>
                    </div>
                    <p className="text-xs text-mute truncate">{t.lastMessage}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="flex-1 flex flex-col justify-between bg-surface p-6">
            <div className="border-b border-hairline pb-4 flex items-center gap-3">
              <img src={threads[0].avatar} alt="Active User" className="w-10 h-10 rounded-full object-cover border border-hairline" />
              <div>
                <h3 className="font-bold text-ink text-base flex items-center gap-1.5">
                  {threads[0].name} <ShieldCheck className="w-4 h-4 text-brand-green" />
                </h3>
                <p className="text-xs text-mute font-medium">Verified Client &bull; Active Escrow Lock</p>
              </div>
            </div>

            <div className="py-6 space-y-4 flex-1 overflow-y-auto">
              <div className="flex justify-start">
                <div className="bg-surface-elevated border border-hairline p-4 rounded-2xl max-w-md text-xs text-ink space-y-1">
                  <p>Hi Alex, we reviewed your Compose Multiplatform PR submission. The layout looks sharp and test coverage passes!</p>
                  <span className="text-[10px] text-mute block text-right">10:40 AM</span>
                </div>
              </div>

              <div className="flex justify-end">
                <div className="bg-brand-green text-white p-4 rounded-2xl max-w-md text-xs space-y-1 shadow-md shadow-brand-green/20">
                  <p>Thanks! We also included SQLDelight offline database caching for low latency startup.</p>
                  <span className="text-[10px] text-white/80 block text-right">10:42 AM</span>
                </div>
              </div>
            </div>

            <div className="border-t border-hairline pt-4 flex items-center gap-3">
              <button className="p-2.5 rounded-full bg-surface-elevated border border-hairline text-mute hover:text-ink">
                <Paperclip className="w-4 h-4" />
              </button>
              <input
                type="text"
                value={messageText}
                onChange={(e) => setMessageText(e.target.value)}
                placeholder="Type your contract message..."
                className="flex-1 px-4 py-2.5 rounded-full border border-hairline bg-surface-elevated text-xs text-ink focus:outline-none focus:border-brand-green"
              />
              <button className="p-2.5 rounded-full bg-brand-green hover:bg-brand-green-hover text-white transition-all shadow-md shadow-brand-green/20">
                <Send className="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
};
