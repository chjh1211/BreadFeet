import { useState, useEffect } from "react";
// 쿠키 아이콘 임포트
import { FaCookieBite } from "react-icons/fa"; 
import axios from 'axios';
import './Taste.css'; 
import { useNavigate } from 'react-router-dom'

const Taste = () => {
  const [questions, setQuestions] = useState([]);
  const [answers, setAnswers] = useState([]);
  const navigate = useNavigate();

  // 컴포넌트 마운트 시 데이터 페칭 (useEffect 사용)
  useEffect(() => {
    const getQuestions = async () => {
      try {
        const res = await axios.get(`http://localhost:3001/Questions`); 
        setQuestions(res.data);
        setAnswers(Array(res.data.length).fill(0)); 
      } catch (err) {
        console.error("❌ 질문 데이터를 불러오지 못했습니다:", err);
      }
    };
    getQuestions(); 
  }, []);

  // 답변 선택 핸들러
  const handleSelect = (index, value) => {
    const newAnswers = [...answers];
    newAnswers[index] = value;
    setAnswers(newAnswers);
  };

  if (questions.length === 0) {
    return <div className="loading-message">질문을 불러오는 중...</div>;
  }

  // 8. 데이터가 있을 때 설문지 UI 렌더링
  return (
    <div className="bread-survey">
      {questions.map((q, i) => (
        <div key={q.id} className="question-block">
          <p className="question-number">Q{i + 1}</p>
          <p className="question-text">{q.question}</p>
          
          <div className="rating-scale-container">
            <span className="scale-label-left">아니다</span> 
            <div className="rating-scale">
              {[1, 2, 3, 4, 5].map((val) => (
                // FaCookieBite 아이콘 사용
                <FaCookieBite
                  key={val}
                  size={36} // 크기를 조금 더 키웠습니다
                  onClick={() => handleSelect(i, val)}
                  // 요청하신 포인트 컬러 적용
                  color={val <= (answers[i] || 0) ? "#402b0cff" : "#cd9f6c"}
                  style={{ cursor: "pointer", transition: "color 0.2s", filter: "drop-shadow(0 1px 1px rgba(0,0,0,0.1))" }}
                />
              ))}
            </div>
            <span className="scale-label-right">그렇다</span> 
          </div>
        </div>
      ))}

      <button
        type="button"
        className="submit-button"
        onClick={() => navigate('/tasteResult')}
      >
        제출하기
      </button>
    </div>
  );    
}

export default Taste;