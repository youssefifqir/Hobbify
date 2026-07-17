interface AvatarStackProps {
  count: string
  avatars?: string[]
}

export default function AvatarStack({ count, avatars = [] }: AvatarStackProps) {
  return (
    <div className="flex items-center gap-2">
      <div className="flex -space-x-2">
        {avatars.slice(0, 3).map((url, i) => (
          <div key={i} className="w-7 h-7 rounded-full border-2 border-surface-container-lowest overflow-hidden bg-surface-container">
            <img src={url} alt="" className="w-full h-full object-cover" />
          </div>
        ))}
        <div className="w-7 h-7 rounded-full border-2 border-surface-container-lowest bg-secondary-container text-[10px] font-semibold flex items-center justify-center text-primary">
          +{count}
        </div>
      </div>
    </div>
  )
}
