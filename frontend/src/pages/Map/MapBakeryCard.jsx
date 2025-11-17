import "./MapBakeryCard.css";
import MatchRateBar from "../../components/MatchRateBar";

const formatDistance = (meters) => {
  if (!Number.isFinite(meters)) return "-";
  if (meters < 1000) return `${Math.round(meters)}m`;
  return `${(meters / 1000).toFixed(1)}km`;
};

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

const MapBakeryCard = ({
  bakery,
  // userData = [],
  isSelected = false,
  onSelect,
}) => {
  // const rate = useMemo(() => {
  //   const targetId = String(bakery.id);
  //   const item = userData.find((x) => String(x.bakeryId) === targetId);
  //   return item?.matchScore ?? 0;
  // }, [bakery.id, userData]);

  const handleSelect = () => {
    onSelect?.(bakery.id);
  };

  const handleKeyDown = (event) => {
    if (event.key === "Enter" || event.key === " ") {
      event.preventDefault();
      onSelect?.(bakery.id);
    }
  };

  return (
    <li
      className={`mapCard ${isSelected ? "is-selected" : ""}`}
      onClick={handleSelect}
      onKeyDown={handleKeyDown}
      tabIndex={0}
      role="button"
      aria-pressed={isSelected}
    >
      <div className="mapCardBody">
        <div className="mapMatchRate">
          <MatchRateBar bakeryId={bakery.id} name={bakery.name} />
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
      </div>
    </li>
  );
};

export default MapBakeryCard;
