import "./BakeryReview.css";
import { useEffect, useRef, useState } from "react";

const Stars = ({ rating }) => {
  const full = Math.floor(rating);
  const half = rating - full >= 0.5;
  const empties = 5 - full - (half ? 1 : 0);

  return (
    <span className="mapCardstars" aria-label={`별점 ${rating}`}>
      {"★".repeat(full)}
      {half ? "★" : ""}
      {"☆".repeat(empties)}
      <span className="mapCardRatingNumber">{rating.toFixed(1)}</span>
    </span>
  );
};

const BakeryReview = ({ review }) => {
  const {
    id,
    nickname,
    profile,
    rating,
    text,
    date,
    likes = 0,
    dislikes = 0,
  } = review;

  // 접기/펼치기 상태 + 버튼 노출 여부
  const [expanded, setExpanded] = useState(false);
  const [showToggle, setShowToggle] = useState(false);
  const textRef = useRef(null);

  // 좋아요 / 싫어요 상태
  const [likeCount, setLikeCount] = useState(likes);
  const [dislikeCount, setDislikeCount] = useState(dislikes);
  const [myReaction, setMyReaction] = useState(null); // "like" | "dislike" | null
  const [isUpdating, setIsUpdating] = useState(false);

  // 부모에서 리뷰 데이터가 갱신되면 카운트 동기화
  useEffect(() => {
    setLikeCount(likes ?? 0);
    setDislikeCount(dislikes ?? 0);
  }, [likes, dislikes]);

  // 처음 렌더(또는 text 변경) 시, 내용이 접혀야 하는지 측정
  useEffect(() => {
    setExpanded(false);
    const el = textRef.current;
    if (!el) return;

    const raf = requestAnimationFrame(() => {
      setShowToggle(el.scrollHeight > el.clientHeight + 1);
    });
    return () => cancelAnimationFrame(raf);
  }, [text]);

  const updateReactionOnServer = async (nextLikes, nextDislikes) => {
    try {
      setIsUpdating(true);
      await fetch(`http://localhost:3001/reviews/${id}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          likes: nextLikes,
          dislikes: nextDislikes,
        }),
      });
    } catch (err) {
      console.error("리뷰 리액션 업데이트 실패:", err);
      alert(
        "리뷰 좋아요/싫어요 반영에 실패했습니다. 잠시 후 다시 시도해주세요."
      );
    } finally {
      setIsUpdating(false);
    }
  };

  const handleLikeClick = () => {
    if (isUpdating) return;

    let nextLikes = likeCount;
    let nextDislikes = dislikeCount;
    let nextReaction = myReaction;

    if (myReaction === "like") {
      // 좋아요 취소
      nextLikes = Math.max(0, likeCount - 1);
      nextReaction = null;
    } else if (myReaction === "dislike") {
      // 싫어요에서 좋아요로 변경
      nextLikes = likeCount + 1;
      nextDislikes = Math.max(0, dislikeCount - 1);
      nextReaction = "like";
    } else {
      // 처음 좋아요
      nextLikes = likeCount + 1;
      nextReaction = "like";
    }

    setLikeCount(nextLikes);
    setDislikeCount(nextDislikes);
    setMyReaction(nextReaction);
    updateReactionOnServer(nextLikes, nextDislikes);
  };

  const handleDislikeClick = () => {
    if (isUpdating) return;

    let nextLikes = likeCount;
    let nextDislikes = dislikeCount;
    let nextReaction = myReaction;

    if (myReaction === "dislike") {
      // 싫어요 취소
      nextDislikes = Math.max(0, dislikeCount - 1);
      nextReaction = null;
    } else if (myReaction === "like") {
      // 좋아요에서 싫어요로 변경
      nextLikes = Math.max(0, likeCount - 1);
      nextDislikes = dislikeCount + 1;
      nextReaction = "dislike";
    } else {
      // 처음 싫어요
      nextDislikes = dislikeCount + 1;
      nextReaction = "dislike";
    }

    setLikeCount(nextLikes);
    setDislikeCount(nextDislikes);
    setMyReaction(nextReaction);
    updateReactionOnServer(nextLikes, nextDislikes);
  };

  return (
    <article className="bdw-review" key={id}>
      <div className="bakeryDetailBar">
        <div className="bakeryDetailBarProfile">
          <img className="bakeryDetailBarProfileImg" src={profile} alt="" />
          <div className="bakeryDetailBarNickName">{nickname}</div>
        </div>
        <Stars rating={rating} />
      </div>

      {/* 리뷰 텍스트 */}
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
        {/* '더보기' / '접기' 버튼 */}
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
        <button
          type="button"
          className={`bdw-review-likes ${
            myReaction === "like" ? "is-active" : ""
          }`}
          onClick={handleLikeClick}
          disabled={isUpdating}
        >
          좋아요 {likeCount}
        </button>
        <button
          type="button"
          className={`bdw-review-dislikes ${
            myReaction === "dislike" ? "is-active" : ""
          }`}
          onClick={handleDislikeClick}
          disabled={isUpdating}
        >
          싫어요 {dislikeCount}
        </button>
      </div>
    </article>
  );
};

export default BakeryReview;
