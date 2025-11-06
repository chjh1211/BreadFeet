import React from "react";
import { useParams } from "react-router-dom";
import "./BakeryDetail.css";
import BakeryReview from "./BakeryReview";
//Review 모달 추가
import Review from './Review';

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

function RatingBars({ counts = [60, 15, 8, 5, 0], total }) {
  const sum = total ?? counts.reduce((s, n) => s + n, 0);
  return (
    <div className="bdw-bars">
      {counts.map((count, idx) => {
        const pct = sum ? (count / sum) * 100 : 0;
        return (
          <div className="bdw-bar" key={idx}>
            <span className="bdw-bar-label">{5 - idx}</span>
            <div className="bdw-bar-track">
              <div className="bdw-bar-fill" style={{ width: `${pct}%` }} />
            </div>
            <span className="bdw-bar-count" style={{ opacity: 0.6 }}>
              {" "}
              ({count})
            </span>
          </div>
        );
      })}
    </div>
  );
}

const REVIEW_SORTS = [
  { key: "helpful", label: "좋아요순", cmp: (a, b) => b.likes - a.likes },
  {
    key: "new",
    label: "최신순",
    cmp: (a, b) => new Date(b.date) - new Date(a.date),
  },
  { key: "high", label: "높은별점순", cmp: (a, b) => b.rating - a.rating },
  { key: "low", label: "낮은별점순", cmp: (a, b) => a.rating - b.rating },
];

const BakeryDetail = () => {
  const { bakeryId } = useParams();
  const idNum = Number(bakeryId);

  const [bakery, setBakery] = React.useState(null);
  const [reviews, setReviews] = React.useState([]);
  //모달 상태
  const [showReviewModal, setShowReviewModal] = React.useState(false);

  const fetchReviews = React.useCallback(() => {
    fetch(`http://localhost:3001/reviews?bakeryId=${idNum}`)
      .then((res) => res.json())
      .then((data) => setReviews(data))
      .catch(() => setReviews([]));
  }, [idNum]); // idNum이 변경될 때만 함수가 다시 생성됨
  
  // Bakery 정보 가져오기
  React.useEffect(() => {
    fetch(`http://localhost:3001/bakery/${idNum}`)
      .then((res) => res.json())
      .then((data) => setBakery(data))
      .catch(() => setBakery(null));
  }, [idNum]);

  // 리뷰 가져오기
  React.useEffect(() => {
      fetchReviews(); // 컴포넌트 마운트 시 최초 실행
    }, [fetchReviews]); // fetchReviews가 의존성 배열에 있어야 경고가 발생하지 않습니다.

  const [sortKey, setSortKey] = React.useState("helpful");
  const [menuOpen, setMenuOpen] = React.useState(false);
  const sortRef = React.useRef(null);

  const currentSort =
    REVIEW_SORTS.find((s) => s.key === sortKey) ?? REVIEW_SORTS[0];

  const sortedReviews = React.useMemo(() => {
    const arr = [...reviews];
    arr.sort(currentSort.cmp);
    return arr;
  }, [reviews, currentSort]);

  // 바깥 클릭/ESC로 닫기
  React.useEffect(() => {
    if (!menuOpen) return;
    const onDocDown = (e) => {
      if (!sortRef.current?.contains(e.target)) setMenuOpen(false);
    };
    const onEsc = (e) => {
      if (e.key === "Escape") setMenuOpen(false);
    };
    document.addEventListener("mousedown", onDocDown);
    document.addEventListener("keydown", onEsc);
    return () => {
      document.removeEventListener("mousedown", onDocDown);
      document.removeEventListener("keydown", onEsc);
    };
  }, [menuOpen]);

  if (!bakery) {
    return (
      <div className="bdw-wrap">
        <header className="bdw-header">
          <h1 className="bdw-title">빵집 정보를 찾을 수 없어요</h1>
        </header>
        <p className="bdw-empty">유효하지 않은 ID: {bakeryId}</p>
      </div>
    );
  }

  return (
    <div className="bdw-wrap">
      <header className="bdw-header">
        <div className="bdw-head-left">
          <h1 className="bdw-title">{bakery.name}</h1>
          <div className="bdw-sub">
            <Stars rating={bakery.rating} />
            <span className="bdw-count">({bakery.reviewCount}개 리뷰)</span>
          </div>
        </div>
      </header>

      <section className="bdw-meta">
        <dl>
          <div>
            <dt>영업시간</dt>
            <dd>{bakery.hours || "-"}</dd>
          </div>
          <div>
            <dt>주소</dt>
            <dd>{bakery.address || "-"}</dd>
          </div>
          <div>
            <dt>전화번호</dt>
            <dd>{bakery.phone || "-"}</dd>
          </div>
          <div>
            <dt>대표메뉴</dt>
            <dd>
              {Array.isArray(bakery.popMenu) && bakery.popMenu.length ? (
                <ul className="bakeryDetailPopMenus" role="list">
                  {[...new Set(bakery.popMenu)].map((menu) => (
                    <li key={menu}>{menu}</li>
                  ))}
                </ul>
              ) : (
                "-"
              )}
            </dd>
          </div>
          <div>
            <dt>편의</dt>
            <dd>
              {Array.isArray(bakery.tags) && bakery.tags.length ? (
                <ul className="bakeryDetailTags" role="list">
                  {[...new Set(bakery.tags)].map((tag) => (
                    <li key={tag}>{tag}</li>
                  ))}
                </ul>
              ) : (
                "-"
              )}
            </dd>
          </div>
        </dl>
      </section>

      <section className="bdw-score">
        <div className="BakeryNumbersWrapper">
          <div className="bdw-score-number">{bakery.rating.toFixed(1)}</div>
          <div>({bakery.reviewCount}개)</div>
        </div>
        <RatingBars />
      </section>

      <section className="bdw-reviews">
        <div className="BakeryDetailReviewContorl bdw-review-control">
          <div className="bdw-sort" ref={sortRef}>
            <button
              className="bdw-sort-btn"
              onClick={() => setMenuOpen((v) => !v)}
              aria-haspopup="menu"
              aria-expanded={menuOpen}
            >
              {currentSort.label}
            </button>
            {menuOpen && (
              <ul className="bdw-sort-menu" role="menu">
                {REVIEW_SORTS.map((opt) => {
                  const active = opt.key === sortKey;
                  return (
                    <li key={opt.key} role="none" className="bdw-sort-item">
                      <button
                        role="menuitem"
                        className={`bdw-sort-option ${
                          active ? "is-active" : ""
                        }`}
                        onClick={() => {
                          setSortKey(opt.key);
                          setMenuOpen(false);
                        }}
                      >
                        <span>{opt.label}</span>
                        {active && <span className="bdw-sort-check">✓</span>}
                      </button>
                    </li>
                  );
                })}
              </ul>
            )}
          </div>

          <button
            className="bdw-review-btn"
            //리뷰 모달 클릭시 보이기
           onClick={() => {
            //console.log("✅ 리뷰쓰기 버튼 클릭됨!");
            setShowReviewModal(true);
            }}
          >
            리뷰쓰기
          </button>
        </div>

        <div className="bakery-detail-rewview-wrapper">
          {sortedReviews.map((r) => (
            <BakeryReview key={r.id} review={r} />
          ))}
        </div>
      </section>

      {/* 리뷰 모달 */}
      {
        //true일 때만 렌더링
        showReviewModal && (
          <Review
            bakeryName = {bakery.name}
            onClose={()=>setShowReviewModal(false)}
            bakeryId={idNum}
            onReviewSuccess={fetchReviews}
          />
        )
      }
    </div>
  );
};

export default BakeryDetail;
