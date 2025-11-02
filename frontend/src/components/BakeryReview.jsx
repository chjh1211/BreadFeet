import "./BakeryReview.css";

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

const BakeryReview = ({ review }) => {
  const { id, nickname, profile, rating, text, date, likes, dislikes } = review;
  const rounded = Math.round(rating);

  return (
    <article className="bdw-review" key={id}>
      <div className="bakeryDetailBar">
        <div className="bakeryDetailBarProfile">
          <img className="bakeryDetailBarProfileImg" src={profile} />
          <div className="bakeryDetailBarNickName">{nickname}</div>
        </div>
        <Stars rating={rounded} />
      </div>
      <div className="bdw-review-text">{text}</div>
      <div className="bdw-review-meta">
        <span className="bdw-review-date">{date}</span>
        <span className="bdw-review-likes">좋아요 {likes}</span>
        <span className="bdw-review-dislikes">싫어요 {dislikes}</span>
      </div>
    </article>
  );
};

export default BakeryReview;
