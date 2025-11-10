import { useNavigate } from "react-router-dom";
import "./AiCTA.css";

export default function AiCTA({ nickname = "감자도리", onStart }) {
  const navigate = useNavigate();

  return (
    <div className="aiCTA">
      <h3 className="aiCTA__title">〈 {nickname} 취향 분석 완료 〉</h3>
      <p className="aiCTA__subtitle">나만의 빵지순례코스 완주하기</p>

      <div className="aiCTA__panel">
        <p className="aiCTA__desc">취향 설문 1분 · AI가 코스를 자동 추천해요</p>
        <button
          type="button"
          className="aiCTA__btn"
          onClick={() => {
            if (onStart) return onStart({ nickname });
            navigate("/map");
          }}
        >
          AI 추천 시작하기
        </button>
      </div>
    </div>
  );
}
