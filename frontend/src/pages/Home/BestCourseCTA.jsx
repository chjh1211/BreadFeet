import { useEffect, useState } from "react";
import "./BestCourseCTA.css";

export default function BestCourseCTA({ onClick }) {
  const [data, setData] = useState(null);
  const [err, setErr] = useState("");

  useEffect(() => {
    let abt = false;
    fetch("http://localhost:3001/editor")
      .then((r) => {
        if (!r.ok) throw new Error(`HTTP ${r.status}`);
        return r.json();
      })
      .then((json) => !abt && setData(json))
      .catch((e) => !abt && setErr(e.message));
    return () => {
      abt = true;
    };
  }, []);

  if (err) {
    return (
      <div className="bestCTA">
        <p className="bestCTA__error">불러오기 실패: {err}</p>
      </div>
    );
  }

  if (!data) {
    return (
      <div className="bestCTA">
        <div className="bestCTA__header">
          <h3>불러오는 중…</h3>
        </div>
      </div>
    );
  }

  const { title, courseTitle, author, bullets = [], mapUrl = "/map" } = data;

  const handleClick = () => {
    if (onClick) return onClick(data);
    window.location.assign(mapUrl);
  };

  return (
    <div className="bestCTA">
      <header className="bestCTA__header">
        <h3>{title}</h3>
        <p className="bestCTA__subtitle">
          <span className="bestCTA__course">{courseTitle}</span>
          <span className="bestCTA__author">{author}</span>
        </p>
      </header>

      <div className="bestCTA__body">
        <ul className="bestCTA__route" aria-label="코스 요약">
          {bullets.map((line, i) => (
            <li key={i}>{line}</li>
          ))}
        </ul>

        <button
          type="button"
          className="bestCTA__btn"
          onClick={handleClick}
          aria-label="맵에서 자세히 보기"
        >
          더 자세히 보기 → <small className="toMap">Map으로</small>
        </button>
      </div>
    </div>
  );
}
