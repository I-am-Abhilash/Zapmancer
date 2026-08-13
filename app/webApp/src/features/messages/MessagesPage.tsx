import React, { useState } from 'react';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Search, Send, Paperclip, CheckCheck, ShieldCheck, Phone, Video } from 'lucide-react';

export const MessagesPage: React.FC = () => {
  const [activeThreadId, setActiveThreadId] = useState('1');
  const [inputMessage, setInputMessage] = useState('');

  const conversations = [
    {
      id: '1',
      name: 'Sarah Jenkins (Acme AI)',
      role: 'Client & Tech Lead',
      avatar: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80',
      lastMessage: 'Awesome! We funded the $1,200 milestone in escrow.',
      time: '10:42 AM',
      unread: 2,
      online: true,
    },
    {
      id: '2',
      name: 'Marcus Vance',
      role: 'KMP Developer',
      avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
      lastMessage: 'I reviewed your PR for the Exposed ORM migration.',
      time: 'Yesterday',
      unread: 0,
      online: false,
    },
  ];

  const [messages, setMessages] = useState([
    { id: 'm1', sender: 'Sarah Jenkins', text: 'Hi Alex, thanks for submitting your proposal for the Compose Desktop app.', time: '10:30 AM', isMe: false },
    { id: 'm2', sender: 'Me', text: 'Hello Sarah! Glad to connect. I have pre-built KMP modules ready for the Ktor API integration.', time: '10:35 AM', isMe: true },
    { id: 'm3', sender: 'Sarah Jenkins', text: 'Awesome! We funded the $1,200 milestone in escrow.', time: '10:42 AM', isMe: false },
  ]);

  const activeThread = conversations.find((c) => c.id === activeThreadId) || conversations[0];

  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault();
    if (!inputMessage.trim()) return;

    setMessages([
      ...messages,
      {
        id: `m_${Date.now()}`,
        sender: 'Me',
        text: inputMessage,
        time: 'Just now',
        isMe: true,
      },
    ]);
    setInputMessage('');
  };

  return (
    <div className="min-h-screen flex flex-col bg-canvas text-ink transition-colors duration-200 antialiased font-sans">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-6">
        
        <div className="bg-surface rounded-xl border border-hairline shadow-sm dark:shadow-none overflow-hidden grid grid-cols-1 md:grid-cols-3 h-[750px]">
          
          {/* Left Column: Conversations List */}
          <div className="border-r border-hairline flex flex-col h-full bg-surface-elevated">
            
            {/* Header & Search */}
            <div className="p-4 border-b border-hairline space-y-3">
              <h2 className="font-extrabold text-xl tracking-tight text-ink">Direct Messages</h2>
              <div className="relative">
                <Search className="w-4 h-4 text-mute absolute left-3.5 top-3" />
                <input
                  type="text"
                  placeholder="Search conversations..."
                  className="w-full pl-9 pr-3 py-2 text-xs rounded-full border border-hairline bg-surface text-ink focus:outline-none focus:border-brand-green placeholder:text-mute"
                />
              </div>
            </div>

            {/* Conversation Threads */}
            <div className="flex-1 overflow-y-auto divide-y divide-hairline">
              {conversations.map((c) => (
                <div
                  key={c.id}
                  onClick={() => setActiveThreadId(c.id)}
                  className={`p-4 flex items-center gap-3 cursor-pointer transition-all ${
                    activeThreadId === c.id
                      ? 'bg-surface border-l-4 border-brand-green'
                      : 'hover:bg-surface'
                  }`}
                >
                  <div className="relative">
                    <img src={c.avatar} alt={c.name} className="w-11 h-11 rounded-full object-cover border border-hairline" />
                    {c.online && (
                      <span className="absolute bottom-0 right-0 w-3 h-3 bg-brand-green rounded-full border-2 border-surface"></span>
                    )}
                  </div>

                  <div className="flex-1 min-w-0">
                    <div className="flex items-center justify-between">
                      <h4 className="font-bold text-xs text-ink truncate">{c.name}</h4>
                      <span className="text-[10px] text-mute">{c.time}</span>
                    </div>
                    <p className="text-xs text-mute truncate mt-0.5">{c.lastMessage}</p>
                  </div>

                  {c.unread > 0 && (
                    <span className="w-5 h-5 bg-brand-green text-white rounded-full text-[10px] font-extrabold flex items-center justify-center">
                      {c.unread}
                    </span>
                  )}
                </div>
              ))}
            </div>

          </div>

          {/* Right Column: Active Chat Window */}
          <div className="md:col-span-2 flex flex-col h-full bg-surface">
            
            {/* Active Thread Header */}
            <div className="p-4 border-b border-hairline flex items-center justify-between">
              <div className="flex items-center gap-3">
                <img src={activeThread.avatar} alt={activeThread.name} className="w-10 h-10 rounded-full object-cover border border-hairline" />
                <div>
                  <h3 className="font-bold text-sm text-ink flex items-center gap-1.5">
                    {activeThread.name}
                    <ShieldCheck className="w-4 h-4 text-brand-green" />
                  </h3>
                  <p className="text-xs text-mute">{activeThread.role}</p>
                </div>
              </div>

              <div className="flex items-center gap-2 text-mute">
                <button className="p-2 hover:bg-surface-elevated hover:text-ink rounded-full transition-colors"><Phone className="w-4 h-4" /></button>
                <button className="p-2 hover:bg-surface-elevated hover:text-ink rounded-full transition-colors"><Video className="w-4 h-4" /></button>
              </div>
            </div>

            {/* Message Feed */}
            <div className="flex-1 p-6 overflow-y-auto space-y-4 bg-canvas">
              {messages.map((m) => (
                <div
                  key={m.id}
                  className={`flex flex-col ${m.isMe ? 'items-end' : 'items-start'}`}
                >
                  <div
                    className={`max-w-md p-4 rounded-xl text-sm leading-relaxed ${
                      m.isMe
                        ? 'bg-brand-green text-white rounded-br-none shadow-md shadow-brand-green/10'
                        : 'bg-surface-elevated text-ink border border-hairline rounded-bl-none'
                    }`}
                  >
                    {m.text}
                  </div>
                  <span className="text-[10px] text-mute mt-1 flex items-center gap-1">
                    {m.time} {m.isMe && <CheckCheck className="w-3 h-3 text-brand-green" />}
                  </span>
                </div>
              ))}
            </div>

            {/* Input Composer */}
            <form onSubmit={handleSendMessage} className="p-4 border-t border-hairline flex items-center gap-3 bg-surface">
              <button type="button" className="p-2.5 text-mute hover:text-ink rounded-full hover:bg-surface-elevated transition-colors">
                <Paperclip className="w-5 h-5 text-brand-green" />
              </button>

              <input
                type="text"
                placeholder="Type a message or discuss milestone requirements..."
                value={inputMessage}
                onChange={(e) => setInputMessage(e.target.value)}
                className="flex-1 px-4 py-3 rounded-full border border-hairline bg-surface-elevated text-sm text-ink focus:outline-none focus:border-brand-green font-medium transition-colors"
              />

              <button
                type="submit"
                className="p-3 bg-brand-green hover:bg-brand-green-hover text-white rounded-full shadow-md shadow-brand-green/20 hover:scale-[1.04] transition-all"
              >
                <Send className="w-4 h-4" />
              </button>
            </form>

          </div>

        </div>

      </main>

      <Footer />
    </div>
  );
};
