---
name: Hobbify Narrative
colors:
  surface: '#f8f9fa'
  surface-dim: '#d9dadb'
  surface-bright: '#f8f9fa'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f3f4f5'
  surface-container: '#edeeef'
  surface-container-high: '#e7e8e9'
  surface-container-highest: '#e1e3e4'
  on-surface: '#191c1d'
  on-surface-variant: '#44474a'
  inverse-surface: '#2e3132'
  inverse-on-surface: '#f0f1f2'
  outline: '#75777a'
  outline-variant: '#c5c6ca'
  surface-tint: '#5d5e61'
  primary: '#000101'
  on-primary: '#ffffff'
  primary-container: '#1a1c1e'
  on-primary-container: '#838486'
  inverse-primary: '#c6c6c9'
  secondary: '#41664c'
  on-secondary: '#ffffff'
  secondary-container: '#c3edcb'
  on-secondary-container: '#476d52'
  tertiary: '#020009'
  on-tertiary: '#ffffff'
  tertiary-container: '#1f192c'
  on-tertiary-container: '#8a8098'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#e2e2e5'
  primary-fixed-dim: '#c6c6c9'
  on-primary-fixed: '#1a1c1e'
  on-primary-fixed-variant: '#454749'
  secondary-fixed: '#c3edcb'
  secondary-fixed-dim: '#a7d0b0'
  on-secondary-fixed: '#00210e'
  on-secondary-fixed-variant: '#2a4e36'
  tertiary-fixed: '#eadef9'
  tertiary-fixed-dim: '#cdc2dc'
  on-tertiary-fixed: '#1f182b'
  on-tertiary-fixed-variant: '#4b4358'
  background: '#f8f9fa'
  on-background: '#191c1d'
  surface-variant: '#e1e3e4'
typography:
  headline-xl:
    fontFamily: Lexend
    fontSize: 48px
    fontWeight: '600'
    lineHeight: '1.2'
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Lexend
    fontSize: 32px
    fontWeight: '500'
    lineHeight: '1.3'
  headline-lg-mobile:
    fontFamily: Lexend
    fontSize: 28px
    fontWeight: '500'
    lineHeight: '1.3'
  headline-md:
    fontFamily: Lexend
    fontSize: 24px
    fontWeight: '500'
    lineHeight: '1.4'
  body-lg:
    fontFamily: Be Vietnam Pro
    fontSize: 18px
    fontWeight: '400'
    lineHeight: '1.6'
  body-md:
    fontFamily: Be Vietnam Pro
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.6'
  label-md:
    fontFamily: Be Vietnam Pro
    fontSize: 14px
    fontWeight: '600'
    lineHeight: '1.2'
    letterSpacing: 0.01em
  label-sm:
    fontFamily: Be Vietnam Pro
    fontSize: 12px
    fontWeight: '500'
    lineHeight: '1.2'
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  unit: 8px
  container-max: 1280px
  gutter: 24px
  margin-desktop: 64px
  margin-mobile: 20px
  section-gap: 80px
---

## Brand & Style

The design system for this product is built on the philosophy of "low-pressure discovery." It aims to transform the often-intimidating process of starting a new hobby into a welcoming, celebratory experience. The personality is encouraging, gentle, and community-focused.

The visual style is a refined **Modern-Organic** hybrid. It utilizes expansive whitespace and a "gallery" approach to content, ensuring that users never feel overwhelmed by complexity. High-quality imagery of people engaged in activities is central to the UI, supported by a tactile interface that uses soft depth and significant roundedness to feel approachable and safe. The emotional response should be one of "lightness" and "possibility."

## Colors

The palette is designed to be "soft-vibrant." It avoids harsh primary colors in favor of pastels that feel natural and calming.

- **Primary:** A deep, near-black charcoal used for high-contrast actions, critical text, and primary buttons to ensure groundedness.
- **Secondary (Mint):** Used for "Success" states, active hobby categories, and positive reinforcement.
- **Tertiary (Lavender):** Used for community-related features, creative hobbies, and secondary highlights.
- **Accent (Yellow):** Reserved for "New" tags, special highlights, or "Hobby Milestones" to provide a warm, sunny glow.
- **Surface & Backgrounds:** The UI relies heavily on an off-white/very light grey base to maintain warmth, avoiding the clinical feel of pure white.

## Typography

The typography strategy prioritizes friendliness without sacrificing clarity. 

**Lexend** is used for all headings. Its geometric but slightly rounded structure feels modern and accessible, reducing the visual "weight" of large text blocks.

**Be Vietnam Pro** is utilized for body text and labels. It offers a contemporary, clean look with excellent legibility at smaller scales. Use `body-lg` for storytelling and descriptive onboarding sections to encourage a relaxed reading pace. `Label` styles should be used for metadata (e.g., distance, member counts, or timestamps) and UI controls.

## Layout & Spacing

This design system uses a **Fluid-Responsive Grid** with a significant emphasis on "Airy" negative space. 

On desktop, the layout should never feel like a dense dashboard. Instead, it uses wide margins (`64px`) and large gaps between sections (`80px`) to guide the user's eye through a narrative flow. Content is grouped into "Collections" or "Stories" rather than rigid data tables.

- **Grid:** 12-column system for desktop, 4-column for mobile.
- **Alignment:** Centralized content containers for readability, with occasional "bleeding" images to create a sense of scale and immersion.
- **Rhythm:** All spacing (padding, margins) must be a multiple of the `8px` base unit to maintain a mathematical but soft harmony.

## Elevation & Depth

Hierarchy is established through **Tonal Layering** and **Soft Ambient Shadows**. 

Avoid sharp, high-opacity shadows. Instead, use "Object-based Depth" where the shadow color is a slightly darkened version of the background or a very soft neutral (e.g., `rgba(0,0,0, 0.04)` with a `24px` blur).

- **Level 0 (Background):** The main canvas.
- **Level 1 (Cards):** Slightly raised with a subtle shadow to indicate interactability.
- **Level 2 (Floating Controls):** Navigation bars or primary action buttons use a slightly more pronounced shadow to appear "above" the content.
- **Backdrop Blurs:** Use subtle `12px` blurs for overlays and modals to maintain context of the background hobby imagery while focusing the user's attention.

## Shapes

The shape language is extremely organic. Every element should feel "tucked in" and safe. 

- **Primary Radius:** `24px` (rounded-lg) for standard cards and containers.
- **Secondary Radius:** `16px` for smaller UI elements like input fields or nested components.
- **Buttons:** Always use pill-shapes (fully rounded ends) to emphasize a "touchable" and friendly aesthetic. 
- **Icons:** Icons should feature rounded terminals and consistent stroke weights (1.5px - 2px) to match the softness of the typography.

## Components

### Buttons
- **Primary:** High-contrast (Primary Charcoal), pill-shaped, with a clear icon suffix (e.g., `->`) to denote progress.
- **Secondary:** Tonal backgrounds (Mint or Lavender) with dark text for lower-priority actions.
- **Ghost:** Transparent with a simple border or just text, used for "Cancel" or "Skip" to reduce pressure.

### Cards & Gallery Items
- Cards should have a `24px` radius and use a "stack" approach: a large image on top with metadata and text in a generous padding area below.
- Hover states on cards should involve a subtle scale-up (e.g., `1.02%`) rather than a color change, feeling more tactile.

### Input Fields & Controls
- **Search:** Large, pill-shaped bars with a soft-grey background.
- **Chips:** Used for hobby tags. They should use the pastel palette (Mint, Yellow, Lavender) to categorize interests visually without being loud.
- **Selection State:** When an interest is selected (e.g., "Cycling"), the card or chip should fill with its designated pastel color and show a subtle checkmark icon.

### Navigation
- On mobile: A floating bottom dock with pill-shaped active indicators.
- On desktop: A spacious top-nav with clear, simple labels and plenty of "breathing room" between links.