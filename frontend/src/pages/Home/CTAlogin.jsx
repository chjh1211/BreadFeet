import { useEffect, useRef, useState } from "react";
import "./CTAlogin.css";
import BestCourseCTA from "./BestCourseCTA";
import AiCTA from "./AiCTA";

export default function CTAlogin({
  autoplay = true,
  interval = 5000,
  pauseOnHover = true,
  nickname, // 선택: AiCTA에 전달
}) {
  const [active, setActive] = useState(0);
  const timerRef = useRef(null);
  const viewportRef = useRef(null);

  // 필요한 경우 여기에서 각 CTA에 prop을 더 넘겨줄 수 있음.
  const slides = [
    { key: "ai", node: <AiCTA nickname={nickname} /> },
    { key: "best", node: <BestCourseCTA /> },
  ];

  const go = (i) => setActive((prev) => (i + slides.length) % slides.length);
  const next = () => go(active + 1);
  const prev = () => go(active - 1);

  // 자동 재생
  useEffect(() => {
    if (!autoplay) return;
    const start = () => {
      clearInterval(timerRef.current);
      timerRef.current = setInterval(
        () => setActive((a) => (a + 1) % slides.length),
        interval
      );
    };
    start();
    return () => clearInterval(timerRef.current);
  }, [autoplay, interval, slides.length]);

  const pause = () => pauseOnHover && clearInterval(timerRef.current);
  const resume = () => {
    if (pauseOnHover && autoplay) {
      clearInterval(timerRef.current);
      timerRef.current = setInterval(
        () => setActive((a) => (a + 1) % slides.length),
        interval
      );
    }
  };

  // 터치 스와이프
  useEffect(() => {
    const el = viewportRef.current;
    if (!el) return;

    let startX = 0;
    let dx = 0;
    let touching = false;

    const onStart = (e) => {
      touching = true;
      startX = e.touches[0].clientX;
      dx = 0;
      pause();
    };
    const onMove = (e) => {
      if (!touching) return;
      dx = e.touches[0].clientX - startX;
      el.style.setProperty("--dx", `${dx}px`);
    };
    const onEnd = () => {
      touching = false;
      el.style.setProperty("--dx", "0px");
      if (Math.abs(dx) > 50) dx < 0 ? next() : prev();
      resume();
    };

    el.addEventListener("touchstart", onStart, { passive: true });
    el.addEventListener("touchmove", onMove, { passive: true });
    el.addEventListener("touchend", onEnd, { passive: true });

    return () => {
      el.removeEventListener("touchstart", onStart);
      el.removeEventListener("touchmove", onMove);
      el.removeEventListener("touchend", onEnd);
    };
  }, [active]); // eslint-disable-line

  // 키보드 접근성
  useEffect(() => {
    const onKey = (e) => {
      if (e.key === "ArrowLeft") prev();
      if (e.key === "ArrowRight") next();
    };
    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
  }, []);

  return (
    <section
      className="ctaLogin"
      aria-roledescription="carousel"
      onMouseEnter={pause}
      onMouseLeave={resume}
    >
      <div className="ctaLogin__viewport" ref={viewportRef}>
        {slides.map((s, i) => (
          <div
            key={s.key}
            className={`ctaLogin__slide ${i === active ? "is-active" : ""}`}
            role="group"
            aria-roledescription="slide"
            aria-label={`${i + 1} / ${slides.length}`}
          >
            {s.node}
          </div>
        ))}
      </div>

      <button className="ctaLogin__nav prev" aria-label="이전" onClick={prev}>
        ‹
      </button>
      <button className="ctaLogin__nav next" aria-label="다음" onClick={next}>
        ›
      </button>

      <div className="ctaLogin__dots" role="tablist">
        {slides.map((_, i) => (
          <button
            key={i}
            role="tab"
            aria-selected={i === active}
            className={`dot ${i === active ? "is-active" : ""}`}
            onClick={() => go(i)}
          />
        ))}
      </div>
    </section>
  );
}
