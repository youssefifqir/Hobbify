export default function ProgressBar({ value, className = '' }: { value: number; className?: string }) {
  return (
    <div className={`progress-bar ${className}`}>
      <div className="progress-fill" style={{ width: `${Math.min(Math.max(value, 0), 100)}%` }} />
    </div>
  )
}
