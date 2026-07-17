interface ChipProps {
  label: string
  active?: boolean
  onClick?: () => void
  size?: 'sm' | 'md'
  variant?: 'default' | 'cost' | 'space' | 'time'
}

const variantStyles = {
  default: 'bg-secondary-container text-primary',
  cost: 'bg-amber-200 text-amber-900',
  space: 'bg-sky-200 text-sky-900',
  time: 'bg-violet-200 text-violet-900',
}

export default function Chip({ label, active, onClick, size = 'sm', variant = 'default' }: ChipProps) {
  const Comp = onClick ? 'button' : 'span'
  return (
    <Comp
      onClick={onClick}
      className={`${size === 'sm' ? 'px-2.5 py-1 text-xs' : 'px-3 py-1.5 text-sm'} rounded-full font-medium transition-all duration-200 ${
        active ? variantStyles[variant] : 'bg-surface-container-low text-on-surface-variant hover:bg-surface-container'
      } ${onClick ? 'cursor-pointer active:scale-95' : ''}`}
    >
      {label}
    </Comp>
  )
}
