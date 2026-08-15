import React, { useState } from 'react';
import './messages.css';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Search, Send, Paperclip, ShieldCheck } from 'lucide-react';

const THREADS = [
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

const MESSAGES = [
  { id: 'm1', text: 'Hi Alex, we reviewed your Compose Multiplatform PR submission. The layout looks sharp and test coverage passes!', time: '10:40 AM', outgoing: false },
  { id: 'm2', text: 'Thanks! We also included SQLDelight offline database caching for low latency startup.', time: '10:42 AM', outgoing: true },
];

export const MessagesPage: React.FC = () => {
  const [activeThread, setActiveThread] = useState('t1');
  const [messageText, setMessageText] = useState('');

  const active = THREADS.find((t) => t.id === activeThread) || THREADS[0];

  return (
    <div className="messages-page">
      <Header isLoggedIn={true} />

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
              <input type="text" placeholder="Search conversations..." className="messages-search-input" />
            </div>
            <div className="messages-threads">
              {THREADS.map((t) => (
                <div
                  key={t.id}
                  onClick={() => setActiveThread(t.id)}
                  className={`messages-thread${activeThread === t.id ? ' active' : ''}`}
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
              {MESSAGES.map((m) => (
                <div key={m.id} className={`msg-bubble-wrap${m.outgoing ? ' outgoing' : ''}`}>
                  <div className={`msg-bubble${m.outgoing ? ' outgoing' : ' incoming'}`}>
                    {m.text}
                    <span className="msg-time">{m.time}</span>
                  </div>
                </div>
              ))}
            </div>

            <div className="messages-composer">
              <button className="messages-attach-btn"><Paperclip size={15} /></button>
              <input
                type="text"
                value={messageText}
                onChange={(e) => setMessageText(e.target.value)}
                placeholder="Type a message..."
                className="messages-composer-input"
              />
              <button className="messages-send-btn"><Send size={14} /> Send</button>
            </div>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
};
