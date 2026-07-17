import { useRef } from 'react'

interface ImageUploadProps {
  value: string
  onChange: (dataUrl: string) => void
  label?: string
}

// Reads the file locally and stores it as a data URL. When a real backend
// exists, swap the FileReader call below for an upload request and set
// `onChange` to the returned URL — the rest of the form doesn't change.
export default function ImageUpload({ value, onChange, label = 'Cover Photo' }: ImageUploadProps) {
  const inputRef = useRef<HTMLInputElement>(null)

  const handleFile = (file: File | undefined) => {
    if (!file) return
    const reader = new FileReader()
    reader.onload = () => onChange(reader.result as string)
    reader.readAsDataURL(file)
  }

  return (
    <div>
      <label className="text-sm font-semibold text-on-surface mb-2 block">{label}</label>
      <div
        onClick={() => inputRef.current?.click()}
        className="relative w-full h-48 rounded-2xl overflow-hidden bg-surface-container-low border-2 border-dashed border-outline-variant cursor-pointer hover:border-secondary transition-colors flex items-center justify-center group"
      >
        {value ? (
          <>
            <img src={value} alt="" className="w-full h-full object-cover" />
            <div className="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
              <span className="text-white text-sm font-semibold">Change photo</span>
            </div>
          </>
        ) : (
          <div className="text-center px-4">
            <svg className="w-8 h-8 mx-auto mb-2 text-on-surface-variant" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 16.5V9.75m0 0l-3 3m3-3l3 3M6.75 19.5a4.5 4.5 0 01-1.41-8.775 5.25 5.25 0 0110.233-2.33 3 3 0 013.758 3.848A3.752 3.752 0 0118 19.5H6.75z" />
            </svg>
            <p className="text-sm font-semibold text-on-surface-variant">Click to upload a photo</p>
            <p className="text-xs text-on-surface-variant/70 mt-1">PNG or JPG</p>
          </div>
        )}
      </div>
      <input
        ref={inputRef}
        type="file"
        accept="image/*"
        className="hidden"
        onChange={e => handleFile(e.target.files?.[0])}
      />
    </div>
  )
}
