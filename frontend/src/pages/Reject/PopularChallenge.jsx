import axios from 'axios';
import "./PopularChallenge.css";
import Challenge from "./Challenge";
import bakeryImage from "../../assets/bakery1.jpg";

const mockData = [
  {
    id: 0,
    name: "대구 소금빵 마스터",
    image: bakeryImage,
  },
  {
    id: 1,
    name: "부산 소금빵 마스터",
    image: bakeryImage,
  },
  {
    id: 2,
    name: "서울 소금빵 마스터",
    image: bakeryImage,
  },
];

const PopularChallenge = () => {
  return (
    <div className="PopularChallenge">
      <h2 className="PopularChallengeTitle">이번주 Top 3 챌린지</h2>
      <div className="ChallengeList">
        {mockData.map((item) => (
          <Challenge key={item.id} name={item.name} image={item.image} />
        ))}
      </div>
    </div>
  );
};

export default PopularChallenge;
