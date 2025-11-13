import "./Challenge.css";

const Challenge = ({ name, image }) => {
  return (
    <div className="ChallengeCard">
      <img src={image} alt={name} className="ChallengeImage" />
      <div className="ChallengeContent">
        <p className="ChallengeName">{name}</p>
        <button className="ChallengeButton">참여</button>
      </div>
    </div>
  );
};

export default Challenge;
