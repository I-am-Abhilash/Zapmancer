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
 * Notion Design System Pill & Segmented Tab Navigation Component
 * Adheres strictly to notion-DESIGN.md:
 * - Pill-tab: rounded-full, hairline border, active state (bg-primary text-white or bg-ink-deep text-on-dark)
 * - Vertical sidebar: rounded-lg container, rounded-md items with primary focus highlight
 */
export function Tabs<T extends string>({
  tabs,
  activeTab,
  onChange,
  orientation = 'horizontal',
}: TabsProps<T>) {
  if (orientation === 'vertical') {
    return (
      <nav className="flex flex-col p-2 rounded-xl bg-surface border border-hairline gap-1 w-full">
        {tabs.map((tab) => {
          const Icon = tab.icon;
          const isActive = activeTab === tab.id;
          return (
            <button
              key={tab.id}
              onClick={() => onChange(tab.id)}
              className={`flex items-center gap-3 px-3.5 py-2.5 rounded-lg text-xs sm:text-sm font-medium transition-all duration-150 w-full text-left cursor-pointer ${
                isActive
                  ? 'bg-primary text-white font-semibold shadow-sm'
                  : 'text-steel hover:text-ink hover:bg-surface-elevated'
              }`}
            >
              {Icon && <Icon className={`w-4 h-4 shrink-0 ${isActive ? 'text-white' : 'text-steel'}`} />}
              <span className="truncate">{tab.label}</span>
            </button>
          );
        })}
      </nav>
    );
  }

  return (
    <div className="inline-flex p-1 rounded-full bg-surface border border-hairline gap-1 overflow-x-auto max-w-full no-scrollbar">
      {tabs.map((tab) => {
        const Icon = tab.icon;
        const isActive = activeTab === tab.id;
        return (
          <button
            key={tab.id}
            onClick={() => onChange(tab.id)}
            className={`inline-flex items-center gap-2 px-4 py-2 rounded-full text-xs sm:text-sm font-medium transition-all duration-150 whitespace-nowrap cursor-pointer ${
              isActive
                ? 'bg-primary text-white font-semibold shadow-sm'
                : 'text-steel hover:text-ink hover:bg-surface-elevated'
            }`}
          >
            {Icon && <Icon className={`w-4 h-4 ${isActive ? 'text-white' : 'text-steel'}`} />}
            {tab.label}
          </button>
        );
      })}
    </div>
  );
}
