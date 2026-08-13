import React from 'react';
import './notifications.css';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Bell, ShieldCheck, DollarSign, Sparkles } from 'lucide-react';

export const NotificationPage: React.FC = () => {
  const notifications = [
    {
      id: '1',
      title: 'Milestone Escrow Released',
      desc: 'Acme AI Systems approved your milestone 1 deliverable. $4,500.00 released to your Stripe wallet.',
      time: '15 minutes ago',
      unread: true,
      icon: DollarSign
    },
    {
      id: '2',
      title: 'New Contract Proposal Invite',
      desc: 'Fintech Core invited you to submit a proposal for "High Concurrency Exposed ORM Migration".',
      time: '2 hours ago',
      unread: false,
      icon: ShieldCheck
    }
  ];

  return (
    <div className="notifications-page">
      <Header isLoggedIn={true} />

      <main className="notifications-main">
        <div className="space-y-1 border-b border-hairline pb-6">
          <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
            <Sparkles className="w-3.5 h-3.5" /> Telemetry Feed
          </div>
          <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight text-ink">Notifications & Activity Feed</h1>
          <p className="text-sm text-mute">Real-time updates regarding contract proposals, milestone escrow locks, and payouts.</p>
        </div>

        <div className="space-y-3">
          {notifications.map((n) => {
            const Icon = n.icon;
            return (
              <div
                key={n.id}
                className={`notification-item ${
                  n.unread
                    ? 'bg-brand-green/5 border-brand-green/30'
                    : 'bg-surface border-hairline'
                }`}
              >
                <div className="w-10 h-10 rounded-full bg-surface-elevated text-brand-green flex items-center justify-center shrink-0 border border-hairline">
                  <Icon className="w-5 h-5" />
                </div>

                <div className="flex-1 space-y-1">
                  <div className="flex items-center justify-between">
                    <h3 className="font-bold text-sm text-ink">{n.title}</h3>
                    <span className="text-[11px] text-mute">{n.time}</span>
                  </div>
                  <p className="text-xs text-mute leading-relaxed">{n.desc}</p>
                </div>
              </div>
            );
          })}
        </div>
      </main>

      <Footer />
    </div>
  );
};
