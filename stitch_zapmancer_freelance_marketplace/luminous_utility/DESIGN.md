---
name: Luminous Utility
colors:
  surface: '#faf8ff'
  surface-dim: '#d2d9f4'
  surface-bright: '#faf8ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f3ff'
  surface-container: '#eaedff'
  surface-container-high: '#e2e7ff'
  surface-container-highest: '#dae2fd'
  on-surface: '#131b2e'
  on-surface-variant: '#434655'
  inverse-surface: '#283044'
  inverse-on-surface: '#eef0ff'
  outline: '#737686'
  outline-variant: '#c3c6d7'
  surface-tint: '#0053db'
  primary: '#004ac6'
  on-primary: '#ffffff'
  primary-container: '#2563eb'
  on-primary-container: '#eeefff'
  inverse-primary: '#b4c5ff'
  secondary: '#006e2f'
  on-secondary: '#ffffff'
  secondary-container: '#6bff8f'
  on-secondary-container: '#007432'
  tertiary: '#525657'
  on-tertiary: '#ffffff'
  tertiary-container: '#6b6e70'
  on-tertiary-container: '#eff1f3'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dbe1ff'
  primary-fixed-dim: '#b4c5ff'
  on-primary-fixed: '#00174b'
  on-primary-fixed-variant: '#003ea8'
  secondary-fixed: '#6bff8f'
  secondary-fixed-dim: '#4ae176'
  on-secondary-fixed: '#002109'
  on-secondary-fixed-variant: '#005321'
  tertiary-fixed: '#e0e3e5'
  tertiary-fixed-dim: '#c4c7c9'
  on-tertiary-fixed: '#191c1e'
  on-tertiary-fixed-variant: '#444749'
  background: '#faf8ff'
  on-background: '#131b2e'
  surface-variant: '#dae2fd'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  title-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.05em
  display-lg-mobile:
    fontFamily: Inter
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 36px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  gutter: 16px
  margin-mobile: 16px
  margin-desktop: 48px
---

## Brand & Style
This design system establishes a high-trust, professional aesthetic that bridges the gap between traditional fintech reliability and modern neobank agility. The visual language is defined by a "Luminous Utility" approach: it prioritizes clarity and information density while maintaining a light, airy, and premium feel.

The personality is approachable yet precise. It avoids the playfulness of consumer-only apps in favor of a sophisticated, structured environment. By utilizing heavy whitespace, crisp borders, and soft depth, the UI evokes an emotional response of financial clarity and effortless control.

## Colors
The palette is centered on a high-contrast light mode foundation. The primary **Utility Blue (#2563EB)** is reserved for core actions and active navigation states, signaling professional-grade tools. 

- **Backgrounds:** A soft off-white (`#F8FAFC`) or very light gray (`#F1F5F9`) serves as the base layer to reduce eye strain compared to pure white.
- **Surfaces:** Pure white (`#FFFFFF`) is used for elevated cards and containers to create a clear layered hierarchy.
- **Accents:** **Success Green (#22C55E)** is utilized for positive financial indicators, balance growth, and completion states.
- **Typography:** Deep Navy (`#0F172A`) ensures maximum readability for body text, with Slate (`#64748B`) used for secondary information.

## Typography
The system uses **Inter** exclusively to leverage its systematic, utilitarian nature. To maintain the professional neobank aesthetic, font weights are used strategically to create hierarchy without relying on excessive color shifts.

Headlines use semi-bold and bold weights with tight letter-spacing for a modern, compact look. Body text stays strictly at a 14px or 16px baseline for high-density functional layouts, ensuring complex data remains legible. Labels and captions use medium weights and slight tracking to differentiate them from interactive body elements.

## Layout & Spacing
The layout follows a high-density functional model designed for efficient data consumption. It employs a 12-column fluid grid for desktop and a single-column fluid system for mobile with 16px margins.

Spacing is strictly based on a 4px baseline. Vertical rhythm is tight (16px between related items, 24px-32px between sections) to allow more information to be visible above the fold. Content is organized into clear logical groups using white space rather than heavy dividers.

## Elevation & Depth
Hierarchy is established through a combination of **soft ambient shadows** and **low-contrast outlines**. 

- **Level 0 (Base):** Off-white background.
- **Level 1 (Cards/Lists):** Pure white surface, a 1px border in `#E2E8F0`, and a subtle drop shadow (0px 4px 6px -1px rgba(0, 0, 0, 0.05)).
- **Level 2 (Overlays/Modals):** Pure white surface with a more pronounced, diffused shadow to indicate significant separation from the base layer.

This "flat-plus" approach ensures the UI feels tactile and layered without the heavy artificiality of traditional skeuomorphism.

## Shapes
The shape language is consistently rounded to soften the professional tone. A standard radius of **12px to 16px** (Rounded-LG/XL) is applied to all primary containers and cards. 

Interactive elements like buttons and inputs use an **8px** radius for a slightly sharper, more "tool-like" feel. Pill shapes are reserved exclusively for status indicators (chips) and the primary mobile navigation bar to maintain a modern, friendly touchpoint.

## Components
- **Buttons:** Primary buttons use a solid Blue (#2563EB) fill with white text. Secondary buttons use a light blue tint or a simple 1px border. All buttons have a height of 44-48px for touch-friendly interaction.
- **Cards:** White backgrounds, 16px corner radius, 1px light slate borders, and soft shadows. Content inside cards should follow a 16px internal padding.
- **Input Fields:** 1px `#CBD5E1` borders that transition to Blue on focus. Labels sit clearly above the field in a medium-weight small font.
- **Chips/Badges:** Use a pill-shape with 10% opacity fills of the status color (e.g., light green background with dark green text for "Success").
- **Lists:** Transactional lists should use a horizontal layout with 12px spacing between the icon, description, and value. Use thin dividers (`#F1F5F9`) only between dissimilar items.
- **Navigation:** A bottom navigation bar on mobile with clear active states indicated by a primary blue icon and label.