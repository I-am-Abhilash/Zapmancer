import type { Config } from 'tailwindcss';

/**
 * ScriptSide single source of truth for the design system.
 * Mirrors docs/DESIGN.md (Nike commerce system) verbatim.
 * Runtime values live in src/index.css :root block; this file defines
 * the canonical TOKEN NAMES + VALUES. Drift check via
 * npm run lint:tokens (scripts/check-token-drift.mjs).
 */
const config: Config = {
  content: [
    './index.html',
    './src/**/*.{ts,tsx}',
  ],
  // data-theme attribute swap on <html> (handled by ThemeContext.tsx).
  darkMode: ['selector', '[data-theme="dark"]'],
  theme: {
    extend: {
      colors: {
        // Canvas / surface
        canvas:          'var(--color-canvas)',
        'soft-cloud':    'var(--color-soft-cloud)',
        'surface-hover': 'var(--color-surface-hover)',
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
        // Category accents
        'accent-pink':        'var(--color-accent-pink)',
        'accent-pink-soft':   'var(--color-accent-pink-soft)',
        'accent-pink-deep':   'var(--color-accent-pink-deep)',
        'accent-purple-soft': 'var(--color-accent-purple-soft)',
        'accent-purple-pale': 'var(--color-accent-purple-pale)',
        'accent-teal':        'var(--color-accent-teal)',
        // Convenience on-primary (DESIGN.md: single hard value, not theme-dependent)
        'on-primary': '#ffffff',
      },
      fontFamily: {
        sans:    ['Inter', '-apple-system', 'BlinkMacSystemFont', 'sans-serif'],
        // Display tier — DESIGN.md: Nike Futura ND, free fallback = Bebas Neue
        display: ['"Bebas Neue"', 'Inter', 'sans-serif'],
        serif:   ['Newsreader', 'Georgia', 'serif'], // ScriptSide legacy serif
      },
      borderRadius: {
        none: '0px',
        sm:   '18px', // DESIGN.md (was 8px legacy ScriptSide)
        md:   '24px', // DESIGN.md (was 16px legacy ScriptSide)
        lg:   '30px',
        full: '9999px',
      },
      spacing: {
        xxs: '2px',
        xs:  '4px',
        sm:  '8px',
        md:  '12px',
        lg:  '18px',
        xl:  '24px',
        xxl: '30px',
        section: '48px',
      },
      letterSpacing: {
        tight:  '-0.025em',
        tighter: '-0.04em',
      },
      maxWidth: {
        page: '1180px',
      },
    },
  },
  // Disable Tailwind's preflight reset to avoid colliding with the
  // existing index.css reset rules. Re-enable after a full migration.
  corePlugins: {
    preflight: false,
  },
  plugins: [],
};

export default config;
