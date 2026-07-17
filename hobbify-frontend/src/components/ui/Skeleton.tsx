export function SkeletonCard() {
  return (
    <div className="card animate-pulse">
      <div className="aspect-[4/3] bg-surface-container-high" />
      <div className="p-4 space-y-3">
        <div className="h-4 bg-surface-container-high rounded w-3/4" />
        <div className="h-3 bg-surface-container-high rounded w-full" />
        <div className="h-3 bg-surface-container-high rounded w-2/3" />
        <div className="flex gap-2">
          <div className="h-5 bg-surface-container-high rounded-full w-14" />
          <div className="h-5 bg-surface-container-high rounded-full w-14" />
        </div>
      </div>
    </div>
  )
}

export function SkeletonGrid({ count = 6 }: { count?: number }) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
      {Array.from({ length: count }).map((_, i) => (
        <SkeletonCard key={i} />
      ))}
    </div>
  )
}
