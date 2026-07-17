import { useEffect, useRef, useState } from 'react'
import { createPortal } from 'react-dom'

const EMOJIS = [
  '🌱', '🌿', '🪴', '🌻', '🍅', '🌸',
  '🎨', '🖌️', '🖍️', '✏️', '📷', '🎞️',
  '🍞', '🍳', '🍰', '☕', '🍷', '🔪',
  '🧶', '🧵', '🪡', '🧺', '🕯️', '🧼',
  '🏺', '🪵', '🔨', '🪚', '🛠️', '⚙️',
  '🎸', '🎹', '🥁', '🎻', '🎤', '🎧',
  '🚴', '🏃', '🧗', '🏊', '⛰️', '🏕️',
  '🧘', '♟️', '🎮', '🎯', '🎲', '📚',
]

interface EmojiPickerProps {
  value: string
  onChange: (emoji: string) => void
}

export default function EmojiPicker({ value, onChange }: EmojiPickerProps) {
  const [open, setOpen] = useState(false)
  const [coords, setCoords] = useState({ top: 0, left: 0 })
  const buttonRef = useRef<HTMLButtonElement>(null)
  const panelRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (!open) return

    const updatePosition = () => {
      const rect = buttonRef.current?.getBoundingClientRect()
      if (rect) setCoords({ top: rect.bottom + 8, left: rect.left })
    }
    updatePosition()

    const onClickAway = (e: MouseEvent) => {
      const target = e.target as Node
      if (!buttonRef.current?.contains(target) && !panelRef.current?.contains(target)) setOpen(false)
    }
    document.addEventListener('mousedown', onClickAway)
    window.addEventListener('scroll', updatePosition, true)
    window.addEventListener('resize', updatePosition)
    return () => {
      document.removeEventListener('mousedown', onClickAway)
      window.removeEventListener('scroll', updatePosition, true)
      window.removeEventListener('resize', updatePosition)
    }
  }, [open])

  return (
    <>
      <button
        ref={buttonRef}
        type="button"
        onClick={() => setOpen(o => !o)}
        className="w-16 h-16 rounded-2xl bg-surface-container-low flex items-center justify-center text-3xl hover:bg-surface-container transition-colors"
        aria-label="Choose icon"
      >
        {value || '🌱'}
      </button>

      {open && createPortal(
        <div
          ref={panelRef}
          style={{ position: 'fixed', top: coords.top, left: coords.left }}
          className="z-50 w-72 max-h-64 overflow-y-auto rounded-2xl bg-surface-container-lowest shadow-xl border border-outline-variant/30 p-3"
        >
          <div className="grid grid-cols-6 gap-1">
            {EMOJIS.map(emoji => (
              <button
                key={emoji}
                type="button"
                onClick={() => { onChange(emoji); setOpen(false) }}
                className={`w-10 h-10 rounded-xl flex items-center justify-center text-xl transition-colors ${
                  emoji === value ? 'bg-secondary-container' : 'hover:bg-surface-container-low'
                }`}
              >
                {emoji}
              </button>
            ))}
          </div>
        </div>,
        document.body
      )}
    </>
  )
}
