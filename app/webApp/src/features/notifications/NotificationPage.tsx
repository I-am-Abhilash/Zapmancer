import React, { useState } from 'react';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Bell, CheckCircle2, DollarSign, MessageSquare, Briefcase, Trash2, Filter } from 'lucide-react';

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
    <div className="min-h-screen flex flex-col bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 transition-colors">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-4xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h1 className="text-3xl font-extrabold tracking-tight flex items-center gap-2">
              <Bell className="w-7 h-7 text-indigo-500" /> Notifications Feed
            </h1>
            <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Stay updated on contracts, milestone payouts, and proposal activity.</p>
          </div>

          <button
            onClick={markAllRead}
            className="text-xs font-bold text-indigo-600 dark:text-indigo-400 hover:underline w-fit"
          >
            Mark All as Read
          </button>
        </div>

        {/* List */}
        <div className="space-y-4">
          {notifications.map((n) => (
            <div
              key={n.id}
              className={`p-6 rounded-3xl border transition-all flex items-start justify-between gap-4 ${
                n.read
                  ? 'bg-white dark:bg-slate-900 border-slate-200 dark:border-slate-800 opacity-80'
                  : 'bg-white dark:bg-slate-900 border-indigo-500 shadow-md'
              }`}
            >
              <div className="flex items-start gap-4">
                <div className={`w-11 h-11 rounded-2xl flex items-center justify-center font-bold ${
                  n.type === 'payment' ? 'bg-emerald-50 text-emerald-600 dark:bg-emerald-950/50' :
                  n.type === 'proposal' ? 'bg-indigo-50 text-indigo-600 dark:bg-indigo-950/50' :
                  'bg-blue-50 text-blue-600 dark:bg-blue-950/50'
                }`}>
                  {n.type === 'payment' ? <DollarSign className="w-5 h-5" /> :
                   n.type === 'proposal' ? <Briefcase className="w-5 h-5" /> :
                   <MessageSquare className="w-5 h-5" />}
                </div>

                <div className="space-y-1">
                  <h4 className="font-bold text-base text-slate-900 dark:text-white flex items-center gap-2">
                    {n.title} {!n.read && <span className="w-2 h-2 rounded-full bg-indigo-600"></span>}
                  </h4>
                  <p className="text-sm text-slate-600 dark:text-slate-300">{n.desc}</p>
                  <span className="text-xs text-slate-400 font-medium block pt-1">{n.time}</span>
                </div>
              </div>

              <button
                onClick={() => deleteNotification(n.id)}
                className="text-slate-400 hover:text-rose-500 p-2 transition-colors"
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
