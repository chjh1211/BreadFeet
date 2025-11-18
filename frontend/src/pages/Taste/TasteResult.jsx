import React, { useEffect, useState } from "react";
import "./TasteResult.css";
import axios from "axios";

const TasteResult = () => {
  const [result, setResult] = useState(null);

  useEffect(() => {
    const fetchResult = async () => {
      try {
        const res = await axios.get("http://localhost:3001/TasteFormResult"); // db.json TasteFormResult
        // user_id 2 결과 가져오기
        const userResult = res.data.find(r => r.user_id === 2);
        setResult(userResult);
      } catch (err) {
        console.error("결과를 불러오지 못했습니다:", err);
      }
    };

    fetchResult();
  }, []);

  if (!result) return <div className="loading">결과를 불러오는 중...</div>;

  const { taste_result, top1_bread, top1_prob } = result;

  // bread1~bread10 순서대로 정렬
  const sortedBreads = Object.entries(taste_result).sort((a, b) => {
    const aNum = parseInt(a[0].replace("bread", "").replace("_result", ""));
    const bNum = parseInt(b[0].replace("bread", "").replace("_result", ""));
    return aNum - bNum;
  });

  return (
    <div className="taste-result-container">
      <h1>🍞 당신의 빵 BTI 결과 🍞</h1>

      <div className="top-result">
        <h2>가장 어울리는 빵</h2>
        <p className="top-bread">{top1_bread}</p>
        <p className="top-prob">{(top1_prob * 100).toFixed(1)}%</p>
      </div>

      <div className="all-breads">
        <h2>전체 취향 확률</h2>
        <ul>
          {sortedBreads.map(([key, value]) => (
            <li key={key}>
              <span className="bread-name">{value.bread}</span>
              <span className="bread-prob">{(value.prob * 100).toFixed(1)}%</span>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default TasteResult;
