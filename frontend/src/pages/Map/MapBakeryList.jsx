import React, { useMemo, useState } from "react";
import "./MapBakeryList.css";
import MapBakeryCard from "./MapBakeryCard";

// HomeRightLogin랑 같은 api 사용?
const mockData = [
  {
    id: 1,
    name: "감자빵집",
    rating: 4.5,
    address: "대구 북구 대학로 80",
    reviewCount: 88,
    phone: "053 123 4567",
    distanceMeters: 3,
  },
  {
    id: 2,
    name: "감자빵집 본점",
    rating: 5,
    address: "대구 북구 대학로 100",
    reviewCount: 152,
    phone: "053 999 1234",
    distanceMeters: 120,
  },
  {
    id: 3,
    name: "감자빵 연구소",
    rating: 4.3,
    address: "대구 북구 대학로 12",
    reviewCount: 61,
    phone: "053 321 7890",
    distanceMeters: 230,
  },
  {
    id: 4,
    name: "감자빵집 수성점",
    rating: 3.6,
    address: "대구 수성구 들안로 45",
    reviewCount: 98,
    phone: "053 222 3333",
    distanceMeters: 1780,
  },
  {
    id: 5,
    name: "감자빵&커피",
    rating: 2.2,
    address: "대구 중구 공평로 7",
    reviewCount: 37,
    phone: "053 777 0000",
    distanceMeters: 540,
  },
  {
    id: 6,
    name: "감자빵집 직영점",
    rating: 1.9,
    address: "대구 남구 중앙대로 50",
    reviewCount: 312,
    phone: "053 456 7890",
    distanceMeters: 2450,
  },
];

// user id 입력시 반환되는 data
const userMockData = [
  { bakeryId: 1, matchScore: 81 },
  { bakeryId: 2, matchScore: 95 },
  { bakeryId: 3, matchScore: 76 },
  { bakeryId: 4, matchScore: 20 },
  { bakeryId: 5, matchScore: 69 },
  { bakeryId: 6, matchScore: 92 },
];

const MapBakeryList = () => {
  const [query, setQuery] = useState("");
  const [sortBy, setSortBy] = useState("distance"); // 'distance' | 'reviews' | 'match'

  const scoreMap = useMemo(() => {
    return new Map(
      userMockData.map(({ bakeryId, matchScore }) => [bakeryId, matchScore])
    );
  }, [userMockData]); // 나중에 api로 받아오는 데이터로 변경

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return mockData;
    return mockData.filter(
      (b) =>
        b.name.toLowerCase().includes(q) ||
        b.address.toLowerCase().includes(q) ||
        b.phone.replaceAll(" ", "").includes(q.replaceAll(" ", ""))
    );
  }, [query]);

  const results = useMemo(() => {
    const withMatch = filtered.map((b) => ({
      ...b,
      matchScore: scoreMap.get(b.id) ?? 0,
    }));

    switch (sortBy) {
      case "reviews":
        withMatch.sort((a, b) => b.reviewCount - a.reviewCount);
        break;
      case "match":
        withMatch.sort((a, b) => b.matchScore - a.matchScore);
        break;
      case "distance":
      default:
        withMatch.sort((a, b) => a.distanceMeters - b.distanceMeters);
        break;
    }
    return withMatch;
  }, [filtered, sortBy, scoreMap]);

  return (
    <div className="mapBakeryListWrapper">
      <div className="mapToolbar">
        <div className="searchWrapper">
          <div className="mapSearch">
            <span className="mapSearchIcon" aria-hidden>
              <img></img>
            </span>
            <input
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="검색"
              aria-label="베이커리 검색"
            />
          </div>
        </div>

        <div className="mapFilters" aria-label="정렬 기준">
          {[
            { key: "distance", label: "거리순" },
            { key: "reviews", label: "리뷰순" },
            { key: "match", label: "취향매칭순" },
          ].map((opt) => (
            <button
              key={opt.key}
              role="tab"
              aria-selected={sortBy === opt.key}
              className={`mapFiltersButton ${
                sortBy === opt.key ? "is-active" : ""
              }`}
              onClick={() => setSortBy(opt.key)}
            >
              {opt.label}
            </button>
          ))}
        </div>
      </div>

      <ul className="mapBakeryList" aria-live="polite">
        {results.map((bakery) => (
          <MapBakeryCard
            id={bakery.id}
            bakery={bakery}
            userData={userMockData}
          />
        ))}
      </ul>
    </div>
  );
};

export default MapBakeryList;
