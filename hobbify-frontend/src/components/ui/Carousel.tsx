import { useState, useRef, useCallback, useEffect, type ReactNode } from 'react'

interface CarouselProps {
  children: ReactNode[]
  desktopColumns?: 2 | 3 | 4
  peekWidth?: number
}

export default function Carousel({ children, desktopColumns = 3, peekWidth = 60 }: CarouselProps) {
  const [current, setCurrent] = useState(0)
  const [touchX, setTouchX] = useState(0)
  const [dragging, setDragging] = useState(false)
  const [translateX, setTranslateX] = useState(0)
  const [slideWidth, setSlideWidth] = useState(0)
  const [isMobile, setIsMobile] = useState(true)
  const containerRef = useRef<HTMLDivElement>(null)

  const totalSlides = children.length

  useEffect(() => {
    const measure = () => {
      if (containerRef.current) {
        const w = containerRef.current.offsetWidth
        const mobile = w < 1024
        setIsMobile(mobile)
        setSlideWidth(mobile ? w - peekWidth : w / desktopColumns)
      }
    }
    measure()
    window.addEventListener('resize', measure)
    return () => window.removeEventListener('resize', measure)
  }, [desktopColumns, peekWidth])

  const goTo = useCallback((index: number) => {
    setCurrent(Math.max(0, Math.min(index, totalSlides - 1)))
  }, [totalSlides])

  const handleTouchStart = (e: React.TouchEvent) => {
    setTouchX(e.touches[0].clientX)
    setDragging(true)
  }

  const handleTouchMove = (e: React.TouchEvent) => {
    if (!dragging) return
    setTranslateX(e.touches[0].clientX - touchX)
  }

  const handleTouchEnd = () => {
    setDragging(false)
    if (Math.abs(translateX) > slideWidth * 0.25) {
      if (translateX > 0 && current > 0) goTo(current - 1)
      if (translateX < 0 && current < totalSlides - 1) goTo(current + 1)
    }
    setTranslateX(0)
  }

  const offset = -(current * slideWidth)

  return (
    <div className="relative select-none">
      <div
        ref={containerRef}
        className={`overflow-hidden ${isMobile ? '' : 'lg:overflow-visible'}`}
        onTouchStart={isMobile ? handleTouchStart : undefined}
        onTouchMove={isMobile ? handleTouchMove : undefined}
        onTouchEnd={isMobile ? handleTouchEnd : undefined}
      >
        <div
          className={`flex gap-6 ${isMobile ? 'transition-transform duration-500 ease-[cubic-bezier(0.34,1.56,0.64,1)]' : 'lg:grid lg:grid-cols-3'}`}
          style={{
            transform: isMobile ? `translateX(${offset + translateX}px)` : 'none',
          }}
        >
          {children.map((child, i) => (
            <div
              key={i}
              className="shrink-0 select-none"
              style={{ width: isMobile ? slideWidth || 300 : 'auto' }}
            >
              {child}
            </div>
          ))}
        </div>
      </div>

      {isMobile && (
        <div className="flex items-center justify-center gap-2 mt-6">
          {Array.from({ length: totalSlides }).map((_, i) => (
            <button
              key={i}
              onClick={() => goTo(i)}
              className={`rounded-full transition-all duration-300 ${
                i === current ? 'w-8 h-2 bg-primary' : 'w-2 h-2 bg-outline-variant'
              }`}
              aria-label={`Go to slide ${i + 1}`}
            />
          ))}
        </div>
      )}

      {isMobile && totalSlides > 1 && (
        <>
          {current > 0 && (
            <button
              onClick={() => goTo(current - 1)}
              className="absolute left-2 top-1/2 -translate-y-1/2 w-10 h-10 rounded-full bg-surface-container-lowest/90 shadow-md flex items-center justify-center active:scale-90 transition-transform backdrop-blur-sm"
              aria-label="Previous"
            >
              <svg className="w-5 h-5 text-on-surface" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" />
              </svg>
            </button>
          )}
          {current < totalSlides - 1 && (
            <button
              onClick={() => goTo(current + 1)}
              className="absolute right-2 top-1/2 -translate-y-1/2 w-10 h-10 rounded-full bg-surface-container-lowest/90 shadow-md flex items-center justify-center active:scale-90 transition-transform backdrop-blur-sm"
              aria-label="Next"
            >
              <svg className="w-5 h-5 text-on-surface" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" />
              </svg>
            </button>
          )}
        </>
      )}
    </div>
  )
}
