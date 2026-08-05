import React, { useState } from 'react';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { Search, Send, Paperclip, CheckCheck, MoreVertical, ShieldCheck, Phone, Video, Smile } from 'lucide-react';

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
    <div className="min-h-screen flex flex-col bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 transition-colors">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-6">
        
        <div className="bg-white dark:bg-slate-900 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-xl overflow-hidden grid grid-cols-1 md:grid-cols-3 h-[750px]">
          
          {/* Left Column: Conversations List */}
          <div className="border-r border-slate-200 dark:border-slate-800 flex flex-col h-full bg-slate-50/50 dark:bg-slate-900/50">
            
            {/* Header & Search */}
            <div className="p-4 border-b border-slate-200 dark:border-slate-800 space-y-3">
              <h2 className="font-extrabold text-xl tracking-tight">Direct Messages</h2>
              <div className="relative">
                <Search className="w-4 h-4 text-slate-400 absolute left-3 top-3" />
                <input
                  type="text"
                  placeholder="Search conversations..."
                  className="w-full pl-9 pr-3 py-2 text-xs rounded-xl border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-800 focus:ring-2 focus:ring-indigo-500"
                />
              </div>
            </div>

            {/* Conversation Threads */}
            <div className="flex-1 overflow-y-auto divide-y divide-slate-100 dark:divide-slate-800">
              {conversations.map((c) => (
                <div
                  key={c.id}
                  onClick={() => setActiveThreadId(c.id)}
                  className={`p-4 flex items-center gap-3 cursor-pointer transition-all ${
                    activeThreadId === c.id
                      ? 'bg-indigo-50 dark:bg-indigo-950/40 border-l-4 border-indigo-600'
                      : 'hover:bg-slate-100 dark:hover:bg-slate-800/60'
                  }`}
                >
                  <div className="relative">
                    <img src={c.avatar} alt={c.name} className="w-11 h-11 rounded-2xl object-cover" />
                    {c.online && (
                      <span className="absolute bottom-0 right-0 w-3 h-3 bg-emerald-500 rounded-full border-2 border-white dark:border-slate-900"></span>
                    )}
                  </div>

                  <div className="flex-1 min-w-0">
                    <div className="flex items-center justify-between">
                      <h4 className="font-bold text-xs text-slate-900 dark:text-white truncate">{c.name}</h4>
                      <span className="text-[10px] text-slate-400">{c.time}</span>
                    </div>
                    <p className="text-xs text-slate-500 dark:text-slate-400 truncate mt-0.5">{c.lastMessage}</p>
                  </div>

                  {c.unread > 0 && (
                    <span className="w-5 h-5 bg-indigo-600 text-white rounded-full text-[10px] font-extrabold flex items-center justify-center">
                      {c.unread}
                    </span>
                  )}
                </div>
              ))}
            </div>

          </div>

          {/* Right Column: Active Chat Window */}
          <div className="md:col-span-2 flex flex-col h-full bg-white dark:bg-slate-900">
            
            {/* Active Thread Header */}
            <div className="p-4 border-b border-slate-200 dark:border-slate-800 flex items-center justify-between">
              <div className="flex items-center gap-3">
                <img src={activeThread.avatar} alt={activeThread.name} className="w-10 h-10 rounded-xl object-cover" />
                <div>
                  <h3 className="font-bold text-sm text-slate-900 dark:text-white flex items-center gap-1.5">
                    {activeThread.name}
                    <ShieldCheck className="w-4 h-4 text-emerald-500" />
                  </h3>
                  <p className="text-xs text-slate-400">{activeThread.role}</p>
                </div>
              </div>

              <div className="flex items-center gap-2 text-slate-400">
                <button className="p-2 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-lg"><Phone className="w-4 h-4" /></button>
                <button className="p-2 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-lg"><Video className="w-4 h-4" /></button>
              </div>
            </div>

            {/* Message Feed */}
            <div className="flex-1 p-6 overflow-y-auto space-y-4 bg-slate-50/30 dark:bg-slate-950/20">
              {messages.map((m) => (
                <div
                  key={m.id}
                  className={`flex flex-col ${m.isMe ? 'items-end' : 'items-start'}`}
                >
                  <div
                    className={`max-w-md p-4 rounded-2xl text-sm leading-relaxed ${
                      m.isMe
                        ? 'bg-indigo-600 text-white rounded-br-none shadow-md shadow-indigo-500/10'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-800 dark:text-slate-200 rounded-bl-none'
                    }`}
                  >
                    {m.text}
                  </div>
                  <span className="text-[10px] text-slate-400 mt-1 flex items-center gap-1">
                    {m.time} {m.isMe && <CheckCheck className="w-3 h-3 text-indigo-400" />}
                  </span>
                </div>
              ))}
            </div>

            {/* Input Composer */}
            <form onSubmit={handleSendMessage} className="p-4 border-t border-slate-200 dark:border-slate-800 flex items-center gap-3">
              <button type="button" className="p-2.5 text-slate-400 hover:text-slate-600 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800">
                <Paperclip className="w-5 h-5" />
              </button>

              <input
                type="text"
                placeholder="Type a message or discuss milestone requirements..."
                value={inputMessage}
                onChange={(e) => setInputMessage(e.target.value)}
                className="flex-1 px-4 py-3 rounded-xl border border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-800 text-sm focus:ring-2 focus:ring-indigo-500 font-medium"
              />

              <button
                type="submit"
                className="p-3 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl shadow-md shadow-indigo-500/20 transition-all"
              >
                <Send className="w-5 h-5" />
              </button>
            </form>

          </div>

        </div>

      </main>

      <Footer />
    </div>
  );
};
