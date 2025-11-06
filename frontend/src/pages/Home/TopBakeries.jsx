import { useNavigate } from "react-router-dom";
import "./TopBakeries.css";

export default function TopBakeries({
  title = "이번 주 인기 빵집",
  bakeries = [],
  limit = 3,
}) {
  const navigate = useNavigate();

  const data = (
    bakeries.length
      ? bakeries
      : [
          {
            id: 101,
            name: "감자빵집",
            location: "대구 북구 대학로 80",
            tags: ["시그니처 감자빵", "테이크아웃", "커피"],
          },
          {
            id: 102,
            name: "감자빵집 본점",
            location: "대구 북구 대학로 100",
            tags: ["대표메뉴 크림빵", "매장 내 취식", "주차"],
          },
          {
            id: 103,
            name: "감자빵 연구소",
            location: "대구 북구 대학로 12",
            tags: ["한정 메뉴", "예약 가능"],
          },
        ]
  ).slice(0, limit);

  const go = (id) => {
    if (!id) return;
    try {
      navigate(`/bakery/${id}`);
    } catch {
      window.location.href = `/bakery/${id}`;
    }
  };

  return (
    <section className="tbl-wrap" aria-labelledby="tbl-title">
      <h2 id="tbl-title" className="tbl-title">
        {title}
      </h2>

      <div className="tbl-panel">
        <ol className="tbl-list" role="list">
          {data.map((b, i) => (
            <li
              key={b.id ?? i}
              className="tbl-row"
              onClick={() => go(b.id)}
              onKeyDown={(e) =>
                (e.key === "Enter" || e.key === " ") && go(b.id)
              }
              tabIndex={0}
              role="button"
            >
              <div className="tbl-rank">{i + 1}</div>

              <div className="tbl-body">
                {/* ⬇⬇ 이름 왼쪽 / 자세히보기 오른쪽 */}
                <div className="tbl-head">
                  <h3 className="tbl-name">{b.name}</h3>

                  {/* 개별 클릭 시 중복 네비 방지용 stopPropagation */}
                  <button
                    type="button"
                    className="tbl-cta"
                    onClick={(e) => {
                      e.stopPropagation();
                      go(b.id);
                    }}
                  >
                    <span>자세히 보기</span>
                    <span className="tbl-arrow" aria-hidden>
                      →
                    </span>
                  </button>

                  {/* 위치는 옵션: 있으면 이름 아래 한 줄로 표시 */}
                  {b.location && <p className="tbl-loc">{b.location}</p>}
                </div>

                <div className="tbl-divider" aria-hidden />

                {/* ⬇⬇ 태그는 아래 줄에 좌측 정렬로 나열 */}
                {!!(b.tags && b.tags.length) && (
                  <div className="tbl-tags">
                    {b.tags.slice(0, 4).map((t, idx) => (
                      <span key={idx} className="tbl-tag">
                        #{t}
                      </span>
                    ))}
                  </div>
                )}
              </div>
            </li>
          ))}
        </ol>
      </div>
    </section>
  );
}
