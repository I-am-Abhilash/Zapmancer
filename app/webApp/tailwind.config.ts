import type { Config } from 'tailwindcss';

/**
 * Green Deck single source of truth for the design system.
 * Mirrors green-deck-DESIGN.md verbatim.
 */
const config: Config = {
  content: [
    './index.html',
    './src/**/*.{ts,tsx}',
  ],
  darkMode: ['selector', '[data-theme="dark"]'],
  theme: {
    extend: {
      colors: {
        // Canvas / surface
        canvas:             'var(--color-canvas)',
        surface:            'var(--color-surface)',
        'surface-elevated': 'var(--color-surface-elevated)',
        'surface-modal':    'var(--color-surface-modal)',
        'soft-cloud':       'var(--color-soft-cloud)',
        'surface-hover':    'var(--color-surface-hover)',
        // Green Deck Brand System Tokens
        'brand-green':       'var(--color-brand-green)',
        'brand-green-hover': 'var(--color-brand-green-hover)',
        // Text scale
        ink:       'var(--color-ink)',
        charcoal:  'var(--color-charcoal)',
        ash:       'var(--color-ash)',
        mute:      'var(--color-mute)',
        stone:     'var(--color-stone)',
        hairline:  'var(--color-hairline)',
        'hairline-soft': 'var(--color-hairline-soft)',
        // Semantic
        sale:        'var(--color-sale)',
        'sale-deep': 'var(--color-sale-deep)',
        success:     'var(--color-success)',
        'success-bright': 'var(--color-success-bright)',
        info:        'var(--color-info)',
        'info-deep': 'var(--color-info-deep)',
        'on-primary': '#ffffff',
      },
      fontFamily: {
        sans: ['"DM Sans"', '-apple-system', 'BlinkMacSystemFont', 'sans-serif'],
        display: ['"DM Sans"', 'sans-serif'],
        mono: ['"JetBrains Mono"', 'monospace'],
      },
      borderRadius: {
        none: '0px',
        sm:   '8px',
        md:   '12px',
        lg:   '16px',
        full: '9999px',
      },
      spacing: {
        xxs: '4px',
        xs:  '8px',
        sm:  '12px',
        md:  '16px',
        lg:  '24px',
        xl:  '32px',
        xxl: '48px',
        section: '64px',
      },
    },
  },
  corePlugins: {
    preflight: false,
  },
  plugins: [],
};

export default config;
