import "./MapPath.css";
import { useMemo, useState } from "react";

const Regions = {
  서울: [
    "강남구",
    "강동구",
    "강북구",
    "강서구",
    "관악구",
    "광진구",
    "구로구",
    "금천구",
    "노원구",
    "도봉구",
    "동대문구",
    "동작구",
    "마포구",
    "서대문구",
    "서초구",
    "성동구",
    "성북구",
    "송파구",
    "양천구",
    "영등포구",
    "용산구",
    "은평구",
    "종로구",
    "중구",
    "중랑구",
  ],
  대구: ["중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군"],
  부산: [
    "중구",
    "서구",
    "동구",
    "영도구",
    "부산진구",
    "동래구",
    "남구",
    "북구",
    "해운대구",
    "사하구",
    "금정구",
    "강서구",
    "연제구",
    "수영구",
    "사상구",
    "기장군",
  ],
  대전: ["동구", "중구", "서구", "유성구", "대덕구"],
  인천: [
    "중구",
    "동구",
    "미추홀구",
    "연수구",
    "남동구",
    "부평구",
    "계양구",
    "서구",
    "강화군",
    "옹진군",
  ],
  광주: ["동구", "서구", "남구", "북구", "광산구"],
  울산: ["중구", "남구", "동구", "북구", "울주군"],
  세종: ["세종시 전체"],
  경기: [
    "수원시",
    "용인시",
    "고양시",
    "성남시",
    "화성시",
    "부천시",
    "남양주시",
    "안산시",
    "평택시",
    "의정부시",
  ], // 축약
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

const mockData = [
  {
    id: 1,
    name: "감자빵집",
    distance: "3m",
    region: "대구",
    subregion: "중구",
  },
  {
    id: 2,
    name: "감자빵집",
    distance: "3m",
    region: "대구",
    subregion: "수성구",
  },
  {
    id: 3,
    name: "감자빵집",
    distance: "3m",
    region: "대구",
    subregion: "북구",
  },
  {
    id: 4,
    name: "감자빵집",
    distance: "3m",
    region: "대구",
    subregion: "달서구",
  },
  {
    id: 5,
    name: "감자빵집",
    distance: "3m",
    region: "대구",
    subregion: "달성군",
  },
  {
    id: 6,
    name: "감자빵집",
    distance: "3m",
    region: "대구",
    subregion: "동구",
  },
  {
    id: 7,
    name: "감자빵집",
    distance: "3m",
    region: "대구",
    subregion: "서구",
  },
  {
    id: 8,
    name: "고구마빵집",
    distance: "3m",
    region: "대구",
    subregion: "중구",
  },
];

const MapPath = () => {
  const [region, setRegion] = useState("대구");
  const [subregion, setSubregion] = useState("지역 전체");

  const subregions = useMemo(
    () => ["지역 전체", ...(Regions[region] ?? [])],
    [region]
  );

  const filtered = useMemo(() => {
    return mockData.filter((r) => {
      const regionOk = region ? r.region === region : true;
      const subOk =
        subregion === "지역 전체" ? true : r.subregion === subregion;
      return regionOk && subOk;
    });
  }, [region, subregion]);

  const handleRegionChange = (e) => {
    setRegion(e.target.value);
    setSubregion("지역 전체"); // 도시 변경시 지역 선택 초기화
  };

  return (
    <div className="MapPath">
      <h3 className="MapPathTitle">순례길 추천</h3>

      <div className="MapPathControls">
        {/* 자치단체 */}
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
          지역 전체
        </label>
        <select
          id="subregion"
          className="MapPathSelect"
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

      {/* path 만들기 */}
      <ul className="MapPathList" aria-label="추천 루트 목록">
        {filtered.map((item) => (
          <li key={item.id} className="MapPathItem">
            <span className="MapPathDot" aria-hidden />
            <div className="MapPathContent">
              <div className="MapPathName">{item.name}</div>
              <div className="MapPathInfo">
                <span>{item.distance}</span>
                <span>
                  {item.region} {item.subregion}
                </span>
              </div>
            </div>
          </li>
        ))}

        {filtered.length === 0 && (
          <li className="MapPathEmpty">해당 지역의 추천 루트가 없습니다.</li>
        )}
      </ul>
    </div>
  );
};

export default MapPath;
