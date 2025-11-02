import React, { useMemo } from "react";
import "../../components/MatchRateBar";
import "./MapBakeryCard.css";
import MatchRateBar from "../../components/MatchRateBar";

const formatDistance = (m) =>
  m < 1000 ? `${m}m` : `${(m / 1000).toFixed(1)}km`;

const Stars = ({ rating }) => {
  const full = Math.floor(rating);
  const half = rating - full >= 0.5;
  const empties = 5 - full - (half ? 1 : 0);
  return (
    <span className="mapCardstars" aria-label={`평점 ${rating}`}>
      {"★".repeat(full)}
      {half ? "☆".slice(0, 0) + "⯨" : ""}
      {"☆".repeat(empties)}
      <span className="mapCardRatingNumber">{rating.toFixed(1)}</span>
    </span>
  );
};

const MapBakeryCard = ({ bakery, userData = [] }) => {
  const rate = useMemo(() => {
    const item = userData.find((x) => x.bakeryId === bakery.id);
    return item?.matchScore ?? 0;
  }, [bakery.id, userData]);

  return (
    <li key={bakery.id} className="mapCard">
      <div className="mapMatchRate">
        {console.log(bakery.id)}
        <MatchRateBar bakeryId={bakery.id} name={bakery.name} rate={rate} />
      </div>
      <div className="mapCardMeta">
        <Stars rating={bakery.rating} />
        <div className="mapCardDistance">
          {formatDistance(bakery.distanceMeters)}
        </div>
      </div>
      <div className="mapCardInfo">
        <div className="mapCardInfo-row">{bakery.address}</div>
        <div className="mapCardInfo-row">
          리뷰 <strong>{bakery.reviewCount}</strong>개
        </div>
        <div className="mapCardInfo-row">{bakery.phone}</div>
      </div>
    </li>
  );
};

export default MapBakeryCard;
