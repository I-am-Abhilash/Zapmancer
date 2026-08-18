import React, { useState, useEffect } from 'react';
import './messages.css';
import { Search, Send, Paperclip, ShieldCheck, Loader2 } from 'lucide-react';
import { messageService, ConversationThread, ChatMessage } from '../../services/messageService';

export const MessagesPage: React.FC = () => {
  const [threads, setThreads] = useState<ConversationThread[]>([]);
  const [activeThreadId, setActiveThreadId] = useState<string>('t1');
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [messageText, setMessageText] = useState('');
  const [loadingThreads, setLoadingThreads] = useState(true);
  const [loadingMessages, setLoadingMessages] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  useEffect(() => {
    let isMounted = true;
    setLoadingThreads(true);
    messageService.getConversations().then((data) => {
      if (isMounted) {
        setThreads(data);
        if (data.length > 0) setActiveThreadId(data[0].id);
        setLoadingThreads(false);
      }
    }).catch(() => {
      if (isMounted) setLoadingThreads(false);
    });

    return () => { isMounted = false; };
  }, []);

  useEffect(() => {
    let isMounted = true;
    setLoadingMessages(true);
    messageService.getMessages(activeThreadId).then((data) => {
      if (isMounted) {
        setMessages(data);
        setLoadingMessages(false);
      }
    }).catch(() => {
      if (isMounted) setLoadingMessages(false);
    });

    return () => { isMounted = false; };
  }, [activeThreadId]);

  const active = threads.find((t) => t.id === activeThreadId) || threads[0] || {
    id: 't1',
    name: 'Acme Corp Team',
    role: 'Client · KMP Wallet Project',
    avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
    lastMessage: '',
    time: 'Now',
    unread: false,
  };

  const handleSendMessage = async (e?: React.FormEvent) => {
    if (e) e.preventDefault();
    const text = messageText.trim();
    if (!text) return;

    setMessageText('');
    const newMsg = await messageService.sendMessage(activeThreadId, text);
    setMessages((prev) => [...prev, newMsg]);

    // Update last message in active thread
    setThreads((prev) => prev.map((t) => t.id === activeThreadId ? { ...t, lastMessage: text, time: 'Just now', unread: false } : t));
  };

  const filteredThreads = threads.filter((t) =>
    t.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    t.lastMessage.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="messages-page">
      <main className="messages-main">
        <div className="messages-header">
          <h1 className="messages-title">Messages</h1>
          <p className="messages-sub">Direct encrypted messaging with clients and freelancers.</p>
        </div>

        <div className="messages-shell">
          {/* Sidebar */}
          <div className="messages-sidebar">
            <div className="messages-sidebar-search">
              <Search size={14} />
              <input
                type="text"
                placeholder="Search conversations..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="messages-search-input"
              />
            </div>
            <div className="messages-threads">
              {loadingThreads ? (
                <div className="p-8 flex flex-col items-center justify-center gap-2">
                  <Loader2 className="w-5 h-5 text-primary animate-spin" />
                  <span className="text-xs text-mute">Loading chats...</span>
                </div>
              ) : filteredThreads.map((t) => (
                <div
                  key={t.id}
                  onClick={() => setActiveThreadId(t.id)}
                  className={`messages-thread${activeThreadId === t.id ? ' active' : ''}`}
                >
                  <img src={t.avatar} alt={t.name} className="messages-thread-avatar" />
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                      <p className="messages-thread-name">{t.name}</p>
                      <span className="messages-thread-time">{t.time}</span>
                    </div>
                    <p className="messages-thread-preview">{t.lastMessage}</p>
                  </div>
                  {t.unread && <span className="messages-unread-dot" />}
                </div>
              ))}
            </div>
          </div>

          {/* Chat pane */}
          <div className="messages-pane">
            <div className="messages-pane-header">
              <img src={active.avatar} alt={active.name} className="messages-thread-avatar" />
              <div>
                <p className="messages-pane-name">
                  {active.name}
                  <span className="messages-verified"><ShieldCheck size={11} /> Verified</span>
                </p>
                <p className="messages-pane-role">{active.role} · Active escrow lock</p>
              </div>
            </div>

            <div className="messages-body">
              {loadingMessages ? (
                <div className="h-full flex items-center justify-center">
                  <Loader2 className="w-6 h-6 text-primary animate-spin" />
                </div>
              ) : (
                messages.map((m) => (
                  <div key={m.id} className={`msg-bubble-wrap${m.outgoing ? ' outgoing' : ''}`}>
                    <div className={`msg-bubble${m.outgoing ? ' outgoing' : ' incoming'}`}>
                      {m.text}
                      <span className="msg-time">{m.time}</span>
                    </div>
                  </div>
                ))
              )}
            </div>

            <form onSubmit={handleSendMessage} className="messages-composer">
              <button type="button" className="messages-attach-btn"><Paperclip size={15} /></button>
              <input
                type="text"
                value={messageText}
                onChange={(e) => setMessageText(e.target.value)}
                placeholder="Type a message... (Press Enter to send)"
                className="messages-composer-input"
              />
              <button type="submit" className="messages-send-btn"><Send size={14} /> Send</button>
            </form>
          </div>
        </div>
      </main>
    </div>
  );
};
