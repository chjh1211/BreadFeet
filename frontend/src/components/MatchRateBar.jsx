import "./MatchRateBar.css";

const MatchRateBar = ({ bakeryId, name }) => {
  const openDetailPopup = (e) => {
    e.stopPropagation();
    window.open(
      `/bakery/${bakeryId}`,
      `resizable=yes,scrollbars=yes,noopener,noreferrer`
    );
  };

  return (
    <div className="MatchRateBar">
      <div className="MatchRateLabel">
        <button className="BakeryName" onClick={openDetailPopup}>
          {name}
        </button>
      </div>
      {/* <div className="MatchRateTrack">
        <div className="MatchRateFill" style={{ width: `${rate}%` }} />
      </div> */}
    </div>
  );
};

export default MatchRateBar;
