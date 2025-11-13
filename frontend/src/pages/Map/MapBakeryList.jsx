import React, { useMemo, useState, useEffect } from "react";
import "./MapBakeryList.css";
import MapBakeryCard from "./MapBakeryCard";

const MapBakeryList = () => {
  const [query, setQuery] = useState("");
  const [sortBy, setSortBy] = useState("distance"); // 'distance' | 'reviews' | 'match'
  const [bakerys, setBakerys] = useState([]);
  const [userMatchRates, setuserMatchRates] = useState([]);
  const userId = 1001;

  useEffect(() => {
    fetch(`http://localhost:3001/bakery`)
      .then((res) => res.json())
      .then((data) => setBakerys(data))
      .catch(() => setBakerys([]));
  }, []);

  useEffect(() => {
    fetch(`http://localhost:3001/matchRates?userId=${userId}`)
      .then((res) => res.json())
      .then((data) => setuserMatchRates(data))
      .catch(() => setuserMatchRates([]));
  }, [userId]);

  const scoreMap = useMemo(() => {
    return new Map(
      userMatchRates.map(({ bakeryId, matchScore }) => [bakeryId, matchScore])
    );
  }, [userMatchRates]);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return bakerys;
    return bakerys.filter(
      (b) =>
        b.name.toLowerCase().includes(q) ||
        b.address.toLowerCase().includes(q) ||
        b.phone.replaceAll(" ", "").includes(q.replaceAll(" ", ""))
    );
  }, [query, bakerys]);

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
            userData={userMatchRates}
          />
        ))}
      </ul>
    </div>
  );
};

export default MapBakeryList;
