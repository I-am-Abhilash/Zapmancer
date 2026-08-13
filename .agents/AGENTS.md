# Workspace Rules & Styling Guidelines

## Central Design System & Dual Theme Support
1. **Central Design System Tokens**: All component modifications across the web application (`app/webApp`) and mobile screens (`app/sharedUI`) must adhere strictly to the central design system tokens (`index.css`, `tailwind.config.ts`, `green-deck-DESIGN.md`). Avoid un-themed hardcoded ad-hoc styles.
2. **Dual Theme Support (Light & Dark Modes)**: All user interface components, pages, cards, forms, and overlays must explicitly support both **Light Mode** and **Dark Mode** seamlessly (using Tailwind `dark:` variants or CSS design system variables).
3. **Reusable UI Component Enforcement**: All tab bars, chips, buttons, inputs, and cards across `webApp` must consume centralized reusable UI components (e.g., `src/components/ui/Tabs.tsx`) to guarantee strict compliance with `green-deck-DESIGN.md` (pill-shaped 9999px radius, Level 2 surface background `bg-surface-elevated`, high-contrast active state).
