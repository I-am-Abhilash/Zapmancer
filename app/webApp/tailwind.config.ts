import type { Config } from 'tailwindcss';

/**
 * Notion Design System Configuration
 * Canonical specification: notion-DESIGN.md
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
        // Canvas & surface
        canvas:             'var(--color-canvas)',
        surface:            'var(--color-surface)',
        'surface-elevated': 'var(--color-surface-elevated)',
        'surface-modal':    'var(--color-surface-modal)',
        'surface-hover':    'var(--color-surface-hover)',
        
        // Notion Brand Accent Tokens
        primary:             'var(--color-primary)',
        'primary-hover':     'var(--color-primary-hover)',
        'primary-deep':      'var(--color-primary-deep)',
        'brand-green':       'var(--color-primary)', // Mapped to Primary Accent (#5645d4) for seamless theme consistency
        'brand-green-hover': 'var(--color-primary-hover)',
        'brand-navy':        'var(--color-brand-navy)',
        'brand-navy-deep':   'var(--color-brand-navy-deep)',
        'brand-navy-mid':    'var(--color-brand-navy-mid)',
        'link-blue':         'var(--color-link-blue)',

        // Pastel Card Tints (notion-DESIGN.md)
        'tint-peach':    'var(--color-card-tint-peach)',
        'tint-rose':     'var(--color-card-tint-rose)',
        'tint-mint':     'var(--color-card-tint-mint)',
        'tint-lavender': 'var(--color-card-tint-lavender)',
        'tint-sky':      'var(--color-card-tint-sky)',
        'tint-yellow':   'var(--color-card-tint-yellow)',
        'tint-gray':     'var(--color-card-tint-gray)',

        // Ink & Text scale
        ink:       'var(--color-ink)',
        'ink-deep':'var(--color-ink-deep)',
        charcoal:  'var(--color-charcoal)',
        slate:     'var(--color-slate)',
        steel:     'var(--color-steel)',
        mute:      'var(--color-mute)',
        stone:     'var(--color-stone)',
        hairline:  'var(--color-hairline)',
        'hairline-soft': 'var(--color-hairline-soft)',
        'hairline-strong': 'var(--color-hairline-strong)',

        // Semantics
        success:     'var(--color-success)',
        warning:     'var(--color-warning)',
        error:       'var(--color-error)',
        info:        'var(--color-info)',
        'on-primary': '#ffffff',
      },
      fontFamily: {
        sans: ['"Inter"', '-apple-system', 'BlinkMacSystemFont', 'sans-serif'],
        display: ['"Inter"', 'sans-serif'],
        mono: ['"JetBrains Mono"', 'monospace'],
      },
      borderRadius: {
        none: '0px',
        xs:   '4px',
        sm:   '6px',
        md:   '8px',
        lg:   '12px',
        xl:   '16px',
        xxl:  '20px',
        full: '9999px',
      },
      spacing: {
        xxs: '4px',
        xs:  '8px',
        sm:  '12px',
        md:  '16px',
        lg:  '20px',
        xl:  '24px',
        xxl: '32px',
        hero: '120px',
      },
    },
  },
  corePlugins: {
    preflight: false,
  },
  plugins: [],
};

export default config;
