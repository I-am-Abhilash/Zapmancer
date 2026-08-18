import { DollarSign, ShieldCheck, Bell, FileText, LucideIcon } from 'lucide-react';

export interface AppNotification {
  id: string;
  title: string;
  desc: string;
  time: string;
  unread: boolean;
  icon: LucideIcon;
  iconClass: string;
}

const FALLBACK_NOTIFICATIONS: AppNotification[] = [
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

export const notificationService = {
  async getNotifications(): Promise<AppNotification[]> {
    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      const headers: Record<string, string> = { 'Accept': 'application/json' };
      if (token) headers['Authorization'] = `Bearer ${token}`;

      const res = await fetch('http://localhost:8080/notifications', { headers });
      if (res.ok) {
        const body = await res.json();
        const items = body.data || body;
        if (Array.isArray(items) && items.length > 0) {
          return items.map((n: any) => {
            let icon: LucideIcon = Bell;
            let iconClass = 'notification-icon-peach';
            if (n.type === 'ESCROW_RELEASED' || n.title?.toLowerCase().includes('escrow')) {
              icon = DollarSign;
              iconClass = 'notification-icon-mint';
            } else if (n.type === 'PROPOSAL_INVITE' || n.title?.toLowerCase().includes('proposal')) {
              icon = FileText;
              iconClass = 'notification-icon-sky';
            } else if (n.type === 'KYC_APPROVED' || n.title?.toLowerCase().includes('kyc')) {
              icon = ShieldCheck;
              iconClass = 'notification-icon-lavender';
            }

            return {
              id: String(n.id),
              title: n.title,
              desc: n.message || n.desc,
              time: n.time || 'Recently',
              unread: !n.isRead,
              icon,
              iconClass,
            };
          });
        }
      }
    } catch {
      // Offline fallback
    }

    return FALLBACK_NOTIFICATIONS;
  },

  async markAsRead(notificationId?: string): Promise<void> {
    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      const endpoint = notificationId
        ? `http://localhost:8080/notifications/${notificationId}/read`
        : 'http://localhost:8080/notifications/read-all';

      await fetch(endpoint, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        },
      });
    } catch {
      // Optimistic local state
    }
  },
};
