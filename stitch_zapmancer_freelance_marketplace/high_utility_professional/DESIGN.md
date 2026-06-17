---
name: High-Utility Professional
colors:
  surface: '#11131b'
  surface-dim: '#11131b'
  surface-bright: '#373942'
  surface-container-lowest: '#0c0e16'
  surface-container-low: '#191b23'
  surface-container: '#1d1f27'
  surface-container-high: '#282a32'
  surface-container-highest: '#32343d'
  on-surface: '#e1e2ed'
  on-surface-variant: '#c3c6d7'
  inverse-surface: '#e1e2ed'
  inverse-on-surface: '#2e3039'
  outline: '#8d90a0'
  outline-variant: '#434655'
  surface-tint: '#b4c5ff'
  primary: '#b4c5ff'
  on-primary: '#002a78'
  primary-container: '#2563eb'
  on-primary-container: '#eeefff'
  inverse-primary: '#0053db'
  secondary: '#adc6ff'
  on-secondary: '#002e6a'
  secondary-container: '#0566d9'
  on-secondary-container: '#e6ecff'
  tertiary: '#ffb596'
  on-tertiary: '#581e00'
  tertiary-container: '#bc4800'
  on-tertiary-container: '#ffede6'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#dbe1ff'
  primary-fixed-dim: '#b4c5ff'
  on-primary-fixed: '#00174b'
  on-primary-fixed-variant: '#003ea8'
  secondary-fixed: '#d8e2ff'
  secondary-fixed-dim: '#adc6ff'
  on-secondary-fixed: '#001a42'
  on-secondary-fixed-variant: '#004395'
  tertiary-fixed: '#ffdbcd'
  tertiary-fixed-dim: '#ffb596'
  on-tertiary-fixed: '#360f00'
  on-tertiary-fixed-variant: '#7d2d00'
  background: '#11131b'
  on-background: '#e1e2ed'
  surface-variant: '#32343d'
typography:
  display:
    fontFamily: Inter
    fontSize: 30px
    fontWeight: '600'
    lineHeight: 38px
    letterSpacing: -0.02em
  h1:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.015em
  h2:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
    letterSpacing: -0.01em
  h3:
    fontFamily: Inter
    fontSize: 16px
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
  body-sm:
    fontFamily: Inter
    fontSize: 13px
    fontWeight: '400'
    lineHeight: 18px
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.01em
  mono:
    fontFamily: jetbrainsMono
    fontSize: 13px
    fontWeight: '400'
    lineHeight: 20px
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  base: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  container-max: 1280px
  gutter: 16px
---

## Brand & Style
The design system is engineered for high-density, mission-critical workflows where clarity and speed are paramount. Drawing inspiration from industry-leading developer tools and financial dashboards, the aesthetic is rooted in **Modern Corporate Minimalism**. 

The brand voice is authoritative, neutral, and precise. It avoids visual noise—eliminating gradients, blurs, and decorative shadows—to ensure the user remains focused on the data and the task at hand. The interface should feel like a high-performance instrument: reliable, responsive, and systematic.

## Colors
The palette is strictly functional. A deep, neutral slate serves as the foundation to maintain a professional dark mode that avoids the "gaming" aesthetic of pure black or neon-tinted grays.

- **Primary (#2563EB):** Reserved exclusively for high-intent actions, progress indicators, and active navigation states.
- **Neutral Scale:** Uses a balanced slate-gray scale. Surfaces use `#0F172A`, while the base background is a darker `#020617` to create natural depth without shadows.
- **Status:** Standard utility colors (Success: `#10B981`, Warning: `#F59E0B`, Error: `#EF4444`) are used sparingly in small-scale UI elements like pips or subtle text.

## Typography
The system utilizes **Inter** for all UI text to maximize legibility at small sizes. The hierarchy is intentionally flat; headings are distinguished by weight rather than excessive scale to maintain information density.

For technical data, code snippets, or ID strings, **JetBrains Mono** is employed to provide clear character distinction. Letter spacing is slightly tightened on headings for a more cohesive, "locked-in" professional look.

## Layout & Spacing
This design system uses a strict **4px baseline grid** to ensure mathematical consistency. Layouts should prioritize a "Sidebar + Main Content" or "Header + Triple Column" structure typical of complex SaaS applications.

- **Density:** Padding is kept tight (8px-12px in list items, 16px in cards) to allow more data on screen.
- **Grid:** A 12-column fluid grid is used for main dashboard views, while settings and forms utilize a fixed-width max-container of 800px to ensure line-length readability.
- **Alignment:** All elements must snap to the 4px increments. Internal component spacing (e.g., icon to text) is consistently 8px.

## Elevation & Depth
Depth is achieved through **Tonal Layering** rather than shadows. 
- **Level 0 (Background):** `#020617` — The lowest layer.
- **Level 1 (Cards/Sidebar):** `#0F172A` — The primary workspace surface.
- **Level 2 (Popovers/Modals):** `#1E293B` — Higher contrast surfaces to indicate temporary interaction layers.

Borders are the primary tool for separation. Use 1px solid borders in `#1E293B` for all containers. No drop shadows should be used except for high-level floating menus, where a minimal, 10% black, non-blurred shadow may be used to provide a "lifted" edge.

## Shapes
Shapes are disciplined and "standard." The system uses a **6px to 8px radius** for almost all UI components, striking a balance between modern friendliness and professional rigidity. 

Inputs, buttons, and cards all share the same radius to create a unified structural language. Large-scale containers (like the main app viewport) may remain sharp or use the standard radius.

## Components
- **Buttons:** 
  - *Primary:* Solid `#2563EB` with white text. No gradients.
  - *Secondary:* Transparent background with a 1px `#1E293B` border and white text.
  - *Ghost:* No border or background until hover.
- **Inputs:** Dark background (`#020617`) with a 1px border. Focus state uses a 1px `#2563EB` border and a subtle 2px outer glow of the same color (0.2 opacity).
- **Chips/Badges:** Small, low-contrast rectangles with 4px radius. Use `#1E293B` background with slightly dimmed text for secondary info.
- **Lists:** Data rows should have a 1px bottom border. Hover states should use a subtle background shift to `#1E293B`.
- **Cards:** No "floating" appearance. Cards are defined by their 1px `#1E293B` border and flat background color.
- **Data Tables:** High-density, no cell borders, only row borders. Header row should use `label-md` typography in a muted gray.