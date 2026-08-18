import React, { useState, useEffect } from 'react';
import './notifications.css';
import { Loader2 } from 'lucide-react';
import { notificationService, AppNotification } from '../../services/notificationService';

export const NotificationPage: React.FC = () => {
  const [items, setItems] = useState<AppNotification[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let isMounted = true;
    setLoading(true);
    notificationService.getNotifications().then((data) => {
      if (isMounted) {
        setItems(data);
        setLoading(false);
      }
    }).catch(() => {
      if (isMounted) setLoading(false);
    });

    return () => { isMounted = false; };
  }, []);

  const markAllRead = () => {
    setItems((prev) => prev.map((n) => ({ ...n, unread: false })));
    notificationService.markAsRead();
  };

  const markItemRead = (id: string) => {
    setItems((prev) => prev.map((n) => n.id === id ? { ...n, unread: false } : n));
    notificationService.markAsRead(id);
  };

  const unreadCount = items.filter((n) => n.unread).length;

  return (
    <div className="notifications-page">
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

        {loading ? (
          <div className="min-h-[300px] flex flex-col items-center justify-center gap-3 bg-surface-elevated rounded-xl border border-hairline p-12">
            <Loader2 className="w-6 h-6 text-primary animate-spin" />
            <p className="text-xs font-semibold text-mute">Loading notification feed...</p>
          </div>
        ) : items.length === 0 ? (
          <div className="notifications-empty">No notifications yet.</div>
        ) : (
          <div className="notifications-list">
            {items.map((n) => {
              const Icon = n.icon;
              return (
                <div
                  key={n.id}
                  onClick={() => markItemRead(n.id)}
                  className={`notification-item${n.unread ? ' unread' : ''}`}
                >
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
    </div>
  );
};
