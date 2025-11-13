import React, { useMemo, useState } from "react";
import "./MapBakeryList.css";
import MapBakeryCard from "./MapBakeryCard";

const MapBakeryList = ({
  bakerys = [],
  userMatchRates = [],
  selectedBakeryId,
  onSelectBakery,
}) => {
  const [query, setQuery] = useState("");
  const [sortBy, setSortBy] = useState("distance"); // 'distance' | 'reviews' | 'match'

  const scoreMap = useMemo(() => {
    return new Map(
      userMatchRates.map(({ bakeryId, matchScore }) => [
        String(bakeryId),
        matchScore,
      ])
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
      matchScore: scoreMap.get(String(b.id)) ?? 0,
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

  const handleBakerySelect = (bakeryId) => {
    onSelectBakery?.(bakeryId);
  };

  return (
    <div className="mapBakeryListWrapper">
      <div className="mapToolbar">
        <div className="searchWrapper">
          <div className="mapSearch">
            <span className="mapSearchIcon" aria-hidden>
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
            key={bakery.id}
            id={bakery.id}
            bakery={bakery}
            userData={userMatchRates}
            isSelected={bakery.id === selectedBakeryId}
            onSelect={handleBakerySelect}
          />
        ))}
        {results.length === 0 && (
          <li className="mapBakeryEmpty">검색 결과가 없습니다.</li>
        )}
      </ul>
    </div>
  );
};

export default MapBakeryList;
