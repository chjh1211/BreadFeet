import "./BakeryReview.css";
import { useEffect, useRef, useState } from "react";

const Stars = ({ rating }) => {
  const full = Math.floor(rating);
  const half = rating - full >= 0.5;
  const empties = 5 - full - (half ? 1 : 0);

  return (
    <span className="mapCardstars" aria-label={`평점 ${rating}`}>
      {"★".repeat(full)}
      {half ? "☆".slice(0, 0) + "⯨" : ""}
      {"☆".repeat(empties)}
      <span className="mapCardRatingNumber">{rating.toFixed(1)}</span>
    </span>
  );
};

const BakeryReview = ({ review }) => {
  const { id, nickname, profile, rating, text, date, likes, dislikes } = review;
  // const rounded = Math.round(rating);

  // 펼치기/접기 상태 + 버튼 노출 여부(텍스트가 길 때만 버튼 보이기)
  const [expanded, setExpanded] = useState(false);
  const [showToggle, setShowToggle] = useState(false);
  const textRef = useRef(null);

  // 처음 렌더 시(접힘 상태) 실제로 오버플로우 되는지 측정해서 버튼 표시 결정
  useEffect(() => {
    setExpanded(false); // 항상 접힘으로 측정
    const el = textRef.current;
    if (!el) return;

    // 다음 페인트 이후 측정 (line-clamp가 적용된 높이 기준)
    const raf = requestAnimationFrame(() => {
      setShowToggle(el.scrollHeight > el.clientHeight + 1);
    });
    return () => cancelAnimationFrame(raf);
  }, [text]);

  return (
    <article className="bdw-review" key={id}>
      <div className="bakeryDetailBar">
        <div className="bakeryDetailBarProfile">
          <img className="bakeryDetailBarProfileImg" src={profile} alt="" />
          <div className="bakeryDetailBarNickName">{nickname}</div>
        </div>
        <Stars rating={rating} />
      </div>

      {/* 토글 */}
      <div
        ref={textRef}
        id={`review-text-${id}`}
        className={`bdw-review-text ${
          expanded ? "is-expanded" : "is-collapsed"
        }`}
      >
        {text}
      </div>
      <div className="toggleDiv">
        {/* 텍스트가 길 때 더보기 */}
        {showToggle && (
          <button
            type="button"
            className="bdw-review-toggle"
            onClick={() => setExpanded((v) => !v)}
            aria-expanded={expanded}
            aria-controls={`review-text-${id}`}
          >
            {expanded ? "접기" : "더보기"}
          </button>
        )}
      </div>

      <div className="bdw-review-meta">
        <span className="bdw-review-date">{date}</span>
        <button className="bdw-review-likes">좋아요 {likes}</button>
        <button className="bdw-review-dislikes">싫어요 {dislikes}</button>
      </div>
    </article>
  );
};

export default BakeryReview;
