import React, { useState } from 'react';
import './notifications.css';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { DollarSign, ShieldCheck, Bell, FileText } from 'lucide-react';

const NOTIFICATIONS = [
  {
    id: '1',
    title: 'Milestone escrow released',
    desc: 'Acme AI Systems approved your milestone 1 deliverable. $4,500.00 has been released to your Stripe wallet.',
    time: '15 min ago',
    unread: true,
    icon: DollarSign,
    iconClass: 'notification-icon-mint',
  },
  {
    id: '2',
    title: 'New contract proposal invite',
    desc: 'Fintech Core invited you to submit a proposal for "High Concurrency Exposed ORM Migration".',
    time: '2 hours ago',
    unread: true,
    icon: FileText,
    iconClass: 'notification-icon-sky',
  },
  {
    id: '3',
    title: 'KYC verification approved',
    desc: 'Your identity and work eligibility documents have been verified. Your profile now shows the KYC badge.',
    time: 'Yesterday',
    unread: false,
    icon: ShieldCheck,
    iconClass: 'notification-icon-lavender',
  },
  {
    id: '4',
    title: 'New message from David Chen',
    desc: 'David sent a message: "Can you check the Ktor WebSocket channel handler when you get a chance?"',
    time: 'Yesterday',
    unread: false,
    icon: Bell,
    iconClass: 'notification-icon-peach',
  },
];

export const NotificationPage: React.FC = () => {
  const [items, setItems] = useState(NOTIFICATIONS);

  const markAllRead = () => setItems((prev) => prev.map((n) => ({ ...n, unread: false })));
  const unreadCount = items.filter((n) => n.unread).length;

  return (
    <div className="notifications-page">
      <Header isLoggedIn={true} />

      <main className="notifications-main">
        <div className="notifications-header">
          <div>
            <h1 className="notifications-title">
              Notifications{unreadCount > 0 && <span style={{ fontSize: 16, fontWeight: 500, color: 'var(--color-primary)', marginLeft: 8 }}>({unreadCount} new)</span>}
            </h1>
            <p className="notifications-sub">Updates on proposals, milestones, and payouts.</p>
          </div>
          {unreadCount > 0 && (
            <button className="notifications-mark-all" onClick={markAllRead}>Mark all as read</button>
          )}
        </div>

        {items.length === 0 ? (
          <div className="notifications-empty">No notifications yet.</div>
        ) : (
          <div className="notifications-list">
            {items.map((n) => {
              const Icon = n.icon;
              return (
                <div key={n.id} className={`notification-item${n.unread ? ' unread' : ''}`}>
                  <div className={`notification-icon ${n.iconClass}`}>
                    <Icon size={16} />
                  </div>
                  <div className="notification-body">
                    <div className="notification-row">
                      <p className="notification-title">{n.title}</p>
                      <span className="notification-time">{n.time}</span>
                    </div>
                    <p className="notification-desc">{n.desc}</p>
                  </div>
                  {n.unread && <span className="notification-unread-dot" />}
                </div>
              );
            })}
          </div>
        )}
      </main>

      <Footer />
    </div>
  );
};
