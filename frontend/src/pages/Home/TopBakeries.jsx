import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import "./TopBakeries.css";

export default function TopBakeries({
  title = "이번 주 인기 빵집",
  bakeries = [],
  limit = 3,
}) {
  const navigate = useNavigate();

  const [items, setItems] = useState([]);

  useEffect(() => {
    const controller = new AbortController();
    fetch("/data/topBakeries.json", { signal: controller.signal })
      .then((res) => res.json())
      .then((data) => setItems(Array.isArray(data) ? data : []))
      .catch(() => setItems([]));
    return () => controller.abort();
  }, []);

  // props가 있으면 그걸 우선 사용, 아니면 fetch 결과 사용
  const data = (bakeries.length ? bakeries : items)
    .slice(0, limit) // 서버가 이미 Top 순서로 준다고 가정
    .map((b) => ({
      id: b.id,
      name: b.name,
      location: b.location ?? b.address ?? "",
      tags: Array.isArray(b.tags) ? b.tags : [],
    }));

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
