# Theme and Design System Invariants
- Always use CSS variables (`var(--background)`, `var(--foreground)`, `var(--primary)`, `var(--glass-bg)`, `var(--glass-border)`) mapped via Tailwind for color utilities.
- Never hardcode fixed hex colors (`#ffffff` or `#000000`) for text or background elements.
- All glassmorphism components must strictly use `backdrop-blur-md` and maintain a minimum contrast ratio of 4.5:1 on both Light (Day/Bridal) and Dark (Night/Gala) themes.
