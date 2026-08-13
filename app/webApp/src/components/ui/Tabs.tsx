import React from 'react';

export interface TabItem<T extends string> {
  id: T;
  label: string;
  icon?: React.ComponentType<{ className?: string }>;
}

interface TabsProps<T extends string> {
  tabs: TabItem<T>[];
  activeTab: T;
  onChange: (id: T) => void;
  orientation?: 'horizontal' | 'vertical';
}

/**
 * Green Deck Pill Tab Navigation Component
 * Adheres strictly to green-deck-DESIGN.md:
 * - Pill-shaped (rounded-full / 9999px radius) for horizontal, rounded-xl for vertical sidebar
 * - Level 2 surface background (bg-surface-elevated) for container & inactive tabs
 * - Selected state: High contrast active accent (bg-brand-green text-white)
 */
export function Tabs<T extends string>({
  tabs,
  activeTab,
  onChange,
  orientation = 'horizontal',
}: TabsProps<T>) {
  if (orientation === 'vertical') {
    return (
      <nav className="flex flex-col p-2 rounded-2xl bg-surface-elevated border border-hairline gap-1.5 w-full">
        {tabs.map((tab) => {
          const Icon = tab.icon;
          const isActive = activeTab === tab.id;
          return (
            <button
              key={tab.id}
              onClick={() => onChange(tab.id)}
              className={`flex items-center gap-3 px-4 py-3 rounded-xl text-xs font-bold uppercase tracking-[0.05em] transition-all duration-200 w-full text-left ${
                isActive
                  ? 'bg-brand-green text-white shadow-md shadow-brand-green/20'
                  : 'text-mute hover:text-ink hover:bg-surface-modal'
              }`}
            >
              {Icon && <Icon className={`w-4 h-4 shrink-0 ${isActive ? 'text-white' : 'text-mute'}`} />}
              <span className="truncate">{tab.label}</span>
            </button>
          );
        })}
      </nav>
    );
  }

  return (
    <div className="inline-flex p-1.5 rounded-full bg-surface-elevated border border-hairline gap-1.5 overflow-x-auto max-w-full no-scrollbar">
      {tabs.map((tab) => {
        const Icon = tab.icon;
        const isActive = activeTab === tab.id;
        return (
          <button
            key={tab.id}
            onClick={() => onChange(tab.id)}
            className={`inline-flex items-center gap-2 px-5 py-2.5 rounded-full text-xs font-bold uppercase tracking-[0.05em] transition-all duration-200 whitespace-nowrap ${
              isActive
                ? 'bg-brand-green text-white shadow-md shadow-brand-green/20 scale-[1.02]'
                : 'text-mute hover:text-ink hover:bg-surface-modal'
            }`}
          >
            {Icon && <Icon className={`w-4 h-4 ${isActive ? 'text-white' : 'text-mute'}`} />}
            {tab.label}
          </button>
        );
      })}
    </div>
  );
}
