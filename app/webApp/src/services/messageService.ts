export interface ConversationThread {
  id: string;
  name: string;
  role: string;
  avatar: string;
  lastMessage: string;
  time: string;
  unread: boolean;
}

export interface ChatMessage {
  id: string;
  text: string;
  time: string;
  outgoing: boolean;
}

const FALLBACK_THREADS: ConversationThread[] = [
  {
    id: 't1',
    name: 'Acme Corp Team',
    role: 'Client · KMP Wallet Project',
    avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
    lastMessage: 'The milestone 1 code review looks great! Releasing funds now.',
    time: '10:42 AM',
    unread: true,
  },
  {
    id: 't2',
    name: 'David Chen',
    role: 'Lead Architect',
    avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
    lastMessage: 'Can you check the Ktor WebSocket channel handler?',
    time: 'Yesterday',
    unread: false,
  },
];

const FALLBACK_MESSAGES: Record<string, ChatMessage[]> = {
  t1: [
    { id: 'm1', text: 'Hi Alex, we reviewed your Compose Multiplatform PR submission. The layout looks sharp and test coverage passes!', time: '10:40 AM', outgoing: false },
    { id: 'm2', text: 'Thanks! We also included SQLDelight offline database caching for low latency startup.', time: '10:42 AM', outgoing: true },
    { id: 'm3', text: 'The milestone 1 code review looks great! Releasing funds now.', time: '10:43 AM', outgoing: false },
  ],
  t2: [
    { id: 'm4', text: 'Hey there! How is the Exposed ORM migration going?', time: 'Yesterday 3:15 PM', outgoing: false },
    { id: 'm5', text: 'Almost complete, HikariCP connection pooling is tested with 500 concurrent workers.', time: 'Yesterday 3:20 PM', outgoing: true },
    { id: 'm6', text: 'Can you check the Ktor WebSocket channel handler?', time: 'Yesterday 3:45 PM', outgoing: false },
  ],
};

export const messageService = {
  async getConversations(): Promise<ConversationThread[]> {
    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      const headers: Record<string, string> = { 'Accept': 'application/json' };
      if (token) headers['Authorization'] = `Bearer ${token}`;

      const res = await fetch('http://localhost:8080/messages/conversations', { headers });
      if (res.ok) {
        const body = await res.json();
        const items = body.data || body;
        if (Array.isArray(items) && items.length > 0) {
          return items.map((c: any) => ({
            id: String(c.id || c.conversationId),
            name: c.participantName || c.name || 'Client Workspace',
            role: c.participantRole || c.role || 'Project Member',
            avatar: c.participantAvatar || c.avatar || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
            lastMessage: c.lastMessage || '',
            time: c.lastMessageTime || 'Recently',
            unread: c.unreadCount > 0,
          }));
        }
      }
    } catch {
      // Offline fallback
    }

    return FALLBACK_THREADS;
  },

  async getMessages(conversationId: string): Promise<ChatMessage[]> {
    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      const headers: Record<string, string> = { 'Accept': 'application/json' };
      if (token) headers['Authorization'] = `Bearer ${token}`;

      const res = await fetch(`http://localhost:8080/messages/${conversationId}`, { headers });
      if (res.ok) {
        const body = await res.json();
        const items = body.data || body;
        if (Array.isArray(items) && items.length > 0) {
          return items.map((m: any) => ({
            id: String(m.id || m.messageId),
            text: m.content || m.text,
            time: m.timestamp || 'Just now',
            outgoing: m.isSender || false,
          }));
        }
      }
    } catch {
      // Offline fallback
    }

    return FALLBACK_MESSAGES[conversationId] || FALLBACK_MESSAGES.t1;
  },

  async sendMessage(conversationId: string, text: string): Promise<ChatMessage> {
    const newMessage: ChatMessage = {
      id: `msg_${Date.now()}`,
      text,
      time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      outgoing: true,
    };

    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      await fetch(`http://localhost:8080/messages/${conversationId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        },
        body: JSON.stringify({ text }),
      });
    } catch {
      // Local optimistic dispatch
    }

    return newMessage;
  },
};
