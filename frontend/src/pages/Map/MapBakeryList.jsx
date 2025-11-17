import React, { useMemo, useState } from "react";
import "./MapBakeryList.css";
import MapBakeryCard from "./MapBakeryCard";

const MapBakeryList = ({ bakerys = [], selectedBakeryId, onSelectBakery }) => {
  const [query, setQuery] = useState("");
  const [sortBy, setSortBy] = useState("distance"); // 'distance' | 'reviews'

  // 검색 필터
  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return bakerys;

    return bakerys.filter((b) => {
      const name = (b.name || "").toLowerCase();
      const address = (b.address || "").toLowerCase();
      const phone = (b.phone || "").replaceAll(" ", "");
      const qPhone = q.replaceAll(" ", "");

      return (
        name.includes(q) ||
        address.includes(q) ||
        (phone && phone.includes(qPhone))
      );
    });
  }, [query, bakerys]);

  // 정렬
  const results = useMemo(() => {
    const sorted = [...filtered];

    switch (sortBy) {
      case "reviews":
        sorted.sort((a, b) => {
          const aCnt = Number(a.reviewCount) || 0;
          const bCnt = Number(b.reviewCount) || 0;
          return bCnt - aCnt;
        });
        break;
      case "distance":
      default:
        sorted.sort((a, b) => {
          const aDist = Number.isFinite(a.distanceMeters)
            ? a.distanceMeters
            : Number.POSITIVE_INFINITY;
          const bDist = Number.isFinite(b.distanceMeters)
            ? b.distanceMeters
            : Number.POSITIVE_INFINITY;
          return aDist - bDist;
        });
        break;
    }

    return sorted;
  }, [filtered, sortBy]);

  const handleBakerySelect = (bakeryId) => {
    onSelectBakery?.(bakeryId);
  };

  return (
    <div className="mapBakeryListWrapper">
      <div className="mapToolbar">
        <div className="searchWrapper">
          <div className="mapSearch">
            <span className="mapSearchIcon" aria-hidden />
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
