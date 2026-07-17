interface BadgeProps {
  children: React.ReactNode
  variant?: 'default' | 'secondary' | 'tertiary'
}

export default function Badge({ children, variant = 'default' }: BadgeProps) {
  const colors = {
    default: 'bg-surface-container text-on-surface',
    secondary: 'bg-secondary-container text-primary',
    tertiary: 'bg-tertiary-fixed text-primary',
  }
  return (
    <span className={`badge ${colors[variant]}`}>
      {children}
    </span>
  )
}
