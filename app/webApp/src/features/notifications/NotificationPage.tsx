import React, { useState } from 'react';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Bell, DollarSign, MessageSquare, Briefcase, Trash2, Sparkles } from 'lucide-react';

export const NotificationPage: React.FC = () => {
  const [notifications, setNotifications] = useState([
    {
      id: '1',
      title: 'Milestone Escrow Funded',
      desc: 'Acme AI Systems funded $1,200 into milestone #1 for Compose Desktop App.',
      time: '15 mins ago',
      type: 'payment',
      read: false,
    },
    {
      id: '2',
      title: 'New Proposal Received',
      desc: 'Sarah Jenkins submitted a bid of $3,200 on your project bounty.',
      time: '2 hours ago',
      type: 'proposal',
      read: false,
    },
    {
      id: '3',
      title: 'Direct Message',
      desc: 'Marcus Vance sent a message regarding the Exposed ORM refactor.',
      time: '1 day ago',
      type: 'message',
      read: true,
    },
  ]);

  const markAllRead = () => {
    setNotifications(notifications.map((n) => ({ ...n, read: true })));
  };

  const deleteNotification = (id: string) => {
    setNotifications(notifications.filter((n) => n.id !== id));
  };

  return (
    <div className="min-h-screen flex flex-col bg-canvas text-ink transition-colors duration-200 antialiased font-sans">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-4xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-hairline pb-6">
          <div className="space-y-1">
            <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
              <Sparkles className="w-3.5 h-3.5" /> Activity Stream
            </div>
            <h1 className="text-3xl font-extrabold tracking-tight text-ink flex items-center gap-2">
              Notifications Feed
            </h1>
            <p className="text-sm text-mute">Stay updated on contracts, milestone payouts, and proposal activity.</p>
          </div>

          <button
            onClick={markAllRead}
            className="text-xs font-bold uppercase tracking-[0.05em] text-brand-green hover:text-brand-green-hover transition-colors w-fit"
          >
            Mark All as Read
          </button>
        </div>

        {/* Notifications List */}
        <div className="space-y-4">
          {notifications.map((n) => (
            <div
              key={n.id}
              className={`p-6 rounded-xl border transition-all flex items-start justify-between gap-4 ${
                n.read
                  ? 'bg-surface border-hairline opacity-75'
                  : 'bg-surface-elevated border-brand-green shadow-md shadow-brand-green/10'
              }`}
            >
              <div className="flex items-start gap-4">
                <div className="w-11 h-11 rounded-full bg-surface text-brand-green flex items-center justify-center font-bold border border-hairline shrink-0">
                  {n.type === 'payment' ? <DollarSign className="w-5 h-5" /> :
                   n.type === 'proposal' ? <Briefcase className="w-5 h-5" /> :
                   <MessageSquare className="w-5 h-5" />}
                </div>

                <div className="space-y-1">
                  <h4 className="font-bold text-base text-ink flex items-center gap-2">
                    {n.title} {!n.read && <span className="w-2 h-2 rounded-full bg-brand-green animate-pulse"></span>}
                  </h4>
                  <p className="text-sm text-mute leading-relaxed">{n.desc}</p>
                  <span className="text-xs text-mute font-medium block pt-1">{n.time}</span>
                </div>
              </div>

              <button
                onClick={() => deleteNotification(n.id)}
                className="text-mute hover:text-red-500 p-2 transition-colors shrink-0"
                title="Remove notification"
              >
                <Trash2 className="w-4 h-4" />
              </button>
            </div>
          ))}
        </div>

      </main>

      <Footer />
    </div>
  );
};
