import React from "react";
import { useParams } from "react-router-dom";
import "./BakeryDetail.css";
import BakeryReview from "./BakeryReview";
import profileImage from "../assets/bakery1.jpg";

// HomeRightLogin & MapBakeryDetail 랑 같은 api 사용 할거
const mockData = [
  {
    id: 1,
    name: "감자빵집",
    rating: 4.5,
    reviewCount: 88,
    address: "대구 북구 대학로 80",
    phone: "053 123 4567",
    hours: "매일 09:00 - 20:00",
    popMenu: ["소금빵", "크림빵", "크루아상"],
    tags: ["매장 내 취식 가능", "테이크아웃", "커피"],
  },
];

function findBakeryById(id) {
  return mockData.find((b) => b.id === id) || null;
}

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
  const { bakeryId } = useParams(); // URL의 :bakeryId
  const idNum = Number(bakeryId);
  const bakery = React.useMemo(() => findBakeryById(idNum), [idNum]);

  React.useEffect(() => {
    if (bakery?.name) document.title = `${bakery.name} • BreadFeet`;
  }, [bakery?.name]);

  const reviews = React.useMemo(
    () => [
      {
        id: 1,
        nickname: "감자도리",
        profile: profileImage,
        rating: 4.5,
        likes: 34,
        dislikes: 2,
        date: "2025-03-21",
        text: "맛있네요",
      },
      {
        id: 2,
        nickname: "감돌이",
        profile: profileImage,
        rating: 4,
        likes: 12,
        dislikes: 1,
        date: "2025-04-03",
        text: "소금빵 맛있고 커피도 괜찮음.",
      },
      {
        id: 3,
        nickname: "미식감자",
        profile: profileImage,
        rating: 2.5,
        likes: 4,
        dislikes: 0,
        date: "2025-02-10",
        text: "기대보단 평범했어요.",
      },
      {
        id: 4,
        nickname: "감자도리토스",
        profile: profileImage,
        rating: 2,
        likes: 2,
        dislikes: 5,
        date: "2025-05-02",
        text: "대기 길고 재고가 빨리 떨어져요.",
      },
      {
        id: 5,
        nickname: "감자버터",
        profile: profileImage,
        rating: 5,
        likes: 52,
        dislikes: 3,
        date: "2025-05-12",
        text: "크로와상이 진짜 맛있음. 재방문 의사 100%",
      },
      {
        id: 6,
        nickname: "악플감자",
        profile: profileImage,
        rating: 1,
        likes: 1,
        dislikes: 10,
        date: "2025-03-05",
        text: "장사접으시길",
      },
    ],
    []
  );

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
            onClick={() => alert("리뷰쓰기 (TODO)")} // 준나 onclick만 바꾸면대
          >
            리뷰쓰기
          </button>
        </div>

        {sortedReviews.map((r) => (
          <BakeryReview key={r.id} review={r} />
        ))}
      </section>
    </div>
  );
};

export default BakeryDetail;
