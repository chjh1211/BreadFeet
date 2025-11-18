import "./MapPath.css";
import { useMemo, useState, useCallback, useEffect } from "react";

const Regions = {
  서울: [
    "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구",
  ],
  대구: ["중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군"],
  부산: [
    "중구", "서구", "동구", "영도구", "부산진구", "동래구", "남구", "북구", "해운대구", "사하구", "금정구", "강서구", "연제구", "수영구", "사상구", "기장군",
  ],
  대전: ["동구", "중구", "서구", "유성구", "대덕구"],
  인천: [
    "중구", "동구", "미추홀구", "연수구", "남동구", "부평구", "계양구", "서구", "강화군", "옹진군",
  ],
  광주: ["동구", "서구", "남구", "북구", "광산구"],
  울산: ["중구", "남구", "동구", "북구", "울주군"],
  세종: ["세종시 전체"],
  경기: ["수원시", "용인시", "고양시", "성남시", "화성시", "부천시", "남양주시", "안산시", "평택시", "의정부시"], // 축약
  강원: ["춘천시", "원주시", "강릉시", "속초시"],
  충북: ["청주시", "충주시", "제천시"],
  충남: ["천안시", "아산시", "공주시", "서산시"],
  전북: ["전주시", "익산시", "군산시"],
  전남: ["여수시", "순천시", "목포시"],
  경북: ["포항시", "구미시", "경주시", "김천시"],
  경남: ["창원시", "김해시", "진주시", "양산시"],
  제주: ["제주시", "서귀포시"],
};

const Korea = Object.keys(Regions);

// 🟢 Prop으로 onAiPathUpdate 함수를 받습니다.
const MapPath = ({ onAiPathUpdate }) => { 
  const [region, setRegion] = useState(Korea[0] || "서울");
  const [subregion, setSubregion] = useState("지역 전체");

  const [aiList, setAiList] = useState([]); 
  const [loading, setLoading] = useState(false); 

  const subregions = useMemo(() => {
    const list = Regions[region] || [];
    return ["지역 전체", ...list];
  }, [region]);
  
  const handleRegionChange = useCallback((e) => {
    const newRegion = e.target.value;
    setRegion(newRegion);
    setSubregion("지역 전체");
  }, []);

  function parseAddress(address) {
    if (!address) return { region: "", subregion: "" };
    const parts = address.split(" ");
    
    const r = parts[0]; 
    const s = parts[1] || ""; 
    
    return {
      region: r,
      subregion: s,
    };
  }

  const handleAICreatePath = async () => {
    setLoading(true); 
    setAiList([]); 
    onAiPathUpdate?.([]); // 🟢 경로 생성 시작 시 기존 경로 초기화

    try {
      const [tasteRes, bakeryRes] = await Promise.all([
        fetch("http://localhost:3001/tasteFormResults/1"),
        fetch("http://localhost:3001/bakery"),
      ]);

      if (!tasteRes.ok || !bakeryRes.ok) {
          throw new Error("데이터 API 호출에 실패했습니다.");
      }

      const taste = await tasteRes.json();
      const bakeryList = await bakeryRes.json();
      
      const topBread = taste.bread1_result;
      
      if (!topBread) {
         alert("최애빵 데이터(bread1_result)를 찾을 수 없습니다.");
         return;
      }

      const matched = bakeryList.filter((b) => {
        const { region: r, subregion: s } = parseAddress(b.address);

        const breadOk = b.representBread === topBread;
        const regionOk = r === region;
        const subOk = subregion === "지역 전체" ? true : s === subregion;

        return breadOk && regionOk && subOk;
      });

      if (matched.length === 0) {
        alert(`${region} ${subregion} 지역에서 최애빵(${topBread})을(를) 대표 빵으로 하는 빵집이 없습니다!`);
        return;
      }

      const count = Math.min(5, matched.length);
      const randomFive = matched.sort(() => 0.5 - Math.random()).slice(0, count);
      
      setAiList(randomFive);
      
    } catch (err) {
      console.error("AI 경로 생성 오류:", err);
      alert("경로 생성 중 오류가 발생했습니다. 콘솔을 확인해주세요.");
      onAiPathUpdate?.([]);
    } finally {
      setLoading(false); 
    }
  };
  
  const handleShowPathOnMap = useCallback(() => {
      if (aiList.length > 0) {
          onAiPathUpdate?.(aiList); 
      }
  }, [aiList, onAiPathUpdate]);

return (
    <div className="MapPath container-fluid py-4"> 
      
      <h3 className="MapPathTitle text-center mb-4">순례길 추천</h3>

      <div className="card shadow-sm mb-4" style={{ backgroundColor: '#f7f2ed', border: 'none' }}>
        <div className="card-body">
          
          <div className="row MapPathControls g-3">
            
            {/* 광역자치단체 */}
            <div className="col-6">
              <label className="MapPathLabel form-label" htmlFor="region">광역자치단체</label>
              <select
                id="region"
                className="MapPathSelect form-select"
                value={region}
                onChange={handleRegionChange}
              >
                {Korea.map((item) => (
                  <option key={item} value={item}>
                    {item}
                  </option>
                ))}
              </select>
            </div>

            {/* 세부 지역 */}
            <div className="col-6">
              <label className="MapPathLabel form-label" htmlFor="subregion">세부 지역</label>
              <select
                id="subregion"
                className="MapPathSelect form-select"
                value={subregion}
                onChange={(e) => setSubregion(e.target.value)}
              >
                {subregions.map((item) => (
                  <option key={item} value={item}>
                    {item}
                  </option>
                ))}
              </select>
            </div>
          </div> 
        </div>
      </div>
      
      {/* ⭐️ AI 경로 생성 버튼 */}
      <div className="d-grid gap-2 mb-4">
        <button 
          className="btn MapPathButton btn-dark"
          onClick={handleAICreatePath} // 🟢 경로 생성 함수 연결
          disabled={loading}
        >
          {loading ? (
            <>
              <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
              경로 생성 중...
            </>
          ) : (
            "AI 경로 생성하기"
          )}
        </button>
      </div>
      

      {/* ⭐️ AI 추천 빵집 경로 섹션 */}
      {(aiList.length > 0 || loading) && (
        <div className="AIPathSection card shadow-sm" style={{ backgroundColor: '#f7f2ed', border: 'none' }}>
          <div className="card-header MapPathTitle" style={{fontSize: '18px', fontWeight: 'bold', backgroundColor: 'transparent', borderBottom: '1px solid #d6c9bd'}}>
            나만의 AI 추천 경로 🥨 🔍
          </div>
          <div className="card-body p-0">
            
            {loading && !aiList.length && (
                <div className="MapPathEmpty text-center p-3">
                    <span className="spinner-grow spinner-grow-sm me-2" role="status" aria-hidden="true"></span>
                    추천 빵집을 찾고 있습니다...
                </div>
            )}
          
          <div className="card-body p-0">
            {aiList.length > 0 && (
              <ul className="MapPathList list-group list-group-flush"> 
                {aiList.map((b) => (
                  <li key={b.id} className="MapPathItem list-group-item d-flex align-items-center bg-transparent border-0">
                    
                    <span className="MapPathDot" /> 
                    
                    <div className="MapPathContent">
                      <div className="MapPathName">{b.name}</div>
                      
                      <div className="MapPathInfo">
                        <span className="badge bg-secondary text-light me-2" style={{ backgroundColor: '#8a7a6c!important' }}>
                          대표빵: {b.representBread}
                        </span>
                        <span className="text-muted">
                          {parseAddress(b.address).region} {parseAddress(b.address).subregion}
                        </span>
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            )}
            </div>
          </div>
        </div>
      )}


      {/* ⭐️ AI 경로 지도에서 보기 버튼 */}
      {aiList.length > 0 && ( // 🟢 AI 추천 목록이 있을 때만 버튼 표시
          <div className="d-grid gap-2 mb-4 mt-4">
            <button 
              className="btn MapPathButton btn-dark" 
              onClick={handleShowPathOnMap} // 🟢 경로 보기 함수 연결
              disabled={loading}
            >
              {loading ? (
                <>
                  <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                  경로 표시 중 ...
                </>
              ) : (
                "AI 경로 지도에서 보기"
              )}
            </button>
          </div>
      )}
      
    </div> 
  );
};

export default MapPath;