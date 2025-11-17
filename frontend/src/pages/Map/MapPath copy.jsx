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

const MapPath = () => {
  const [region, setRegion] = useState(Korea[0] || "서울"); // 기본값을 Korea의 첫 번째 요소로 설정
  const [subregion, setSubregion] = useState("지역 전체");

  const [aiList, setAiList] = useState([]); // AI 추천 결과 목록
  const [loading, setLoading] = useState(false); // 로딩 상태

  // ⭐ 현재 선택된 광역시/도의 하위 지역 목록 (useMemo 사용)
  const subregions = useMemo(() => {
    // '지역 전체'를 목록의 맨 앞에 추가
    const list = Regions[region] || [];
    return ["지역 전체", ...list];
  }, [region]);
  
  // ⭐ 광역자치단체 변경 핸들러
  const handleRegionChange = useCallback((e) => {
    const newRegion = e.target.value;
    setRegion(newRegion);
    setSubregion("지역 전체"); // 광역 변경 시 하위 지역 초기화
  }, []);

  /** 주소에서 광역시 / 구·군/시 추출 */
  function parseAddress(address) {
    if (!address) return { region: "", subregion: "" };
    const parts = address.split(" ");
    
    // 첫 번째 파트는 광역자치단체 (예: "대구", "경기")
    const r = parts[0]; 
    // 두 번째 파트는 구/군/시 (예: "북구", "수원시")
    const s = parts[1] || ""; 
    
    return {
      region: r,
      subregion: s,
    };
  }

  // ⭐ AI 경로 생성 (최애빵(bread1_result) 기준으로 필터링)
  const handleAICreatePath = async () => {
    setLoading(true); // 로딩 시작
    setAiList([]); // 이전 결과 초기화

    try {
      // 1) 최애빵(bread1_result) 가져오기 및 bakery 전체 조회 병렬 실행
      const [tasteRes, bakeryRes] = await Promise.all([
        fetch("http://localhost:3001/tasteFormResults/1"), // userId 1001의 결과 가정
        fetch("http://localhost:3001/bakery"),
      ]);

      if (!tasteRes.ok || !bakeryRes.ok) {
          throw new Error("데이터 API 호출에 실패했습니다.");
      }

      const taste = await tasteRes.json();
      const bakeryList = await bakeryRes.json();
      
      const topBread = taste.bread1_result; // 최애빵 (예: "크림빵")
      
      if (!topBread) {
         alert("최애빵 데이터(bread1_result)를 찾을 수 없습니다.");
         return;
      }

      // 2) 대표빵 + 광역/세부 지역 필터링
      const matched = bakeryList.filter((b) => {
        const { region: r, subregion: s } = parseAddress(b.address);

        // a. 대표빵 일치 확인
        const breadOk = b.representBread === topBread;
        
        // b. 광역자치단체 일치 확인
        const regionOk = r === region;
        
        // c. 세부 지역 일치 확인 ('지역 전체' 선택 시 무조건 통과)
        const subOk = subregion === "지역 전체" ? true : s === subregion;

        return breadOk && regionOk && subOk;
      });

      if (matched.length === 0) {
        alert(`${region} ${subregion} 지역에서 최애빵(${topBread})을(를) 대표 빵으로 하는 빵집이 없습니다!`);
        return;
      }

      // 3) 랜덤 5개 추출
      const count = Math.min(5, matched.length); // 5개 미만이면 있는 개수만큼만
      const randomFive = matched.sort(() => 0.5 - Math.random()).slice(0, count);
      setAiList(randomFive);
    } catch (err) {
      console.error("AI 경로 생성 오류:", err);
      alert("경로 생성 중 오류가 발생했습니다. 콘솔을 확인해주세요.");
    } finally {
      setLoading(false); // 로딩 종료
    }
  };


  return (
    <div className="MapPath">
      <h3 className="MapPathTitle">순례길 추천</h3>

      <div className="MapPathControls">
        {/* 광역자치단체 */}
        <label className="MapPathLabel" htmlFor="region">
          광역자치단체
        </label>
        <select
          id="region"
          className="MapPathSelect"
          value={region}
          onChange={handleRegionChange}
        >
          {Korea.map((item) => (
            <option key={item} value={item}>
              {item}
            </option>
          ))}
        </select>

        {/* 자치단체의 지역 */}
        <label className="MapPathLabel" htmlFor="subregion">
          세부 지역
        </label>
        <select
          id="subregion"
          className="MapPathSelect"
          value={subregion}
          onChange={(e) => setSubregion(e.target.value)}
        >
          {subregions.map((item) => (
            // '지역 전체'를 맨 앞에 포함
            <option key={item} value={item}>
              {item}
            </option>
          ))}
        </select>
      </div>

      {/* AI 경로 생성 버튼 */}
      <button 
        className="MapPathButton" 
        onClick={handleAICreatePath}
        disabled={loading} // 로딩 중 버튼 비활성화
      >
        {loading ? "경로 생성 중..." : "AI 경로 생성하기"}
      </button>
  
      {/* 로딩 상태 및 결과 표시 */}
      {loading && <div className="MapPathLoading">경로를 찾고 있습니다...</div>}

      {aiList.length > 0 && (
        <div className="AIPathSection">
          <h4>AI 추천 빵집 경로</h4>
          <ul className="MapPathList">
            {aiList.map((b) => (
              <li key={b.id} className="MapPathItem">
                <span className="MapPathDot" />
                <div className="MapPathContent">
                  <div className="MapPathName">{b.name}</div>
                  <div className="MapPathInfo">
                    <span>대표빵: {b.representBread}</span>
                    <span>
                      {parseAddress(b.address).region} {parseAddress(b.address).subregion}
                    </span>
                  </div>
                </div>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div> 
  );
};

export default MapPath;